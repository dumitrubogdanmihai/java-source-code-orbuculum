package ro.orbuculum.agent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
  
  public static void main(String[] args) throws IOException {
    Agent agent = new Agent(new AstIndexer());
    agent.startTrackingRepository("dumitrubogdanmihai/java-source-code-orbuculum");
    agent.updateIndex();
  }
  
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
   * @throws IOException 
   */
  public Agent(AstIndexer indexer) {
    Parser parser = new Parser();
    this.githubSparseIndexer = new GithubSparseIndexer(parser, indexer);
    this.githubIndexer = new GithubGrossIndexer(parser, indexer);
    this.timer = new Timer();
    
    try {
      github = GitHub.connectUsingPassword("dumitrumihaibogdan@gmail.com", "XXX");
    } catch (IOException e) {
      e.printStackTrace();
    }
    timer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            Agent.this.updateIndex();
          }
        }, 0, 600000);// 10 minutes
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
   * @return authenticated user's email.
   */
  public Optional<String> getAuthorizedEmail() {
    try {
      return Optional.of(github.getMyself().getEmail());
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  /**
   * @return tracked repositories list.
   */
  public List<String> getTrackedRepositories() {
    return new ArrayList<>(ledger.keySet());
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
//          repo.getBlob("");
        }
        ledger.put(repoName, sha1);
      }
    }
  }
}
