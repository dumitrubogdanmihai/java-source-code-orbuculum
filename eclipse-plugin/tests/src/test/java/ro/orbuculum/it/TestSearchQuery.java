package ro.orbuculum.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import retrofit2.Response;
import ro.orbuculum.indexer.Api;
import ro.orbuculum.indexer.IndexerAgentApi;
import ro.orbuculum.search.SearchQuery;
import ro.orbuculum.search.SearchResult;
import ro.orbuculum.search.SearchResultEntity;

/**
 * Full round-trip by indexing and asserting indexed documents.
 * 
 * @author bogdan_dumitru
 */
public class TestSearchQuery {

  //  @ClassRule
  //  public static Containers containers = new Containers();

//  @Rule
//  public WipeIndex wipeIndex = new WipeIndex();


  /**
   * @throws IOException 
   * 
   */
  @Test
  public void testEmptyQuery() throws IOException {
    IndexerAgentApi restApi = new Api().getRestApi();
    String path = new File("src/test/resources/TestSearchQuery/testEmptyQuery/project-dir")
        .getCanonicalPath();
    Response<Void> request = restApi.index(path).execute();
    assertTrue(request.toString(), request.isSuccessful());

    SearchQuery sq = new SearchQuery("", null);
    sq.run(null);
    SearchResult searchResult = (SearchResult) sq.getSearchResult();
    List<SearchResultEntity> result = searchResult.getResult();
    assertEquals(3, result.size());
  }
  
  /**
   * 
   */
  @Test
  public void testIllegalQuery() {
    
  }
  
  /**
   * 
   */
  @Test
  public void testMultilineQuery() {
    
  }
}
