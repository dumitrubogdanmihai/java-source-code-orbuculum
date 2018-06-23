package ro.orbuculum.agent.indexer.handler;

import org.kohsuke.github.GHCommit.File;
import org.kohsuke.github.GHRepository;
import org.apache.solr.client.solrj.SolrClient;

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
   * Repository.
   */
	private final GHRepository repo;
	/**
	 * Java source file.
	 */
	private final String sourcesFilePath;
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
	 * Constructor.
	 * 
	 * @param projectDir   Project directory.
	 * @param sourcesFile  Source file.
	 */
	public Context(SolrClient solrClient, GHRepository repo, String sourcesFilePath) {
		this.solrClient = solrClient;
    this.repo = repo;
		this.sourcesFilePath = sourcesFilePath;
	}
	
	public boolean isClassInSamePackage(String className) {
	  String relativeFile = new java.io.File(sourcesFilePath).getParent() + className + ".java";
	  try {
	    repo.getFileContent(relativeFile);
	    return true;
	  } catch (Exception e) {
      return false;
    }
	}
	
	public String getSourcesFilePath() {
		return sourcesFilePath;
	}
	public GHRepository getRepo() {
		return repo;
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
}
