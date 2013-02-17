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

import compiler.DLXCompiler;


import tests.TestUtils;
import front.Parser.ParserException;
import front.Scanner.ScannerException;

public class VCGPrinter {
    @Test
    public void generateCFGs() throws IOException {
        String testFilesFolder = "src/testCases";
        String[] testFiles = TestUtils.listFiles(testFilesFolder, ".tst");// Edit here to run one test

        for (String testFile : testFiles) {
            // init output file and scanner

            PrintStream out = null;
            try {
                out = new PrintStream(new FileOutputStream(testFilesFolder + "/" + testFile + ".vcg"));
            } catch (FileNotFoundException e) {
                System.err.println("init:Source file " + testFile + "not found");
            }

            // open graph and initial settings; closed in "finally" block
            out.println("graph: { title: \"Control Flow Graph\"\n"
                    + "    layoutalgorithm: dfs\n"
                    + "    display_edge_labels: yes\n"
                    + "    manhatten_edges: yes\n"
                    + "\n"
                    + "    classname 1 : \"CFG Edges (blue)\"\n" 
                    + "    classname 2 : \"Const Lists (red)\"\n"
                    + "    classname 3 : \"Live Variable Lists (green)\"\n"
                    + "		  yspace: 34\n"
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

            // parse
            DLXCompiler compiler = new DLXCompiler(testFilesFolder + "/" + testFile);;
//            Parser parser = new Parser(testFilesFolder + "/" + testFile);
            int nodeNumber = 0;
            HashMap<BasicBlock, Integer> nodeMap = new HashMap<BasicBlock, Integer>();
            try {
//                parser.parse();
            	compiler.compile();
                List<CFG> CFGs = compiler.parser.CFGs;
                
                for (CFG cfg : CFGs) {//
                    cfg.calculateDepths();
                    
                    // TODO print CFG's frame or globals if CFG is main!
                    // locals, parameters and globals are not present 
                    // in the instruction stream anymore
                    
                    // Nodes
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
                            out.print(": "+ cfg.label);
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
                    out.println();
                }
//                System.out.println(nodeMap);
                // Edges
                for (BasicBlock node : nodeMap.keySet()) {
                    // out edges
//                    System.out.println(nodeMap.get(node)+ " : " +node +"\nSucc: "+node.succ +"\n");
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

            } catch (ParserException
                    | ScannerException e) {
                e.printStackTrace();
            } 
            finally {
                // close output file and scanner
                out.println("}");
                out.close();
//                parser.terminate();
                compiler.terminate();
            }
        }
    }
}
