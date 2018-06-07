package ro.orbuculum.agent.indexer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;

import ro.orbuculum.agent.indexer.handler.Handler;
import ro.orbuculum.agent.indexer.handler.unit.CompilationUnitHandler;
import ro.orbuculum.agent.indexer.parser.Parser;

/**
 * Index Java sources to Solr.
 * @author Bogdan
 */
public class GraphWalker {
	/**
	 * Client used to index documents.
	 */
	private final SolrClient solr;

	/**
	 * Constructor.
	 * @param port	Solr server port.
	 * @param core	Target core.
	 */
	public GraphWalker(String port, String core) {
		this("localhost", port, core);
	}

	/**
	 * Constructor.
	 * @param host 	Solr server host.
	 * @param port  Solr server port.
	 * @param core  Target core.
	 */
	public GraphWalker(String host, String port, String core) {
		this.solr = new HttpSolrClient.Builder("http://" + host + ":" + port + "/solr/" + core).build();
	}

	/**
	 * Index file's fields.
	 * @param file	Root path of java sources to be indexed.
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public void index(String path) throws SolrServerException, IOException {
		CompilationUnit unit = Parser.parse(new File(path));
		index(unit, Arrays.asList(new CompilationUnitHandler()));
	}

	/**
	 * Index a parsed java file as a {@link CompilationUnit}.
	 * @param unit	Compilation unit to index.
	 */
	public void index(Node node, List<Handler> handlers) {
		if (handlers != null && ! handlers.isEmpty()) {
			for (Handler handler : handlers) {
				if (handler.handle(node)) {
					List<Node> children = node.getChildrenNodes();
					for (Node child : children) {
						List<Handler> childrenHandlers = handler.getChildrenHandlers();
						index(child, childrenHandlers);
					}
					handler.commit(solr);			
				}
			}
		}
	}
}
