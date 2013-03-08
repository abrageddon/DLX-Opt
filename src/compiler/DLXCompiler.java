package compiler;
import ir.cfg.*;
import ir.instructions.*;

import java.util.Iterator;

import back.codeGen.CodeGenerator;
import back.regAloc.RegisterAllocator;

import front.Parser;
import front.Parser.ParserException;
import front.Scanner.ScannerException;


public class DLXCompiler {
	
	// TODO the parser should not be visible outside the compiler, e.g. in VCGPrinter
	public Parser parser;

	public DLXCompiler(String srcFile) {
		this.parser = new Parser(srcFile);
	}

	public void terminate() {
		parser.terminate();
	}

	public void compile() throws ParserException, ScannerException {
			parser.parse();
			new SSAGenerator(parser.CFGs).generateSSA();
			renumberInstructions();
			new RegisterAllocator(parser.CFGs).allocateRegisters();
			new CodeGenerator().generateCode();
	}
		
	void renumberInstructions() {
		int instrCnt = 0;
		for (CFG cfg : parser.CFGs) {
			// Iterate over BBs and initialize entry and exit states
			Iterator<BasicBlock> blockIterator = cfg.topDownIterator();
			while (blockIterator.hasNext()) {
				BasicBlock bb = blockIterator.next();
				for (Instruction inst : bb.getInstructions()) {
					inst.setInstrNumber(instrCnt++);
				}
			}
		}
	}
		

}
