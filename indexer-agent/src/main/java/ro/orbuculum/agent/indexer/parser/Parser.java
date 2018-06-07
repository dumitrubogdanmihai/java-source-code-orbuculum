package ro.orbuculum.agent.indexer.parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;

/**
 * Parse recursively and return a stream of {@link Node}.
 */
public class Parser {

	/**
	 * Logger.s
	 */
	private static final Logger logger = LogManager.getLogger(Parser.class);

	/**
	 * Parse recursively and return a stream of {@link Node}s. 
	 * @param rootPath	Start point.
	 * @return			Stream of {@link Node}s
	 * @throws IOException
	 */
	public static Stream<CompilationUnit> getNodesStream(String rootPath) throws IOException {
		return Files.walk(Paths.get(rootPath))
				.map(Path::toFile)
				.filter(File::isFile)
				.filter(f -> f.getName().endsWith(".java"))
				.map(f -> parse(f))
				.filter(Objects::nonNull);
	}

	/**
	 * Safe parse.
	 * @param f File to be parsed.
	 * @return	Corresponding {@link CompilationUnit} or <code>null</code> if fail to parse.
	 */
	public static CompilationUnit parse(File f) {
		try {
			return JavaParser.parse(f);
		} catch (Exception e) {
			logger.warn(e, e);
		}
		return null;
	}
}
