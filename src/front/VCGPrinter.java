package front;

import ir.cfg.BasicBlock;
import ir.cfg.CFG;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;
import tests.TestUtils;
import front.Parser.ParserException;
import front.Scanner.ScannerException;

public class VCGPrinter {
	@Test
	public void generateCFGs() throws IOException {
		String testFilesFolder = "src/testCases";
		String[] testFiles = TestUtils.listFiles(testFilesFolder, "1-11.tst");
		
		for (String testFile : testFiles) {
			// init output file and scanner
			
			PrintStream out = null;
			try {
				out = new PrintStream(
						new FileOutputStream(testFilesFolder + "/" + testFile + ".vcg"));
			} catch (FileNotFoundException e) {
				System.err.println("init:Source file " + testFile + "not found");
			}
			
			out.println("graph: { title: \"CFG_GRAPH\"\n" +
					"	layoutalgorithm: dfs\n" +
					"	display_edge_labels: yes\n" +
					"	manhatten_edges: yes\n" +
					"\n" +
					"node.color: lightyellow\n" +
					"node.textcolor: blue\n"+
					"edge.color: blue\n"+
					"edge.arrowsize: 15\n"+
					"edge.thickness: 4\n"+
					"stretch: 43\n"+
					"shrink: 100\n"+
					"classname 1 : \"CFG Edges (blue)\"\n"+
					"classname 2 : \"Const Lists (red)\"\n"+
					"classname 3 : \"Live Variable Lists (green)\"");
			

			// parse
			Parser parser = new Parser(testFilesFolder + "/" + testFile);
			int nodeNumber = 0;
			HashMap<BasicBlock, Integer> nodeMap = new HashMap<BasicBlock, Integer>();
			try {
				parser.parse();
				List<CFG> CFGs = parser.CFGs;
				
				for (CFG cfg:CFGs){
					//Nodes
					Iterator<BasicBlock> blockIterator = cfg.topDownIterator();
					while (blockIterator.hasNext()){
						BasicBlock currentBlock = blockIterator.next();
						
						//insert into node map
						nodeMap.put(currentBlock, nodeNumber);
						
						// basic name label
						out.print("node: { title:\""+nodeNumber+"\"label: \""+currentBlock.label);
						
						//special formats
						if (currentBlock.label.equals("exit") || currentBlock.label.equals("start")){
							out.print("shape: ellipse color: aquamarine ");
						}else if (currentBlock.label.equals("while-cond")){
							out.print("\n"+currentBlock.instructions+"\" shape: rhomb color: pink ");
						}else if (currentBlock.label.equals("while-body") || currentBlock.label.equals("while-next")){
							out.print("\n"+currentBlock.instructions+"\" ");
						}
						//close
						out.println("}");
						
						//next
						++nodeNumber;
					}
					
					//Edges
					// TODO only add edges once
					for (BasicBlock node:nodeMap.keySet()){
						//in edges
						for (BasicBlock source: node.pred){
							out.println("edge: { sourcename:\""+nodeMap.get(source)+"\" targetname:\""+nodeMap.get(node)+"\" class: 1}");
						}
						//out edges
						for (BasicBlock dest: node.pred){
							out.println("edge: { sourcename:\""+nodeMap.get(node)+"\" targetname:\""+nodeMap.get(dest)+"\" class: 1}");
						}
						//backedges
					}
				}
				
				
			} catch (ParserException | ScannerException e) {
				e.printStackTrace();
			} finally {
				// close output file and scanner
				out.println("}");
				out.close();
				parser.terminate();
			}
		}
	}
}
