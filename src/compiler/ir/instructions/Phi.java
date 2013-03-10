package compiler.ir.instructions;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import compiler.back.regAloc.VirtualRegister;
import compiler.back.regAloc.VirtualRegisterFactory;
import compiler.ir.cfg.BasicBlock;

public class Phi extends Instruction {

	// for each phi value remember the block it came from
	private Hashtable<BasicBlock, Instruction> values;
	
	public Phi(Hashtable<BasicBlock, Instruction> values) {
		this.values = values;
		this.outputOp = VirtualRegisterFactory.newRegister();
	}
	
	public List<VirtualRegister> getInputOperands() {
		// lazily fill the input operands list
		if(inputOps == null) {
			this.inputOps = new ArrayList<VirtualRegister>();
			for(Instruction val : values.values()) {
				this.inputOps.add(Instruction.resolve(val).outputOp);
			}
		}
		return inputOps;
	}
	
	public VirtualRegister getInputOperand(BasicBlock bb) {
		return Instruction.resolve(values.get(bb)).outputOp;
	}

	public List<Instruction> getValues() {
		return new ArrayList<Instruction>(values.values());
	}
	
    public String toString() {
    	String ret = "";
    	ret += getInstrNumber() + " : PHI (";
    	boolean comma = false;
    	for (Instruction val : values.values()) {
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
    	for (Instruction val : values.values()) {
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
