package ro.orbuculum.unit;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.junit.ClassRule;
import org.junit.Test;

import ro.orbuculum.Containers;
import ro.orbuculum.indexer.Indexer;
import ro.orbuculum.search.querent.api.Solr;
import ro.orbuculum.search.querent.api.Result;

public class TestSample {
  
  @ClassRule
  public static Containers containers = new Containers();
  
  @Test
  public void testSample() throws InterruptedException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Indexer indexer = new Indexer(baos);
    //    indexer.inde

    final AtomicReference<Result> resultRef = new AtomicReference<Result>();
    Solr.get("", new Consumer<Result>() {
      public void accept(Result r) {
        resultRef.set(r);

        synchronized (resultRef) {
          resultRef.notifyAll();
        }
      }
    });

    synchronized (resultRef) {
      resultRef.wait(10000);
    }
    assertNotNull(resultRef.get());
    System.err.println(resultRef.get().getName());
    Thread.sleep(2000);
  }
}
