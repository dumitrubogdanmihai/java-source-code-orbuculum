package ro.orbuculum.search.querent.jaxb;

import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
public class Result {
	private	Map<String, String> fields;
	private String name;
	private	Integer numFound;
	private	Integer start;

	@XmlElement(name="doc")
	@XmlJavaTypeAdapter(MapAdapter.class)
	public Map<String, String> getFields() {
		return fields;
	}
	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}
	@XmlAttribute
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@XmlAttribute
	public Integer getNumFound() {
		return numFound;
	}
	public void setNumFound(Integer numFound) {
		this.numFound = numFound;
	}
	@XmlAttribute
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
}
