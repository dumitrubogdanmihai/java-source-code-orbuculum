package ro.orbuculum.agent.indexer.syntax.handler.unit.clazz.method;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;

import ro.orbuculum.agent.indexer.syntax.handler.BindingsResolver;
import ro.orbuculum.agent.indexer.syntax.handler.Context;
import ro.orbuculum.agent.indexer.syntax.handler.Visitor;
import ro.orbuculum.agent.indexer.syntax.handler.unit.clazz.method.block.BlockStmtVisitor;

/**
 * The main visitor, as the Solr schema was designed that each document represents a method, not a class.
 * 
 * @author bogdan
 */
public class MethodDeclarationVisitor implements Visitor {

  /**
   * Logger.
   */
  private static final Logger logger = LogManager.getLogger(MethodDeclarationVisitor.class);

  /**
   * Document with data to update the index.
   */
  private SolrInputDocument document;

  /**
   * Context.
   */
  private Context context;

  /**
   * Constructor.
   * @param context Context.
   */
  public MethodDeclarationVisitor(Context context) {
    this.context = context;
  }

  @Override
  public boolean startVisit(Node node) {
    if (node instanceof  MethodDeclaration) {
      MethodDeclaration method = (MethodDeclaration) node;
      context.setMethod(method);

      document = new SolrInputDocument();
      document.addField("project", context.getProject());
      document.addField("path", context.getSourcesFilePath());
      document.addField("class", 
          BindingsResolver.getId(
              context.getCompilationUnit(), 
              context.getClassDeclaration()));
      document.addField("method", method.getName());
      document.addField("method-raw", method.toString());
      if (method.getParameters() != null) {
        for (Parameter param : method.getParameters()) {
          String resolvedClassName = BindingsResolver
              .resolveClassName(param.getType().toString(), context);
          document.addField("parameter", resolvedClassName);
        }
      }
      if (method.getJavaDoc() != null) {
        document.addField("javadoc", method.getJavaDoc());
      }
      document.addField("offset-start", method.getBeginLine());
      document.addField("offset-end", method.getEndLine());
      return true;
    } else {
      return false;
    }
  }

  @Override
  public List<Visitor> getChildrenVisitors() {
    return Arrays.asList(new BlockStmtVisitor(document, context));
  }

  @Override
  public void endVisit() {
    try {
      this.context.getSolrClient().add(document);
      UpdateResponse response = this.context.getSolrClient().commit();
      int status = response.getStatus();
      if (status != 102) {
        logger.warn(response);
      }
    } catch (SolrServerException | IOException e) {
      logger.warn(e, e);
    }
  }
}
