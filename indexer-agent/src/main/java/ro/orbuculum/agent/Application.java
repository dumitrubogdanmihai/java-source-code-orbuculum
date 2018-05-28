package ro.orbuculum.agent;

import java.util.HashSet;
import java.util.Set;

import ro.orbuculum.agent.rest.RESTAgent;

public class Application extends javax.ws.rs.core.Application {
	private Set<Object> singletons = new HashSet<>();
	
	public Application() {
		singletons.add(new RESTAgent());
	}
	
	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
