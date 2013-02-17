package ir.instructions;

public class Move extends Instruction {

	public Instruction src;
	public Instruction dest;
	
	public Move(Instruction src, Instruction dest) {
		this.src = src;
		this.dest = dest;
	}
	
	public String toString() {
		return getInstrNumber() + " : MOVE (" + src.getInstrLabel() + ")" + 
										 "(" + dest.getInstrLabel() + ")";
	}
	
}
