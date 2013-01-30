package ir.instructions;

public class BranchEqual extends ControlFlowInstr {

	public BranchEqual(ArithmeticBinary cmp) {
		super(cmp);
	}

	public String toString(){
		return "BranchEqual";
	}
}
