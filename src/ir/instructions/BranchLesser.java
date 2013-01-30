package ir.instructions;

public class BranchLesser extends ControlFlowInstr {

	public BranchLesser(ArithmeticBinary cmp) {
		super(cmp);
	}

	public String toString(){
		return "BranchLesser";
	}
}
