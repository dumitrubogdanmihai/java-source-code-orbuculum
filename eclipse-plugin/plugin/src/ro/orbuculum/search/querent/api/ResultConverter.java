package ro.orbuculum.search.querent.api;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;
import ro.orbuculum.search.querent.Unmarshaller;
import ro.orbuculum.search.querent.jaxb.Result;

class ResultConverter implements Converter<Result, RequestBody > {
	private static final MediaType MEDIA_TYPE = MediaType.parse("application/xml; charset=UTF-8");

	@Override
	public RequestBody convert(Result result) throws IOException {
		try {
			return RequestBody.create(MEDIA_TYPE, Unmarshaller.marshall(result));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}
}