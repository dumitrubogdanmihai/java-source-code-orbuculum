package ro.orbuculum.search;

import java.util.Map;

import ro.orbuculum.search.querent.api.Fields;

/**
 * Retrieved Solr documents representation.
 * 
 * @author bogdan
 */
public class SearchResultEntity {
  /**
   * The stored fields.
   */
  private Map<String, String> docFields;

  /**
   * Constructor.
   * @param docFields retrieved fields.
   */
  public SearchResultEntity(Map<String, String> docFields) {
    this.docFields = docFields;
  }

  /**
   * Get the value for a field.
   * @param field The field.
   * @return The value.
   */
  public String get(Fields field) {
    return docFields.get(field.getName());
  }
}
