package compiler.ir.instructions;

import java.util.ArrayList;
import java.util.List;

import compiler.back.regAloc.VirtualRegister;

public class Move extends Instruction {

	public Instruction src;
	public Instruction dest;
	
	public Move(Instruction src, Instruction dest) {
		this.src = src;
		this.dest = dest;
	}
	
	public List<VirtualRegister> getInputOperands() {
		// lazily fill the input operands list
		if(inputOps == null) {
			this.inputOps = new ArrayList<VirtualRegister>();
			this.inputOps.add(Instruction.resolve(src).outputOp);
			this.inputOps.add(Instruction.resolve(dest).outputOp);		
		}
		return inputOps;
	}

	public String toString() {
		return getInstrNumber() + " : MOVE" +
				"(" + src.getInstrLabel() + ")" + 
				"(" + dest.getInstrLabel() + ")" + "/n" +  
				" [ " + Instruction.resolve(src).outputOp + "," + 
				 	    Instruction.resolve(dest).outputOp + "]" + " -> " + outputOp;
	}
	
}
