package compiler.ir.instructions;

import compiler.front.symbolTable.*;
import compiler.front.symbolTable.Symbol.SymbolKind;
import compiler.front.symbolTable.Type.TypeKind;

public class Global extends Scalar {

	public Global(Symbol symbol) {
		super(symbol);
	}

    public String toString(){
    	String ret = "GLOBAL : ";
    	if (symbol.kind == SymbolKind.VAR){
	    	if (((VarSymbol)symbol).type.kind == TypeKind.ARRAY ){
	    		ret += "@";
	    	}
    	}
        return  ret + getSymbol();
    }

}
