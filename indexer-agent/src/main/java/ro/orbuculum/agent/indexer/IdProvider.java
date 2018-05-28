package ro.orbuculum.agent.indexer;

import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.Type;

/**
 * Generated ID's for documents. 
 * @author Bogdan
 */
public class IdProvider {
	
	/**
	 * Private constructor.
	 */
	private IdProvider() {
	};

	/**
	 * Get id for {@link ClassOrInterfaceDeclaration}.
	 * @param unit	Compilation unit.
	 * @param node	Class.
	 * @return	id.
	 */
	public static String getId(CompilationUnit unit, ClassOrInterfaceDeclaration node) {
		return node.getName() + "." + unit.getPackage().getName().toString();
	}
	/**
	 * Get id for {@link MethodDeclaration}.
	 * @param unit	Compilation unit.
	 * @param node	Method.
	 * @return	id.
	 */
	public static String getId(CompilationUnit unit, ClassOrInterfaceDeclaration clazz, MethodDeclaration node) {
		String toReturn =  getId(unit, (ClassOrInterfaceDeclaration) node.getParentNode()) + "#" + node.getName();
		List<Parameter> parameters = node.getParameters();
		if (parameters != null) {
			toReturn = "(";
			for (int i = 0; i < parameters.size(); i++) {
				Parameter param = parameters.get(i);
				toReturn += resolveParameter(unit, param);
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
	public static String resolveParameter(CompilationUnit unit, Parameter param) {
		Type type = param.getType();

		List<ImportDeclaration> imports = unit.getImports();
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
