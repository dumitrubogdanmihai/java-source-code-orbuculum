package ro.orbuculum.agent.indexer.syntax.handler.unit.clazz.method.block.expr;

import java.util.Arrays;
import java.util.List;

import org.apache.solr.common.SolrInputDocument;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.ExpressionStmt;

import ro.orbuculum.agent.indexer.syntax.handler.Context;
import ro.orbuculum.agent.indexer.syntax.handler.Visitor;
import ro.orbuculum.agent.indexer.syntax.handler.unit.clazz.method.block.expr.call.MethodCallExprVisitor;

/**
 * Expression visitor that delegate to method call visitor.
 * 
 * @author bogdan
 */
public class ExpressionStmtVisitor implements Visitor {
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
	 * @param document Document to be indexed at the end of this sub tree.
	 * @param context  Context.
	 */
	public ExpressionStmtVisitor (
			SolrInputDocument document, 
			Context context) {
		this.document = document;
		this.context = context;
	}
	
	@Override
	public boolean startVisit(Node node) {
		return node instanceof ExpressionStmt;
	}

	@Override
	public List<Visitor> getChildrenVisitors() {
		return Arrays.asList(new MethodCallExprVisitor(this.document, context));
	}
}
