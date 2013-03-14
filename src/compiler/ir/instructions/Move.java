package compiler.ir.instructions;

import java.util.ArrayList;
import java.util.List;

import compiler.back.regAloc.RealRegisterPool;
import compiler.back.regAloc.VirtualRegister;
import compiler.back.regAloc.VirtualRegisterFactory;

public class Move extends Instruction {

	public Instruction src;
	public Instruction dest;
	
	public Move(Instruction src, Instruction dest) {
		this.src = src;
		this.dest = dest;
		
//		this.outputOp = VirtualRegisterFactory.newRegister();
		// the dest is a PHI function so we get it's outputOp
		this.outputOp = Instruction.resolve(dest).outputOp;
		
		this.inputOps = new ArrayList<VirtualRegister>();
		VirtualRegister outputOp = Instruction.resolve(src).outputOp;	
//		if(outputOp == null) {
//			this.inputOps.add(VirtualRegisterFactory.newRegister(RealRegisterPool.REG_0));
//		} else {
			this.inputOps.add(outputOp);
//		}

	}
	
	public List<VirtualRegister> getInputOperands() {
		// lazily fill the input operands list
//		if(inputOps == null) {
//			this.inputOps = new ArrayList<VirtualRegister>();
//			VirtualRegister outputOp = Instruction.resolve(src).outputOp;	
//			if(outputOp == null) {
//				this.inputOps.add(VirtualRegisterFactory.newRegister(RealRegisterPool.REG_0));
//			} else {
//				this.inputOps.add(outputOp);
//			}
////			this.inputOps.add(Instruction.resolve(dest).outputOp);		
//		}
		return inputOps;
	}

	public String toString() {
		return getInstrNumber() + " : MOVE" +
				"(" + src.getInstrLabel() + ")" + 
				"(" + dest.getInstrLabel() + ")" + "\n" +  
				" [" + inputOps.get(0) + "] " + " -> " + outputOp;
	}
	
}
