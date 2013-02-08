package ir.instructions;

public class BranchLesserEqual extends ControlFlowInstr {

	public BranchLesserEqual(Cmp cmp) {
		super(cmp);
	}

	protected String getOperator() {
		return "BLE";
	}
	
//	public String toString(){
//		return "BLE" + super.toString();
//	}
}
