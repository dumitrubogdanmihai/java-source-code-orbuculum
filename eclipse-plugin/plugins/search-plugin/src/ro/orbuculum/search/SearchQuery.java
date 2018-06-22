package ro.orbuculum.search;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;

import ro.orbuculum.search.querent.api.Fields;
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
  private final String text;

  /**
   * The result.
   */
  private final SearchResult searchResult;

  /**
   * Pattern that match one line query.
   * The first group match the field name of the Solr document (one of {@link Fields}).
   * The second match the field value.
   */
  private static Pattern QUERY_PATTERN = 
      Pattern.compile("^([a-z\\-]+:)?([a-zA-Z0-9]+)$");

  /**
   * Constructor.
   * 
   * @param text The text from the search dialog.
   */
  public SearchQuery(String text) {
    this.text = text;
    this.searchResult = new SearchResult(this);
  }

  /**
   * Process the raw query inserted by the user in the text field.
   * @param rawQuery User input.
   * @return  Fixed query.
   */
  public static String processQuery(String rawQuery) 
      throws IllegalArgumentException {
    String toReturn = "";
    if (rawQuery == null || rawQuery.isEmpty()) {
      toReturn += "*:*";  
    } else {
      String[] lines = rawQuery.split("\r?\n");
      for (int i = 0; i < lines.length; i++) {
        String line = lines[i];
        if (i != 0) {
          toReturn += " AND ";
        }
        
        Matcher matcher = QUERY_PATTERN.matcher(line);
        if (matcher.find()) {
          String fieldName = matcher.group(1);
          String fieldQuery = matcher.group(2);

          if (fieldName == null) {
            fieldName = "method"; 
          } else {
            // Trim the ":" separator.
            fieldName = fieldName.substring(0, fieldName.length() - 1); 
          }
          toReturn += fieldName + ":" + fieldQuery;
        } else {
          throw new IllegalArgumentException("Illegal query: " + rawQuery);
        }
      }
    }
    return toReturn;
  }

  @Override
  public IStatus run(IProgressMonitor monitor) throws OperationCanceledException {
    String q = processQuery(text);

    Solr.get(q, r -> {
      if (r != null) {
        List<Doc> docs = r.getDocs();
        for (Doc doc : docs) {
          searchResult.addEntity(new SearchResultEntity(doc));
        }
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
}
