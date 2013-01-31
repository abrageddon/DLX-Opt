package ir.instructions;

import front.symbolTable.Symbol;

public class LoadAddress extends Instruction {

	public LoadAddress(Symbol sym) {
		// assert (sym is Array)
		// loads sym's base address
	}

	public String toString(){
		return "LOAD_ADDRESS";
	}
}