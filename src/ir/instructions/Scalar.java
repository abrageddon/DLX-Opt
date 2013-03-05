package ir.instructions;

import java.util.HashSet;

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
    public HashSet<Symbol> getVariables() {
        HashSet<Symbol> ret = new HashSet<Symbol>();
        ret.add(symbol);
        return ret;
    }
	
}
