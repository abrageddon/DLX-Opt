package compiler.ir.instructions;

import compiler.front.symbolTable.Symbol;

public class Global extends Scalar {

	public Global(Symbol symbol) {
		super(symbol);
	}

    public String toString(){
        return "GLOBAL : " + getSymbol();
    }

}
