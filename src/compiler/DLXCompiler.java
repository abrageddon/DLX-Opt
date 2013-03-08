package compiler;

import compiler.back.codeGen.CodeGenerator;
import compiler.back.regAloc.RegisterAllocator;
import compiler.front.Parser;
import compiler.front.SSAGenerator;
import compiler.front.Parser.ParserException;
import compiler.front.Scanner.ScannerException;

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
			new RegisterAllocator(parser.CFGs).allocateRegisters();
			new CodeGenerator().generateCode();
	}
	
}
