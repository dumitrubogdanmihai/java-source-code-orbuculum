package ro.orbuculum.search.querent.api;

import java.io.IOException;
import java.util.function.Consumer;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import ro.orbuculum.search.querent.jaxb.Result;

public class Solr {
	static OkHttpClient client;
	static {
		Builder builder = new OkHttpClient.Builder();
		builder.addNetworkInterceptor(new Interceptor() {
			@Override
			public okhttp3.Response intercept(Chain chain) throws IOException {
				 Request request = chain.request();
				 System.err.println("request:"+request.url());
                 okhttp3.Response response = chain.proceed(request);
                 if (response.code() == 403) {
                 }
                 return response;
			}
		});
		client  = builder.build();
	}
	private static final Retrofit SOLR_CORE = new Retrofit.Builder()
			.baseUrl("http://localhost:8983/solr/orbuculum/")
			.addConverterFactory(new ConverterFactory())
			.client(client)
			.build();

	public static void get(String q, Consumer<Result> consumer) {
		System.err.println("q:"+q);
		SolrApi api = SOLR_CORE.create(SolrApi.class);
		if (q == null || q.isEmpty()) {
			q = "*:*";
		}
		System.err.println("q2:"+q);
		Call<Result> documents = api.getDocuments(q);
		documents.enqueue(new Callback<Result>() {
			@Override
			public void onResponse(Call<Result> arg0, Response<Result> arg1) {
				consumer.accept(arg1.body());
			}
			@Override
			public void onFailure(Call<Result> arg0, Throwable arg1) {
				System.err.println(arg0);
				arg1.printStackTrace();
				consumer.accept(null);
			}
		});
	}
}
