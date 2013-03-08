package tests;

import java.io.IOException;

import org.junit.Test;

import compiler.front.Parser;
import compiler.front.Parser.ParserException;
import compiler.front.Scanner.ScannerException;


public class ParserTest {

	@Test
	public void testParseFiles() throws IOException {
		parseFiles("src/testCases", "tst");
		parseFiles("src/testsMichael", "txt");
	}
	
	public void parseFiles(String dir, String ext) throws IOException {
		
		String[] testFiles = TestUtils.listFiles(dir, ext);
		
		for (String testFile : testFiles) {
			System.out.println(" -> " + testFile);
			Parser parser = new Parser(dir + "/" + testFile);
			try {
				parser.parse();
				System.out.println(parser.CFGs);
			} catch (ParserException | ScannerException e) {
				e.printStackTrace();
			} finally {
				parser.terminate();
			}
		}
		
	}
	
//	@Test
	public void parseFile() throws IOException {

		String testFile = "src/testCases/temp_test1-0.tst";
		Parser parser = new Parser(testFile);
		try {
			parser.parse();
			System.out.println(parser.CFGs);
		} catch (ParserException | ScannerException e) {
			e.printStackTrace();
		} finally {
			parser.terminate();
		}
	}
}
