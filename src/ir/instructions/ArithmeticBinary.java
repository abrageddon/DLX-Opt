package ir.instructions;

public abstract class ArithmeticBinary extends Instruction {

	public Instruction left;
	public Instruction right;
	
	public ArithmeticBinary(Instruction left, Instruction right) {
		this.left = left;
		this.right = right;
	}

	protected abstract String getOperator();
	
	public String toString() {
		return super.toString() + " :" + getOperator() +
				"(" + left.getInstrNumber() + ")" +
				"(" + right.getInstrNumber() + ")";
	}
}
