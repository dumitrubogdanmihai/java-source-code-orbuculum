package ro.orbuculum.indexer;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Indexer server API definition.
 * 
 * @author bogdan
 */
public interface IndexerAgentApi {
  @POST("/api/indexLocalFilePath")
  @Deprecated
  // TODO: Remove me...
  Call<Void> index(@Query("localFilePath") String localFilePath);
  
  @POST("/api/startTrackingRepository")
  Call<Void> startTrackingRepository(
      @Query("repo") String repo);

  @POST("/api/setOauthAccessToken")
  Call<Void> setOauthAccessToken(
      @Field("oauthAccessToken") String oauthAccessToken);

  @GET("/api/getAuthorizedEmail")
  Call<String> getAuthorizedEmail();

  @GET("/api/getTrackedRepositories")
  Call<String> getTrackedRepositories();
}