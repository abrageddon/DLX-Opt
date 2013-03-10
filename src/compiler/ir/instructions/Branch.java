package compiler.ir.instructions;

import compiler.ir.cfg.BasicBlock;

public class Branch extends ControlFlowInstr {

	public Branch(BasicBlock target) {
		super(target);
	}

	protected String getOperator() {
		return "BRA";
	}

}
