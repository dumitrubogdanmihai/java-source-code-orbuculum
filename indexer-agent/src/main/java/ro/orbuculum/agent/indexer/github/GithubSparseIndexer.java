package ro.orbuculum.agent.indexer.github;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHCommit.File;

import com.github.javaparser.ast.CompilationUnit;

import ro.orbuculum.agent.indexer.Parser;
import ro.orbuculum.agent.indexer.syntax.AstIndexer;

/**
 * Index hot commits.
 * (files touched by a list of commits)
 * 
 * @author bogdan
 */
public class GithubSparseIndexer {

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
  public GithubSparseIndexer(Parser parser, AstIndexer indexer) {
    this.parser = parser;
    this.indexer = indexer;
  }

  /**
   * Index every Java source blob touched since the given commit identifier.
   * 
   * @param lastSha1Indexer Commit identifier.
   * @param repo            Source.
   * 
   * @return Sha1(commit identifier) to which Solr was updated.
   */
  // TODO: Reduce cognitive complexity.
  public String indexSince(String lastSha1Indexer, GHRepository repo) {
    try {
      // Put new commits in stack to capture files properly...
      Stack<GHCommit> newCommits = new Stack<>();
      GHCommit iCommit = repo.getCommit(repo.getBranch("master").getSHA1());
      while (iCommit != null && !iCommit.getSHA1().equals(lastSha1Indexer)) {
        newCommits.add(iCommit);
        if (logger.isDebugEnabled()) {
          logger.debug("found new commit: " + iCommit.getSHA1());
        }
        if (iCommit.getParents().isEmpty()) {
          iCommit = null;
        } else {
          // TODO: Design a fancy algorithm to take into account merges.
          iCommit = iCommit.getParents().get(0);
        }
      }

      // Store in a map to preserve the last modification of a file
      // otherwise it will run into nasty exceptions when trying to parse a file that isn't here anymore.
      Map<String, File> filesMap = new HashMap<>();
      while (!newCommits.isEmpty()) {
        GHCommit commit = newCommits.pop();
        if (logger.isDebugEnabled()) {
          logger.debug("\n\n new commit files:" + commit.getSHA1());
        }
        for (File file : commit.getFiles()) { 
          if (logger.isDebugEnabled()) {
            logger.debug(" " + file.getFileName() + " " + file.getStatus() + 
                "  prev name " + file.getPreviousFilename());
            if (filesMap.get(file.getFileName()) != null) {
              System.err.println(" - replace old " + filesMap.get(file.getFileName()).getStatus());
            } else {
              System.err.println();
            }
          }

          if ("renamed".equals(file.getStatus())) {
            // TODO: Delete it from index.
            filesMap.remove(file.getPreviousFilename());
          }

          filesMap.put(file.getFileName(), file);
        }
      }

      // Parse and push into Solr.
      indexFiles(new ArrayList<>(filesMap.values()), repo);
    } catch (IOException e) {
      // TODO: This catch is too broad?
      logger.warn(e, e);
    }

    // TODO: Hardcoded master branch looks fishy.
    // TODO: Find a more elegant way to return.
    try {
      return repo.getBranch("master").getSHA1();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Feed Solr.
   * 
   * @param entries Food.
   * @param repo    McDonalds.
   */
  private void indexFiles(List<File> files, GHRepository repo) {
    for (File file : files) {
      if (!("removed".equals(file.getStatus()))) {
        logger.debug("Index file: " + file.getFileName() + " " + file.getStatus() + " from sha " + file.getSha());
        try {
          String fileName = file.getFileName();
          if (fileName.endsWith(".java")) {
            logger.debug("Aaand parse " + fileName);
            CompilationUnit parse = parser.parse(repo.getFileContent(fileName).read());
            try {
              indexer.index(parse, repo, file.getFileName());
            } catch (SolrServerException | IOException e) {
              e.printStackTrace();
            }
          } else {
            // TODO: Non-java files should be filtered as soon as possible, not in the wolf's mouth.
            logger.debug("Ignore non java file.");
          }
        } catch (IOException e) {
          logger.warn(e, e);
        }
      } else {
        logger.debug("Should only be removed from the index " + file.getFileName());
      }
    }
  }
}
