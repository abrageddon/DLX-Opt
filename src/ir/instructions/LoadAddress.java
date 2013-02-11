package ir.instructions;

import front.symbolTable.Symbol;

public class LoadAddress extends Instruction {

	public Symbol symbol;
	
	public LoadAddress(Symbol symbol) {
		// assert (sym is Array)
		// loads sym's base address
		this.symbol  = symbol;
	}

	public String toString(){
		return getInstrNumber() + " : LOAD_ADDRESS " + symbol.toString();
	}
}