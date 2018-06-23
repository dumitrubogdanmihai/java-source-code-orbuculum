package ro.orbuculum.agent.indexer;

import java.io.InputStream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

/**
 * Parser for Java source files.
 */
public class Parser {

  /**
   * Logger.
   */
  private static final Logger logger = LogManager.getLogger(Parser.class);

  /**
   * Parse a source file and return a instance of {@link CompilationUnit} 
   * that is the root of the Abstract Syntax Tree.
   * 
   * @param is To be parsed.
   * 
   * @return Corresponding {@link CompilationUnit} or <code>null</code> if parse fails.
   * TODO: Consider using {@link java.util.Optional}
   */
  public CompilationUnit parse(InputStream is) {
    try {
      return JavaParser.parse(is);
    } catch (Exception e) {
      logger.warn(e, e);
    }
    return null;
  }
}
