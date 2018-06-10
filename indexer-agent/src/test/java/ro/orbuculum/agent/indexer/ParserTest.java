package ro.orbuculum.agent.indexer;


import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.JAXBException;

import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;

import com.github.javaparser.ast.CompilationUnit;

import ro.orbuculum.agent.indexer.parser.Parser;

public class ParserTest {
	@Test
	public void test() throws IOException, SolrServerException {
		String path = "src/main/java/ro/orbuculum/agent";
		Stream<CompilationUnit> stream = Parser.getNodesStream(path);
		List<CompilationUnit> collect = stream.collect(Collectors.toList());
		
		assertThat(collect.size(), greaterThan(1));
	}
	
	@Test
	public void parser() throws IOException, JAXBException {
		CompilationUnit parse = Parser.parse(new File("src/main/java/ro/orbuculum/agent/indexer/Main.java"));
		System.err.println(parse);
		//TODO fill with some asserts.
	}
}
