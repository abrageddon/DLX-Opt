package tests;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Test;

import front.Scanner;
import front.Tokens;

public class ScannerTest {

	@Test
	public void scanFiles() throws IOException {
		String testFilesFolder = "src/testCases";
		String[] testFiles = TestUtils.listFiles(testFilesFolder, "tst");
		
		for (String testFile : testFiles) {
			// init source file and scanner
			PrintStream out = null;
			try {
				out = new PrintStream(
						new FileOutputStream(testFilesFolder + "/" + testFile + ".scanner.out"));
			} catch (FileNotFoundException e) {
				System.err.println("init:Source file " + testFile + "not found");
			}
			Scanner scanner = new Scanner();
			scanner.open(testFilesFolder + "/" + testFile);

			// scan and print
			Tokens t = scanner.getNextToken();
			while (t != Tokens.EOF && t != Tokens.SCAN_ERROR) {
				if (t == Tokens.NUMBER || t == Tokens.IDENT) {
					out.println(t.lexeme + ":" + scanner.getCurrentLexeme());
				} else {
					out.println(t.lexeme);
				}
				t = scanner.getNextToken();
			}
			out.println(t.lexeme);

			// close source file and scanner
			out.close();
			scanner.close();
		}
	}

}
