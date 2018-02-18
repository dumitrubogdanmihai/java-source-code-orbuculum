package com.indexer;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.stream.Stream;

import org.junit.Test;

import com.github.javaparser.ast.CompilationUnit;

public class ParserTest {
	@Test
	public void test() throws IOException {
		String path = "src/test/java/com/indexer";
		Stream<CompilationUnit> stream = Parser.getNodesStream(path);
		assertThat(stream.count(), greaterThan(1L));
	}
}
