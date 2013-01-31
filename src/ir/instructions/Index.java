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
		if (base == null || offset == null){
			return "INDEX";
		}
		return base.toString() + " @ " + offset.toString();
	}
}
