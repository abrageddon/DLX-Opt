package ir.instructions;

public class BranchEqual extends ControlFlowInstr {

	public BranchEqual(Cmp cmp) {
		super(cmp);
	}

	protected String getOperator() {
		return "BEQ";
	}

//	public String toString(){
//		return "BEQ" + super.toString();
//	}
}
