package ro.orbuculum.agent;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

import org.junit.rules.ExternalResource;

public class SolrServerRule extends ExternalResource {
	private Process process;

	@Override
	protected void before() throws Throwable {
		super.before();

		ProcessBuilder pb = new ProcessBuilder("docker-compose", "up", "orbuculum");
		pb.directory(new File("../solr"));
		pb.redirectOutput(Redirect.INHERIT);
		pb.redirectError(Redirect.INHERIT);
		process = pb.start();
	}
	
	@Override
	protected void after() {
		super.after();
		process.destroy();

		ProcessBuilder pb = new ProcessBuilder("docker-compose", "stop");
		pb.directory(new File("../solr"));
		pb.redirectOutput(Redirect.INHERIT);
		pb.redirectError(Redirect.INHERIT);
		try {
			Process stopProcess = pb.start();
			stopProcess.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
