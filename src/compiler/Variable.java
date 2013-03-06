package compiler;

import ir.instructions.Index;

import java.util.ArrayList;

import front.symbolTable.Symbol;

public class Variable {
    public Symbol variable;
    public Index address;
	public int startLine, endLine;
	//Interference map
	public ArrayList<Variable> interference;
	public int prefrence;
	//Register selected. Negative means Pseudo Register.
	public int register;
	

    public Variable(Symbol symbol) {
        variable = symbol;
    }
    public Variable(Index addr) {
        address = addr;
    }
    public String toString(){
        
        if (variable != null){
            return variable.toString();
        }else if (address != null){
            return "@" + address.toString();
        }
        
        return "";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (variable != null){
            return variable.equals(((Variable)obj).variable);
        }else if (address != null){
            return address.equals(((Variable)obj).address);
        }
        return false;
    }
    
}
