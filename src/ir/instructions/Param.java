package ir.instructions;

import front.symbolTable.Symbol;

public class Param extends Scalar {

	public Param(Symbol symbol) {
		super(symbol);
	}

	public String getInstrLabel() {
		return symbol.ident;
	}
	
	public String toString(){
        return "PARAM : " + getSymbol();
	}
}
