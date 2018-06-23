package ro.orbuculum.agent.indexer;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import static org.junit.Assert.*;
import org.junit.Test;

import ro.orbuculum.agent.indexer.syntax.AstIndexer;

/**
 * Asserts the data that {@link AstIndexer} manage to extract from the AST.
 * 
 * @author bogdan
 */
public class AstIndexerTest {
  /**
   * Test the indexer against a class with a blank method.
   * 
   * @throws SolrServerException
   * @throws IOException
   */
  @Test
  public void testBlankMethod() throws SolrServerException, IOException {
    List<SolrInputDocument> documentsToIndex = VisitorSpyUtil.getDocumentsToIndex(
        new File("src/test/java/ro/orbuculum/agent/sample/BlankMethod.java"), 1);
    assertEquals("method=method", documentsToIndex.get(0).getField("method").toString());
  }
  
  /**
   * Test the indexer against a class with method calls.
   * 
   * @throws SolrServerException
   * @throws IOException
   */
  @Test
  public void testMethodCalls() throws SolrServerException, IOException {
    List<SolrInputDocument> documentsToIndex = VisitorSpyUtil.getDocumentsToIndex(
        new File("src/test/java/ro/orbuculum/agent/sample/MethodCalls.java"), 1);
    assertEquals("method=method0", documentsToIndex.get(0).getField("method").toString());
    assertEquals("method-call=org.junit.Assert.assertTrue", documentsToIndex.get(0).getField("method-call").toString());
  }

  /**
   * Assert that the class names are resolved.
   * 
   * @throws SolrServerException
   * @throws IOException
   */
  @Test
  public void testParameters() throws SolrServerException, IOException {
    List<SolrInputDocument> documentsToIndex = VisitorSpyUtil.getDocumentsToIndex(
        new File("src/test/java/ro/orbuculum/agent/sample/Parameters.java"), 7);
    assertEquals("method=method0", documentsToIndex.get(0).getField("method").toString());
    assertEquals("parameter=int", documentsToIndex.get(0).getField("parameter").toString());

    assertEquals("method=method1", documentsToIndex.get(1).getField("method").toString());
    assertEquals("parameter=java.lang.Integer", documentsToIndex.get(1).getField("parameter").toString());

    assertEquals("method=method2", documentsToIndex.get(2).getField("method").toString());
    assertEquals("parameter=java.lang.String", documentsToIndex.get(2).getField("parameter").toString());

    assertEquals("method=method3", documentsToIndex.get(3).getField("method").toString());
    assertEquals("parameter=ro.orbuculum.agent.sample.BlankMethod", documentsToIndex.get(3).getField("parameter").toString());

    assertEquals("method=method4", documentsToIndex.get(4).getField("method").toString());
    assertEquals("parameter=ro.orbuculum.agent.sample.Parameters", documentsToIndex.get(4).getField("parameter").toString());

    assertEquals("method=method5", documentsToIndex.get(5).getField("method").toString());
    assertEquals("parameter=java.net.URL", documentsToIndex.get(5).getField("parameter").toString());

    assertEquals("method=method6", documentsToIndex.get(6).getField("method").toString());
    assertEquals("parameter=[java.net.URL, java.io.File]", documentsToIndex.get(6).getField("parameter").toString());
  }
}