package ro.orbuculum.indexer;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Indexer server API definition.
 * 
 * @author bogdan
 */
public interface IndexerAgentApi {
  @POST("/api/index")
  Call<Void> index(@Query("projectPath") String projectPath);
}
