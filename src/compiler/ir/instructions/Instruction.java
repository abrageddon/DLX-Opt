package compiler.ir.instructions;

import java.util.List;

//import back.regAloc.Variable;
import compiler.back.regAloc.VirtualRegister;

public class Instruction {

	private Instruction forward = null;
	
	/**
	 * Intentionally uninitialized; might be null since not all instructions need it. 
	 */
	public VirtualRegister outputOp;
	
	/**
	 * Intentionally uninitialized; might be null since not all instructions need it. 
	 */
	public List<VirtualRegister> inputOps;
	
	private int instrNumber;
	
	public Instruction() {
		super();
	}
 	
	public Instruction(Integer instrNumber) {
	    setInstrNumber(instrNumber);
	}
	
	public void setInstrNumber(int instrNumber) {
		this.instrNumber = instrNumber;
	}
	
	public int getInstrNumber() {
		return instrNumber;
	}

	public String getInstrLabel() {
		return String.valueOf(getInstrNumber());
	}

	/**
	 * @return outputOp might be null if the instruction does not have output operand
	 */
	public VirtualRegister getOutputOperand() {
		return outputOp;
	}
	
	/**
	 * @return inputOps might be null if the instruction does not have input operands
	 */
	public List<VirtualRegister> getInputOperands() {
		return inputOps;
	}
	
	public static Instruction resolve(Instruction i) {
		if (i != null && i.forward != null) {
			i.forward = resolve(i.forward);
			return i.forward;
		}
		return i;
	}
	
	public static void forward(Instruction i, Instruction j) {
		assert i.forward == null;
		i.forward = j;
	}
	
	public String toString() {
		return getInstrLabel();
	}
	
}
