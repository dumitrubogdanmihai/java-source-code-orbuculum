package ro.orbuculum.search.querent.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ro.orbuculum.search.querent.jaxb.Result;

public interface Api {
	@GET("select?wt=xml")
	Call<Result> getDocuments(@Query("q") String q);
	@GET("select?wt=xml&q=*:*")
	Call<Result> getDocuments();
}
