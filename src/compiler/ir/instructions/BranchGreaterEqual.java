package compiler.ir.instructions;

public class BranchGreaterEqual extends ControlFlowInstr {

	public BranchGreaterEqual(Cmp cmp) {
		super(cmp);
	}

	protected String getOperator() {
		return "BGE";
	}

}
