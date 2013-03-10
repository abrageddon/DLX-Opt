package compiler.ir.instructions;

import java.util.ArrayList;
import java.util.List;

import compiler.back.regAloc.VirtualRegister;

public class Phi extends Instruction {

	public List<Instruction> values;
	
	public Phi(List<Instruction> values) {
		this.values = values;
		outputOp = new VirtualRegister();
	}
	
	public List<VirtualRegister> getInputOperands() {
		// lazily fill the input operands list
		if(inputOps == null) {
			this.inputOps = new ArrayList<VirtualRegister>();
			for(Instruction val : values) {
				this.inputOps.add(Instruction.resolve(val).outputOp);
			}
		}
		return inputOps;
	}

	
    public String toString() {
    	String ret = "";
    	ret += getInstrNumber() + " : PHI (";
    	boolean comma = false;
    	for (Instruction val : values) {
    	    if (comma) {
    	        ret += ", ";
    	    } else {
    	        comma = true;
    	    }
    		ret += Instruction.resolve(val).getInstrLabel();
    	}
    	ret += ")";

    	ret += " \n [";
    	comma = false;
    	for (Instruction val : values) {
    	    if (comma) {
    	        ret += ", ";
    	    } else {
    	        comma = true;
    	    }
    		ret += Instruction.resolve(val).outputOp;	
    	}
    	ret += "] -> " + this.outputOp;

        return ret;
    }
}
