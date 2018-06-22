package ro.orbuculum.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;

import retrofit2.Response;
import ro.orbuculum.indexer.Indexer;
import ro.orbuculum.indexer.IndexerAgentApi;
import ro.orbuculum.rules.WipeIndex;
import ro.orbuculum.search.querent.api.Solr;
import ro.orbuculum.search.querent.jaxb.Result;

/**
 * Full round-trip by indexing and asserting indexed documents.
 * 
 * @author bogdan_dumitru
 */
public class FullRoundtripTest {

//  @ClassRule
//  public static Containers containers = new Containers();

  @Rule
  public WipeIndex wipeIndex = new WipeIndex();

  /**
   * Asserts that containers URLs are properly configured.
   * 
   * @throws InterruptedException
   * @throws IOException
   */
  @Test
  public void testApiContracts() throws InterruptedException, IOException {
    IndexerAgentApi restApi = new Indexer().getRestApi();
    Response<Void> request = restApi.index(".").execute();
    assertTrue(request.toString(), request.isSuccessful());

    Result result = Solr.get();    
    assertNotNull(result);
  }

  /**
   * Asserts that indexer-agent really do something.
   * 
   * @throws InterruptedException
   * @throws IOException
   */
  @Test
  public void testIndex() throws InterruptedException, IOException {
    assertEquals((Integer)0, Solr.get().getNumFound());

    IndexerAgentApi restApi = new Indexer().getRestApi();
    String path = new File("./src/main/java/ro/orbuculum/rules").getCanonicalPath();
    Response<Void> request = restApi.index(path).execute();
    assertTrue(request.toString(), request.isSuccessful());

    assertTrue(Solr.get().getNumFound() > 0);
  }

  /**
   * Asserts that the documents are updated, not appended to the index.
   * 
   * @throws InterruptedException
   * @throws IOException
   */
  @Test
  public void testUpdateIndex() throws InterruptedException, IOException {
    assertEquals((Integer)0, Solr.get().getNumFound());

    IndexerAgentApi restApi = new Indexer().getRestApi();
    String path = new File("src/test/resources/FullRoundTripTest/testUpdateIndex/project-1/project-dir")
        .getCanonicalPath();
    Response<Void> request = restApi.index(path).execute();
    assertTrue(request.toString(), request.isSuccessful());

    Result result = Solr.get();
    assertEquals((Integer)1, result.getNumFound());
    assertEquals("response", result.getName());
    assertEquals("method0", result.getDocs().get(0).getMethod());
    
    // Update a new version of previous indexed source.
    path = new File("src/test/resources/FullRoundTripTest/testUpdateIndex/project-1.1/project-dir")
        .getCanonicalPath();
    request = restApi.index(path).execute();
    assertTrue(request.toString(), request.isSuccessful());
    
    result = Solr.get();
    assertEquals((Integer)2, result.getNumFound());
    assertEquals("method0", result.getDocs().get(0).getMethod());
    assertEquals("method1", result.getDocs().get(1).getMethod());
  }
}
