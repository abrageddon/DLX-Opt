package ir.instructions;

import java.util.HashSet;

import back.regAloc.Variable;


import front.symbolTable.Symbol;

public class Scalar extends Instruction {

	public Symbol symbol;
	
	public Scalar(Symbol symbol) {
		this.symbol = symbol;
	}
	
	public String toString() {
		return getInstrNumber() + " : " + getSymbol();
	}
	
	public String getSymbol(){
        assert symbol != null;
        return symbol.toString();
	}
	
    @Override
    public HashSet<Variable> getVariables() {
        HashSet<Variable> ret = new HashSet<Variable>();
        ret.add( new Variable(symbol) );
        return ret;
    }
	
}
