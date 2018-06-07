package ro.orbuculum.agent.indexer.handler.unit.clazz;

import java.util.Arrays;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import ro.orbuculum.agent.indexer.handler.Handler;
import ro.orbuculum.agent.indexer.handler.unit.CompilationUnitHandler;
import ro.orbuculum.agent.indexer.handler.unit.clazz.method.MethodDeclarationHandler;

public class ClassOrInterfaceDeclarationHandler implements Handler {
	private ClassOrInterfaceDeclaration classDeclaration;
	private CompilationUnit compilationUnit;

	public ClassOrInterfaceDeclarationHandler(CompilationUnit compilationUnit) {
		this.compilationUnit = compilationUnit;
	}
	
	@Override
	public boolean handle(Node node) {
		if (node instanceof  ClassOrInterfaceDeclaration) {
			 classDeclaration = (ClassOrInterfaceDeclaration) node;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<Handler> getChildrenHandlers() {
		return Arrays.asList(new MethodDeclarationHandler(compilationUnit, classDeclaration));
	}

	@Override
	public void commit(SolrClient client) {
	}

	public static String getId(CompilationUnit cu, ClassOrInterfaceDeclaration unit) {
		return CompilationUnitHandler.getId(cu) + "." + unit.getName();
	}
}
