package ro.orbuculum.agent.rest;

import java.io.IOException;

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
	public Response index (
			@QueryParam("path") String path) 
					throws SolrServerException, IOException {
		GraphWalker indexer = new GraphWalker("8983", "java-source-code");
		indexer.index(path);
		return Response.ok().build();
	}
}
