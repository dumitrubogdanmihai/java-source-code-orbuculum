package ro.orbuculum.search;

import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;

import ro.orbuculum.search.querent.api.Solr;

/**
 * User query.
 * 
 * @author bogdan
 */
public class SearchQuery implements ISearchQuery {

  /**
   * The raw text user inserted.
   */
  private final String text;

  /**
   * The result.
   */
  private final SearchResult searchResult;

  /**
   * Constructor.
   * 
   * @param text The text from the search dialog.
   */
  public SearchQuery(String text) {
    this.text = text;
    this.searchResult = new SearchResult(this);
  }

  @Override
  public IStatus run(IProgressMonitor monitor) throws OperationCanceledException {
    String q = "*:" + (text != null && !text.isEmpty() ? text : "*");
    Solr.get(q, r -> {
      if (r != null) {
        List<Map<String, String>> docs = r.getDocs();
        docs.stream().forEach(docFields -> {
          searchResult.addEntity(convert(docFields));
        });
      }
    });
    return Status.OK_STATUS;
  }

  @Override
  public ISearchResult getSearchResult() {
    return searchResult;
  }

  @Override
  public String getLabel() {
    return "Filesystem search";
  }

  @Override
  public boolean canRerun() {
    return true;
  }

  @Override
  public boolean canRunInBackground() {
    return true;
  }

  /**
   * Convert retrieved Solr document fields into a result entity.  
   * @param docFields  The fields.
   * @return The entity.
   */
  public static SearchResultEntity convert(Map<String, String> docFields) {
    return new SearchResultEntity(docFields);
  }
}
