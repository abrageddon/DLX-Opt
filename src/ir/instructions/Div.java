package ir.instructions;

public class Div extends ArithmeticBinary {

	public Div(Instruction left, Instruction right) {
		super(left, right);
	}

	public String toString(){
		if (left == null || right == null){
			return "DIV";
		}
		return left.toString() + " / " + right.toString();
	}
}
