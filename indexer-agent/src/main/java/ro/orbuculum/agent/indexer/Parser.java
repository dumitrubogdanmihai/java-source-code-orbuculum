package ro.orbuculum.agent.indexer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
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
   * @param f File to be parsed.
   * 
   * @return Corresponding {@link CompilationUnit} or <code>null</code> if parse fails.
   */
  public static CompilationUnit parse(InputStream is) {
//    try {
//      String text = IOUtils.toString(is, StandardCharsets.UTF_8.name());
//      System.err.println("IS: " + text);
//    } catch (IOException e1) {
//      // TODO Auto-generated catch block
//      e1.printStackTrace();
//    }
    try {
      return JavaParser.parse(is);
    } catch (Exception e) {
      logger.warn(e, e);
    }
    return null;
  }
}
