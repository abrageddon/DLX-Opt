package ir.instructions;

public class Mul extends ArithmeticBinary {

	public Mul(Instruction left, Instruction right) {
		super(left, right);
	}

	protected String getOperator() {
		return "MUL";
	}


//	public String toString(){
//		if (left == null || right == null){
//			return "MUL";
//		}
//		return left.toString() +" * "+right.toString();
//	}
	
}
