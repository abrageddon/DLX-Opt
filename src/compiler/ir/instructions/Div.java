package compiler.ir.instructions;

public class Div extends ArithmeticBinary {

	public Div(Instruction left, Instruction right) {
		super(left, right);
	}

	protected String getOperator() {
		return "DIV";
	}
}
