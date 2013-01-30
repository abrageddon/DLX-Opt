package ir.instructions;

public class Mul extends ArithmeticBinary {

	public Mul(Instruction left, Instruction right) {
		super(left, right);
	}


	public String toString(){
		if (left == null || right == null){
			return "MUL";
		}
		return left.toString() +" * "+right.toString();
	}
	
}
