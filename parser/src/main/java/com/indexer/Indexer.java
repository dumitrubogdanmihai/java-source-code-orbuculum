package com.indexer;

import java.io.IOException;
import java.util.stream.Stream;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

import com.github.javaparser.ast.Node;

/**
 * Index Java sources to Solr.
 * @author Bogdan
 */
public class Indexer {
	/**
	 * Client used to index documents.
	 */
	private final SolrClient solr;
	
	/**
	 * Constructor.
	 * @param port	Solr server port.
	 * @param core	Target core.
	 */
	public Indexer(String port, String core) {
		this("localhost", port, core);
	}

	/**
	 * Constructor.
	 * @param host 	Solr server host.
	 * @param port  Solr server port.
	 * @param core  Target core.
	 */
	public Indexer(String host, String port, String core) {
		this.solr = new HttpSolrClient.Builder("http://" + host + ":" + port + "/solr/DITA").build();
	}

	/**
	 * Index file's fields.
	 * @param file	Root path of java sources to be indexed.
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public void index(String rootPath) throws SolrServerException, IOException {
		Stream<Node> nodesStream = Parser.getNodesStream(rootPath);
		nodesStream.forEach(n -> index(n));
	}
	
	/**
	 * Index a node element.
	 * @param node	The node element.
	 */
	private void index(Node node) {
		try {
			SolrInputDocument document = new SolrInputDocument();
			document.addField("file", "");
			solr.add(document);
			solr.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
