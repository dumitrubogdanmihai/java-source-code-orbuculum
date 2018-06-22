package ro.orbuculum.search;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.ISearchResultListener;
import org.eclipse.search.ui.ISearchResultPage;
import org.eclipse.search.ui.ISearchResultViewPart;
import org.eclipse.search.ui.SearchResultEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import ro.orbuculum.Activator;

/**
 * Page where search results are presented.
 *  
 * @author bogdan
 */
public class SearchResultPage implements ISearchResultPage, ISearchResultListener {

  /**
   * Some id.
   */
  private String id;

  /**
   * Composite.
   */
  private Composite rootControl;

  /**
   * Site.
   */
  private IPageSite site;

  /**
   * Table where results are presented.
   */
  private TableViewer viewer;

  /**
   * Retrieved result containing indexed Solr documents.
   */
  private SearchResult searchResult;

  public SearchResultPage() {
    System.err.println("NEW SearchResultPage!!!");
  }
  
  @Override
  public void createControl(Composite parent) {
    this.rootControl = new Composite(parent, SWT.NULL);
    this.rootControl.setLayout(new FillLayout());

    viewer = new TableViewer(rootControl, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
    createColumns();
    createControl();

    // Make lines and header visible
    final Table table = viewer.getTable();
    table.setHeaderVisible(true);
    table.setLinesVisible(true);

    viewer.setContentProvider(new ArrayContentProvider());
    // Make the selection available to other views
    getSite().setSelectionProvider(viewer);
  }

  @Override
  public void searchResultChanged(SearchResultEvent e) {
    if (e == null) {
      Display.getDefault().asyncExec(new Runnable() {
        @Override
        public void run() {
          SearchResultPage.this.viewer.setInput(null);
        }
      });
    } else if (e instanceof ro.orbuculum.search.SearchResultEvent) {
      Display.getDefault().asyncExec(new Runnable() {
        @Override
        public void run() {
          ArrayList<SearchResultEntity> result = 
              (ArrayList<SearchResultEntity>) SearchResultPage.this.searchResult.getResult();
          SearchResultPage.this.viewer.setInput(result);
        }
      });
    }
  }

  @Override
  public IPageSite getSite() {
    return this.site;
  }

  @Override
  public void init(IPageSite arg0) throws PartInitException {
    this.site = arg0;
  }

  @Override
  public void dispose() {
  }

  @Override
  public void saveState(IMemento memento) {
  }

  @Override
  public void restoreState(IMemento memento) {
  }

  @Override
  public void setInput(ISearchResult searchResult, Object uiState) {
    SearchResult newSearchResult = (SearchResult) searchResult;
    if (newSearchResult != null) {
      this.searchResult = newSearchResult;
      this.searchResult.addListener(this);
    } else {
      this.searchResult.removeListener(this);
      searchResultChanged(null);
    }
  }

  @Override
  public void setViewPart(ISearchResultViewPart part) {
  }

  @Override
  public void setActionBars(IActionBars arg0) {
  }

  @Override
  public void setFocus() {
    viewer.getControl().setFocus();
  }

  @Override
  public Object getUIState() {
    return null;
  }

  @Override
  public void setID(String id) {
    this.id = id;
  }

  @Override
  public String getID() {
    return this.id;
  }

  @Override
  public String getLabel() {
    return "Filesystem Search Results";
  }

  @Override
  public Control getControl() {
    return rootControl;
  }

  /**
   * Attach behavior.
   */
  private void createControl() {
    viewer.addSelectionChangedListener(new ISelectionChangedListener() {
      @Override
      public void selectionChanged(SelectionChangedEvent event) {
        IStructuredSelection selection = viewer.getStructuredSelection();
        Object firstElement = selection.getFirstElement();
        System.err.println("selection:"+selection + " " + firstElement);
      }
    });

    viewer.addDoubleClickListener(new IDoubleClickListener() {
      @Override
      public void doubleClick(DoubleClickEvent event) {
        ISelection selection = viewer.getSelection();
        ro.orbuculum.search.SearchResultEntity entity = (SearchResultEntity) ((IStructuredSelection)selection).getFirstElement();
        try {
          IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(entity.getProject());
          IFile file = project.getFile(entity.getPath());
          IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
          AbstractTextEditor editor = (AbstractTextEditor) IDE.openEditor(activePage, file);
//        
//          IDocumentProvider documentProvider = editor.getDocumentProvider();
//          JavaSourceViewer viewer2 = (JavaSourceViewer) editor.getViewer();
          IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput());
          
          Integer from = entity.getLineStart();
          Integer to = entity.getLineEnd();
          try {
            int startOffset = document.getLineOffset(from - 1);
            int endOffset = document.getLineOffset(to) - 1;
            editor.selectAndReveal(startOffset, endOffset - startOffset);
          } catch (Exception e) {   
            Activator.getDefault().getLog().log(new Status(Status.ERROR, Activator.PLUGIN_ID, e.toString(), e)); 
          }
        } catch (PartInitException e) {
          Activator.getDefault().getLog().log(new Status(Status.ERROR, Activator.PLUGIN_ID, e.toString(), e)); 
        }
      }
    });
  }

  /**
   * Attach columns.
   */
  private void createColumns() {
    TableViewerColumn colFirstName = new TableViewerColumn(viewer,  SWT.NONE, 0);
    colFirstName.getColumn().setWidth(200);
    colFirstName.getColumn().setText("Clasa");
    colFirstName.setLabelProvider(new ColumnLabelProvider() {
      @Override
      public String getText(Object element) {
        SearchResultEntity entity = (SearchResultEntity) element;
        return entity.getClassName();
      }
    });

    TableViewerColumn colSecondName = new TableViewerColumn(viewer,  SWT.NONE, 1);
    colSecondName.getColumn().setWidth(200);
    colSecondName.getColumn().setText("Metoda");
    colSecondName.setLabelProvider(new ColumnLabelProvider() {
      @Override
      public String getText(Object element) {
        SearchResultEntity entity = (SearchResultEntity) element;
        return entity.getMethod();
      }
    });
  }
}
