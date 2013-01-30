package ir.instructions;

import front.symbolTable.Symbol;

public class Scalar extends Instruction {

	public Symbol symbol;
	
	public Scalar(Symbol symbol) {
		this.symbol = symbol;
	}
	
}
