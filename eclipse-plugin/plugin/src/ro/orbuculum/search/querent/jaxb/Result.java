package ro.orbuculum.search.querent.jaxb;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
public class Result {
	private	List<Map<String, String>> docs;
	private String name;
	private	Integer numFound;
	private	Integer start;

	@XmlElement(name="doc")
	@XmlJavaTypeAdapter(MapAdapter.class)
	public List<Map<String, String>> getDocs() {
		return docs;
	}
	public void setDocs(List<Map<String, String>> docs) {
		this.docs = docs;
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
