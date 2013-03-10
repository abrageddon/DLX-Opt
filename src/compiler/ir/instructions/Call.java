package compiler.ir.instructions;

import java.util.ArrayList;
import java.util.List;

import compiler.back.regAloc.VirtualRegister;
import compiler.back.regAloc.VirtualRegisterFactory;
import compiler.front.symbolTable.FunctionSymbol;

public class Call extends Instruction {
	public FunctionSymbol function;
	public List<Instruction> args;

	public Call(FunctionSymbol fnct, List<Instruction> args) {
		this.function = fnct;
		this.args = args;

		this.outputOp = VirtualRegisterFactory.newRegister();
	}

	public List<VirtualRegister> getInputOperands() {
		// lazily fill the input operands list
		if(inputOps == null) {
			this.inputOps = new ArrayList<VirtualRegister>();
			for (Instruction arg : args ) {
				this.inputOps.add(Instruction.resolve(arg).outputOp);
			}
		}
		return inputOps;
	}

	public String toString() {
		String str = getInstrNumber() + " : CALL " + function.ident + "(";
		boolean comma = false;
		if (args != null) {
			for (Instruction inst : args ) {
				if (comma) {
					str += ", ";
				} else {
					comma = true;
				}
				str += inst.getInstrNumber();
			}
		}
		str += ")";

		str += " \n [";
		comma = false;
		if (args != null) {
			for (Instruction val : args) {
				if (comma) {
					str += ", ";
				} else {
					comma = true;
				}
				str += Instruction.resolve(val).outputOp;	
			}
		}
		str += "] -> " + this.outputOp;

		return str;
	}
}
