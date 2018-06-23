package ro.orbuculum.agent.indexer.handler;

import java.util.List;
import java.util.Optional;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;

/**
 * Main job is to resolve a class name to a fully qualified identifier. 
 * 
 * @author bogdan
 */
public class BindingsResolver {
	/**
	 * Logger.
	 */
	private static final Logger logger = LogManager.getLogger(BindingsResolver.class);

	/**
	 * Resolve a class name to a fully qualified identifier.
	 * 
	 * @param className  The simple name.
	 * @param context    The context.
	 * 
	 * @return Fully qualified or initial name.
	 */
	public static String resolveClassName(String className, Context context) {
		List<ImportDeclaration> imports = context.getCompilationUnit().getImports();
		if (imports != null) {
			// Look at imports.
			for (ImportDeclaration imp : imports) {
				if (imp.getName().getName().equals(className)) {
					return imp.getName().toString();
				}
			}
		}

		// Look for classes from the same package.
		if (context.isClassInSamePackage(className)) {
		  String pckge = context.getCompilationUnit().getPackage().getName().toString();
		  return pckge + "." + className;
		}

		// Look inside 'java.lang'
		try {
			Class.forName("java.lang." + className);
			return "java.lang." + className;
		} catch (ClassNotFoundException e) {}

		//TODO: Look for inner classes.

		if (!Character.isLowerCase(className.charAt(0))) {
		  logger.warn("Cannot resolve class name \"" + className + "\"" 
		      + " from " + context.getSourcesFilePath());
		}

		// May be a primitive type.
		return className;
	}

	/**
	 * Try to resolve a method call.
	 * 
	 * @param call     Method call node.
	 * @param context  The Context.
	 * 
	 * @return Fully qualified or initial name.
	 */
	public static String resolveMethodCall(MethodCallExpr call, Context context) {
		Expression scope = call.getScope();
		if (scope != null) {
			if (scope instanceof NameExpr) {
				NameExpr name = (NameExpr) scope;
				return resolveClassName(name.getName(), context) + "#" + call.getName();
			}
		} else {
			Optional<MethodDeclaration> classMethod = context.getClassDeclaration().getMembers().stream()
					.filter(m -> m instanceof MethodDeclaration)
					.map(m -> ((MethodDeclaration) m))
					.filter(m -> m.getName().equals(call.getName()))
					.findFirst();
			if (classMethod.isPresent()) {
				MethodDeclaration method = (MethodDeclaration) classMethod.get();
				String id = getId(method, context);
				return id;
			}
			
			List<ImportDeclaration> imports = context.getCompilationUnit().getImports();
			// TODO: check for static imports.
			for (ImportDeclaration imprt : imports) {
				if (imprt.isStatic()) {
					if(imprt.getName().toString().endsWith("." + call.getName())) {
						return imprt.getName().toString();
					}
				}
				
			}
		}
		return call.getName();
	}
	
	/**
	 * Compute the id of a source file.
	 * @param unit Compilation unit node.
	 * @return id.
	 */
	public static String getId(CompilationUnit unit) {
		return unit.getPackage().getName().toStringWithoutComments();
	}
	
	/**
	 * Compute the id of a class. 
	 * @param cu   Compilation unit.
	 * @param clzz The class.
	 * @return id.
	 */
	public static String getId(CompilationUnit cu, ClassOrInterfaceDeclaration clzz) {
		return getId(cu) + "." + clzz.getName();
	}

	/**
	 * Compute the id of a method.
	 * @param unit	Compilation unit.
	 * @param node	Method.
	 * @return	id.
	 */
	public static String getId(MethodDeclaration node, Context context) {
		String toReturn =  BindingsResolver.getId(context.getCompilationUnit(), context.getClassDeclaration());
		toReturn += "#" + node.getName() + "(";
		
		List<Parameter> parameters = node.getParameters();
		if (parameters != null) {
			for (int i = 0; i < parameters.size(); i++) {
				Parameter param = parameters.get(i);
				toReturn += BindingsResolver.resolveClassName(param.getType().toString(), context);
				if (i < parameters.size() -1) {
					toReturn += ", ";
				}
			}
		}
		toReturn += ")";
		return toReturn;
	}
}
