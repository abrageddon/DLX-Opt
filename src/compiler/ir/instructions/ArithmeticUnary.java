package compiler.ir.instructions;

import java.util.ArrayList;
import java.util.List;

import compiler.back.regAloc.VirtualRegister;

public abstract class ArithmeticUnary extends Instruction {

	public Instruction operand;
	
	public ArithmeticUnary(Instruction operand) {
		this.operand = operand;
		this.outputOp = new VirtualRegister();
	}
	
	public List<VirtualRegister> getInputOperands() {
		// lazily fill the input operands list
		if(inputOps == null) {
			this.inputOps = new ArrayList<VirtualRegister>();
			this.inputOps.add(Instruction.resolve(operand).outputOp);			
		}
		return inputOps;
	}
	
	protected abstract String getOperator();
	
	public String toString() {
		return getInstrNumber() + " : " + getOperator() +
				" (" + Instruction.resolve(operand).getInstrLabel() + ")" +
				"[" + Instruction.resolve(operand).outputOp + "] -> " + this.outputOp;
		
	}

}

