package ir.instructions;

public class Sub extends ArithmeticBinary {

	public Sub(Instruction left, Instruction right) {
		super(left, right);
	}

	public String toString(){
		if (left == null || right == null){
			return "SUB";
		}
		return left.toString() +" - "+ right.toString();
	}
}
