package compiler.ir.instructions;

public class BranchGreater extends ControlFlowInstr {

	public BranchGreater(Cmp cmp) {
		super(cmp);
	}

	protected String getOperator() {
		return "BGT";
	}
	
}
