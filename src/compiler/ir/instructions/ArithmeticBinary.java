package compiler.ir.instructions;

import java.util.ArrayList;
import java.util.List;

import compiler.back.regAloc.VirtualRegister;

public abstract class ArithmeticBinary extends Instruction {

	public Instruction left;
	public Instruction right;

	public ArithmeticBinary(Instruction left, Instruction right) {
		this.left = left;
		this.right = right;
		this.outputOp = new VirtualRegister();
	}
	
	public List<VirtualRegister> getInputOperands() {
		// lazily fill the input operands list
		if(inputOps == null) {
			this.inputOps = new ArrayList<VirtualRegister>();
			this.inputOps.add(Instruction.resolve(left).outputOp);
			this.inputOps.add(Instruction.resolve(right).outputOp);			
		}
		return inputOps;
	}

	protected abstract String getOperator();

	public String toString() {
		return getInstrNumber() + " : " + getOperator() +
				" (" + Instruction.resolve(left).getInstrLabel() + ")" +
				"(" + Instruction.resolve(right).getInstrLabel() + ")" + "\n" + 
				" [" + Instruction.resolve(left).outputOp + ", " + 
					  Instruction.resolve(right).outputOp + "] -> " + this.outputOp;		
	}
}
