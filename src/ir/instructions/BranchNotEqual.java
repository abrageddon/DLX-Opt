package ir.instructions;

public class BranchNotEqual extends ControlFlowInstr {

	public BranchNotEqual(ArithmeticBinary cmp) {
		super(cmp);
	}

	public String toString(){
		return "BranchNotEqual";
	}
}
