package ir.instructions;

import java.util.List;

import front.symbolTable.FunctionSymbol;

public class Call extends Instruction {
	public FunctionSymbol function;
	public List<Instruction> args;
	
	public Call(FunctionSymbol fnct, List<Instruction> args) {
		this.function = fnct;
		this.args = args;
	}
	
	public String toString() {
//		return getInstrNumber() + " : CALL " + function.ident + "("+((FunctionSymbol)function).formalParams+")";
		String argsString = "";
		for(Instruction inst : args) {
			argsString += inst.getInstrNumber() + " ";
		}
		return getInstrNumber() + " : CALL " + function.ident + "("+ argsString +")";
	}
	
}
