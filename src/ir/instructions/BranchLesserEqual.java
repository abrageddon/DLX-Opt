package ir.instructions;

public class BranchLesserEqual extends ControlFlowInstr {

	public BranchLesserEqual(ArithmeticBinary cmp) {
		super(cmp);
	}

	public String toString(){
		return "BranchLesserEqual";
	}
}
