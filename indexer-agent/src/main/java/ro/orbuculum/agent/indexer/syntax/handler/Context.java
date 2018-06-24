package ro.orbuculum.agent.indexer.syntax.handler;

import org.apache.solr.client.solrj.SolrClient;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import ro.orbuculum.agent.indexer.syntax.handler.fs.FsAccess;

/**
 * Updated and passed around from one visitor to another.
 * 
 * @author bogdan
 */
public class Context {
	/**
	 * Java source file.
	 */
	private final String sourcesFilePath;
  /**
   * Project name.
   */
  private final String project;
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
	 * Solr client.
	 */
  private SolrClient solrClient;
  
  /**
   * Query abstract file system.
   */
  private FsAccess fsAccess;
	
	/**
	 * Constructor.
	 * 
	 * @param projectDir   Project directory.
	 * @param sourcesFile  Source file.
	 */
	public Context(
	    SolrClient solrClient, 
	    String project, 
	    String sourcesFilePath, 
	    FsAccess fsAccess) {
		this.solrClient = solrClient;
    this.project = project;
		this.sourcesFilePath = sourcesFilePath;
    this.fsAccess = fsAccess;
	}
	
	public String getSourcesFilePath() {
		return sourcesFilePath;
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

  public SolrClient getSolrClient() {
    return solrClient;
  }

  public String getProject() {
    return project;
  }

  public FsAccess getFsAccess() {
    return fsAccess;
  }
}
