package ro.orbuculum.agent.indexer.handler.unit.clazz.method.block.expr.call;

import java.util.Arrays;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;

import ro.orbuculum.agent.indexer.handler.Handler;

public class MethodCallExprHandler implements Handler {
	private Node node;
	private CompilationUnit compilationUnit;
	private ClassOrInterfaceDeclaration classDecl;
	private SolrInputDocument document;

	public MethodCallExprHandler (
			SolrInputDocument document, 
			CompilationUnit cu, 
			ClassOrInterfaceDeclaration classDecl) {
		this.document = document;
		this.compilationUnit = cu;
		this.classDecl = classDecl;
	}
	@Override
	public boolean handle(Node node) {
		this.node = node;
		// Recursively seek for method calls.
		return true;
	}

	@Override
	public List<Handler> getChildrenHandlers() {
		return Arrays.asList(new MethodCallExprHandler(document, compilationUnit, classDecl));
	}

	@Override
	public void commit(SolrClient client) {
		if (node instanceof MethodCallExpr) {
			MethodCallExpr call = (MethodCallExpr) node;
			String scope = call.getScope() != null ? call.getScope().toString() : null;
			String methodName = call.getName();
			//TODO: resolve scope..
			String methodQualifName = scope + "#" + methodName;
			document.addField("method-call", methodQualifName);
		}
	}
}