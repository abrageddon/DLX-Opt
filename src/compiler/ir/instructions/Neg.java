package compiler.ir.instructions;

public class Neg extends ArithmeticUnary {

	public Neg(Instruction operand) {
		super(operand);
	}

	protected String getOperator() {
		return "NOT";
	}
}
