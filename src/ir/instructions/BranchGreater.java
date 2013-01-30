package ir.instructions;

public class BranchGreater extends ControlFlowInstr {

	public BranchGreater(ArithmeticBinary cmp) {
		super(cmp);
	}

	public String toString(){
		return "BranchGreater";
	}
}
