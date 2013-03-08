package ir.instructions;

import java.util.HashSet;

import back.regAloc.Variable;


import front.symbolTable.Symbol;

public class StoreValue extends Instruction {

	
	public Symbol symbol;
    public Instruction value;
	public Index address;
	
	public StoreValue(Index address, Instruction value) {
		// store value to address
        this.value = value;
		this.address = address;
	}

	public StoreValue(Symbol symbol, Instruction value) {
		// store value to address
		// this.address = symbol.getAddress(); //TODO
		this.symbol = symbol;
		this.value = value;
	}
	
	public String toString(){
//	    if (regA != 0){
//	        return getInstrNumber() + " : STORE " +
//	                "(" + value.getInstrNumber() + ")" +
//	                "(r:" + regA + ")";
//	    }
	    
		return getInstrNumber() + " : STORE " +
				"(" + value.getInstrNumber() + ")" +
				"(@" + (symbol != null ? symbol.ident  : address.getInstrNumber() ) + ")" + 
				" \n [" + Instruction.resolve(value).outputOp +"]";
	}

    @Override
    public HashSet<Variable> getVariables() {
        HashSet<Variable> ret = new HashSet<Variable>();
        if (symbol != null){
            ret.add( new Variable(symbol) );
        }else if (address != null){
            //TODO fix to deal with addresses
            ret.add( new Variable(address) );
        }
        return ret;
    }
	
	

}
