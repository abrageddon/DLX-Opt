package ir.instructions;

import front.symbolTable.Symbol;

public class Local extends Scalar {

	public Local(Symbol symbol) {
		super(symbol);
	}

    public String toString(){
//        return getInstrNumber() + " : " + getSymbol()+".local";
        return getSymbol()+".local";
    }
}
