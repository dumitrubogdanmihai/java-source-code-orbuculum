package ro.orbuculum.agent.indexer.handler.unit.clazz;

import java.util.Arrays;
import java.util.List;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import ro.orbuculum.agent.indexer.handler.Context;
import ro.orbuculum.agent.indexer.handler.Visitor;
import ro.orbuculum.agent.indexer.handler.unit.clazz.method.MethodDeclarationVisitor;

/**
 * Class visitor that only delegates to the method visitor.
 * 
 * @author bogdan
 */
public class ClassOrInterfaceDeclarationVisitor implements Visitor {

  /**
   * Context.
   */
  private Context context;

  /**
   * Constructor.
   * @param context Context.
   */
  public ClassOrInterfaceDeclarationVisitor(Context context) {
    this.context = context;
  }

  @Override
  public boolean visit(Node node) {
    if (node instanceof  ClassOrInterfaceDeclaration) {
      context.setClassDeclaration((ClassOrInterfaceDeclaration) node);
      return true;
    } else {
      return false;
    }
  }

  @Override
  public List<Visitor> getChildrenVisitors() {
    return Arrays.asList(new MethodDeclarationVisitor(context));
  }
}
