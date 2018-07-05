package ro.orbuculum.search.querent.api;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaCore;

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
    
    System.err.println("Solr get q : " + q);    

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
  
  public static List<String> getOpenedProjects() {
    List<String> toReturn = new ArrayList<>();
    IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
    IProject[] projects = workspaceRoot.getProjects();
    for (int i = 0; i < projects.length; i++) {
      IProject project = projects[i];
      try {
        if (project.isOpen()/* && project.hasNature(JavaCore.NATURE_ID)*/) {
          toReturn.add(project.getName());
        } else {
          System.err.println("Project : " + project.getName() + "  " + project.isOpen() + " "  + project.hasNature(JavaCore.NATURE_ID));
        }
      } catch (CoreException e) {
        e.printStackTrace();
      }
    }
    return toReturn;
  }
  
  public static String getQueryOpenedProjects(List<String> openedProjects) {
    String toReturn = "(";
        
    if (openedProjects.isEmpty()) {
      toReturn += "project:*";
    } else {
      for (int i = 0; i < openedProjects.size(); i++) {
        if (i != openedProjects.size() - 1) {
          toReturn += " OR ";
        }
        toReturn += "project:\"" + openedProjects.get(i) + "\"";
      }
    }

    toReturn += ")";
    return toReturn;
  }
}
