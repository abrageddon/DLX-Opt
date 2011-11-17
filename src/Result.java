/**
 *
 * @author Steven Neisius
 */
public class Result {
    int kind;//Const, Var, Reg, Cond

    public static final int Constant = 1;
    public static final int Variable = 2;
    public static final int Register = 3;
    public static final int Conditon = 4;
    public static final int Parameter = 5;

    int value;//Const
    int address;//Var
    int regno;//Reg
    int cond, fixuplocation;//Cond

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
}
