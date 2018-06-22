package ro.orbuculum.search.querent.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ro.orbuculum.search.querent.jaxb.Result;

/**
 * Solr API definition.
 * 
 * @author bogdan
 */
interface SolrApi {
  @GET("select?wt=xml")
  Call<Result> getDocuments(
      @Query(value = "q") String q, 
      @Query(value = "sort") String sort);
  @GET("select?wt=xml")
  Call<Result> getDocuments(
      @Query(value = "q") String q);
}
