package ro.orbuculum.search.querent;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import ro.orbuculum.search.querent.jaxb.Result;

public class TestQuerent {

	@Test
	public void testMarshall() throws JAXBException {
		Result result = new Result();
		Map<String, String> fields = new HashMap<>();
		fields.put("A", "B");
		result.setName("nume");
		result.setNumFound(1);
		result.setStart(1);
		result.setDocs(Arrays.asList(fields));
		String marshall = Unmarshaller.marshall(result);
		assertEquals(
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" + 
			"<result name=\"nume\" numFound=\"1\" start=\"1\">\n" + 
			"    <doc>\n" + 
			"        <str name=\"A\">B</str>\n" + 
			"    </doc>\n" + 
			"</result>\n" + 
			"", marshall);
	}

	@Test
	public void testUnmarshal() throws InterruptedException, IOException, JAXBException {
		String xml = 
				"<response>" +
				"<result name=\"response\" numFound=\"0\" start=\"0\">\n" + 
				"</result>\n"+
				"</response>\n";
		Result result = Unmarshaller.unmarshall(xml);
		assertEquals("response", result.getName());
		assertEquals(null, result.getDocs());
	}

	@Test
	public void testUnmarshal1() throws InterruptedException, IOException, JAXBException {
		String xml = 
				"<response>" +
				"<result name=\"response\" numFound=\"1\" start=\"0\">\n" + 
				"<doc>\n" + 
				"<str name=\"method\">method</str>\n" + 
				"<str name=\"method-q-name\">ro.orbuculum.agent.sample.Blank</str>\n" + 
				"<str name=\"class\">ro.orbuculum.agent.sample.Blank</str>\n" + 
				"<str name=\"offset\">3</str>\n" + 
				"<str name=\"id\">c06d0b2c-b948-4c22-a142-da2f41d6030a</str>\n" + 
				"</doc>\n" + 
				"</result>\n"+
				"</response>\n";
		Result result = Unmarshaller.unmarshall(xml);
		assertEquals("response", result.getName());
		assertEquals(1, result.getDocs().size());
		assertEquals(5, result.getDocs().get(0).size());
	}
	
	@Test
	public void testUnmarshal2() throws InterruptedException, IOException, JAXBException {
		String xml = 
				"<response>" +
				"<result name=\"response\" numFound=\"2\" start=\"0\">\n" + 
				"<doc>\n" + 
				"<str name=\"method\">method</str>\n" + 
				"<str name=\"method-q-name\">ro.orbuculum.agent.sample.Blank</str>\n" + 
				"<str name=\"class\">ro.orbuculum.agent.sample.Blank</str>\n" + 
				"<str name=\"offset\">3</str>\n" + 
				"<str name=\"id\">c06d0b2c-b948-4c22-a142-da2f41d6030a</str>\n" + 
				"</doc>\n" + 
				"<doc>\n" + 
				"<str name=\"method\">method</str>\n" + 
				"<str name=\"method-q-name\">ro.orbuculum.agent.sample.Blank</str>\n" + 
				"<str name=\"class\">ro.orbuculum.agent.sample.Blank</str>\n" + 
				"<str name=\"offset\">3</str>\n" + 
				"<str name=\"id\">c06d0b2c-b948-4c22-a142-da2f41d6030a</str>\n" + 
				"</doc>\n" + 
				"</result>\n"+
				"</response>\n";
		Result result = Unmarshaller.unmarshall(xml);
		assertEquals(Integer.valueOf(2), result.getNumFound());
		assertEquals(2, result.getDocs().size());
		assertEquals(5, result.getDocs().get(0).size());
	}
}
