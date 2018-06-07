package ro.orbuculum.agent;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;

import org.junit.rules.ExternalResource;

public class SolrServerRule extends ExternalResource {
	private Process process;

	@Override
	protected void before() throws Throwable {
		ProcessBuilder pb = new ProcessBuilder("docker-compose", "up", "orbuculum");
		pb.directory(new File("../solr"));
		pb.redirectOutput(Redirect.INHERIT);
		pb.redirectError(Redirect.INHERIT);
		process = pb.start();
		super.before();
	}
	
	@Override
	protected void after() {
//		process.destroy();
//		super.after();
	}
}
