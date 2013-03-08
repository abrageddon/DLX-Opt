package compiler.ir.instructions;

import java.util.ArrayList;
import java.util.List;

import compiler.back.regAloc.VirtualRegister;
import compiler.ir.cfg.BasicBlock;

public abstract class ControlFlowInstr extends Instruction {

//	public Instruction targetInstr;
	public BasicBlock targetBB;
	public Cmp cmp;

	public ControlFlowInstr(Cmp cmp) 	{
		this.cmp = cmp;
	}

	public ControlFlowInstr(BasicBlock target) {
		this.targetBB = target;
	}
	
	public ControlFlowInstr(BasicBlock target, Cmp cmp) {
		this.targetBB = target;
		this.cmp = cmp;
	}

	public void setTargetBB(BasicBlock targetBB) {
		this.targetBB = targetBB;
	}
	
	public Instruction getTargetInstr() {
		return targetBB.getFirstInstruction();
	}
	
	public List<VirtualRegister> getInputOperands() {
		// lazily fill the input operands list
		if(inputOps == null) {
			this.inputOps = new ArrayList<VirtualRegister>();
			this.inputOps.add(Instruction.resolve(cmp).outputOp);			
		}
		return inputOps;
	}

	
	protected abstract String getOperator();
	
	public String toString() {
		return getInstrNumber() + " : " + getOperator() +
//				" to " + targetBB.label +
				" on (" + cmp.getInstrNumber() + ")" + "/n" +
				" [" + Instruction.resolve(cmp).outputOp + "]";
	}
}
