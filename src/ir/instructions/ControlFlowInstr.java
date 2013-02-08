package ir.instructions;

import ir.cfg.BasicBlock;

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
	
	protected abstract String getOperator();
	
	public String toString() {
		return super.toString() + " : " + getOperator() +
//				" to " + targetBB.label +
				" on (" + cmp.getInstrNumber() + ")";
	}
}
