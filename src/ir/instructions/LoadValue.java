package ir.instructions;

import back.regAloc.VirtualRegister;


import front.symbolTable.Symbol;

public class LoadValue extends Instruction {
	
	public Symbol symbol;
	public Index address;
	
	public LoadValue(Index address) {
		// load value from address
		this.address = address;
		outputOp = new VirtualRegister();
	}

	public LoadValue(Symbol symbol) {
		// load value from symbol address address
		this.symbol = symbol;
		outputOp = new VirtualRegister();
	}
	
	public String toString() {
		return getInstrNumber() + " : LOAD " +
				"(@" + (symbol != null ? symbol.ident  : address.getInstrNumber() ) + ") -> " + outputOp;
	}
}