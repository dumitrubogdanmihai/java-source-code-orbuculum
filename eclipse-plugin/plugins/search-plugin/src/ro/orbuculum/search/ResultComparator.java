package ro.orbuculum.search;

import java.util.Comparator;

import ro.orbuculum.search.querent.jaxb.Doc;

public class ResultComparator implements Comparator<Doc>  {

  private Doc openedDoc;

  public ResultComparator(Doc openedDoc) {
    this.openedDoc = openedDoc;
  }
  
  @Override
  public int compare(Doc doc1, Doc doc2) {
    int dist1 = dist(doc1);
    int dist2 = dist(doc2);
    
    if (dist1 != dist2) {
      return dist1 - dist2;
    } else {
      Comparator<String> c = Comparator.comparing(String::toString);
      return c.compare(doc1.getClassName(), doc2.getClassName());
    }
  }
  
  /**
   * Considering packages as tree path it will compute the distance between two nodes.
   * @param doc
   * @return
   */
  public int dist(Doc doc) {
    if (this.openedDoc == null) {
      return 0;
    }
    String[] openedClassNodes = this.openedDoc.getClassName().split("\\r?\\n");
    String[] docClassNodes = doc.getClassName().split("\\r?\\n");
    
    int min = Math.min(openedClassNodes.length, docClassNodes.length);
    for (int i = 0; i < min; i ++) {
      if (!openedClassNodes[i].equals(docClassNodes[i])) {
        return (openedClassNodes.length - i) + (docClassNodes.length - i) ;
      }
    }
    
    return 0;
  }
}
