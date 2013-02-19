package ir.instructions;

import front.symbolTable.Symbol;

public class Global extends Scalar {

	public Global(Symbol symbol) {
		super(symbol);
	}

    public String toString(){
        return "GLOBAL : " + getSymbol();
    }

}
