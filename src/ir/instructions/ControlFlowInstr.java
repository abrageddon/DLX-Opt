package ir.instructions;

public class ControlFlowInstr extends Instruction {

	ArithmeticBinary cmp;
	public ControlFlowInstr(ArithmeticBinary cmp) {
		this.cmp = cmp;
	}
	
}
