package ro.orbuculum.agent;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;

import org.junit.rules.ExternalResource;

public class ResetSolrCoreRule extends ExternalResource {

	@Override
	protected void before() throws Throwable {
		ProcessBuilder pb = new ProcessBuilder("bash", "build-core-safe.sh");
		pb.directory(new File("../solr"));
		pb.redirectOutput(Redirect.INHERIT);
		pb.redirectError(Redirect.INHERIT);
		Process process = pb.start();
		process.waitFor();
	}
	
	@Override
	protected void after() {
		super.after();
	}
}
