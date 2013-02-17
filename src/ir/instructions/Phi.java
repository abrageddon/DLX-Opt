package ir.instructions;

import java.util.List;

public class Phi extends Instruction {

	public List<Instruction> values;

	public Phi(List<Instruction> values) {
		this.values = values;
	}
	
    public String toString(){
    	String ret = "";
    	ret += getInstrNumber() + " : PHI: (";
    	for (Instruction val : values) {
    		ret += val.getInstrLabel() + " ";
    	}
    	ret += ")";
    	
        return ret;
        
    }
}
