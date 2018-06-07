package ro.orbuculum.search.querent.api;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import ro.orbuculum.search.querent.Unmarshaller;
import ro.orbuculum.search.querent.jaxb.Result;

class ResponseConverter implements Converter<ResponseBody, Result> {
	@Override
	public Result convert(ResponseBody value) throws IOException {
		try {
			return Unmarshaller.unmarshall(value.string());
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}
}
