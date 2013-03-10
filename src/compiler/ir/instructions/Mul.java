package compiler.ir.instructions;

public class Mul extends ArithmeticBinary {

	public Mul(Instruction left, Instruction right) {
		super(left, right);
	}

	protected String getOperator() {
		return "MUL";
	}
}
