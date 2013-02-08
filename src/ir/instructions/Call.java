package ir.instructions;

import front.symbolTable.Symbol;

public class Call extends Instruction {
	public Symbol function;
	
	public Call(Symbol fnct) {
		this.function = fnct;
	}
	
	public String toString() {
		return super.toString() + " : CALL " + function.ident;
	}
	
}
