package ro.orbuculum.agent.rest;

import java.io.IOException;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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
   * Give agent access to Github repositories.
   * 
   * @param oauthAccessToke Token.
   * 
   * @return 200 or 400.
   */
  @POST
  @Path("setOauthAccessToken")
  public Response index(
      @FormParam("oauthAccessToken") String oauthAccessToken) {
    try {
      agent.setAuthToken(oauthAccessToken);
      return Response.ok().build();
    } catch (IOException e) {
      logger.error(e, e);
      return Response.status(Status.BAD_REQUEST).build();
    }
  }

  /**
   * Tell agent to start tracking repository.
   * This would translate to: "Take care of him to be indexed from now on".
   * 
   * @param oauthAccessToke Token.
   * 
   * @return 200 or 400.
   */
  @POST
  @Path("setOauthAccessToken")
  public Response startTrackingRepository(
      @PathParam("repo") String repo) {
    agent.startTrackingRepository(repo);
    return Response.ok().build();
  }
}
