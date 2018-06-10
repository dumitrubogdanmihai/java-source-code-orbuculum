package ro.orbuculum.search;

import java.util.Map;

import ro.orbuculum.search.querent.api.Fields;

public class SearchResultEntity {
	private Map<String, String> docFields;
	public SearchResultEntity(Map<String, String> docFields) {
		this.docFields = docFields;
	}
	public String get(Fields field) {
		return docFields.get(field.getName());
	}
}
