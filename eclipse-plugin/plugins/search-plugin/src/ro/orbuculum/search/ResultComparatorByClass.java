package ro.orbuculum.search;

import java.util.Comparator;

import ro.orbuculum.search.querent.jaxb.Doc;

public class ResultComparatorByClass implements Comparator<Doc>  {
  @Override
  public int compare(Doc doc1, Doc doc2) {
    Comparator<String> c = Comparator.comparing(String::toString);
    return c.compare(doc1.getClassName(), doc2.getClassName());
  }
}
