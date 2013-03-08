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
		return getInstrNumber() + " : ADDA " +
				"(" + ((LoadValue)base).symbol.ident + ")" + 
				"(" + offset.getInstrNumber() + ")";
	}

//    @Override
//    public boolean equals(Object obj) {
//        //TODO fix this
//        if (obj instanceof Index){
//            Index cmp = (Index)obj;
//            boolean ret=false;
//            ret = ret || ((LoadValue)base).symbol.equals(((LoadValue)cmp.base).symbol);
//            ret = ret || (offset).getInstrLabel().equals(cmp.offset.getInstrNumber());
//            return ret;
//        }
//        return false;
//    }
}
