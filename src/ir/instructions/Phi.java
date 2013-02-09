package ir.instructions;

import java.util.List;

public class Phi extends Instruction {

	public List<Scalar> values;

    public String toString(){
        return getInstrNumber() + " : PHI: " + values;
    }
}
