package ro.orbuculum.indexer;

import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import ro.orbuculum.Activator;

/**
 * Indexer server interface.
 * 
 * @author bogdan
 */
public class Indexer {
  /**
   * Indexer API.
   */
	private IndexerAgentApi restApi;
	
	/**
	 * Console output stream.
	 */
  private OutputStream os;

	/**
	 * Constructor.
	 * @param os Where to dump some logs.
	 */
	public Indexer(OutputStream os) {
		this.os = os;
    Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("http://localhost:8080")
				.build();
		restApi = retrofit.create(IndexerAgentApi.class);
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

	private void index(IPath path, IJavaProject javaProject) throws IOException {
		Call<Void> call = restApi.index(path.toString());
		call.enqueue(new Callback<Void>() {
			@Override
			public void onResponse(Call<Void> call, Response<Void> response) {
			  try {
          os.write((response.message() + response.code() + "\n").getBytes());
        } catch (IOException e) {
          Activator.getDefault().getLog().log(new Status(Status.ERROR, Activator.PLUGIN_ID, e.toString(), e)); 
        }
			}

			@Override
			public void onFailure(Call<Void> call, Throwable e) {
			  Activator.getDefault().getLog().log(new Status(Status.ERROR, Activator.PLUGIN_ID, e.toString(), e)); 
			}
		});
	}
}
