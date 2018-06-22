package ro.orbuculum.unit;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import ro.orbuculum.search.querent.QueryBuilder;
import ro.orbuculum.search.querent.api.Solr;

/**
 * Full round-trip by indexing and asserting indexed documents.
 * 
 * @author bogdan_dumitru
 */
public class TestQueryBuilder {

  /**
   * Asserts that illegal queries are detected.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testIllegalMethodQuery() {
    QueryBuilder qb = new QueryBuilder(null); 
    qb.processMethodQuery("meth222od:methodName");
  }

  /**
   * Asserts that illegal queries are detected.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testIllegalMethodQuery1() {
    QueryBuilder qb = new QueryBuilder(null);
    qb.processMethodQuery("meth222od:metho:dName");
  }

  /**
   * Asserts that illegal queries are detected.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testIllegalMethodQuery2() {
    QueryBuilder qb = new QueryBuilder(null);
    qb.processMethodQuery(":dName");
  }

  /**
   * @throws IOException 
   * 
   */
  @Test
  public void testEmptyMethodQuery() throws IOException {
    QueryBuilder qb = new QueryBuilder(null);
    String processedQuery;
    processedQuery = qb.processMethodQuery(null);
    assertEquals("*:*", processedQuery);

    processedQuery = qb.processMethodQuery("");
    assertEquals("*:*", processedQuery);
    
    processedQuery = qb.processMethodQuery("methodName");
    assertEquals("method:methodName", processedQuery);

    processedQuery = qb.processMethodQuery("method:methodName");
    assertEquals("method:methodName", processedQuery);
  }

  /**
   * 
   */
  @Test
  public void testMultilineMethodQueries() {
    QueryBuilder qb = new QueryBuilder(null);
    String processedQuery;
    processedQuery = qb.processMethodQuery(
        "method:methodName\n" + 
        "anotherName");
    assertEquals("method:methodName AND method:anotherName", processedQuery);

    processedQuery = qb.processMethodQuery(
        "method:methodName\r\n" + 
        "anotherName");
    assertEquals("method:methodName AND method:anotherName", processedQuery);

    processedQuery = qb.processMethodQuery(
        "method:methodName\r\n" + 
        "method:anotherName");
    assertEquals("method:methodName AND method:anotherName", processedQuery);
    
    processedQuery = qb.processMethodQuery(
        "methodName\r\n" + 
        "class:className");
    assertEquals("method:methodName AND class:className", processedQuery);
  }
  

  /**
   * 
   */
  @Test
  public void testProjectQuery() {
    QueryBuilder qb = new QueryBuilder(null);
    String processedQuery = qb.processProjectQuery(null);
    assertEquals("project:*", processedQuery);

    processedQuery = qb.processProjectQuery("pr1");
    assertEquals("project:\"pr1\"", processedQuery);
    
    qb = new QueryBuilder(Arrays.asList("pr1", "pr2"));
    processedQuery = qb.processProjectQuery(null);
    assertEquals("project:\"pr1\" OR project:\"pr2\"", processedQuery);
    
    processedQuery = qb.processProjectQuery("pr3");
    assertEquals("project:\"pr3\"", processedQuery);

    processedQuery = qb.processProjectQuery("pr1");
    assertEquals("project:\"pr1\"", processedQuery);
  }
  

  /**
   * 
   */
  @Test
  public void testWholeQuery() {
    QueryBuilder qb = new QueryBuilder(Arrays.asList("pr1", "pr2"));
    String processedQuery = qb.getQQuery(
        "methodName\r\n" + 
        "class:className", 
        "pr1");
    assertEquals("(method:methodName AND class:className) AND (project:\"pr1\")", processedQuery);
  }
  

  /**
   * 
   */
  @Test
  public void testWildcardQuery() {
    QueryBuilder queryBuilder = new QueryBuilder(Arrays.asList("A","B"));
    String query = queryBuilder.getQQuery("method:*build*", null);
    assertEquals("(method:*build*) AND (project:\"A\" OR project:\"B\")", query);
  }
}
