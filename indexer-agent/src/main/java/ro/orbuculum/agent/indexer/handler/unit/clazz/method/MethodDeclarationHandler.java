package ro.orbuculum.agent.indexer.handler.unit.clazz.method;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.Type;

import ro.orbuculum.agent.indexer.handler.Handler;
import ro.orbuculum.agent.indexer.handler.unit.clazz.ClassOrInterfaceDeclarationHandler;
import ro.orbuculum.agent.indexer.handler.unit.clazz.method.block.BlockStmtHandler;

public class MethodDeclarationHandler implements Handler {

	/**
	 * Logger.
	 */
	private static final Logger logger = LogManager.getLogger(MethodDeclarationHandler.class);

	private SolrInputDocument document;
	private CompilationUnit compilationUnit;
	private ClassOrInterfaceDeclaration classDecl;

	public MethodDeclarationHandler(
			CompilationUnit cu, 
			ClassOrInterfaceDeclaration classDecl) {
		this.compilationUnit = cu;
		this.classDecl = classDecl;
	}
	@Override
	public boolean handle(Node node) {
		if (node instanceof  MethodDeclaration) {
			MethodDeclaration method = (MethodDeclaration) node;
			document = new SolrInputDocument();
			document.addField("method", method.getName());
			document.addField("method-q-name", getId(method));
			document.addField("method-raw", method.toString());
			document.addField("class", ClassOrInterfaceDeclarationHandler.getId(compilationUnit, classDecl));
			for (Parameter param : method.getParameters()) {
				document.addField("parameter", resolveParameter(param));
			}
			document.addField("javadoc", method.getJavaDoc());
			document.addField("offset", method.getBeginLine());
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<Handler> getChildrenHandlers() {
		return Arrays.asList(new BlockStmtHandler(this.document, this.compilationUnit, this.classDecl));
	}

	@Override
	public void commit(SolrClient solr) {
		try {
			solr.add(document);
			UpdateResponse response = solr.commit();
			int status = response.getStatus();
			if (status != 102) {
				logger.warn(response);
			}
		} catch (SolrServerException | IOException e) {
			logger.warn(e, e);
		}
	}
	
	/**
	 * Get id for {@link MethodDeclaration}.
	 * @param unit	Compilation unit.
	 * @param node	Method.
	 * @return	id.
	 */
	String getId(MethodDeclaration node) {
		String toReturn =  ClassOrInterfaceDeclarationHandler.getId(compilationUnit, classDecl);
		List<Parameter> parameters = node.getParameters();
		if (parameters != null) {
			toReturn = "(";
			for (int i = 0; i < parameters.size(); i++) {
				Parameter param = parameters.get(i);
				toReturn += resolveParameter(param);
				if (i < parameters.size() -1) {
					toReturn += ", ";
				}
			}
			toReturn = ")";
		}
		return toReturn;
	}

	/**
	 * TODO
	 * @param unit
	 * @param param
	 * @return
	 */
	private String resolveParameter(Parameter param) {
		Type type = param.getType();

		List<ImportDeclaration> imports = compilationUnit.getImports();
		if (imports != null) {
			for (ImportDeclaration imp : imports) {
				// This may be unreliable because:
				// classes from same package, root and java.lang package are automatically imported.
				// you may import all static classes declared inside another class.
				if (imp.getName().getName().equals(type.toString())) {
					return imp.getName().toString();
				}
			}
		}
		return type.toString();
	}
}
