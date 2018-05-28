package ro.orbuculum.search;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.ISearchResultListener;

public class SearchResult implements ISearchResult {

	private final ISearchQuery query;
	private final ListenerList<ISearchResultListener> listeners = new ListenerList<>();
	
	private final Collection<SearchResultEntity> result = new ArrayList<>();
	
	public SearchResult(ISearchQuery query) {
		this.query = query;
	}
	
	public void addEntity(SearchResultEntity entity) {
		getResult().add(entity);
		notifyListeners(entity);
	}

	private void notifyListeners(SearchResultEntity f) {
		SearchResultEvent event = new SearchResultEvent(this, f);
		for (Object listener : listeners.getListeners()) {
			((ISearchResultListener) listener).searchResultChanged(event);
		}
	}

	@Override
	public void addListener(ISearchResultListener arg0) {
		listeners.add(arg0);
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
	public void removeListener(ISearchResultListener arg0) {
		listeners.remove(arg0);
	}

	public Collection<SearchResultEntity> getResult() {
		return result;
	}
}
