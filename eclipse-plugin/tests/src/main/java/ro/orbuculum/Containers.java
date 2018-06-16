package ro.orbuculum;
import java.io.File;

import org.junit.rules.ExternalResource;

public class Containers extends ExternalResource {
  private Process start;

  @Override
  protected void before() throws Throwable {
    ProcessBuilder pb = new ProcessBuilder()
        .command("docker-compose", "up")
        .directory(new File("../.."))
        .inheritIO();
    start = pb.start();
    super.before();
  }
  
  @Override
  protected void after() {
    start.destroy();
    super.after();
  }
}
