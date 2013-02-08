package ir.instructions;

public class BranchNotEqual extends ControlFlowInstr {

	public BranchNotEqual(Cmp cmp) {
		super(cmp);
	}

	protected String getOperator() {
		return "BNE";
	}
//
//	public String toString(){
//		return "BNE" + super.toString();
//	}
}
