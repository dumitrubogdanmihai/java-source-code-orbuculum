package ro.orbuculum.search.querent.jaxb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class MapAdapter extends XmlAdapter<Entries, Map<String, String>> {
	public Entries marshal(Map<String, String> arg0) throws Exception {
		Entries entries = new Entries();
		entries.setStr(new ArrayList<Str>());
		for (Map.Entry<String, String> entry : arg0.entrySet()) {
			Str field = new Str();
			field.setKey(entry.getKey());
			field.setValue(entry.getValue());
			entries.getStr().add(field);
		}
		return entries;
	}

	public Map<String, String> unmarshal(Entries arg0) throws Exception {
		Map<String, String> r = new HashMap<>();
		for (Str mapelement : arg0.getStr())
			r.put(mapelement.getKey(), mapelement.getValue());
		return r;
	}
}
