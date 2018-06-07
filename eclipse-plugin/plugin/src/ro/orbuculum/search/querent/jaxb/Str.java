package ro.orbuculum.search.querent.jaxb;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement()
public class Str {
	private String key;
	private String value;
	
	@XmlAttribute(name="name")
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

	@XmlValue
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
