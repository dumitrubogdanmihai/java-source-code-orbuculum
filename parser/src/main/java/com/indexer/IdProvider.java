package com.indexer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

/**
 * Generated ID's for documents. 
 * @author Bogdan
 */
public class IdProvider {
	
	/**
	 * Private constructor.
	 */
	private IdProvider() {};

	/**
	 * Get id for {@link ClassOrInterfaceDeclaration}.
	 * @param unit	Compilation unit.
	 * @param node	Class.
	 * @return	id.
	 */
	public static String getId(CompilationUnit unit, ClassOrInterfaceDeclaration node) {
		return node.getName() + "." + unit.getPackage().getName().toString();
	}
}
