package compiler.ir.instructions;

import java.util.ArrayList;
import java.util.List;

import compiler.back.regAloc.VirtualRegister;
import compiler.back.regAloc.VirtualRegisterFactory;


// adda x y
public class Index extends Instruction {

	public Instruction base;
	public Instruction offset;
	
	public Index(Instruction base, Instruction offset) {
		this.base = base;
		this.offset = offset;
		this.outputOp = VirtualRegisterFactory.newRegister();
	}

	public List<VirtualRegister> getInputOperands() {
		// lazily fill the input operands list
		if(inputOps == null) {
			this.inputOps = new ArrayList<VirtualRegister>();
			this.inputOps.add(Instruction.resolve(base).outputOp);
			this.inputOps.add(Instruction.resolve(offset).outputOp);			
		}
		return inputOps;
	}
	
	public String toString(){
		return getInstrNumber() + " : ADDA " +
				"(@" + ((LoadValue)base).symbol.ident + ")" + 
				"(" + offset.getInstrNumber() + ")" +
				"\n [" + Instruction.resolve(base).outputOp + ", " +
					  Instruction.resolve(offset).outputOp  + "] -> "+ outputOp;
	}
}
