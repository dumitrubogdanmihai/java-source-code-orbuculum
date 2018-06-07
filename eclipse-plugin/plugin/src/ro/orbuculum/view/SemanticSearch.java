package ro.orbuculum.view;


import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
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

import ro.orbuculum.indexer.Indexer;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class SemanticSearch extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "ro.orbuculum.view.SemanticSearch";
	private Button button;

	/**
	 * The constructor.
	 */
	public SemanticSearch() {
	}
	
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
    MessageConsole myConsole = new MessageConsole(name, null);
    conMan.addConsoles(new IConsole[]{myConsole});
    return myConsole;
 }
	
	@Override
	public void createPartControl(Composite arg0) {
		arg0.setLayout(new RowLayout());
		
		button = new Button(arg0, SWT.NONE);
		button.setText("Update index");
		button.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent arg0) {
				MessageConsole console = findConsole("Update index");
				console.clearConsole();
				console.activate();
		    IOConsoleOutputStream outputStream = console.newOutputStream();
				Indexer i = new Indexer(outputStream);
				try {
					i.indexJavaProjects();
				} catch (CoreException | IOException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
			}
		});
		
		Label label = new Label(arg0, SWT.BOTTOM);
		label.setText("Update current index for all opened projects...");
	}

	@Override
	public void setFocus() {
		button.setFocus();
	}
}
