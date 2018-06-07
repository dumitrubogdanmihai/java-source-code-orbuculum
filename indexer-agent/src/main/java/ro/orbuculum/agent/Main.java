package ro.orbuculum.agent;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;

import ro.orbuculum.agent.indexer.GraphWalker;

/**
 * Index recursively java source code from a give directory..
 */
public class Main {
	/**
	 * Indexer entrypoint.
	 * 
	 * @param args  Use like: "-host localhost -port 8983 -core java-source-code -path /path/to/index"
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	public static void main(String[] args) throws SolrServerException, IOException {
		try {
			List<String> input = Arrays.asList(args);

			String host = "localhost";  
			int hostIndex = input.indexOf("-host");
			if (hostIndex != -1) {
				host = input.get(hostIndex + 1);
			}

			String port = "8983";  
			int portIndex = input.indexOf("-port");
			if (portIndex != -1) {
				port = input.get(portIndex + 1);
			}

			String core = "java-source-code";  
			int coreIndex = input.indexOf("-core");
			if (coreIndex != -1) {
				core = input.get(coreIndex + 1);
			}

			String rootPath = ".";  
			int rootPathIndex = input.indexOf("-path");
			if (rootPathIndex != -1) {
				rootPath = input.get(rootPathIndex + 1);
			}

			GraphWalker dfs = new GraphWalker(host, port, core);
			dfs.index(rootPath);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Invalid arguments. Use like this: -host localhost -port 8983 -core java-source-code -path /path/to/index/");
			throw e;
		}
	}
}
