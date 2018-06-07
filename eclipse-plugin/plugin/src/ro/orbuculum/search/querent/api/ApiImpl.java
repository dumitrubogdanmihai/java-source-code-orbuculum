package ro.orbuculum.search.querent.api;

import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import ro.orbuculum.search.querent.jaxb.Result;

public class ApiImpl {
	private static final Retrofit SOLR_CORE = new Retrofit.Builder()
			.baseUrl("http://localhost:8983/solr/orbuculum/")
			.addConverterFactory(new ConverterFactory())
			.build();

	public static void get(String q, Consumer<Result> consumer) {
		Api api = SOLR_CORE.create(Api.class);
		Call<Result> documents = api.getDocuments(q);
		documents.enqueue(new Callback<Result>() {
			@Override
			public void onResponse(Call<Result> arg0, Response<Result> arg1) {
				consumer.accept(arg1.body());
			}
			@Override
			public void onFailure(Call<Result> arg0, Throwable arg1) {
				arg1.printStackTrace();
				consumer.accept(null);
			}
		});
	}
}
