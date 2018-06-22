package ro.orbuculum.unit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ro.orbuculum.search.querent.jaxb.Doc;
import ro.orbuculum.search.querent.jaxb.Result;
import ro.orbuculum.search.querent.jaxb.Unmarshaller;

public class TestUnmarshaller2 {

  @Test
  public void testUnmarshal3() throws Exception {
    String xml = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
        "<response>\n" + 
        "\n" + 
        "<lst name=\"responseHeader\">\n" + 
        "  <int name=\"status\">0</int>\n" + 
        "  <int name=\"QTime\">1</int>\n" + 
        "  <lst name=\"params\">\n" + 
        "    <str name=\"q\">*:*</str>\n" + 
        "    <str name=\"wt\">xml</str>\n" + 
        "    <str name=\"_\">1529612081135</str>\n" + 
        "  </lst>\n" + 
        "</lst>\n" + 
        "<result name=\"response\" numFound=\"1\" start=\"0\">\n" + 
        "  <doc>\n" + 
        "    <str name=\"project\">ProjName</str>\n" + 
        "    <str name=\"path\">src/pk/Cls.java</str>\n" + 
        "    <str name=\"class\">pk.Cls</str>\n" + 
        "    <str name=\"method\">mth</str>\n" + 
        "    <arr name=\"offset-start\">\n" + 
        "      <long>4</long>\n" + 
        "    </arr>\n" + 
        "    <arr name=\"offset-end\">\n" + 
        "      <long>8</long>\n" + 
        "    </arr>\n" + 
        "    <str name=\"id\">ec0fa3a8-f5a0-4b39-ba48-4b701b728811</str>\n" + 
        "    <long name=\"_version_\">1603915957010432000</long></doc>\n" + 
        "</result>\n" + 
        "</response>";
    
    Result result = Unmarshaller.unmarshal(xml);
    assertEquals(Integer.valueOf(1), result.getNumFound());
    
    Doc doc = result.getDocs().get(0);
    assertEquals((Integer)4, doc.getLineStart());
    assertEquals((Integer)8, doc.getLineEnd());
    assertEquals("pk.Cls", doc.getClassName());
    assertEquals("mth", doc.getMethod());
    assertEquals("src/pk/Cls.java", doc.getPath());
    assertEquals("ProjName", doc.getProject());
    }
}
