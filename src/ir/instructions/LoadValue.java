package ir.instructions;

import java.util.HashSet;

import back.regAloc.Variable;
import back.regAloc.VirtualRegister;


import front.symbolTable.Symbol;

public class LoadValue extends Instruction {
	
	public Symbol symbol;
	public Index address;
	
	public LoadValue(Index address) {
		// load value from address
		this.address = address;
		outputOp = new VirtualRegister();
	}

	public LoadValue(Symbol symbol) {
		// load value from symbol address address
		this.symbol = symbol;
		outputOp = new VirtualRegister();
	}
	
	public String toString() {
		return getInstrNumber() + " : LOAD " +
				"(@" + (symbol != null ? symbol.ident  : address.getInstrNumber() ) + ") -> " + outputOp;
	}
	
    @Override
    public HashSet<Variable> getVariables() {
        HashSet<Variable> ret = new HashSet<Variable>();
        if (symbol != null){
            ret.add( new Variable(symbol) );
        }else{
            //TODO fix to deal with addresses
            ret.add( new Variable(address) );
        }
        return ret;
    }
}