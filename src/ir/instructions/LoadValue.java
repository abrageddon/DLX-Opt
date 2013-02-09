package ir.instructions;

public class LoadValue extends Instruction {
	
	public LoadValue(Index address) {
		// load value from address
	}

	public String toString(){
		return getInstrNumber() + " : LOAD_VALUE";
	}
}