package compiler.ir.instructions;

import compiler.back.regAloc.VirtualRegister;

public class Immediate extends Instruction {

	public Integer value;
	
	public Immediate(Integer value) {
		this.value = value;
		this.outputOp = new VirtualRegister();
	}

	public Immediate(String value) {
		this(Integer.valueOf(value));
	}
	
	public String toString() {
		return getInstrNumber() + " : IMM #" + value.toString() + " -> " + this.outputOp ;
	}
}
