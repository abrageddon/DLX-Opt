package compiler.ir.instructions;

import compiler.front.symbolTable.Symbol;

public class Local extends Scalar {

	public Local(Symbol symbol) {
		super(symbol);
	}

	public String getInstrLabel() {
		return symbol.ident;
	}

    public String toString(){
        return "LOCAL : " + getSymbol();
    }
    
}
