package ro.orbuculum.agent.indexer.handler.unit.clazz.method.block;

import java.util.Arrays;
import java.util.List;

import org.apache.solr.common.SolrInputDocument;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.BlockStmt;

import ro.orbuculum.agent.indexer.handler.Context;
import ro.orbuculum.agent.indexer.handler.Visitor;
import ro.orbuculum.agent.indexer.handler.unit.clazz.method.block.expr.ExpressionStmtVisitor;

/**
 * Block visitor that only delegates to the expression visitor.
 * 
 * @author bogdan
 */
public class BlockStmtVisitor implements Visitor {
	
  /**
   * Method's document.
   */
	private SolrInputDocument document;
	
	/**
	 * Context.
	 */
	private Context context;

	/**
	 * Constructor.
	 * @param document Document to be indexed at the end of this subtree.
	 * @param context  Context.
	 */
	public BlockStmtVisitor (SolrInputDocument document, Context context) {
		this.document = document;
		this.context = context;
	}
	@Override
	public boolean visit(Node node) {
		return node instanceof BlockStmt;
	}

	@Override
	public List<Visitor> getChildrenVisitors() {
		return Arrays.asList(new ExpressionStmtVisitor(document, context));
	}
}