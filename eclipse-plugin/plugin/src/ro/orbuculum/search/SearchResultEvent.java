package ro.orbuculum.search;

import org.eclipse.search.ui.ISearchResult;

/**
 * Event fired for each search entity founded.
 *  
 * @author bogdan
 */
public class SearchResultEvent extends org.eclipse.search.ui.SearchResultEvent {

	/**
	 * Version.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The new entity.
	 */
	private final SearchResultEntity addedEntity;

	/**
	 * Constructor.
	 * @param searchResult Result.
	 * @param addedEntity  Entity.
	 */
	protected SearchResultEvent(ISearchResult searchResult, SearchResultEntity addedEntity) {
		super(searchResult);
		this.addedEntity = addedEntity;
	}

	/**
	 * @return Get the entity.
	 */
	public SearchResultEntity getAddedEntity() {
		return addedEntity;
	}
}
