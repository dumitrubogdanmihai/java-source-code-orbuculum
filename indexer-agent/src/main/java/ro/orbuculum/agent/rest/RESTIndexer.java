package ro.orbuculum.agent.rest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.solr.client.solrj.SolrServerException;

import ro.orbuculum.agent.indexer.GraphWalker;

@Path("/api/agent")
public class RESTIndexer {
	@POST
	@Path("index")
	public Response index (@QueryParam("projectPath") String projectPath) 
					throws SolrServerException, IOException {
		GraphWalker indexer = new GraphWalker("8983", "orbuculum");
		File project = new File(projectPath);
		
		Files.walk(project.toPath())
    .filter(s -> s.toString().endsWith(".java"))
    .map(p -> new File(p.toUri()))
    .forEach(f -> {
			try {
				indexer.index(project, f);
			} catch (SolrServerException | IOException e) {
				e.printStackTrace();
			}
		});
		
		return Response.ok().build();
	}
}
