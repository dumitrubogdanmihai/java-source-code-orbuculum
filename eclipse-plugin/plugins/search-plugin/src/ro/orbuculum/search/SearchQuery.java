package ro.orbuculum.search;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;

import ro.orbuculum.Activator;
import ro.orbuculum.search.querent.QueryBuilder;
import ro.orbuculum.search.querent.api.Solr;
import ro.orbuculum.search.querent.jaxb.Doc;

/**
 * User query.
 * 
 * @author bogdan
 */
public class SearchQuery implements ISearchQuery {

  /**
   * The raw text user inserted.
   */
  private final String methodQueryText;

  /**
   * The result.
   */
  private final SearchResult searchResult;

  /**
   * Project to be searched.
   */
  private String project;

  private QueryBuilder queryBuilder;

  /**
   * Constructor.
   * 
   * @param methodQueryText,  The text from the search dialog.
   */
  public SearchQuery(String methodQueryText, String project) {
    this.methodQueryText = methodQueryText;
    this.project = project;
    this.searchResult = new SearchResult(this);
    this.queryBuilder = new QueryBuilder(Solr.getOpenedProjects());
  }

  @Override
  public IStatus run(IProgressMonitor monitor) throws OperationCanceledException {
    try {
      String q = queryBuilder.getQQuery(methodQueryText, project);
      System.err.println("Run : " + q);
      Solr.get(q, r -> {
        if (r != null) {
          List<Doc> docs = r.getDocs();
          for (int i = 0; docs != null && i < docs.size(); i++) {
            Doc doc = docs.get(i);
            searchResult.addEntity(new SearchResultEntity(doc));
          }
        }
      });
      return Status.OK_STATUS;
    } catch (IllegalArgumentException e) {
      return new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage());
    }
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
}
