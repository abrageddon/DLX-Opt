package compiler.ir.instructions;

import compiler.back.regAloc.VirtualRegister;

public abstract class ArithmeticBinary extends Instruction {

	public Instruction left;
	public Instruction right;
	
	public ArithmeticBinary(Instruction left, Instruction right) {
		this.left = left;
		this.right = right;
		outputOp = new VirtualRegister();
//		this.inputOp1 = left.outputOp;
//		this.inputOp2 = right.outputOp;
	}

	protected abstract String getOperator();
	
	public String toString() {
		return getInstrNumber() + " : " + getOperator() +
				" (" + Instruction.resolve(left).getInstrLabel() + ")" +
				"(" + Instruction.resolve(right).getInstrLabel() + ")" + "\n" + 
				"[" + Instruction.resolve(left).outputOp + " : "
				+ Instruction.resolve(right).outputOp + "] -> " + this.outputOp;
		
//		return getInstrNumber() + " : " + getOperator() +
//				" (" + left.getInstrNumber() + ")" +
//				"(" + right.getInstrNumber() + ")";

	}
}
