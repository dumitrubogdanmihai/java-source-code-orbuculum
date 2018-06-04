package ro.orbuculum.agent.indexer.handler.unit;

import java.util.Arrays;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;

import ro.orbuculum.agent.indexer.handler.Handler;
import ro.orbuculum.agent.indexer.handler.unit.clazz.ClassOrInterfaceDeclarationHandler;

public class CompilationUnitHandler implements Handler {
	private CompilationUnit compilationUnit;

	@Override
	public boolean handle(Node node) {
		if (node instanceof CompilationUnit) {
			compilationUnit = (CompilationUnit) node;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<Handler> getChildrenHandlers() {
		return Arrays.asList(new ClassOrInterfaceDeclarationHandler(compilationUnit));
	}

	@Override
	public void commit(SolrClient client) {
	}
	public static String getId(CompilationUnit unit) {
		return unit.getPackage().getName().toStringWithoutComments();
	}
}
