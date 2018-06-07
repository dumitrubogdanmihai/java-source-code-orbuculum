package ro.orbuculum.agent.indexer.handler.unit.clazz.method.block.expr;

import java.util.Arrays;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.stmt.ExpressionStmt;

import ro.orbuculum.agent.indexer.handler.Handler;
import ro.orbuculum.agent.indexer.handler.unit.clazz.method.block.expr.call.MethodCallExprHandler;

public class ExpressionStmtHandler implements Handler {


	private CompilationUnit compilationUnit;
	private ClassOrInterfaceDeclaration classDecl;
	private SolrInputDocument document;

	public ExpressionStmtHandler (
			SolrInputDocument document, 
			CompilationUnit cu, 
			ClassOrInterfaceDeclaration classDecl) {
		this.document = document;
		this.compilationUnit = cu;
		this.classDecl = classDecl;
	}
	
	@Override
	public boolean handle(Node node) {
		return node instanceof ExpressionStmt;
	}

	@Override
	public List<Handler> getChildrenHandlers() {
		return Arrays.asList(new MethodCallExprHandler(this.document, this.compilationUnit, this.classDecl));
	}

	@Override
	public void commit(SolrClient client) {
	}
}
