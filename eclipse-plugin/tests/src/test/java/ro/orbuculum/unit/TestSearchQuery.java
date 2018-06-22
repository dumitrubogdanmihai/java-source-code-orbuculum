package ro.orbuculum.unit;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import ro.orbuculum.search.SearchQuery;

/**
 * Full round-trip by indexing and asserting indexed documents.
 * 
 * @author bogdan_dumitru
 */
public class TestSearchQuery {

  /**
   * Asserts that illegal queries are detected.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testIllegalQuery() {
    SearchQuery.processQuery("meth222od:methodName");
  }

  /**
   * Asserts that illegal queries are detected.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testIllegalQuery1() {
    SearchQuery.processQuery("meth222od:metho:dName");
  }

  /**
   * Asserts that illegal queries are detected.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testIllegalQuery2() {
    SearchQuery.processQuery(":dName");
  }

  /**
   * @throws IOException 
   * 
   */
  @Test
  public void testEmptyQuery() throws IOException {
    String processedQuery;
    processedQuery = SearchQuery.processQuery(null);
    assertEquals("*:*", processedQuery);

    processedQuery = SearchQuery.processQuery("");
    assertEquals("*:*", processedQuery);
    
    processedQuery = SearchQuery.processQuery("methodName");
    assertEquals("method:methodName", processedQuery);

    processedQuery = SearchQuery.processQuery("method:methodName");
    assertEquals("method:methodName", processedQuery);
  }

  /**
   * 
   */
  @Test
  public void testMultilineQuery() {
    String processedQuery;
    processedQuery = SearchQuery.processQuery(
        "method:methodName\n" + 
        "anotherName");
    assertEquals("method:methodName AND method:anotherName", processedQuery);

    processedQuery = SearchQuery.processQuery(
        "method:methodName\r\n" + 
        "anotherName");
    assertEquals("method:methodName AND method:anotherName", processedQuery);

    processedQuery = SearchQuery.processQuery(
        "method:methodName\r\n" + 
        "method:anotherName");
    assertEquals("method:methodName AND method:anotherName", processedQuery);
    
    processedQuery = SearchQuery.processQuery(
        "methodName\r\n" + 
        "class:className");
    assertEquals("method:methodName AND class:className", processedQuery);
  }
}
