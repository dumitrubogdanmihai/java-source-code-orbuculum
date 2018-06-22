package ro.orbuculum.agent.indexer.handler.unit.clazz;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import ro.orbuculum.agent.indexer.handler.BindingsResolver;
import ro.orbuculum.agent.indexer.handler.Context;
import ro.orbuculum.agent.indexer.handler.Visitor;
import ro.orbuculum.agent.indexer.handler.unit.clazz.method.MethodDeclarationVisitor;

import org.slf4j.MDC;
import org.slf4j.spi.MDCAdapter;
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
      SolrClient solrClient = this.context.getSolrClient();
      try {
        String project = context.getProjectDir().getName();
        String classId = BindingsResolver.getId(
            context.getCompilationUnit(), 
            context.getClassDeclaration());
        solrClient.deleteByQuery("project:" + project + " AND class:" + classId);
      } catch (SolrServerException | IOException e) {
        e.printStackTrace();
      }
      
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
