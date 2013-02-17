package ir.instructions;

public class Immediate extends Instruction {

	Integer value;
	
	public Immediate(Integer value) {
		this.value = value;
	}

	public Immediate(String value) {
		this(Integer.valueOf(value));
	}

//	public String getInstrNumber() {
//		return this.toString();
//	}
	
	public String toString() {
		return getInstrNumber() + " : IMM #" + value.toString();
	}
}
