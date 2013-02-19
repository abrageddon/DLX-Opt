package front;

import ir.cfg.BasicBlock;
import ir.cfg.CFG;
import ir.instructions.Global;
import ir.instructions.Instruction;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.junit.Test;

import compiler.DLXCompiler;

import tests.TestUtils;
import front.Parser.ParserException;
import front.Scanner.ScannerException;

public class VCGPrinter {
    private DLXCompiler compiler;
    private List<CFG> CFGs;
    private HashMap<BasicBlock, Integer> nodeMap;
    private HashMap<BasicBlock, HashSet<BasicBlock>> dominatorMap;
    private Integer nodeNumber;
//    private String fileName;
    
	@Test
    public void generateCFGs() throws IOException {
        String testFilesFolder = "src/testCases";
        String[] testFiles = TestUtils.listFiles(testFilesFolder, ".tst");// Edit here to run one test

        for (String testFile : testFiles) {
            // init output file and scanner

//        	fileName = testFile;
        	
            PrintStream vcgOut = null;
            try {
                vcgOut = new PrintStream(new FileOutputStream(testFilesFolder + "/" + testFile + ".vcg"));
            } catch (FileNotFoundException e) {
                System.err.println("init:Source file " + testFile + "not found");
            }
            

            // open graph and initial settings; closed in "finally" block
            openGraph(vcgOut);

            // parse
            compiler = new DLXCompiler(testFilesFolder + "/" + testFile);
//            Parser parser = new Parser(testFilesFolder + "/" + testFile);
            nodeNumber = 0;
            nodeMap = new HashMap<BasicBlock, Integer>();
    		dominatorMap = new HashMap<BasicBlock, HashSet<BasicBlock>>();
            try {
//                parser.parse();
            	compiler.compile();
                CFGs = compiler.parser.CFGs;
                
                for (CFG cfg : CFGs) {//
                    cfg.calculateDepths();
                    
                    // Nodes
                    buildNodes(vcgOut, cfg);
                    
                    vcgOut.println();
                    
                    // Build dominator tree
                    createDominatorEdges(cfg);
                }
                
                // Edges
                cfgEdges(vcgOut);

                vcgOut.println();

                // Print dominator edges
                dominatorEdges(vcgOut);

            } catch (ParserException
                    | ScannerException e) {
                e.printStackTrace();
            } 
            finally {
                // close output file and scanner
                closeGraph(vcgOut);
                compiler.terminate();
            }
        }
    }


    private void openGraph(PrintStream out) {
        out.println("graph: { title: \"Control Flow Graph\"\n"
                + "    layoutalgorithm: dfs\n"
                + "    display_edge_labels: yes\n"
                + "    manhatten_edges: yes\n"
                + "\n"
                + "    classname 1 : \"CFG Edges (blue)\"\n" 
                + "    classname 2 : \"Const Lists (red)\"\n"
                + "    classname 3 : \"Live Variable Lists (green)\"\n"
                + "    classname 4 : \"Dominator Graph (gray)\"\n"
                + "       yspace: 34\n"
                + "       xspace: 30\n"
                + "       xlspace: 10\n"
                // scaling: 0.75
                + "       portsharing: no\n" 
                + "       finetuning: no\n" 
                + "       equalydist: yes\n" 
                + "       orientation: toptobottom\n" 
                + "       lateedgelabels: no\n"
                + "       dirtyedgelabels: no\n" 
                + "       linearsegments: yes\n" 
                + "       nearedges: yes\n" 
                + "       fstraightphase: yes\n" 
                + "       straightphase: yes\n"
                + "       priorityphase: yes\n" 
                + "       crossingphase2: yes\n" 
                + "       crossingoptimization: yes\n" 
                + "       crossingweight: medianbary\n" 
                + "       arrowmode: fixed\n"
                + "       node.borderwidth: 3\n" 
                + "       node.bordercolor: darkyellow\n" 
                + "       node.color: lightyellow\n"
                + "       node.textcolor: black\n"
                + "       edge.arrowsize: 15\n"
                + "       edge.thickness: 4\n");
    }

    private void closeGraph(PrintStream out) {
        out.println("}");
        out.close();
    }

    private void buildNodes(PrintStream out, CFG cfg) {
        Iterator<BasicBlock> blockIterator = cfg.topDownIterator();
        while (blockIterator.hasNext()) {
            BasicBlock currentBlock = blockIterator.next();

            // insert into node map
            nodeMap.put(currentBlock, nodeNumber);

            // basic name; label open
            out.print("    node: { title:\"" + nodeNumber 
                    + "\" info1: \""+ currentBlock.label + "\nNode: "+ nodeNumber + "\nDepth: "+ currentBlock.depth + "\nFunction: " + cfg.label
                    + "\" vertical_order: "+currentBlock.depth + " label: \"" + currentBlock.label);
            
            // function names for start and exit blocks
            if (currentBlock.label.equals("exit") || currentBlock.label.equals("start")){
                out.print(" : "+ cfg.label);
            }
            if (currentBlock.label.equals("start")) {
                if (cfg.label.equals("main")) {
                    for (Global global : compiler.parser.globals) {
                        out.print("\n" + global.toString());
                    }
                }
                for (Instruction frameItem : cfg.frame) {
                    out.print("\n" + frameItem.toString());
                }
            }
            
            // print instructions if they exist
            if (!currentBlock.isInstructionsEmpty()){
                for (Instruction instruction : currentBlock.getInstructions()) {
                    out.print("\n" + instruction.toString());
                }   
            }
            
            // label closed
            out.print("\" ");
            
            // special formats
            if (currentBlock.label.equals("exit")){
                out.print("shape: ellipse color: pink bordercolor: darkred ");
            } else if(currentBlock.label.equals("start")) {
                out.print("shape: ellipse color: lightgreen bordercolor: darkgreen ");
            } else if (currentBlock.label.equals("while-cond") || currentBlock.label.equals("if-cond")) {
                out.print("shape: rhomb color: lightcyan bordercolor: darkblue ");
            }
            
            // close
            out.print("}\n");

            // next node
            ++nodeNumber;
        }
    }

    
    private void cfgEdges(PrintStream out) {
        for (BasicBlock node : nodeMap.keySet()) {
            // out edges
            for (BasicBlock dest : node.succ) {
                if (node.label.equals("if-cond") && dest.label.equals("then")) {
                    out.println("    bentnearedge: { sourcename:\"" + nodeMap.get(node) + "\" targetname:\"" + nodeMap.get(dest) + "\"  label: \"true\" color: darkgreen class: 1}");
                } else if (node.label.equals("if-cond") && dest.label.equals("else")) {
                    out.println("    bentnearedge: { sourcename:\"" + nodeMap.get(node) + "\" targetname:\"" + nodeMap.get(dest) + "\"  label: \"false\" color: red class: 1}");
                } else if (node.label.equals("while-cond") && dest.label.equals("while-body")) {
                    out.println("    bentnearedge: { sourcename:\"" + nodeMap.get(node) + "\" targetname:\"" + nodeMap.get(dest) + "\"  label: \"true\" color: darkgreen class: 1}");
                } else if (node.label.equals("while-cond") && dest.label.equals("while-next")) {
                    out.println("    bentnearedge: { sourcename:\"" + nodeMap.get(node) + "\" targetname:\"" + nodeMap.get(dest) + "\"  label: \"false\" color: red class: 1}");
                } else if (node.depth > dest.depth) {
                    out.println("    backedge: { sourcename:\"" + nodeMap.get(node) + "\" targetname:\"" + nodeMap.get(dest) + "\"  label: \"back\" color: orange class: 1}");
                } else {
                    out.println("    edge: { sourcename:\"" + nodeMap.get(node) + "\" targetname:\"" + nodeMap.get(dest) + "\" class: 1}");
                }
            }
        }
    }
    
	@SuppressWarnings("unchecked")
	private void createDominatorEdges(CFG cfg) {
		Iterator<BasicBlock> blockIterator = cfg.bottomUpIterator();
		HashSet<BasicBlock> allNodeSet = new HashSet<BasicBlock>();
		Stack<BasicBlock> workList = new Stack<BasicBlock>();

		while (blockIterator.hasNext()) {
		    BasicBlock currentBlock = blockIterator.next();
		    allNodeSet.add(currentBlock);
		}
		
//      For (each n in NodeSet)
		blockIterator = cfg.topDownIterator();
		while (blockIterator.hasNext()) {
//      	Dom(n) = NodeSet
		    BasicBlock currentBlock = blockIterator.next();
			currentBlock.semiDom = (HashSet<BasicBlock>) allNodeSet.clone();
		}
//      WorkList = {StartNode}
		workList.push(cfg.startBB);
//      While (WorkList != null) {
		while (!workList.isEmpty()){
//          Remove any node Y from WorkList
			BasicBlock workNode = workList.pop();
//          New = {Y} U intersects Dom(X);X in Pred(Y)
			HashSet<BasicBlock> newDom = (HashSet<BasicBlock>) allNodeSet.clone();// TODO figure out this part
			if (!workNode.pred.isEmpty()){
				for (BasicBlock pred: workNode.pred){
					newDom.retainAll( pred.semiDom );
				}
			}else{
				newDom = new HashSet<BasicBlock>();
			}
			newDom.add(workNode);
//          If New != Dom(Y) {
			if(! ( newDom.containsAll(workNode.semiDom) 
					&& workNode.semiDom.containsAll(newDom)  ) ){
//              Dom(Y) = New
				workNode.semiDom = (HashSet<BasicBlock>) newDom.clone();
				
				
//              For (each Z in Succ(Y))
				for(BasicBlock succ: workNode.succ){
//                  WorkList = WorkList U {Z}
					workList.push(succ);
				}
			}
			
			
		}

        // find immediate dominator
        for (BasicBlock node : nodeMap.keySet()) {
            HashSet<BasicBlock> possibleIDoms = (HashSet<BasicBlock>) node.semiDom.clone();
            for (BasicBlock iDom : possibleIDoms) {
                if(iDom == null || iDom == node){continue;}
                if(node.iDom == null ){
                    node.iDom = iDom;
                }else if(node.iDom.depth < iDom.depth && iDom != node){
                    node.iDom = iDom;
                }
            }
		}
		
/*
//		for i := n by -1 until 2 do
//		    w := vertex(i);
        blockIterator = cfg.bottomUpIterator();
        while (blockIterator.hasNext()) {
//          for each v in bucket(parent(w)) do
            BasicBlock currentBlock = blockIterator.next();
            for (BasicBlock bucketParent: currentBlock.semiDom){
//		        delete v from bucket(parent(w));
                bucketParent.semiDom.remove(currentBlock);
//		        u := EVAL(v);
//		        dom(v) := if semi(u) < semi(v) then u
//		            else parent(w) fi od od;
            }
		    
		    
//		    for i := 2 until n do
//		        w := vertex(i);
//		        if dom(w) != vertex(semi(w)) then dom(w) := dom(dom(w)) fi od;
//		    dom(r) := 0;
        }
//*/
		
	}

    private void dominatorEdges(PrintStream out) {
        for (BasicBlock node : nodeMap.keySet()) {
//             dominator edges
//            for (BasicBlock dominator : node.semiDom) {
//        		out.println("    edge: { sourcename:\"" + nodeMap.get(dominator) + "\" targetname:\"" + nodeMap.get(node) + "\" label: \"DOM\" color: darkgray  class: 4}");
//            }
            if (node.iDom != null){
                out.println("    edge: { sourcename:\"" + nodeMap.get(node.iDom) + "\" targetname:\"" + nodeMap.get(node) + "\" label: \"DOM\" color: lightgray  class: 4}");
            }
        }
    }
}
