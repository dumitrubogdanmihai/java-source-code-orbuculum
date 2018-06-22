package ro.orbuculum.search.querent;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryBuilder {
  

  /**
   * Pattern that match one line query.
   * The first group match the field name of the Solr document.
   * The second match the field value.
   */
  private static Pattern QUERY_PATTERN = 
      Pattern.compile("^([a-z\\-]+:)?([a-zA-Z0-9*]+)$");
  
  /**
   * List of opened projects names.
   */
  private List<String> openedProjects;

  
  public QueryBuilder(List<String> list) {
    this.openedProjects = list;
  }

  public String getQQuery(String rawMethodQuery, String project)
      throws IllegalArgumentException {
    return "(" + processMethodQuery(rawMethodQuery) + 
        ") AND (" + processProjectQuery(project) + ")";
  }
  
  /**
   * Process the raw query inserted by the user in the text field.
   * @param rawQuery User input.
   * @return Value of the "q" query parameter.
   */
  public String processMethodQuery(String rawQuery) 
      throws IllegalArgumentException {
    String toReturn = "";
    if (rawQuery == null || rawQuery.isEmpty() || rawQuery.trim().isEmpty()) {
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

  public String processProjectQuery(String project) {
    String toReturn = "";
    if (project == null) {
      if (openedProjects == null || openedProjects.isEmpty()) {
        toReturn += "project:*";
      } else {
        int size = openedProjects.size();
        for (int i = 0 ; i < size; i++) {
          if (i != 0) {
            toReturn += " OR ";
          }
          toReturn += "project:\"" + openedProjects.get(i) + "\"";
        }
      }
    } else {
      toReturn = "project:\"" + project + "\"";
    }
    return toReturn;
  }
}
