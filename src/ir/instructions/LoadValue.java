package ir.instructions;

import java.util.HashSet;

import front.symbolTable.Symbol;

public class LoadValue extends Instruction {
	
	public Symbol symbol;
	public Index address;
	
	public LoadValue(Index address) {
		// load value from address
		this.address = address;
	}

	public LoadValue(Symbol symbol) {
		// load value from symbol address address
		this.symbol = symbol;
	}
	
	public String toString() {
		return getInstrNumber() + " : LOAD " +
				"(@" + (symbol != null ? symbol.ident  : address.getInstrNumber() ) + ")";
	}
	
    @Override
    public HashSet<Symbol> getVariables() {
        HashSet<Symbol> ret = new HashSet<Symbol>();
        ret.add(symbol);
        return ret;
    }
}