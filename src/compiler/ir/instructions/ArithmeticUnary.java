package compiler.ir.instructions;

import compiler.back.regAloc.VirtualRegister;

public class ArithmeticUnary extends Instruction {

	public Value operand;
	
	public ArithmeticUnary() {
		outputOp = new VirtualRegister();
	}
}

