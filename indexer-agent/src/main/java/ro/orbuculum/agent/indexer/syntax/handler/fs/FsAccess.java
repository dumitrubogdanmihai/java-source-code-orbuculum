package ro.orbuculum.agent.indexer.syntax.handler.fs;

/**
 * Abstract file system from where the source files are parsed.
 * 
 * @author bogdan
 */
public interface FsAccess {
  boolean exist(String filePath);
  default public boolean haveSibling(String filePath, String filename) {
    String siblingFile = new java.io.File(filePath).getParent() + filename;
    return exist(siblingFile);
  }
}
