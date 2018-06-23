package ro.orbuculum.agent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.solr.client.solrj.SolrServerException;
import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHCommit.File;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTree;
import org.kohsuke.github.GHTreeEntry;
import org.kohsuke.github.GitHub;

import com.github.javaparser.ast.CompilationUnit;

import ro.orbuculum.agent.indexer.AstIndexer;
import ro.orbuculum.agent.indexer.Parser;

public class Agent {
  private Map<String,String> ledger = new HashMap<>();
  private AstIndexer indexer;
  private GitHub github;

  public Agent(AstIndexer indexer) throws IOException {
    this.indexer = indexer;
    github = GitHub.connectUsingPassword("dumitrumihaibogdan@gmail.com", "THIS_IS_NOT_MY_PASSWORD");
  }


  public static void main(String[] args) throws IOException {
    Agent agent = new Agent(new AstIndexer());
    agent.addRepository("dumitrubogdanmihai/java-source-code-orbuculum");
    agent.updateIndex();
  }

  public void setAuthToken(String oauthAccessToken) throws IOException {
    github = GitHub.connectUsingOAuth(oauthAccessToken);
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            Agent.this.updateIndex();
          }
        }, 2000, 2000);
  }

  public void addRepository(String repo) {
    ledger.put(repo, "7b2035c");
  }

  private void updateIndex() {
    for (String repoName : ledger.keySet()) {
      String sha1 = ledger.get(repoName);

      GHRepository repo = null;
      try {
        repo = github.getRepository(repoName);
      } catch (IOException e1) {
        e1.printStackTrace();
      }

      if (repo != null) {
        if (sha1 != null) {
          ;
          try {
            String sha1Head = repo.getBranch("master").getSHA1();
            GHCommit iCommit = repo.getCommit(sha1Head);
            while (!iCommit.getSHA1().equals(sha1)) {
              System.err.println("\niCommit: " + iCommit.getSHA1());
              
              List<File> files = null;
              if (repo != null) {
                files =  getFilesToIndex(repo, iCommit.getSHA1());
              }
              for (File f : files) {
                System.err.println(f.getFileName());
              }
              
              if (files != null) {
                indexFiles(files, repo);
              }
              
              if (iCommit.getParents().isEmpty()) {
                iCommit = null;
              } else {
                iCommit = iCommit.getParents().get(0);
              }
            }
          } catch (IOException e) {
            e.printStackTrace();
          }
        } else {
          List<GHTreeEntry> allEntriesToIndex = getAllEntriesToIndex(repo);
          if (allEntriesToIndex != null) {
            indexEntries(allEntriesToIndex, repo);
          }
        }
      }
    }
  }

  private void indexFiles(List<File> files, GHRepository repo) {
    for (File file : files) {
      if (!("removed".equals(file.getStatus()))) {
        try {
          String fileName = file.getFileName();
          if (fileName.endsWith(".java")) {
            CompilationUnit parse = Parser.parse(repo.getFileContent(fileName).read());
            try {
              indexer.index(parse, repo, file.getFileName());
            } catch (SolrServerException | IOException e) {
              e.printStackTrace();
            }
          }
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
    }
  }


  private void indexEntries(List<GHTreeEntry> entries, GHRepository repo) {
    for (GHTreeEntry entry : entries) {
      String filePath = entry.getPath();
      if (filePath.endsWith(".java")) {
        try {
          CompilationUnit parse = Parser.parse(entry.readAsBlob());
          indexer.index(parse, repo, filePath);
        } catch (SolrServerException | IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private  List<File> getFilesToIndex(GHRepository repo, String sha1) {
    GHCommit commit = null;
    if (repo != null) {
      try {
        commit = repo.getCommit(sha1).getParents().get(0);
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
    List<File> files = null;
    if (commit != null) {
      try {
        files = commit.getFiles();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
    return files;
  }


  private  List<GHTreeEntry> getAllEntriesToIndex(GHRepository repo) {
    try {
      GHBranch branch = repo.getBranch(repo.getDefaultBranch());
      GHTree tree = repo.getTreeRecursive(branch.getSHA1(), 1);
      return collectFilesRecurively(tree);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  private List<GHTreeEntry> collectFilesRecurively(GHTree tree) {
    List<GHTreeEntry> toReturn = new ArrayList<>();
    int size = tree.getTree().size();
    for (int i = 0; i < size; i++) {
      GHTreeEntry entry = tree.getTree().get(i);
      switch (entry.getType()) {
      case "blob":
        toReturn.add(entry);
        break;
      case "tree":
        try {
          toReturn.addAll(collectFilesRecurively(entry.asTree()));
        } catch (IOException e) {
          e.printStackTrace();
        }
        break;
      default: 
        System.err.println("Unknown " + entry.getType());
        break;
      }
    }
    return toReturn;
  }
}
