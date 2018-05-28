package ro.orbuculum.search;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;

public class SearchQuery implements ISearchQuery {

	private final String text;
	private final SearchResult searchResult;
	
	public SearchQuery(String text) {
		this.text = text;
		this.searchResult = new SearchResult(this);
	}
	
	@Override
	public IStatus run(IProgressMonitor monitor) throws OperationCanceledException {
		System.err.println("for text : " +text);
//		Collection<File> entries = new HashSet<File>();
//		entries.add(root);
//		this.searchResult.addFile(new File("/home/bogdan/index.html"));
		this.searchResult.addEntity(new SearchResultEntity(
				"CLZ1",
				"METHOD1", 
				"playground2/src/main/java/playground/playground2/App.java",
				0,
				20));
//		do {
//			File entry = entries.iterator().next();
//			entries.remove(entry);
//			
//			entry.listFiles(new FileFilter() {
//				@Override
//				public boolean accept(File pathname) {
//					if ((pathname.isFile()) && (pathname.getName().contains(filter))) {
//						FileSearchQuery.this.searchResult.addFile(pathname);
//						return false;
//					} 
//					if (pathname.isDirectory() && recursieve) {
//						entries.add(pathname);
//						return false;
//					}
//					return false;
//				}
//			});
//		} while (!entries.isEmpty());
		
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
