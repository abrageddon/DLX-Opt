package ir.instructions;

public class Instruction {

	
	private Instruction forward;
	
	private int instrNumber;
	
	public void setInstrNumber(int instrNumber) {
		this.instrNumber = instrNumber;
	}
	
	public String getInstrNumber() {
		if (this instanceof Immediate) {
			return ((Immediate)this).toString();
		}
		return String.valueOf(instrNumber);
	}
	
	
	public static Instruction resolve(Instruction i) {
		if (i.forward != null) {
			i.forward = resolve(i.forward);
			return i.forward;
		}
		return i;
	}
	
	public static void forward(Instruction i, Instruction j) {
		assert i.forward == null;
		i.forward = j;
	}
	
	public String toString() {
		return getInstrNumber();
	}
}
