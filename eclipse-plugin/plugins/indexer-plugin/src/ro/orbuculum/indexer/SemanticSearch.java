package ro.orbuculum.indexer;


import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.part.ViewPart;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
   * Indexer agent server api.
   */
  private Api api;

  private Label labelEmailLabel;

  /**
   * The constructor.
   */
  public SemanticSearch() {
    MessageConsole console = findConsole("Update index");
    console.clearConsole();
    console.activate();
    IOConsoleOutputStream outputStream = console.newOutputStream();
    api = new Api(outputStream);
  }

  @Override
  public void createPartControl(Composite composite) {
    
    GridLayout gridLayout = new GridLayout();
    gridLayout.numColumns = 2;
    composite.setLayout(gridLayout);
    
    {
      button = new Button(composite, SWT.PUSH);
      button.setText("Update index");
      button.addMouseListener(new MouseListener() {
        @Override
        public void mouseUp(MouseEvent arg0) {
          try {
            api.indexJavaProjects();
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
      Label label = new Label(composite, SWT.NONE);
      label.setText("Index all.");
    }

    {
      Button trackRepo = new Button(composite, SWT.PUSH);
      trackRepo.setText("Track repo");
//TODO:action...
      Text repoText = new Text(composite, SWT.NONE);
      repoText.setText("repositoryName");
    }
    
    {
      Button button = new Button(composite, SWT.PUSH);
      button.setText("Set auth token");

      Text text = new Text(composite, SWT.NONE);
      button.addMouseListener(new MouseListener() {
        @Override
        public void mouseUp(MouseEvent arg0) {
          api.getRestApi().setOauthAccessToken(text.getText()).enqueue(
              new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                  if (response.isSuccessful()) {
                    updateLabelEmailLabel();
                    System.err.println("REFETCH SOME DATA");
                  }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                  Display.getDefault().asyncExec(new Runnable() {
                    public void run() {
                      text.setText("Failed to fetch data because: " + t.getMessage());
                    }
                  });
                }
              });;
        }
        
        @Override
        public void mouseDown(MouseEvent arg0) {
        }
        
        @Override
        public void mouseDoubleClick(MouseEvent arg0) {
        }
      });
    }

    {
      Label labelEmail = new Label(composite, SWT.NONE);
      labelEmail.setText("Email:");

      labelEmailLabel = new Label(composite, SWT.NONE);
      labelEmailLabel.setText("pending...");
      updateLabelEmailLabel();
    }

    {
      Label labelReposLabel = new Label(composite, SWT.NONE);
      labelReposLabel.setText("Repositories:");

      Label labelReposRepos = new Label(composite, SWT.NONE);
      labelReposRepos.setText("pending...");
      api.getRestApi().getTrackedRepositories().enqueue(new Callback<String>() {
        @Override
        public void onResponse(Call<String> call, Response<String> response) {
          Display.getDefault().asyncExec(new Runnable() {
            public void run() {
              labelReposRepos.setText(response.message());
            }
          });
        }

        @Override
        public void onFailure(Call<String> call, Throwable t) {
          System.err.println("Failed to fetch data because: " + t);
          t.printStackTrace();
          Display.getDefault().asyncExec(new Runnable() {
            public void run() {
              labelReposRepos.setText("Failed to fetch data because: " + t.getMessage());
            }
          });
        }
      });
    }
  }

  private void updateLabelEmailLabel() {
    api.getRestApi().getAuthorizedEmail().enqueue(new Callback<String>() {
      @Override
      public void onResponse(Call<String> call, Response<String> response) {
        Display.getDefault().asyncExec(new Runnable() {
          public void run() {
            labelEmailLabel.setText(response.message());
          }
        });
      }
      @Override
      public void onFailure(Call<String> call, Throwable t) {
        System.err.println("Failed to fetch data because: " + t);
        t.printStackTrace();
        Display.getDefault().asyncExec(new Runnable() {
          public void run() {
            labelEmailLabel.setText("Failed to fetch data because: " + t.getMessage());
          }
        });

      }
    });
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
