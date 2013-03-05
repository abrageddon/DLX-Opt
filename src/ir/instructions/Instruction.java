package ir.instructions;

import front.symbolTable.*;

import java.util.HashSet;

public class Instruction {

	private Instruction forward = null;

    public int regA;//return val
    public int regB;//left op
    public int regC;//right op
	
	
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
	
	public HashSet<Symbol> getVariables(){    
	    return null;
	}
}
