package ro.orbuculum.agent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import ro.orbuculum.agent.indexer.Parser;
import ro.orbuculum.agent.indexer.github.GithubGrossIndexer;
import ro.orbuculum.agent.indexer.github.GithubSparseIndexer;
import ro.orbuculum.agent.indexer.syntax.AstIndexer;

/**
 * Ensure that Solr index is synchronized with repositories changes.</br></br>
 * 
 * Basically "to index" means that I will look into chosen repositories for *.java files
 * that will be parsed arising the AbstractSyntaxTree
 * who will provide the semantic part which will be then stored (indexed) 
 * in a database (in the "orbuculum" Solr core).
 *  
 * @author bogdan
 */
public class Agent {
  /**
   * Logger.
   */
  private static final Logger logger = LogManager.getLogger(Agent.class);

  /**
   * Connector.
   */
  private GitHub github;

  /**
   * Used to index hot commits.
   */
  private GithubSparseIndexer githubSparseIndexer;

  /**
   * Used to index a whole new repository.
   */
  private GithubGrossIndexer githubIndexer;

  /**
   * Map repository to last indexed sha1.
   */
  private Map<String,String> ledger = new HashMap<>();

  /**
   * Query Github for new commits.
   */
  private Timer timer;

  /**
   * Constructor.
   * 
   * @param indexer Used to parse raw java files.
   */
  public Agent(AstIndexer indexer) {
    Parser parser = new Parser();
    this.githubSparseIndexer = new GithubSparseIndexer(parser, indexer);
    this.githubIndexer = new GithubGrossIndexer(parser, indexer);
    this.timer = new Timer();
  }

  /**
   * Give access to Github repositories.
   * 
   * @param oauthAccessToken  Token.
   * https://help.github.com/articles/git-automation-with-oauth-tokens/
   * 
   * @throws IOException
   */
  public void setAuthToken(String oauthAccessToken) throws IOException {
    github = GitHub.connectUsingOAuth(oauthAccessToken);
    timer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            Agent.this.updateIndex();
          }
        }, 2000, 2000);
  }

  /**
   * Put under the magnifying glass a repo.
   * Will index current state end every commit from now on.
   * 
   * @param repo
   */
  public void startTrackingRepository(String repo) {
    ledger.put(repo, null);
  }

  /**
   * Update Solr index accordingly to the ledger.
   */
  private void updateIndex() {
    for (String repoName : ledger.keySet()) {
      String lastSha1Indexer = ledger.get(repoName);

      GHRepository repo = null;
      try {
        repo = github.getRepository(repoName);
      } catch (IOException e) {
        logger.error(e, e);
      }

      if (repo != null) {
        String sha1 = lastSha1Indexer;
        if (lastSha1Indexer != null) {
          sha1 = githubSparseIndexer.indexSince(lastSha1Indexer, repo);
        } else {
          sha1 = githubIndexer.indexWhole(repo);
        }
        ledger.put(repoName, sha1);
      }
    }
  }
}
