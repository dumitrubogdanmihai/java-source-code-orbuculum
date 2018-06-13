package ro.orbuculum.agent.indexer.handler.unit;

import java.util.Arrays;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;

import ro.orbuculum.agent.indexer.handler.Context;
import ro.orbuculum.agent.indexer.handler.Visitor;
import ro.orbuculum.agent.indexer.handler.unit.clazz.ClassOrInterfaceDeclarationVisitor;

/**
 * Root visitor that only delegates to the class visitor.
 * 
 * @author bogdan
 */
public class CompilationUnitVisitor implements Visitor {

  /**
   * The context.
   */
  private Context context;

  /**
   * Constructor.
   * @param context Context.
   */
  public CompilationUnitVisitor(Context context) {
    this.context = context;
  }

  @Override
  public boolean visit(Node node) {
    if (node instanceof CompilationUnit) {
      context.setCompilationUnit((CompilationUnit) node);
      return true;
    } else {
      return false;
    }
  }

  @Override
  public List<Visitor> getChildrenVisitors() {
    return Arrays.asList(new ClassOrInterfaceDeclarationVisitor(context));
  }
}
