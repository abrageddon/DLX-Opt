
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.HashMap;

public class Function {
    //Setup Args Order

    private int startLine;
    private ArrayList<Integer> param;
    //var name to local offset in function
//    private HashMap<String, Integer> paramters;
    private ArrayList<Integer> vars;
    private ArrayList<Integer> arrays;
    private HashMap<Integer, ArrayList<Integer>> arraysDims;
    //function returns some value
    private boolean doesRet;

    Function(int pc, boolean ret) {
        startLine = pc;
        doesRet = ret;
//        paramters = new HashMap<String, Integer>();
        vars = new ArrayList<Integer>();
        param = new ArrayList<Integer>();
        arrays = new ArrayList<Integer>();
        arraysDims = new HashMap<Integer, ArrayList<Integer>>();

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

    void addArray(int id, ArrayList<Integer> arrayDim) {
        if (!arrays.contains(id)) {
            arrays.add(id);
            arraysDims.put(id, arrayDim);
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

    int getArraysSize() {
        int totalSize = 0;

        for (Integer id : arrays) {
            int arraySize = 0;
            if (arraysDims.containsKey(id)) {
                arraySize = 1;
                for (Integer dim : arraysDims.get(id)) {
                    arraySize *= dim;
                }
            }
            totalSize += arraySize;
        }
        
        return totalSize;
    }

    int getArrayOffset(int id) {
        int offset = 0;
        int i=0;
        while (arrays.get(i) != id){
            int arraySize = 1;
            for (int dim=0;dim<arraysDims.get(i).size();dim++){
                arraySize *=arraysDims.get(i).get(dim);
            }
            offset += arraySize;
        }

        return offset * 4;
    }

    int[] getArrayDims(int id) {
        ArrayList<Integer> aDim = arraysDims.get(id);
        int arraySize = aDim.size();
        int[] dims = new int[arraySize];
        for (int i=0; i<dims.length; i++){
            dims[i]=arraysDims.get(id).get(i);
        }

        return dims;
    }
}
