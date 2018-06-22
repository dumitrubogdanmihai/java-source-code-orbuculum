package ro.orbuculum.unit;

import static org.junit.Assert.*;

import java.util.stream.Collectors;

import org.junit.Test;

import ro.orbuculum.search.SearchResult;
import ro.orbuculum.search.SearchResultEntity;
import ro.orbuculum.search.querent.jaxb.Doc;

public class TestResultsAreSorted {
  @Test
  public void testSortedResult() {
   SearchResult sr = new SearchResult(null);
   
   Doc doc1 = new Doc();
   doc1.setClassName("a.b.c.Class11");
   doc1.setProject("Project1");
   

   Doc doc2 = new Doc();
   doc2.setClassName("a.b.c.Class12");
   doc2.setProject("Project1");

   Doc doc3 = new Doc();
   doc3.setClassName("a.Class21");
   doc3.setProject("Project2");
   
   Doc doc4 = new Doc();
   doc4.setClassName("Class21");
   doc4.setProject("Project2");

   sr.addEntity(new SearchResultEntity(doc4));
   sr.addEntity(new SearchResultEntity(doc3));
   sr.addEntity(new SearchResultEntity(doc2));
   sr.addEntity(new SearchResultEntity(doc1));
   
   String collect = sr.getResult().stream()
   .map(SearchResultEntity::getClassName)
   .collect(Collectors.joining("\n"));
   
   assertEquals(
       "Class21\n" + 
       "a.Class21\n" + 
       "a.b.c.Class11\n" + 
       "a.b.c.Class12", collect);
  }
}
