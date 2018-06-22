package ro.orbuculum.search;

import ro.orbuculum.search.querent.jaxb.Doc;

/**
 * Retrieved Solr documents representation.
 * 
 * @author bogdan
 */
public class SearchResultEntity extends Doc {
  /**
   * Solr indexed document.
   */
  private Doc doc;

  /**
   * Constructor.
   * @param docFields retrieved fields.
   */
  public SearchResultEntity(Doc doc) {
    this.doc = doc;
  }
  
  @Override
  public String getClassName() {
    return doc.getClassName();
  }
  
  @Override
  public Integer getLineEnd() {
    return doc.getLineEnd();
  }
  
  @Override
  public Integer getLineStart() {
    return doc.getLineStart();
  }
  
  @Override
  public String getMethod() {
    return doc.getMethod();
  }
  
  @Override
  public String getPath() {
    return doc.getPath();
  }
  
  @Override
  public String getProject() {
    return doc.getProject();
  }
}
