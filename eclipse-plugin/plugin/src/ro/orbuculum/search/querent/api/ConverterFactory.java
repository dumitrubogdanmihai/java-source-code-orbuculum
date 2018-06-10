package ro.orbuculum.search.querent.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.Converter;
import retrofit2.Retrofit;

final class ConverterFactory extends Converter.Factory {

	public static ConverterFactory create() {
		return new ConverterFactory();
	}

	@Override
	public ResponseConverter
	responseBodyConverter(
			Type type, 
			Annotation[] 
			annotations,
			Retrofit retrofit) {
		return new ResponseConverter();
	}

	@Override
	public ResultConverter requestBodyConverter(
			Type type,
			Annotation[] parameterAnnotations, 
			Annotation[] methodAnnotations, 
			Retrofit retrofit) {
		return new ResultConverter();
	}
}