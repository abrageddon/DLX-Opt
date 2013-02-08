package ir.instructions;

public class BranchLesser extends ControlFlowInstr {

	public BranchLesser(Cmp cmp) {
		super(cmp);
	}

	protected String getOperator() {
		return "BLT";
	}

//	public String toString(){
//		return "BLT" + super.toString();
//	}
}
