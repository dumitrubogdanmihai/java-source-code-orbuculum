package ro.orbuculum.agent.indexer.handler.unit.clazz.method.block.expr.call;

import java.util.Arrays;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.MethodCallExpr;

import ro.orbuculum.agent.indexer.handler.Context;
import ro.orbuculum.agent.indexer.handler.Visitor;
import ro.orbuculum.agent.indexer.handler.BindingsResolver;

/**
 * Method call visitor.
 * 
 * @author bogdan
 */
public class MethodCallExprVisitor implements Visitor {
  /**
   * AST node.
   */
	private Node node;
	
	/**
	 * Document to be indexed.
	 */
	private SolrInputDocument document;
	
	/**
	 * Context.
	 */
	private Context context;

	/**
	 * Constructor.
	 * @param document Document to be indexed.
	 * @param context  Context.
	 */
	public MethodCallExprVisitor (
			SolrInputDocument document, 
			Context context) {
		this.document = document;
		this.context = context;
	}
	
	@Override
	public boolean visit(Node node) {
		this.node = node;
		// Recursively seek for method calls.
		return true;
	}

	@Override
	public List<Visitor> getChildrenVisitors() {
		return Arrays.asList(new MethodCallExprVisitor(document, context));
	}

	@Override
	public void commit(SolrClient client) {
		if (node instanceof MethodCallExpr) {
			MethodCallExpr call = (MethodCallExpr) node;
			document.addField("method-call", BindingsResolver.resolveMethodCall(call, context));
		}
	}
}