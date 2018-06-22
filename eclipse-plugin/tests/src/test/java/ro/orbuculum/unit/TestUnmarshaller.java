package ro.orbuculum.unit;

public class TestUnmarshaller {/*

  @Test
  public void testMarshall() throws Exception {
    Result result = new Result();
    Map<String, String> fields = new HashMap<>();
    fields.put("A", "B");
    result.setName("nume");
    result.setNumFound(1);
    result.setStart(1);
    result.setDocs(Arrays.asList(fields));
    String marshall = Unmarshaller.marshall(result);
    assertEquals(
        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" + 
            "<result name=\"nume\" numFound=\"1\" start=\"1\">\n" + 
            "    <doc>\n" + 
            "        <str name=\"A\">B</str>\n" + 
            "    </doc>\n" + 
            "</result>\n" + 
            "", marshall);
  }

  @Test
  public void testUnmarshal() throws Exception {
    String xml = 
        "<response>" +
            "<result name=\"response\" numFound=\"0\" start=\"0\">\n" + 
            "</result>\n"+
            "</response>\n";
    Result result = Unmarshaller.unmarshal(xml);
    assertEquals("response", result.getName());
    assertEquals(null, result.getDocs());
  }

  @Test
  public void testUnmarshal1() throws Exception {
    String xml = 
        "<response>" +
            "<result name=\"response\" numFound=\"1\" start=\"0\">\n" + 
            "<doc>\n" + 
            "<str name=\"method\">method</str>\n" + 
            "<str name=\"method-q-name\">ro.orbuculum.agent.sample.Blank</str>\n" + 
            "<str name=\"class\">ro.orbuculum.agent.sample.Blank</str>\n" + 
            "<str name=\"offset\">3</str>\n" + 
            "<str name=\"id\">c06d0b2c-b948-4c22-a142-da2f41d6030a</str>\n" + 
            "</doc>\n" + 
            "</result>\n"+
            "</response>\n";
    Result result = Unmarshaller.unmarshal(xml);
    assertEquals("response", result.getName());
    assertEquals(1, result.getDocs().size());
    assertEquals(5, result.getDocs().get(0).size());
  }

  @Test
  public void testUnmarshal2() throws Exception {
    String xml = 
        "<response>" +
            "<result name=\"response\" numFound=\"2\" start=\"0\">\n" + 
            "<doc>\n" + 
            "<str name=\"method\">method</str>\n" + 
            "<str name=\"method-q-name\">ro.orbuculum.agent.sample.Blank</str>\n" + 
            "<str name=\"class\">ro.orbuculum.agent.sample.Blank</str>\n" + 
            "<str name=\"offset\">3</str>\n" + 
            "<str name=\"id\">c06d0b2c-b948-4c22-a142-da2f41d6030a</str>\n" + 
            "</doc>\n" + 
            "<doc>\n" + 
            "<str name=\"method\">method</str>\n" + 
            "<str name=\"method-q-name\">ro.orbuculum.agent.sample.Blank</str>\n" + 
            "<str name=\"class\">ro.orbuculum.agent.sample.Blank</str>\n" + 
            "<str name=\"offset\">3</str>\n" + 
            "<str name=\"id\">c06d0b2c-b948-4c22-a142-da2f41d6030a</str>\n" + 
            "</doc>\n" + 
            "</result>\n"+
            "</response>\n";
    Result result = Unmarshaller.unmarshal(xml);
    assertEquals(Integer.valueOf(2), result.getNumFound());
    assertEquals(2, result.getDocs().size());
    assertEquals(5, result.getDocs().get(0).size());
  }

  @Test
  public void testUnmarshal3() throws Exception {
    String xml = 
        "<response>\n" + 
        "<lst name=\"responseHeader\">\n" + 
        "<int name=\"status\">0</int>\n" + 
        "<int name=\"QTime\">0</int>\n" + 
        "<lst name=\"params\">\n" + 
        "<str name=\"q\">*:*</str>\n" + 
        "<str name=\"wt\">xml</str>\n" + 
        "</lst>\n" + 
        "</lst>\n" + 
        "<result name=\"response\" numFound=\"2\" start=\"0\">\n" + 
        "<doc>\n" + 
        "<str name=\"project\">project-dir</str>\n" + 
        "<str name=\"path\">MethodCalls.java</str>\n" + 
        "<str name=\"class\">ro.orbuculum.agent.sample.MethodCalls</str>\n" + 
        "<str name=\"method\">method0</str>\n" + 
        "<str name=\"method-raw\">void method0() { assertTrue(true); }</str>\n" + 
        "<arr name=\"offset-start\">\n" + 
        "<long>6</long>\n" + 
        "</arr>\n" + 
        "<arr name=\"offset-end\">\n" + 
        "<long>8</long>\n" + 
        "</arr>\n" + 
        "<arr name=\"method-call\">\n" + 
        "<str>org.junit.Assert.assertTrue</str>\n" + 
        "</arr>\n" + 
        "<str name=\"id\">4335b309-3e65-4b03-9255-679943deed09</str>\n" + 
        "<long name=\"_version_\">1603911630436958208</long>\n" + 
        "</doc>\n" + 
        "<doc>\n" + 
        "<str name=\"project\">project-dir</str>\n" + 
        "<str name=\"path\">MethodCalls.java</str>\n" + 
        "<str name=\"class\">ro.orbuculum.agent.sample.MethodCalls</str>\n" + 
        "<str name=\"method\">method1</str>\n" + 
        "<str name=\"method-raw\">void method1() { assertTrue(true); }</str>\n" + 
        "<arr name=\"offset-start\">\n" + 
        "<long>9</long>\n" + 
        "</arr>\n" + 
        "<arr name=\"offset-end\">\n" + 
        "<long>11</long>\n" + 
        "</arr>\n" + 
        "<arr name=\"method-call\">\n" + 
        "<str>org.junit.Assert.assertTrue</str>\n" + 
        "</arr>\n" + 
        "<str name=\"id\">9dbf08bb-2261-48e8-9774-dd98dcf9c293</str>\n" + 
        "<long name=\"_version_\">1603911630598438912</long>\n" + 
        "</doc>\n" + 
        "</result>\n" + 
        "</response>";
    Result result = Unmarshaller.unmarshal(xml);
    assertEquals(Integer.valueOf(2), result.getNumFound());
    assertEquals(2, result.getDocs().size());
    assertEquals("", result.getDocs().get(0).get(Fields.OFFSET_START));
  }
*/}
