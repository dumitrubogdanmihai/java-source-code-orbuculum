package ro.orbuculum.search.querent.api;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.eclipse.core.runtime.Status;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import ro.orbuculum.Activator;
import ro.orbuculum.search.querent.Unmarshaller;

/**
 * Used to automatically unmarshal the HTTP response.
 * 
 * @author bogdan
 */
class ResponseConverter implements Converter<ResponseBody, Result> {
	@Override
	public Result convert(ResponseBody value) throws IOException {
		try {
			return Unmarshaller.unmarshal(value.string());
		} catch (JAXBException e) {  
		  Activator.getDefault().getLog().log(new Status(Status.ERROR, Activator.PLUGIN_ID, e.toString(), e)); 
		}
		return null;
	}
}
