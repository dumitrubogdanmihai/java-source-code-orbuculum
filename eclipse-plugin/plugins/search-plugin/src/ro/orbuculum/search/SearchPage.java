package ro.orbuculum.search;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchPage;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * Search page that produce the {@link SearchQuery}.
 * 
 * @author bogdan
 */
public class SearchPage implements ISearchPage {

  /**
   * The main input.
   */
	private Text text;
	
	/**
	 * Container.
	 */
	private ISearchPageContainer container;
	
	/**
	 * Control.
	 */
	private Composite control;

	@Override
	public void createControl(Composite arg0) {
	  this.control = arg0;
		this.text = new Text(arg0,  SWT.CANCEL | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
		this.text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		this.text.setText("Insert your query as the exaple below:\n"
        + "class:Controller\n"
        + "method:build\n"
        + "call:factory\n"
        + "keyword:new");
		this.text.selectAll();
	}

	@Override
	public boolean performAction() {
		SearchQuery searchQuery = new SearchQuery(text.getText());
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
	  // TODO:
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
