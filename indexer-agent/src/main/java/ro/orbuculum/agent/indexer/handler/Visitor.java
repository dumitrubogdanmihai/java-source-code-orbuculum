package ro.orbuculum.agent.indexer.handler;

import java.util.List;

import com.github.javaparser.ast.Node;

public interface Visitor {
	boolean visit(Node node);
	List<Visitor> getChildrenVisitors();
	default void commit() {}
}
