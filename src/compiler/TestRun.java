package compiler;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import org.junit.Test;

import compiler.DLXCompiler;
import compiler.front.Parser.ParserException;
import compiler.front.Scanner.ScannerException;

import tests.TestUtils;

public class TestRun {
	private DLXCompiler compiler;

    public static void main(String args[]) {
        if (args.length < 1) {
            System.err.println("Usage: TestRun <input code file>");
            return;
        }

        try {
        	String fileName = args[0];
        	int prog[] = null;
        	List<String> data = null;
        	if (fileName.endsWith(".bin")){
        		prog = readBin(new File(fileName) );
        		data = readDebug(new File(fileName) );
        	}else{
        	    new DLXCompiler(fileName).compile();
        		prog = readBin(new File(fileName + ".bin"));
        		data = readDebug(new File(fileName + ".bin"));
        	}
        	
            DLX dlx = new DLX();
            
            File inFile = new File( fileName.substring(0, fileName.length()-4)+".in" );
            if (inFile.exists()){
//	            InputStream origIn = System.in,
//	                        newIn = new BufferedInputStream(
//	                                new FileInputStream( inFile ));
//	            System.setIn(newIn);

	            dlx.load(prog);
	            dlx.loadInputs(inFile);
//	            dlx.loadDebug(data);//Comment out to stop debugging data
//	            dlx.displayProgram();
	            dlx.execute();
	            
//	            System.setIn(origIn);
//	            newIn.close();
	            
	            //TODO Diff with given correct answers
			}else{
	            dlx.load(prog);
//	            dlx.loadDebug(data);//Comment out to stop debugging data
//	            dlx.displayProgram();
	            dlx.execute();
			}
            
            
        } catch (IOException | ParserException | ScannerException e) {
            System.err.println("Error reading input files!: \n" + e);
        }
	}
    
	public void terminate() {
		compiler.terminate();
	}

	@Test
	public void runAll() {
		String testFilesFolder = "src/testCases";
		String[] testFiles = TestUtils.listFiles(testFilesFolder, ".tst");

		for (String testFile : testFiles) {
			// init output file and scanner
			
			compiler = new DLXCompiler(testFilesFolder + "/" + testFile);
			try {
				compiler.compile();
				File binFile = new File(testFilesFolder + "/" + testFile + ".bin");
				System.err.println(testFile);
	            DLX dlx = new DLX();
	            dlx.load(readBin(binFile));
				
	            // Redirect System.in from DLX to data file
				File inFile = new File( testFilesFolder + "/" + testFile.substring(0, testFile.length()-4)+".in" );
	            if (inFile.exists()){
//		            InputStream origIn = System.in,
//		                        newIn = new BufferedInputStream(
//		                                new FileInputStream( inFile ));
//		            System.setIn(newIn);

		            dlx.loadInputs(inFile);
		            dlx.execute();
		            
//		            System.setIn(origIn);
//		            newIn.close();
		            
		            //TODO Diff with given correct answers
				}
	            
				
				
			} catch (ParserException | ScannerException | IOException e) {
				System.err.println(testFile);
				e.printStackTrace();
			}
		}
	}


	private static int[] readBin(File binFile) {
		List<Integer> prog = new ArrayList<Integer>();
		int lineNum = 0;
		try {
			BufferedReader read = new BufferedReader(
			                new FileReader( binFile ));
			while(read.ready()){
				String line = read.readLine();
				if(line.trim().startsWith("#")){
					continue;
				}
				if (line.trim().length() != 0){
					prog.add(lineNum++, Integer.valueOf(line));
				}
			}
			read.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return convertIntegers(prog);
	}

	private static List<String> readDebug(File binFile) {
		ArrayList<String> data = new ArrayList<String>();
		int lineNum = -1;
		try {
			BufferedReader read = new BufferedReader(
			                new FileReader( binFile ));
			while(read.ready()){
				String line = read.readLine();
				if(line.trim().startsWith("#")){
					while(data.size()<=lineNum){
						data.add("");
					}
					if (data.get(lineNum).equals("")){
						data.set(lineNum, line);
					}else{
						data.set(lineNum, data.get(lineNum) +"\n"+ line);
					}
				}else if (line.trim().length() != 0){
					lineNum++;
				}
			}
			read.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return data;
	}
	
	public static int[] convertIntegers(List<Integer> integers)
	{
	    int[] ret = new int[integers.size()];
	    Iterator<Integer> iterator = integers.iterator();
	    for (int i = 0; i < ret.length; i++)
	    {
	        ret[i] = iterator.next().intValue();
	    }
	    return ret;
	}
}
