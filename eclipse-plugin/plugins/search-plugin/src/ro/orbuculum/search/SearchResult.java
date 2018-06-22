package ro.orbuculum.search;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.ISearchResultListener;

/**
 * Retrieved Solr documents representation.
 * @author bogdan
 *
 */
public class SearchResult implements ISearchResult {

  /**
   * Search query.
   */
	private final ISearchQuery query;
	
	/**
	 * Listeners.
	 */
	private final ListenerList<ISearchResultListener> listeners = new ListenerList<>();
	
	/**
	 * List of retrieved entities.
	 */
	private final List<SearchResultEntity> result = new ArrayList<>();
	
	/**
	 * Constructor.
	 * @param query  The query.
	 */
	public SearchResult(ISearchQuery query) {
		this.query = query;
	}
	
	/**
	 * Add a entity.
	 * @param entity The entity.
	 */
	public void addEntity(SearchResultEntity entity) {
		result.add(entity);
		notifyListeners(entity);
	}
	
	

	/**
	 * @return Get retrieved results.
	 */
  public List<SearchResultEntity> getResult() {
    return result;
  }
  

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getLabel() {
		return getResult().size() + " file(s) found";
	}

	@Override
	public ISearchQuery getQuery() {
		return query;
	}

	@Override
	public String getTooltip() {
		return "Found files";
	}

  @Override
  public void addListener(ISearchResultListener arg0) {
    listeners.add(arg0);
  }
  
	@Override
	public void removeListener(ISearchResultListener arg0) {
		listeners.remove(arg0);
	}
	
	/**
   * Notify,
   * @param f entity.
   */
  private void notifyListeners(SearchResultEntity f) {
    SearchResultEvent event = new SearchResultEvent(this, f);
    for (Object listener : listeners.getListeners()) {
      ((ISearchResultListener) listener).searchResultChanged(event);
    }
  }
}
