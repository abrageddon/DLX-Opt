package ir.instructions;

import back.regAloc.VirtualRegister;

public class Immediate extends Instruction {

	Integer value;
	
	public Immediate(Integer value) {
		this.value = value;
		outputOp = new VirtualRegister();
	}

	public Immediate(String value) {
		this(Integer.valueOf(value));
	}
	
	public String toString() {
		return getInstrNumber() + " : IMM #" + value.toString() + " -> " + this.outputOp ;
	}
}
