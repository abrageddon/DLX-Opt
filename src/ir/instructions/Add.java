package ir.instructions;

public class Add extends ArithmeticBinary {

	public Add(Instruction left, Instruction right) {
		super(left, right);
	}

	protected String getOperator() {
		return " ADD ";
	}

	
//	public String toString(){
//		if (left == null || right == null){
//			return "ADD";
//		}
//		return left.toString() + " + " + right.toString();
//	}
}