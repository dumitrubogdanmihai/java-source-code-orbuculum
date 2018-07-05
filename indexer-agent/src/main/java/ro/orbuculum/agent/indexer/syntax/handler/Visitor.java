package ro.orbuculum.agent.indexer.syntax.handler;

import java.util.List;

import com.github.javaparser.ast.Node;

public interface Visitor {
	boolean startVisit(Node node);
	List<Visitor> getChildrenVisitors();
	default void endVisit() {}
}
