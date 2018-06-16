package ro.orbuculum.search.querent.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Solr API definition.
 * 
 * @author bogdan
 */
interface SolrApi {
	@GET("select?wt=xml")
	Call<Result> getDocuments(@Query(value = "q") String q);
}
