package ir.instructions;

import front.symbolTable.Symbol;

public class Scalar extends Instruction {

	public Symbol symbol;
	
	public Scalar(Symbol symbol) {
		this.symbol = symbol;
	}

	public String toString(){
		if (symbol == null){
			return "SCALAR";
		}
		return symbol.toString();
	}
}
