package tests;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Test;

import compiler.front.Scanner;
import compiler.front.Tokens;
import compiler.front.Scanner.ScannerException;


public class ScannerTest {

	@Test
	public void scanFiles() throws IOException {
		String testFilesFolder = "src/testCases";
		String[] testFiles = TestUtils.listFiles(testFilesFolder, "tst");
		
		for (String testFile : testFiles) {
			// init output file and scanner
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
			try {
				do {
					scanner.next();
					if (scanner.currentToken == Tokens.NUMBER ||
							scanner.currentToken == Tokens.IDENT) {
						out.println(scanner.currentToken.lexeme + ":" +
							scanner.currentLexeme);
					} else {
						out.println(scanner.currentToken.lexeme);
					}
					
				} while (scanner.currentToken != Tokens.EOF);
//				
//				scanner.next();
//				while (scanner.currentToken != Tokens.EOF) {
//					if (scanner.currentToken == Tokens.NUMBER ||
//							scanner.currentToken == Tokens.IDENT) {
//						out.println(scanner.currentToken.lexeme + ":" +
//							scanner.currentLexeme);
//					} else {
//						out.println(scanner.currentToken.lexeme);
//					}
//					scanner.next();
//				}
//				out.println(scanner.currentToken.lexeme);
			} catch (ScannerException e) {
				e.printStackTrace();
			} finally {
				// close output file and scanner
				out.close();
				scanner.close();
			}
		}
	}

}
