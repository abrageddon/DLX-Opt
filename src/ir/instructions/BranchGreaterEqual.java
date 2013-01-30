package ir.instructions;

public class BranchGreaterEqual extends ControlFlowInstr {

	public BranchGreaterEqual(ArithmeticBinary cmp) {
		super(cmp);
	}

	public String toString(){
		return "BranchGreaterEqual";
	}
}
