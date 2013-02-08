package ir.instructions;

public class Cmp extends ArithmeticBinary {

	public Cmp(Instruction left, Instruction right) {
		super(left, right);
	}

	protected String getOperator() {
		return " CMP ";
	}
	
//	public String toString(){
//		return "CMP " +
//				"(" + left.getInstrNumber() + ")" +
//				"(" + right.getInstrNumber() + ")" ;
//	}
}
