package ro.orbuculum.search;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchPage;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import ro.orbuculum.search.querent.api.Solr;

/**
 * Search page that produce the {@link SearchQuery}.
 * 
 * @author bogdan
 */
public class SearchPage implements ISearchPage {

  /**
   * The main input.
   */
	private Text methodQueryText;
	
	/**
	 * Container.
	 */
	private ISearchPageContainer container;
	
	/**
	 * Control.
	 */
	private Composite control;

	/**
	 * Project combo.
	 */
  private Combo projectCombo;

  public SearchPage() {
    System.err.println("NEW SEARCH PAGE !!!");
  }
  
	@Override
	public void createControl(Composite arg0) {
	  this.control = arg0;
	  
    projectCombo = new Combo(arg0, SWT.DROP_DOWN | SWT.BORDER);
    projectCombo.setText("Select the project that will be searched for:");
    projectCombo.add("All opened projects");
    Solr.getOpenedProjects().forEach(p -> projectCombo.add(p));
	  
		methodQueryText = new Text(arg0,  SWT.CANCEL | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
		methodQueryText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		methodQueryText.setText("Insert your query as the exaple below:\n"
        + "class:Controller\n"
        + "method:build\n"
        + "call:factory\n"
        + "keyword:new");
		methodQueryText.selectAll();
	}

	@Override
	public boolean performAction() {
	  System.err.println("performAction" + projectCombo.getSelectionIndex());
	  String selectedProject = (projectCombo.getSelectionIndex() == -1 ? null : projectCombo.getText());
	  String rawMethodQuery = methodQueryText.getText();
		SearchQuery searchQuery = new SearchQuery(rawMethodQuery, selectedProject);
		NewSearchUI.runQueryInForeground(container.getRunnableContext(), searchQuery);
		return true;
	}

	@Override
	public void dispose() {
	}

	@Override
	public Control getControl() {
		return this.control;
	}

	@Override
	public String getDescription() {
		return "Description...:";
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public Image getImage() {
		return  null;
	}

	@Override
	public String getMessage() {
	  // TODO:
		return "Message";
	}

	@Override
	public String getTitle() {
		return "Semantic Search";
	}

	@Override
	public void performHelp() {
	}

	@Override
	public void setDescription(String arg0) {
	}

	@Override
	public void setImageDescriptor(ImageDescriptor arg0) {
	}

	@Override
	public void setTitle(String arg0) {
	}

	@Override
	public void setVisible(boolean arg0) {
	}

	@Override
	public void setContainer(ISearchPageContainer arg0) {
		this.container = arg0;
	}
}
