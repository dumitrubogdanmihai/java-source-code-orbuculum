package ro.orbuculum.agent.indexer;


import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.JAXBException;

import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;

import com.github.javaparser.ast.CompilationUnit;

import ro.orbuculum.agent.parser.Parser;

public class ParserTest {
	@Test
	public void test() throws IOException, SolrServerException {
		String path = "src/main/java/ro/orbuculum/agent";
		Stream<CompilationUnit> stream = Parser.getNodesStream(path);
		List<CompilationUnit> collect = stream.collect(Collectors.toList());
		
		assertThat(collect.size(), greaterThan(1));
		
		GraphWalker indexer = new GraphWalker("8983", "orbuculum");
		indexer.index("src/main/java/ro/orbuculum/agent/indexer/GraphWalker.java");
	}
	
	@Test
	public void parser() throws IOException, JAXBException {
//		CompilationUnit parse = Parser.parse(new File("src/main/java/ro/orbuculum/agent/indexer/Main.java"));
//		System.err.println(parse);
//		
//		
//		FileOutputStream fos = new FileOutputStream("settings.xml");
//    XMLEncoder encoder = new XMLEncoder(fos);
//    encoder.writeObject(parse);
//    encoder.close();
//    fos.close();
//
//		FileOutputStream fos2 = new FileOutputStream("settings2.xml");
//    JAXBContext newInstance = JAXBContext.newInstance(CompilationUnit.class);
//    Marshaller createMarshaller = newInstance.createMarshaller();
//    createMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//    createMarshaller.marshal(parse, fos2);
	}
}
