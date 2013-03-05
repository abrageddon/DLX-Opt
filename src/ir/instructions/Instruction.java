package ir.instructions;

public class Instruction {

	private Instruction forward = null;

//    private Memory regA;//return val
//    private Memory regB;//left op
//    private Memory regC;//right op
	
	
	private int instrNumber;
	
	public Instruction() {
		super();
	}
 	
	public Instruction(Integer instrNumber) {
	    setInstrNumber(instrNumber);
	}
	
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
