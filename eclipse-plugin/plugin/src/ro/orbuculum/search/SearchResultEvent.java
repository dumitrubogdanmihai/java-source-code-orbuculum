package ro.orbuculum.search;

import org.eclipse.search.ui.ISearchResult;

public class SearchResultEvent extends org.eclipse.search.ui.SearchResultEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final SearchResultEntity addedEntity;

	protected SearchResultEvent(ISearchResult searchResult, SearchResultEntity addedEntity) {
		super(searchResult);
		this.addedEntity = addedEntity;
	}

	public SearchResultEntity getAddedEntity() {
		return addedEntity;
	}
}
