package ir.instructions;

public class StoreValue extends Instruction {

	public StoreValue(Index address, Instruction value) {
		// store value to address
	}

	public String toString(){
		return getInstrNumber() + " : STORE_VALUE";
	}

}
