package ir.instructions;

import java.util.ArrayList;

import front.symbolTable.*;

public class Call extends Instruction {
    public Symbol function;

    public Call(Symbol fnct) {
        this.function = fnct;
    }

    public String toString() {
        String str = getInstrNumber() + " : CALL " + function.ident + "(";
        if (((FunctionSymbol) function).formalParams != null) {
            boolean first = true;
            for (ParamSymbol parSym : ((FunctionSymbol) function).formalParams) {

                if (first) {
                    first = false;
                } else {
                    str += ", ";
                }
                str += parSym.toString();
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
