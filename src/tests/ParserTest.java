package tests;

import java.io.IOException;

import org.junit.Test;

import front.Parser;
import front.Parser.ParserException;
import front.Scanner.ScannerException;

public class ParserTest {

	@Test
	public void parseFiles() throws IOException {
		
		String testFilesFolder = "src/testCases";
		String[] testFiles = TestUtils.listFiles(testFilesFolder, "tst");
		
		for (String testFile : testFiles) {

			Parser parser = new Parser(testFilesFolder + "/" + testFile);
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
	
	@Test
	public void parseFile() throws IOException {

		String testFile = "src/testCases/test1-3.tst";
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
