package ir.instructions;

import front.symbolTable.Symbol;

public class Param extends Scalar {

	public Param(Symbol symbol) {
		super(symbol);
	}

	public String toString(){
        return getInstrNumber() + " : PARAM " + getSymbol();
//        return getSymbol()+".param";
	}
}
