package ro.orbuculum.agent.rest;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.solr.client.solrj.SolrServerException;

import ro.orbuculum.agent.indexer.Indexer;

@Path("/api/agent")
public class RESTAgent {
	@GET
	@Path("index")
	public Response index (
			@QueryParam("path") String path) 
					throws SolrServerException, IOException {
		Indexer indexer = new Indexer("8983", "java-source-code");
		indexer.indexAll(path);
		return Response.ok().build();
	}
}
