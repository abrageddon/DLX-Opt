package ir.instructions;

public class Branch extends ControlFlowInstr {

	public Branch(ArithmeticBinary cmp) {
		super(cmp);
	}

	public String toString(){
		return "Branch";
	}
}
