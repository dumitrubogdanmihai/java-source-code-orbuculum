package ro.orbuculum.indexer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import ro.orbuculum.Activator;

/**
 * Indexer server interface.
 * 
 * @author bogdan
 */
public class Api {
  /**
   * Indexer API.
   */
  private IndexerAgentApi restApi;

  /**
   * Console output stream.
   */
  private OutputStream os;

  /**
   * OkHttp client.
   */
  private static OkHttpClient client = 
      new OkHttpClient.Builder()
      .readTimeout(1, TimeUnit.MINUTES)
      .build();
//  static {
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
//  }
  
  /**
   * Constructor.
   */
  public Api() {
    this(null);
  }
  /**
   * Constructor.
   * @param os Where to dump some logs.
   */
  public Api(OutputStream os) {
    this.os = os;
    Retrofit retrofit = new Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl("http://localhost:8080/")
        .client(client)
        .build();
    this.restApi = retrofit.create(IndexerAgentApi.class);
  }

  /**
   * Index currently opened Java projects.
   * 
   * @throws CoreException
   * @throws IOException
   */
  public void indexJavaProjects() throws CoreException, IOException {
    IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
    for (IProject project : workspaceRoot.getProjects()) {
      if (project.isOpen() && project.hasNature(JavaCore.NATURE_ID)) {
        IPath path = project.getLocation();
        IJavaProject javaProject = JavaCore.create(project);
        index(path, javaProject);
      }
    }
  }

  /**
   * Index Java project.
   * 
   * @param path          Fully qualified project path.
   * @param javaProject   The project.
   * 
   * @throws IOException
   */
  private void index(IPath path, IJavaProject javaProject) throws IOException {
    Call<Void> call = getRestApi().index(path.toString());
    call.enqueue(new Callback<Void>() {
      @Override
      public void onResponse(Call<Void> call, Response<Void> response) {
        try {
          if (os != null) {
            os.write((response.message() + response.code() + "\n").getBytes());
          }
        } catch (IOException e) {
          Activator.getDefault().getLog().log(new Status(Status.ERROR, Activator.PLUGIN_ID, e.toString(), e)); 
        }
      }

      @Override
      public void onFailure(Call<Void> call, Throwable e) {
        try {
          if (os != null) {
            os.write(("Indexing failed: " + e.toString()).getBytes());
            os.write(call.toString().getBytes());
          } else {
            System.err.println("Indexing failed: " + e + " " + call);
            e.printStackTrace();
          }
        } catch (IOException e1) {
          Activator.getDefault().getLog().log(new Status(Status.ERROR, Activator.PLUGIN_ID, e.toString(), e)); 
        }
      }
    });
  }

  /**
   * @return    Indexer-agent API.
   */
  public IndexerAgentApi getRestApi() {
    return restApi;
  }
}
