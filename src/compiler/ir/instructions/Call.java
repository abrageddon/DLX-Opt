package compiler.ir.instructions;

import java.util.List;

import compiler.back.regAloc.VirtualRegister;
import compiler.front.symbolTable.FunctionSymbol;




public class Call extends Instruction {
	public FunctionSymbol function;
	public List<Instruction> args;
	
	public Call(FunctionSymbol fnct, List<Instruction> args) {
		this.function = fnct;
		this.args = args;
		
		outputOp = new VirtualRegister();
	}

    public String toString() {
        String str = getInstrNumber() + " : CALL " + function.ident + "(";
        if (args != null) {
            boolean first = true;
            for (Instruction inst : args ) {
                if (first) {
                    first = false;
                } else {
                    str += ", ";
                }
                str += inst.getInstrNumber();
            }
        }
        return str + ")";
    }
	
    //TODO parameter variables
//    @Override
//    public HashSet<Symbol> getVariables() {
//        HashSet<Symbol> ret = new HashSet<Symbol>();
//        ret.add(symbol);
//        return ret;
//    }
}
