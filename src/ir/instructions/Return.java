package ir.instructions;

public class Return extends Instruction {

	private Instruction returnValue;

	public Return(Instruction returnValue) {
		this.returnValue = returnValue;
	}
	
	public Instruction getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(Instruction returnValue) {
		this.returnValue = returnValue;
	}
	
	public String toString() {
		return getInstrNumber()  + " : RET (" + Instruction.resolve(returnValue).getInstrLabel() + ")";
	}
}
