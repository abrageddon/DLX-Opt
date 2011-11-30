
import java.util.ArrayList;
//import java.util.HashMap;

public class Function {
    //Setup Args Order

    private int startLine;

    private ArrayList<Integer> param;
    //var name to local offset in function
//    private HashMap<String, Integer> paramters;
    private ArrayList<Integer> vars;
    private ArrayList<Integer> arrays;
    //function returns some value
    private boolean doesRet;

    Function(int pc, boolean ret) {
        startLine = pc;
        doesRet = ret;
//        paramters = new HashMap<String, Integer>();
        vars = new ArrayList<Integer>();
        param = new ArrayList<Integer>();
        arrays = new ArrayList<Integer>();

    }

    public boolean isFunc() {
        return doesRet;
    }

    void addParam(Integer id) {
        if (!param.contains(id)) {
            param.add(id);
        }
    }

    boolean containsVar(int id) {
        return vars.contains(id);
    }

    boolean containsArray(int id) {
        return arrays.contains(id);
    }

    void addVar(int id) {
        if (!vars.contains(id)) {
            vars.add(id);
        }
    }

    void addArray(int id) {
        if (!arrays.contains(id)) {
            arrays.add(id);
        }
    }

    int getParam(int id) {
        return param.indexOf(id);
    }

    int getVar(int id) {
        return vars.indexOf(id);
    }

    int getArray(int id) {
        return arrays.indexOf(id);
    }

    boolean containsParam(int id) {
        return param.contains(id);
    }

    int getParamNum() {
        return param.size();
    }

    int getVarNum() {
        return vars.size();
    }

    int getArrayNum() {
        return arrays.size();
    }

    int getStartLine() {
        return startLine;
    }
}
