package ro.orbuculum.agent.indexer.handler.unit.clazz.method.block;

import java.util.Arrays;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;

import ro.orbuculum.agent.indexer.handler.Handler;
import ro.orbuculum.agent.indexer.handler.unit.clazz.method.block.expr.ExpressionStmtHandler;

public class BlockStmtHandler implements Handler {
	
	private CompilationUnit compilationUnit;
	private ClassOrInterfaceDeclaration classDecl;
	private SolrInputDocument document;

	public BlockStmtHandler (
			SolrInputDocument document, 
			CompilationUnit cu, 
			ClassOrInterfaceDeclaration classDecl) {
		this.document = document;
		this.compilationUnit = cu;
		this.classDecl = classDecl;
	}
	@Override
	public boolean handle(Node node) {
		return node instanceof BlockStmt;
	}

	@Override
	public List<Handler> getChildrenHandlers() {
		return Arrays.asList(new ExpressionStmtHandler(this.document, this.compilationUnit, this.classDecl));
	}

	@Override
	public void commit(SolrClient client) {
	}
}
