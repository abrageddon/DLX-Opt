package ir.instructions;

// adda x y
public class Index extends Instruction {

	public Instruction base;
	public Instruction offset;
	
	public Index(Instruction base, Instruction offset) {
		this.base = base;
		this.offset = offset;
	}

	public String toString(){
		return super.toString() + " : ADDA " +
				"(" + ((LoadValue)base).symbol.ident + ")" + 
				"(" + offset.getInstrNumber() + ")";
	}
}
