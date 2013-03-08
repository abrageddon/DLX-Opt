package ir.instructions;

//import back.regAloc.Variable;
import back.regAloc.VirtualRegister;

public class Instruction {

	private Instruction forward = null;

//    public int regA;//return val
//    public int regB;//left op
//    public int regC;//right op
	    
//    public VirtualRegister inputOp1;
//    public VirtualRegister inputOp2;

	public VirtualRegister outputOp;
	
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
	
	public int getInstrNumber() {
		return instrNumber;
	}

	public String getInstrLabel() {
		return String.valueOf(getInstrNumber());
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
		return getInstrLabel();
	}
	
}
