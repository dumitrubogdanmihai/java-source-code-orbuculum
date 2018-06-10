package ro.orbuculum.agent;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;

import ro.orbuculum.agent.rest.RESTIndexer;

public class Main {
	public static void main(String[] args) throws SolrServerException, IOException {
		try {
			new RESTIndexer().index(
					"/home/bogdan/development/github/java-source-code-orbuculum/indexer-agent");
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Invalid arguments. Use like this: -host localhost -port 8983 -core java-source-code -path /path/to/index/");
			throw e;
		}
	}
}
