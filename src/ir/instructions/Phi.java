package ir.instructions;

import java.util.List;

public class Phi extends Instruction {

	public List<Instruction> values;

	public Phi(List<Instruction> values) {
		this.values = values;
	}
	
    public String toString(){
    	String ret = "";
    	ret += getInstrNumber() + " : PHI (";
    	boolean comma = false;
    	for (Instruction val : values) {
    	    if(comma){
    	        ret += ", ";
    	    }else{
    	        comma = true;
    	    }
    		ret += val.getInstrLabel();
    	}
    	ret += ")";
    	
        return ret;
        
    }
}
