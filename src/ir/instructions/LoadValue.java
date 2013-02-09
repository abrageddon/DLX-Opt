package ir.instructions;

import front.symbolTable.Symbol;

public class LoadValue extends Instruction {
	
	public Symbol symbol;
	public Index address;
	
	public LoadValue(Index address) {
		// load value from address
		this.address = address;
	}

	public LoadValue(Symbol symbol) {
		// load value from symbol address address
		this.symbol = symbol;
	}
	
	public String toString() {
		return super.toString() + " : LOAD " +
				"(@" + (symbol != null ? symbol.ident  : address.getInstrNumber() ) + ")";
	}
}