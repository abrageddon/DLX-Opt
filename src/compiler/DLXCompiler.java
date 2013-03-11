package compiler;


import java.util.Iterator;

import compiler.back.codeGen.CodeGenerator;
import compiler.back.regAloc.RegisterAllocator;
import compiler.front.Parser;
import compiler.front.SSAGenerator;
import compiler.front.Parser.ParserException;
import compiler.front.Scanner.ScannerException;
import compiler.ir.cfg.BasicBlock;
import compiler.ir.cfg.CFG;
import compiler.ir.instructions.Instruction;

public class DLXCompiler {
	
	// TODO the parser should not be visible outside the compiler, e.g. in VCGPrinter
	public Parser parser;
	public String outFile;

	public DLXCompiler(String srcFile) {
		this.outFile = srcFile + ".bin";
		this.parser = new Parser(srcFile);
	}

	public void terminate() {
		parser.terminate();
	}

	public void compile() throws ParserException, ScannerException {
			parser.parse();

			new SSAGenerator(parser.CFGs).generateSSA();

			SSAGenerator ssaGen = new SSAGenerator(parser.CFGs); 
			ssaGen.generateSSA();
            parser.renumberInstructions();
			// TODO should display the graph before we generate SSA
			ssaGen.deconstructSSA();
			
            parser.renumberInstructions();
			new RegisterAllocator(parser.CFGs).allocateRegisters();
			new CodeGenerator(parser.CFGs).generateCode(outFile);

	}
	
}
