package compiler.ir.instructions;

import java.util.ArrayList;
import java.util.List;

import compiler.back.regAloc.VirtualRegister;
import compiler.back.regAloc.VirtualRegisterFactory;
import compiler.front.symbolTable.Symbol;

public class LoadValue extends Instruction {
	
	public Symbol symbol; // TODO why do we need the symbol?
	public Index address;
	
	public LoadValue(Index address) {
		// load value from address
		this.address = address;
		this.outputOp = VirtualRegisterFactory.newRegister();
	}

	public LoadValue(Symbol symbol) {
		// load value from symbol address address
		this.symbol = symbol;
		outputOp = VirtualRegisterFactory.newRegister();
	}

	public List<VirtualRegister> getInputOperands() {
		// lazily fill the input operands list
		if(inputOps == null) {
			this.inputOps = new ArrayList<VirtualRegister>();
			this.inputOps.add(Instruction.resolve(address).outputOp);
		}
		return inputOps;
	}

	public String toString() {
		return getInstrNumber() + " : LOAD " +
				"(@" + (symbol != null ? symbol.ident  : address.getInstrNumber() ) + ")" +
				(address != null ? "\n [" + Instruction.resolve(address).outputOp + "]" : "") +
				" -> " + outputOp;
	}
}