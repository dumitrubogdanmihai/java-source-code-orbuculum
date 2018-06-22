package ro.orbuculum.rules;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;

import org.junit.rules.ExternalResource;

/**
 * Removes indexed documents.
 * 
 * @author bogdan_dumitru
 */
public class WipeIndex extends ExternalResource {
  @Override
  protected void before() throws Throwable {
    ProcessBuilder pb = new ProcessBuilder()
        .command("bash", "delete-all-documents.sh")
        .directory(new File("../../solr"))
        .inheritIO()
        .redirectOutput(Redirect.PIPE);
    pb.start().waitFor();
    
    System.out.println("Index wiped");
    
    super.before();
  }
}
