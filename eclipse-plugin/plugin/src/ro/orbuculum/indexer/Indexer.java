package ro.orbuculum.indexer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Indexer {
	
	private OutputStream os;
	private RestApi restApi;

	public Indexer(OutputStream os) {
		Retrofit retrofit = new Retrofit.Builder()
	    .baseUrl("http://localhost:8080")
	    .build();
		restApi = retrofit.create(RestApi.class);
		
		this.os = os;
	}
	
	public void indexJavaProjects() throws CoreException, IOException {
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		for (IProject project : workspaceRoot.getProjects()) {
			if (project.isOpen() && project.hasNature(JavaCore.NATURE_ID)) {
				IJavaProject javaProject = JavaCore.create(project);
				os.write(("Java project "+javaProject.getPath() + "\n").getBytes());
				IFolder outputFolder = workspaceRoot.getFolder(new Path("/playground2/src"));
				traverse(outputFolder);
			}
		}
	}

	private void traverse(IContainer container) throws CoreException, IOException {
		for (IResource member : container.members()) {
			if (member instanceof IFile) {
				IFile file = (IFile) member;
				index(file.getLocation().toFile());
			} else if (member instanceof IContainer) {
				traverse((IContainer) member);
			} else {
				os.write(("MEMBER: " + member + "\n").getBytes());
			}
		}
	}

	private void index(File file) throws IOException {
		os.write((" add to index: " + file + "\n").getBytes());
		Call<Void> call = restApi.index(file.getAbsolutePath());
		call.enqueue(new Callback<Void>() {
			
			@Override
			public void onResponse(Call<Void> call, Response<Void> response) {
				// TODO Auto-generated method stub
				System.err.println("onResponse: " + response);
			}
			
			@Override
			public void onFailure(Call<Void> call, Throwable t) {
				System.err.println("onFailure: " + call + " " + t);
			}
		});
	}
}
