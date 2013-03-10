package compiler.ir.instructions;

import compiler.front.symbolTable.Symbol;
import compiler.front.symbolTable.VarSymbol;
import compiler.front.symbolTable.Symbol.SymbolKind;
import compiler.front.symbolTable.Type.TypeKind;

public class Local extends Scalar {

	public Local(Symbol symbol) {
		super(symbol);
	}

	public String getInstrLabel() {
		return symbol.ident;
	}

    public String toString(){
    	String ret = "LOCAL : ";
    	if (symbol.kind == SymbolKind.VAR){
	    	if (((VarSymbol)symbol).type.kind == TypeKind.ARRAY ){
	    		ret += "@";
	    	}
    	}
        return  ret + getSymbol();
    }
    
}
