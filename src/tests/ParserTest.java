package tests;

import java.io.IOException;

import org.junit.Test;

import front.Parser;

public class ParserTest {

	@Test
	public void parseFiles() throws IOException {
		
		String testFilesFolder = "src/testCases";
		String[] testFiles = TestUtils.listFiles(testFilesFolder, "tst");
		
		for (String testFile : testFiles) {

			Parser parser = new Parser(testFilesFolder + "/" + testFile);
			parser.parse();
//			scanner.close();
		}
		
	}
}
