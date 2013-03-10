package compiler.ir.instructions;

public class Add extends ArithmeticBinary {

	public Add(Instruction left, Instruction right) {
		super(left, right);
	}

	protected String getOperator() {
		return "ADD";
	}
}