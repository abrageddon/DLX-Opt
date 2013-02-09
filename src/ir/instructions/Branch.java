package ir.instructions;

import ir.cfg.BasicBlock;

public class Branch extends ControlFlowInstr {

	public Branch(BasicBlock target) {
		super(target);
	}

	protected String getOperator() {
		return "BRA";
	}

//	public String toString(){
//		return getInstrNumber() + ":" + getOperator() +" to " + targetBB.label;
//	}
}
