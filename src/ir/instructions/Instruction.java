package ir.instructions;

public class Instruction {

	private Instruction forward = null;
	
	private int instrNumber;
	
	public void setInstrNumber(int instrNumber) {
		this.instrNumber = instrNumber;
	}
	
	public String getInstrNumber() {
		return String.valueOf(instrNumber);
	}

	public String getInstrLabel() {
		return getInstrNumber();
	}
	
	public static Instruction resolve(Instruction i) {
//		System.out.println("RES: " + i);
		if (i.forward != null && !i.forward.equals(i)) {
			i.forward = resolve(i.forward);
//			System.out.println("\t\t" + i.forward);
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
