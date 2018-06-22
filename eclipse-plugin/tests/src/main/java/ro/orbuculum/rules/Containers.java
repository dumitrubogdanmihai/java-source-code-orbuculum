package ro.orbuculum.rules;
import java.io.File;
import java.lang.ProcessBuilder.Redirect;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.rules.ExternalResource;

/**
 * Rule that will start containers if aren't already started.
 * 
 * @author bogdan
 */
public class Containers extends ExternalResource {
  private Process dockerProcess;

  @Override
  protected void before() throws Throwable {
    if( ! pingSolr()) {
      ProcessBuilder pb = new ProcessBuilder()
          .command("docker-compose", "up")
          .directory(new File("../.."))
          .inheritIO()
          .redirectOutput(Redirect.PIPE);
      dockerProcess = pb.start();
      
      // Wait 10 seconds for Sorl to start.
      for (int i = 0; i < 200; i++) {
        if (pingSolr()) {
          break;
        }
        Thread.sleep(100);
      }
    }
    
    super.before();
  }

  @Override
  protected void after() {
    if (dockerProcess != null) {
      dockerProcess.destroy();
    }
    super.after();
  }

  /**
   * Ping for Solr container.
   * @return <code>true</code> if is up and running.
   */
  private boolean pingSolr() {
    try {
      URL url = new URL("http://localhost:8983/solr/#/");
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");
      int responseCode = con.getResponseCode();
      return responseCode == 200;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
}
