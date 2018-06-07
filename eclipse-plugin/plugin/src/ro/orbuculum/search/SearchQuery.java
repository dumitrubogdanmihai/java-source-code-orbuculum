package ro.orbuculum.search;

import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;

import ro.orbuculum.search.querent.api.ApiImpl;
import ro.orbuculum.search.querent.api.Fields;

public class SearchQuery implements ISearchQuery {

	private final String text;
	private final SearchResult searchResult;

	public SearchQuery(String text) {
		this.text = text;
		this.searchResult = new SearchResult(this);
	}

	@Override
	public IStatus run(IProgressMonitor monitor) throws OperationCanceledException {
		String q = "*:" + (text != null ? text : "*"); 
		ApiImpl.get(q, r -> {
			System.err.println(r);
			if (r != null) {
				List<Map<String, String>> docs = r.getDocs();
				docs.stream().forEach(docFields -> {
					searchResult.addEntity(new SearchResultEntity(
							docFields.get(Fields.OBJECT.getName()),
							docFields.get(Fields.METHOD.getName()),
							"playground2/src/main/java/" + docFields.get(Fields.OBJECT.getName()) + ".java",
							Integer.parseInt(docFields.get(Fields.OFFSET.getName())),
							Integer.parseInt(docFields.get(Fields.OFFSET.getName())) + 10));
				});
			}
		});
		return Status.OK_STATUS;
	}

	@Override
	public ISearchResult getSearchResult() {
		System.err.println("getSearchResult");
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
