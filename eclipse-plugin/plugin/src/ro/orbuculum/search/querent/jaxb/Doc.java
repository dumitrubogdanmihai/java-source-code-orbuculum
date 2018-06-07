package ro.orbuculum.search.querent.jaxb;

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
public class Doc {
	private Map<String, String> fiels;

	@XmlJavaTypeAdapter(MapAdapter.class)
	public Map<String, String> getFiels() {
		return fiels;
	}

	public void setFiels(Map<String, String> fiels) {
		this.fiels = fiels;
	}
}
