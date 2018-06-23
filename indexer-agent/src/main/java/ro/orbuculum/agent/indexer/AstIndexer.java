package ro.orbuculum.agent.indexer;

import org.kohsuke.github.GHCommit.File;
import org.kohsuke.github.GHRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient.Builder;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;

import ro.orbuculum.agent.indexer.handler.Context;
import ro.orbuculum.agent.indexer.handler.Visitor;
import ro.orbuculum.agent.indexer.handler.unit.CompilationUnitVisitor;

/**
 * Traverse the Abstract Syntax Tree and index it using {@link Visitor} instances.
 * 
 * @author Bogdan
 */
public class AstIndexer {

  /**
   * Client used to actually index documents.
   */
  private final SolrClient solr;

  public AstIndexer() {
    this("8983", "orbuculum");
  }
  
  /**
   * Constructor.
   * 
   * @param port	Solr server port.
   * @param core	Target core.
   */
  public AstIndexer(String port, String core) {
    this("localhost", port, core);
  }

  /**
   * Constructor.
   * 
   * @param host  Solr server host.
   * @param port  Solr server port.
   * @param core  Target core.
   */
  public AstIndexer(String host, String port, String core) {
    this.solr = buildSolrClient(host, port, core);
  }

  /**
   * Constructor.
   * 
   * @param client  Solr clienet.
   */
  public AstIndexer(SolrClient client) {
    this.solr = client;
  }

  /**
   * Index AST.
   * 
   * @param unit          AST root.
   * @param projectDir    Project directory.
   * @param javaResource  Source file within project.
   * 
   * @throws SolrServerException
   * @throws IOException
   */
  public void index(CompilationUnit unit, GHRepository repo, String javaResourcePath) 
      throws SolrServerException, IOException {
    Context context = new Context(this.solr, repo, javaResourcePath);
    CompilationUnitVisitor rootHandler = new CompilationUnitVisitor(context);
    index(unit, Arrays.asList(rootHandler));
  }

  /**
   * Build the client used to update the index. 
   * 
   * @param host  Server host.
   * @param port  Server port.
   * @param core  Solr's core.
   * 
   * @return The client.
   */
  protected SolrClient buildSolrClient(String host, String port, String core) {
    String serverUrl = "http://" + host + ":" + port + "/solr/" + core; 
    Builder builder = new HttpSolrClient.Builder(serverUrl);
    return builder.build();
  }

  /**
   * Here lays the traverse algorithm:
   * <li> for each visitor (aka handler)
   * <li>  if the node can be visited (indexed by the handler)
   * <li>   apply children visitors for children nodes 
   *              (who update the document of the parent visitor)
   * <li>   commit the data
   * 
   * @param unit	Compilation unit to index.
   */
  private void index(Node node, List<Visitor> visitors) {
    if (visitors != null && ! visitors.isEmpty()) {
      for (Visitor visitor : visitors) {
        // The node can be consumed.
        if (visitor.visit(node)) {
          // First apply children visitors that may contribute to the
          // document that the parent visitor will push to Solr index. 
          for (Node child : node.getChildrenNodes()) {
            List<Visitor> childrenVisitors = visitor.getChildrenVisitors();
            index(child, childrenVisitors);
          }
          // Update the index.
          visitor.commit();			
        }
      }
    }
  }
}
