package ro.orbuculum.agent.indexer;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.mockito.ArgumentCaptor;

import com.github.javaparser.ast.CompilationUnit;

/**
 * Handle Mockito staff and intercept the documents that would be indexed.
 * 
 * @author bogdan
 */
public class VisitorSpyUtil {

  /**
   * Intercept documents that would be indexed.
   * @param sampleSource  Source file.
   * @param documents     Expected documents.
   * 
   * @return  List of documents.
   * 
   * @throws SolrServerException
   * @throws IOException
   */
  public static List<SolrInputDocument> getDocumentsToIndex (
      File sampleSource, int documents) throws SolrServerException, IOException {
    // Given
    SolrClient solrClientMock = mock(SolrClient.class);
    AstIndexer indexerSpy = spy(new AstIndexer("test-port", "test-core") {
      @Override
      protected SolrClient buildSolrClient(String host, String port, String core) {
        return solrClientMock;
      }
    });

    // When
    UpdateResponse responseMock = mock(UpdateResponse.class);
    when(responseMock.getStatus()).thenReturn(102);
    when(solrClientMock.commit()).thenReturn(responseMock);

    // Then...
    CompilationUnit parsedSource = Parser.parse(sampleSource);
    indexerSpy.index(parsedSource, new File(".").getCanonicalFile(), sampleSource);

    ArgumentCaptor<SolrInputDocument> captor = ArgumentCaptor.forClass(SolrInputDocument.class);
    verify(solrClientMock, times(documents)).add(captor.capture());
    return captor.getAllValues();
  }
}
