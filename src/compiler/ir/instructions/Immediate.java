package compiler.ir.instructions;

import compiler.back.regAloc.VirtualRegister;
import compiler.back.regAloc.VirtualRegisterFactory;

public class Immediate extends Instruction {

	public Integer value;
	
	public Immediate(Integer value) {
		this.value = value;
		this.outputOp = VirtualRegisterFactory.newRegister();
	}

	public Immediate(String value) {
		this(Integer.valueOf(value));
	}
	
	public String toString() {
		return getInstrNumber() + " : IMM #" + value.toString() + " -> " + this.outputOp ;
	}
}
