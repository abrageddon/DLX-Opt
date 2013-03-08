package ir.instructions;

import java.util.ArrayList;
import java.util.List;

import back.regAloc.VirtualRegister;


public class Phi extends Instruction {

	public List<Instruction> values;
//	public List<VirtualRegister> regs;
	
	public Phi(List<Instruction> values) {
		this.values = values;
		outputOp = new VirtualRegister();
//		regs = new ArrayList<VirtualRegister>();
//		for(Instruction inst : values) {
//			regs.add(Instruction.resolve(inst).outputOp);
//		}
//		
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
    		ret += Instruction.resolve(val).getInstrLabel();
    	}
    	ret += ")";

//    	ret += " \n [";
//    	comma = false;
//    	for (VirtualRegister reg : regs) {
//    	    if(comma){
//    	        ret += ", ";
//    	    }else{
//    	        comma = true;
//    	    }
//    		ret += reg;	
//    	}
//    	ret += "] -> " + this.outputOp;

    	ret += " \n [";
    	comma = false;
    	for (Instruction val : values) {
    	    if(comma){
    	        ret += ", ";
    	    }else{
    	        comma = true;
    	    }
    		ret += Instruction.resolve(val).outputOp;	
    	}
    	ret += "] -> " + this.outputOp;

    	
        return ret;
        
    }
}
