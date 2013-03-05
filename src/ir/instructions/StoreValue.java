package ir.instructions;

import java.util.ArrayList;
import java.util.HashSet;

import front.symbolTable.Symbol;

public class StoreValue extends Instruction {

	
	public Symbol symbol;
	public Index address;
	public Instruction value;
	
	public StoreValue(Index address, Instruction value) {
		// store value to address
		this.address = address;
		this.value = value;
	}

	public StoreValue(Symbol symbol, Instruction value) {
		// store value to address
		// this.address = symbol.getAddress(); //TODO
		this.symbol = symbol;
		this.value = value;
	}
	
	public String toString(){
		return getInstrNumber() + " : STORE " +
				"(" + value.getInstrNumber() + ")" +
				"(@" + (symbol != null ? symbol.ident  : address.getInstrNumber() ) + ")";
	}

    @Override
    public HashSet<Symbol> getVariables() {
        HashSet<Symbol> ret = new HashSet<Symbol>();
        ret.add(symbol);
        return ret;
    }
	
	

}
