package ro.orbuculum.search.querent.api;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import ro.orbuculum.search.querent.jaxb.Result;

/**
 * Solr server interface.
 * 
 * @author bogdan
 */
public class Solr {
  /**
   * OkHttp client.
   */
  private static OkHttpClient client;
  static {
//    Builder builder = new OkHttpClient.Builder();
//    builder.addNetworkInterceptor(new Interceptor() {
//      @Override
//      public okhttp3.Response intercept(Chain chain) throws IOException {
//        Request request = chain.request();
//        System.err.println("request:"+request.url());
//        okhttp3.Response response = chain.proceed(request);
//        if (response.code() == 403) {
//        }
//        return response;
//      }
//    });
    client = new OkHttpClient.Builder().build();
  }
  
  /**
   * API interface builder.
   */
  private static final Retrofit SOLR_CORE = new Retrofit.Builder()
      .baseUrl("http://localhost:8983/solr/orbuculum/")
      .addConverterFactory(new ConverterFactory())
      .client(client)
      .build();

  /**
   * Query Solr index synchronously for all documents.
   * 
   * @return Parsed response.
   */
  public static Result get() {
    return get(null);
  }
  
  /**
   * Query Solr index synchronously.
   * 
   * @param q Query. May be <code>null</code>.
   * 
   * @return Parsed response.
   */
  public static Result get(String q) {
    final AtomicReference<Result> resultRef = new AtomicReference<Result>();
    
    get(null, new Consumer<Result>() {
      public void accept(Result r) {
        resultRef.set(r);
        synchronized (resultRef) {
          resultRef.notifyAll();
        }
      }
    });
  
    synchronized (resultRef) {
      try {
        resultRef.wait(2000);
      } catch (InterruptedException e) {
        // Ignore.
      }
    }
    
    return resultRef.get();
  }

  /**
   * Query Solr index.
   * 
   * @param q Query.
   * @param consumer Consumer.
   */
  public static void get(String q, Consumer<Result> consumer) {  
    SolrApi api = SOLR_CORE.create(SolrApi.class);
    if (q == null || q.isEmpty() || !q.contains("*")) {
      q = "*:*";
    }
    
    Call<Result> documents = api.getDocuments(q);
    documents.enqueue(new Callback<Result>() {
      @Override
      public void onResponse(Call<Result> arg0, Response<Result> arg1) {
        consumer.accept(arg1.body());
      }
      
      @Override
      public void onFailure(Call<Result> arg0, Throwable e) {
        e.printStackTrace();
//        Activator.getDefault().getLog().log(new Status(Status.ERROR, Activator.PLUGIN_ID, e.toString(), e)); 
        consumer.accept(null);
      }
    });
  }
}
