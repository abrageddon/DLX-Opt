package ir.instructions;

import front.symbolTable.Symbol;

public class Scalar extends Instruction {

	public Symbol symbol;
	
	public Scalar(Symbol symbol) {
		this.symbol = symbol;
	}
	
	public String toString() {
		return getInstrNumber() + " : " + getSymbol();
	}
	
	public String getSymbol(){
        assert symbol != null;
        return symbol.toString();
	}
	
}
