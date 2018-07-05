package ro.orbuculum.agent.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ro.orbuculum.agent.Agent;
import ro.orbuculum.agent.indexer.syntax.AstIndexer;

/**
 * The plug-in tells me what should be indexed.
 * 
 * @author bogdan
 */
@Path("/api/")
public class RESTAgent {
  /**
   * Logger.
   */
  private static final Logger logger = LogManager.getLogger(RESTAgent.class);

  /**
   * Employed to keep Solr index updated.
   */
  private Agent agent;

  /**
   * Constructor.
   */
  public RESTAgent() {
    AstIndexer indexer = new AstIndexer();
    this.agent = new Agent(indexer);
  }

  /**
   * {@link Agent#startTrackingRepository(String)}
   */
  @POST
  @Path("startTrackingRepository")
  public Response startTrackingRepository(
      @QueryParam("repo") String repo) {
    agent.startTrackingRepository(repo);
    return Response.ok().build();
  }
  
  /**
   * {@link Agent#startTrackingRepository(String)}
   */
  @GET
  @Path("startTrackingRepository")
  public Response startTrackingRepositoryGet(
      @QueryParam("repo") String repo) {
    agent.startTrackingRepository(repo);
    return Response.ok().build();
  }

  /**
   * {@link Agent#getTrackedRepositories()}
   * @return
   */
  @GET
  @Path("getTrackedRepositories")
  public String getTrackedRepositories() {
    return agent.getTrackedRepositories().toString();
  }
}
