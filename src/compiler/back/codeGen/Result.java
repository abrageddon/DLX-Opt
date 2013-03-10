package compiler.back.codeGen;

public class Result {

    public Result() {
        //Default = const 0 register
        this.kind = Register;
        this.regno = 0;
    }
    int kind;//Const, Var, Reg, Cond

    public static final int Constant = 1;
    public static final int Variable = 2;
    public static final int Register = 3;
    public static final int Conditon = 4;
    public static final int Parameter = 5;
    public static final int GlobalVar = 6;
    public static final int Array = 7;
    public static final int GlobalArray = 8;

    int value;//Const
    int address;//Var
    int regno;//Reg
    int cond, fixuplocation;//Cond
//    int addressReg;

    public boolean isConst(){
        return (kind == Constant?true:false);
    }
    public boolean isVar(){
        return (kind == Variable?true:false);
    }
    public boolean isReg(){
        return (kind == Register?true:false);
    }
    public boolean isCond(){
        return (kind == Conditon?true:false);
    }
    public boolean isParam(){
        return (kind == Parameter?true:false);
    }
    public boolean isGlobalVar(){
        return (kind == GlobalVar?true:false);
    }
    public boolean isArray(){
        return (kind == Array?true:false);
    }
    public boolean isGlobalArray(){
        return (kind == GlobalArray?true:false);
    }

    public void setConst(){
        kind = Constant;
    }
    public void setVar(){
        kind = Variable;
    }
    public void setReg(){
        kind = Register;
    }
    public void setCond(){
        kind = Conditon;
    }
    public void setParam(){
        kind = Parameter;
    }
    public void setArray(){
        kind = Array;
    }
    public void setGlobalArray(){
        kind = GlobalArray;
    }
    public void setGlobalVar(){
        kind = GlobalVar;
    }
}
