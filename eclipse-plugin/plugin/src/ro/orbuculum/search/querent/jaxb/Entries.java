package ro.orbuculum.search.querent.jaxb;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * Just a required wrapper class.
 * @author bogdan
 */
public class Entries {
	private List<Str> str;

	@XmlElement(name="str")
	public List<Str> getStr() {
		return str;
	}

	public void setStr(List<Str> str) {
		this.str = str;
	}
}
