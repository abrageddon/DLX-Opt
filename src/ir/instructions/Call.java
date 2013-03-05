package ir.instructions;

import front.symbolTable.FunctionSymbol;
import front.symbolTable.Symbol;

public class Call extends Instruction {
	public Symbol function;
	
	public Call(Symbol fnct) {
		this.function = fnct;
	}
	
	public String toString() {
		return getInstrNumber() + " : CALL " + function.ident + "("+((FunctionSymbol)function).formalParams+")";
	}
	
}
