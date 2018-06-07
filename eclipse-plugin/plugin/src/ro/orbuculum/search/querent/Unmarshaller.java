package ro.orbuculum.search.querent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import ro.orbuculum.search.querent.jaxb.Response;
import ro.orbuculum.search.querent.jaxb.Result;

public class Unmarshaller {

	public static String marshall(Result result) throws JAXBException {
		StringWriter sw = new StringWriter();
		JAXBContext newInstance = JAXBContext.newInstance(Response.class);
		Marshaller marshaller = newInstance.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(result, sw);
		return sw.toString();
	}
	
	
	public static Result unmarshall(String xml) throws JAXBException {
		Result toReturn = null;
		JAXBContext newInstance = JAXBContext.newInstance(Response.class);
		javax.xml.bind.Unmarshaller unmarshaller = newInstance.createUnmarshaller();
		
		ByteArrayInputStream is = null;
		try {
			is = new ByteArrayInputStream(xml.getBytes());
			Response response = (Response) unmarshaller.unmarshal(is);
			toReturn = response.getResult();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return toReturn;
	}
}
