package com.indexer;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

/**
 * Index Java sources to Solr.
 * @author Bogdan
 */
public class Indexer {
	/**
	 * Logger.
	 */
	private static final Logger logger = LogManager.getLogger(Indexer.class);
	
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
		this.solr = new HttpSolrClient.Builder("http://" + host + ":" + port + "/solr/" + core).build();
	}

	/**
	 * Index file's fields.
	 * @param file	Root path of java sources to be indexed.
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public void index(String rootPath) throws SolrServerException, IOException {
		Stream<CompilationUnit> unitsStream = Parser.getNodesStream(rootPath);
		unitsStream.forEach(u -> index(u));
	}
	
	/**
	 * Index a node element.
	 * @param unit	Compile unit to index (extracted from a .java file).
	 */
	private void index(CompilationUnit unit) {
		unit.getChildrenNodes().stream()
		.filter(u -> u instanceof ClassOrInterfaceDeclaration)
		.map(u -> (ClassOrInterfaceDeclaration) u)
		.forEach(c -> index(unit, c));
	}

	/**
	 * Index a class.
	 * @param unit		Compile unit.	
	 * @param clazz		Class to index.
	 */
	private void index(CompilationUnit unit, ClassOrInterfaceDeclaration clazz) {
		List<Node> nodes = clazz.getChildrenNodes();
		for (Node n : nodes) {
			if (n instanceof MethodDeclaration) {
				try {
					MethodDeclaration method = (MethodDeclaration) n;
					String methodName = method.getName();
					String className = clazz.getName();
					String packageName = unit.getPackage().getName().toString();
					
					String id = packageName + "." + className;
					
					SolrInputDocument document = new SolrInputDocument();
					document.addField("id", id);
					document.addField("method", methodName);
					
					solr.add(document);
					solr.commit();
				} catch (Exception e) {
					logger.warn(e, e);
				}
			} else if (n instanceof ClassOrInterfaceDeclaration) {
				index(unit, (ClassOrInterfaceDeclaration) n);
			}
		}
	}
	
	/**
	 * Deep flatten.
	 * @param node	Node with inner nodes.
	 * @return		Flatten stream.
	 */
	@SuppressWarnings("unused")
	private static Stream<Node> flatten(Node node) {
		return Stream.concat(
				Stream.of(node), 
				node.getChildrenNodes().stream().flatMap(Indexer::flatten));
	}
}
