package ro.orbuculum.indexer;


import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.part.ViewPart;

import ro.orbuculum.Activator;


/**
 * View with a button that tells the server to index the opened projects.
 */

public class SemanticSearch extends ViewPart {
  /**
   * The ID of the view as specified by the extension.
   */
  public static final String ID = "ro.orbuculum.indexer.SemanticSearch";

  /**
   * Update index button.
   */
  private Button button;

  /**
   * The constructor.
   */
  public SemanticSearch() {}

  @Override
  public void createPartControl(Composite composite) {
    composite.setLayout(new RowLayout());

    button = new Button(composite, SWT.NONE);
    button.setText("Update index");
    button.addMouseListener(new MouseListener() {
      @Override
      public void mouseUp(MouseEvent arg0) {
        MessageConsole console = findConsole("Update index");
        console.clearConsole();
        console.activate();
        IOConsoleOutputStream outputStream = console.newOutputStream();
        try {
          new Indexer(outputStream).indexJavaProjects();
        } catch (CoreException | IOException e) {
          Activator.getDefault().getLog().log(new Status(Status.ERROR, Activator.PLUGIN_ID, e.toString(), e)); 
        }
      }

      @Override
      public void mouseDown(MouseEvent arg0) {
      }

      @Override
      public void mouseDoubleClick(MouseEvent arg0) {
      }
    });

    Label label = new Label(composite, SWT.BOTTOM);
    label.setText("Update current index for all opened projects...");
  }

  @Override
  public void setFocus() {
    button.setFocus();
  }
  
  /**
   * Get the console to print process logs. 
   * @param name
   * @return
   */
  private static MessageConsole findConsole(String name) {
    ConsolePlugin plugin = ConsolePlugin.getDefault();
    IConsoleManager conMan = plugin.getConsoleManager();
    IConsole[] existing = conMan.getConsoles();
    for (int i = 0; i < existing.length; i++) {
      if (name.equals(existing[i].getName())) {
        return (MessageConsole) existing[i];
      }
    }
    // No console found, so create a new one
    MessageConsole console = new MessageConsole(name, null);
    conMan.addConsoles(new IConsole[]{console});
    return console;
  }
}
