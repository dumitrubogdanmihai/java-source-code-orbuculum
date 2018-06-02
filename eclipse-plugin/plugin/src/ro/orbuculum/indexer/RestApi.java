package ro.orbuculum.indexer;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApi {
  @POST("/api/agent/index")
  Call<Void> index(@Query("path") String path);
}
