package ro.orbuculum.agent;

import java.util.HashSet;
import java.util.Set;

import ro.orbuculum.agent.rest.RESTAgent;

/**
 * Define components.
 * 
 * @author bogdan
 */
public class Application extends javax.ws.rs.core.Application {
  
  /**
   * End-points set.
   */
  private Set<Object> singletons = new HashSet<>();

  
  /**
   * Constructor.
   */
  public Application() {
    singletons.add(new RESTAgent());
  }

  @Override
  public Set<Object> getSingletons() {
    return singletons;
  }
}
