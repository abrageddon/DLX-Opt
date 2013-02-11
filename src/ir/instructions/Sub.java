package ir.instructions;

public class Sub extends ArithmeticBinary {

	public Sub(Instruction left, Instruction right) {
		super(left, right);
	}

	protected String getOperator() {
		return "SUB";
	}
}
