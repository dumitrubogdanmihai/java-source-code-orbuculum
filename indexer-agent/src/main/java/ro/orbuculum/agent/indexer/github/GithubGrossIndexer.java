package ro.orbuculum.agent.indexer.github;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.math3.util.Pair;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTree;
import org.kohsuke.github.GHTreeEntry;

import com.github.javaparser.ast.CompilationUnit;

import ro.orbuculum.agent.indexer.Parser;
import ro.orbuculum.agent.indexer.syntax.AstIndexer;
import ro.orbuculum.agent.indexer.syntax.handler.fs.FsAccess;
import ro.orbuculum.agent.indexer.syntax.handler.fs.GithubFsAccess;

/**
 * Index whole repository.
 * 
 * @author bogdan
 */
public class GithubGrossIndexer {

  /**
   * Logger.
   */
  private static final Logger logger = LogManager.getLogger(GithubGrossIndexer.class);

  /**
   * Parse raw file into abstract syntax tree.
   */
  private Parser parser;

  /**
   * Syntax tree indexer.
   */
  private AstIndexer indexer;

  /**
   * Constructor.
   * 
   * @param parser  From raw source to syntax.
   * @param indexer From syntax to Solr index.
   */
  public GithubGrossIndexer(Parser parser, AstIndexer indexer) {
    this.parser = parser;
    this.indexer = indexer;
  }

  /**
   * Index every Java source blob from the give repository.
   * 
   * @param repo From.
   * 
   * @return Sha1(commit identifier) to which Solr was updated.
   */
  public String indexWhole(GHRepository repo) {
    List<Entry<GHTreeEntry, String>> allEntriesToIndex = getAllEntriesToIndex(repo);
    if (allEntriesToIndex != null) {
      indexEntries(allEntriesToIndex, repo);
    }

    try {
      return repo.getBranch("master").getSHA1();
    } catch (IOException e) {
      logger.warn(e, e);
    }

    return null;
  }

  /**
   * Collect the list of all entries that need to be indexed.
   * 
   * @param repo Solr food.
   * 
   * @return entries to be indexed.
   */
  private static List<Entry<GHTreeEntry, String>> getAllEntriesToIndex(GHRepository repo) {
    try {
      GHBranch branch = repo.getBranch(repo.getDefaultBranch());
      GHTree tree = repo.getTreeRecursive(branch.getSHA1(), 1);
      return collectFilesRecurively(tree, "");
    } catch (IOException e) {
      logger.warn(e, e);
    }
    return null;
  }
  private static List<Entry<GHTreeEntry, String>> collectFilesRecurively(GHTree tree, String path) {
    List<Entry<GHTreeEntry, String>> toReturn = new ArrayList<>();
    int size = tree.getTree().size();
    for (int i = 0; i < size; i++) {
      GHTreeEntry entry = tree.getTree().get(i);
      switch (entry.getType()) {
      case "blob":
        AbstractMap.SimpleEntry<GHTreeEntry, String> pair = new AbstractMap.SimpleEntry<GHTreeEntry, String>(entry,  path);
        toReturn.add(pair);
        break;
      case "tree":
        try {
          GHTree asTree = entry.asTree();
          String pathNew = path;
          if (!pathNew.isEmpty()) {
            pathNew = pathNew + "/" + entry.getPath();
          } else {
            pathNew = entry.getPath();
          }
          toReturn.addAll(collectFilesRecurively(asTree, pathNew));
        } catch (IOException e) {
          logger.warn(e, e);
        }
        break;
      default: 
        logger.error("Unknown " + entry.getType());
        break;
      }
    }
    return toReturn;
  }

  /**
   * Feed Solr.
   * 
   * @param entries Food.
   * @param repo    McDonalds.
   */
  private void indexEntries(List<Entry<GHTreeEntry, String>> entries, GHRepository repo) {
    for (Entry<GHTreeEntry, String> e : entries) {
      GHTreeEntry entry = e.getKey();
      String filePath = entry.getPath();
      if (filePath.endsWith(".java")) {
        try {
          CompilationUnit parsed = parser.parse(entry.readAsBlob());
          String project = repo.getName();
          FsAccess fsAccess = new GithubFsAccess(repo); 
          String pth;
          if (e.getValue().isEmpty()) {
            pth = filePath;
          } else {
            pth = e.getValue() + "/" + filePath;
          }
          indexer.index(parsed, project, pth, fsAccess);
        } catch (SolrServerException | IOException ex) {
          logger.warn(ex  , ex);
        }
      }
    }
  }
}
