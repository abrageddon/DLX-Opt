package front;

import ir.cfg.BasicBlock;
import ir.cfg.CFG;
import ir.instructions.Instruction;

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
		String[] testFiles = TestUtils.listFiles(testFilesFolder, ".tst");
		
		for (String testFile : testFiles) {
			// init output file and scanner
			
			PrintStream out = null;
			try {
				out = new PrintStream(
						new FileOutputStream(testFilesFolder + "/" + testFile + ".vcg"));
			} catch (FileNotFoundException e) {
				System.err.println("init:Source file " + testFile + "not found");
			}
			
			out.println("graph: { title: \"Control Flow Graph\"\n" +
//					"    layoutalgorithm: dfs\n" +
					"    display_edge_labels: yes\n" +
					"    manhatten_edges: no\n" +
					"\n" +
//					"    node.color: lightyellow\n" +
//					"    node.textcolor: blue\n"+
//					"    edge.color: blue\n"+
//					"    edge.arrowsize: 15\n"+
//					"    edge.thickness: 4\n"+
//					"    stretch: 43\n"+
//					"    shrink: 100\n"+
					"    classname 1 : \"CFG Edges (blue)\"\n"+
					"    classname 2 : \"Const Lists (red)\"\n"+
					"    classname 3 : \"Live Variable Lists (green)\"\n"+
					"		yspace: 34"+
					"       xspace: 30"+
					"       xlspace: 10"+
					       // scaling: 0.75
					"       portsharing: no"+
					"       finetuning: no"+
					"       equalydist: yes"+
					"       orientation: toptobottom"+
					"       lateedgelabels: no"+
					"       dirtyedgelabels: no"+
					"       linearsegments: no"+
					"       nearedges: yes"+
					"       fstraightphase: yes"+
					"       straightphase: yes"+
					"       priorityphase: yes"+
					"       crossingphase2: yes"+
					"       crossingoptimization: yes"+
					"       crossingweight: bary"+
					"       arrowmode: fixed"+
					"       node.borderwidth: 3"+
					"       node.color: 19"+
					"       node.bordercolor: 11"+
					"       edge.color: 7"+
					"       edge.arrowsize: 8"+
					"       edge.arrowcolor: 31\n"
					);
			

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
						out.print("    node: { title:\""+nodeNumber+"\" label: \""+currentBlock.label);
						
						//special formats
						if (currentBlock.label.equals("exit") || currentBlock.label.equals("start")){
							for (Instruction instruction:currentBlock.instructions){
								out.print("\n"+ instruction.toString());
							}
							out.print("\" shape: ellipse color: aquamarine ");
						}else if (currentBlock.label.equals("while-cond") || currentBlock.label.equals("if-cond")){
							for (Instruction instruction:currentBlock.instructions){
								out.print("\n"+ instruction.toString());
							}
							out.print("\" shape: rhomb color: pink ");
						}else if (currentBlock.label.equals("while-body") 
								|| currentBlock.label.equals("while-next") 
								|| currentBlock.label.equals("then") 
								|| currentBlock.label.equals("else") 
								|| currentBlock.label.equals("fi-join")){
							for (Instruction instruction:currentBlock.instructions){
								out.print("\n"+ instruction.toString());
							}
							out.print("\" ");
						}
						//close
						out.println("}");
						
						//next node
						++nodeNumber;
					}
					out.println();
					
					//Edges
					// TODO only add edges once
					for (BasicBlock node:nodeMap.keySet()){
						//out edges
						for (BasicBlock dest: node.succ){
							if (node.label.equals("if-cond") && dest.label.equals("then")){
								out.println("    bentnearedge: { sourcename:\""+nodeMap.get(node)+"\" targetname:\""+nodeMap.get(dest)+"\"  label: \"true\" class: 1}");
							}else if (node.label.equals("if-cond") && dest.label.equals("else")){
								out.println("    bentnearedge: { sourcename:\""+nodeMap.get(node)+"\" targetname:\""+nodeMap.get(dest)+"\"  label: \"false\" class: 1}");
							}else if (node.label.equals("while-cond") && dest.label.equals("while-body")){
								out.println("    bentnearedge: { sourcename:\""+nodeMap.get(node)+"\" targetname:\""+nodeMap.get(dest)+"\"  label: \"true\" class: 1}");
							}else if (node.label.equals("while-cond") && dest.label.equals("while-next")){
								out.println("    bentnearedge: { sourcename:\""+nodeMap.get(node)+"\" targetname:\""+nodeMap.get(dest)+"\"  label: \"false\" class: 1}");
							}else if (node.label.equals("while-body") && dest.label.equals("while-cond")){
								out.println("    backedge: { sourcename:\""+nodeMap.get(node)+"\" targetname:\""+nodeMap.get(dest)+"\" class: 1}");
							}else {
								out.println("    edge: { sourcename:\""+nodeMap.get(node)+"\" targetname:\""+nodeMap.get(dest)+"\" class: 1}");
							}
						}
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
