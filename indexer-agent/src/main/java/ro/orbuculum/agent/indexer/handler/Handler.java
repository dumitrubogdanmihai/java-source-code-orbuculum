package ro.orbuculum.agent.indexer.handler;

import java.util.List;

import org.apache.solr.client.solrj.SolrClient;

import com.github.javaparser.ast.Node;

public interface Handler {
	boolean handle(Node node);
	List<Handler> getChildrenHandlers();
	void commit(SolrClient client);
}
