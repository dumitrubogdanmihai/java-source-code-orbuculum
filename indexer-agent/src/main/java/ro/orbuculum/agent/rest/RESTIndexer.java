package ro.orbuculum.agent.rest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;

import ro.orbuculum.agent.indexer.AstIndexer;
import ro.orbuculum.agent.indexer.Parser;

/**
 * The plug-in tells me what should be indexed.
 * 
 * @author bogdan
 */
@Path("/api/")
public class RESTIndexer {
  /**
   * Logger.
   */
  private static final Logger logger = LogManager.getLogger(RESTIndexer.class);

  /**
   * Parse and update the index of a given resource.
   * 
   * @param projectPath Project to be indexed.
   * 
   * @return   {@link HttpStatus#SC_OK}
   * 
   * @throws SolrServerException
   * @throws IOException
   */
  @POST
  @Path("index")
  public Response index(
      @QueryParam("projectPath") String projectPath) 
          throws SolrServerException, IOException {
    System.out.println(projectPath);
    
    AstIndexer indexer = new AstIndexer("8983", "orbuculum");
    File project = new File(projectPath);

    // Iterate over Java source files and index them.
    Files.walk(project.toPath())
    .filter(f -> f.toString().endsWith(".java"))
    .map(s -> new File(s.toUri()))
    .forEach(source -> {
      try {
        indexer.index(Parser.parse(source), project, source);
      } catch (SolrServerException | IOException e) {
        logger.warn(e, e);
      }
    });

    return Response.ok().build();
  }
  
 @GET
 @Path("index")
 public Response index() {
   return Response.ok().build();
  }
}
