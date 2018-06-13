package ro.orbuculum.agent.indexer.handler;

import java.io.File;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

/**
 * Updated and passed around from one visitor to another.
 * 
 * @author bogdan
 */
public class Context {
  /**
   * Project directory.
   */
	private final File projectDir;
	/**
	 * Java source file.
	 */
	private final File sourcesFile;
	/**
	 * Compilation unit node.
	 */
	private CompilationUnit compilationUnit;
	/**
	 * Class node.
	 */
	private ClassOrInterfaceDeclaration classDeclaration;
	/**
	 * Method node.
	 */
	private MethodDeclaration method;
	
	/**
	 * Constructor.
	 * 
	 * @param projectDir   Project directory.
	 * @param sourcesFile  Source file.
	 */
	public Context(File projectDir, File sourcesFile) {
		this.projectDir = projectDir;
		this.sourcesFile = sourcesFile;
	}
	
	public File getSourcesFile() {
		return sourcesFile;
	}
	public File getProjectDir() {
		return projectDir;
	}

	public CompilationUnit getCompilationUnit() {
		return compilationUnit;
	}

	public void setCompilationUnit(CompilationUnit compilationUnit) {
		this.compilationUnit = compilationUnit;
	}

	public ClassOrInterfaceDeclaration getClassDeclaration() {
		return classDeclaration;
	}

	public void setClassDeclaration(ClassOrInterfaceDeclaration classDeclaration) {
		this.classDeclaration = classDeclaration;
	}

	public MethodDeclaration getMethod() {
		return method;
	}

	public void setMethod(MethodDeclaration method) {
		this.method = method;
	}
}
