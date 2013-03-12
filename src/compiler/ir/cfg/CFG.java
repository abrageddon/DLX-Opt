package compiler.ir.cfg;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;

import compiler.front.symbolTable.*;
import compiler.ir.instructions.*;

public class CFG {

	public String label;
	
	public BasicBlock startBB;
	public BasicBlock exitBB;
	
	public BasicBlock currentBB;
	public BasicBlock currentJoinBB;
	
	// Store frame in the CFG since each CFG corresponds to a single function
	public List<Instruction> frame;
	
	// Code Generation data
    private int startLine;
    private ArrayList<String> identifiers;
    private ArrayList<Integer> param;
    //var name to local offset in function
    private ArrayList<Integer> vars;
    private ArrayList<Integer> arrays;
    private HashMap<Integer, ArrayList<Integer>> arraysDims;
    //function returns some value
    private boolean doesRet;
	
	
	
	public CFG(String lbl) {
		label = lbl;
		startBB = new BasicBlock("start");
		exitBB = new BasicBlock("exit");
		currentBB = startBB;
		
		frame = new ArrayList<Instruction>();

		startLine = -1;
        identifiers = new ArrayList<String>();
        vars = new ArrayList<Integer>();
        param = new ArrayList<Integer>();
        arrays = new ArrayList<Integer>();
        arraysDims = new HashMap<Integer, ArrayList<Integer>>();
	}

	public void setCurrentBB(BasicBlock current) {
		currentBB = current;
	}

	public void setCurrentJoinBB(BasicBlock currentJoin) {
		currentJoinBB = currentJoin;
	}

	// branch is '=>' in comments
	public static void addBranch(BasicBlock source, BasicBlock destination) {
		source.succ.add(destination);
		destination.pred.add(source);
	}
	
	// linear link is '->' in comments
	public static void addLinearLink(BasicBlock source, BasicBlock destination) {
		source.next = destination;
		destination.prev = source;
	}
	
	public String liniarPassPrint() {
		
		String str = "";

		Iterator<BasicBlock> bbIt = topDownIterator();
		while(bbIt.hasNext()) {
			str += bbIt.next() + "\n";
		}
		
		return str;
	}
	
	public String toString() {
		return "#" + label + "#" + "\n" + liniarPassPrint();
	}
	
	public void calculateDepths(){
	    Integer depth = 0;
        HashSet<BasicBlock> visited = new HashSet<BasicBlock>();
        Stack<BasicBlock> currentLevel = new Stack<BasicBlock>();
        Stack<BasicBlock> nextLevel = new Stack<BasicBlock>();
        currentLevel.push(startBB);
        
        while (! (nextLevel.isEmpty() && currentLevel.isEmpty()) ){
            // assign depths and visit
            for (BasicBlock block: currentLevel){
                block.depth=depth;
                visited.add(block);
            }
            
            // add next level nodes
            while(!currentLevel.isEmpty()){
                BasicBlock node = currentLevel.pop();
                for (BasicBlock edgeNode: node.succ){
                    if (!visited.contains(edgeNode)){
                        nextLevel.add(edgeNode);
                    }else if (!edgeNode.label.equals("while-cond")){
                        nextLevel.add(edgeNode);
                    }
                }
            }
            currentLevel = nextLevel;
            nextLevel = new Stack<BasicBlock>();
            depth++;
        }
	}

   @SuppressWarnings("unchecked")
    public void createDominatorEdges() {
        Iterator<BasicBlock> blockIterator = this.bottomUpIterator();
        HashSet<BasicBlock> allNodeSet = new HashSet<BasicBlock>();
        Stack<BasicBlock> workList = new Stack<BasicBlock>();

        while (blockIterator.hasNext()) {
            BasicBlock currentBlock = blockIterator.next();
            allNodeSet.add(currentBlock);
        }
        
//	      For (each n in NodeSet)
        blockIterator = this.topDownIterator();
        while (blockIterator.hasNext()) {
//	          Dom(n) = NodeSet
            BasicBlock currentBlock = blockIterator.next();
            currentBlock.semiDom = (HashSet<BasicBlock>) allNodeSet.clone();
        }
//	      WorkList = {StartNode}
        workList.push(this.startBB);
//	      While (WorkList != null) {
        while (!workList.isEmpty()){
//	          Remove any node Y from WorkList
            BasicBlock workNode = workList.pop();
//	          New = {Y} U intersects Dom(X);X in Pred(Y)
            HashSet<BasicBlock> newDom = (HashSet<BasicBlock>) allNodeSet.clone();// TODO figure out this part
            if (!workNode.pred.isEmpty()){
                for (BasicBlock pred: workNode.pred){
                    newDom.retainAll( pred.semiDom );
                }
            }else{
                newDom = new HashSet<BasicBlock>();
            }
            newDom.add(workNode);
//	          If New != Dom(Y) {
            if(! ( newDom.containsAll(workNode.semiDom) 
                    && workNode.semiDom.containsAll(newDom)  ) ){
//	              Dom(Y) = New
                workNode.semiDom = (HashSet<BasicBlock>) newDom.clone();
//	              For (each Z in Succ(Y))
                for(BasicBlock succ: workNode.succ){
//	                  WorkList = WorkList U {Z}
                    workList.push(succ);
                }
            }
            
            
        }

        // find immediate dominator
        blockIterator = this.topDownIterator();
        while (blockIterator.hasNext()) {
            BasicBlock node = blockIterator.next();
            HashSet<BasicBlock> possibleIDoms = (HashSet<BasicBlock>) node.semiDom.clone();
            for (BasicBlock iDom : possibleIDoms) {
                if(iDom == null || iDom == node){continue;}
                if(node.iDom == null ){
                    node.iDom = iDom;
                }else if(node.iDom.depth < iDom.depth && iDom != node){
                    node.iDom = iDom;
                }
            }
        }
    }

	public Iterator<BasicBlock> topDownIterator() {
		return new TopDownLiniarIterator();
	}	
	
	public Iterator<BasicBlock> bottomUpIterator() {
		return new BottomUpLiniarIterator();
	}
	
	public class TopDownLiniarIterator implements Iterator<BasicBlock> {

		private BasicBlock nextBB;
		
		public TopDownLiniarIterator() {
			nextBB = startBB;
		}
		
		public boolean hasNext() {
			return nextBB != null;
		}

		public BasicBlock next() {
			BasicBlock ret = nextBB;
			nextBB = nextBB.next;
			return ret;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	public class BottomUpLiniarIterator implements Iterator<BasicBlock> {

		private BasicBlock prevBB;

		public BottomUpLiniarIterator() {
			prevBB = exitBB;
		}

		public boolean hasNext() {
			return prevBB != null;
		}

		public BasicBlock next() {
			BasicBlock ret = prevBB;
			prevBB = prevBB.prev;
			return ret;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
	public void setStartLine(int pc){
        startLine = pc;
	}
	
	public void setIsFunc(Boolean bool){
		doesRet = bool;
	}

	public int getStartLine() {
        return startLine;
    }
	
    public boolean isFunc() {
        return doesRet;
    }

    public void addParam(Integer id) {
        if (!param.contains(id)) {
            param.add(id);
        }
    }

    public void addVar(int id) {
        if (!vars.contains(id)) {
            vars.add(id);
        }
    }

    public void addArray(int id, ArrayList<Integer> arrayDim) {
        if (!arrays.contains(id)) {
            arrays.add(id);
            arraysDims.put(id, arrayDim);
        }
    }

    public boolean containsParam(int id) {
        return param.contains(id);
    }
    
    public boolean containsVar(int id) {
        return vars.contains(id);
    }

    public boolean containsArray(int id) {
        return arrays.contains(id);
    }

    public int getParam(int id) {
        return param.indexOf(id);
    }

    public int getVar(int id) {
        return vars.indexOf(id);
    }

    public int getArray(int id) {
        return arrays.indexOf(id);
    }

    public int getParamNum() {
        return param.size();
    }

    public int getVarNum() {
        return vars.size();
    }

    public int getArrayNum() {
        return arrays.size();
    }

    public int getArraysSize() {
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

    public int getArrayOffset(int id) {
        int offset = vars.size() ;

//        System.err.println("vars "+vars.size()+" offset="+(offset*4));
        
        
        for (int i=0;i < id; i++){
            int arraySize = 1;
            for (int dim=0;dim<arraysDims.get(id).size();dim++){
                arraySize *=arraysDims.get(id).get(dim);
            }

//            System.err.println("arraySize="+arraySize);
            
            offset += arraySize;
        }

//        System.err.println("offset="+(offset*4));
        return offset * 4;
    }

    public int[] getArrayDims(int id) {
        ArrayList<Integer> aDim = arraysDims.get(id);
        int arraySize = aDim.size();
        int[] dims = new int[arraySize];
        for (int i=0; i<dims.length; i++){
            dims[i]=arraysDims.get(id).get(i);
        }

        return dims;
    }

    public void addParam(String ident) {
        if (!param.contains(String2Id(ident))) {
            param.add(String2Id(ident));
        }
    }

    public void addVar(VarSymbol varSymbol) {
    	if (varSymbol.type.kind == Type.TypeKind.VAR ){
	        if (!vars.contains(String2Id(varSymbol.ident))) {
	            vars.add(String2Id(varSymbol.ident));
	        }
    	}else if (varSymbol.type.kind == Type.TypeKind.ARRAY){
            if (!arrays.contains(String2Id(varSymbol.ident))) {
                arrays.add(String2Id(varSymbol.ident));

                ArrayList<Integer> arrayDim = new ArrayList<Integer>();
                for (int i=0; i<((ArrayType)varSymbol.type).dim; i++){
                	arrayDim.add(((ArrayType)varSymbol.type).dimSize.get(i));
                }
                arraysDims.put(String2Id(varSymbol.ident), arrayDim);
            }
    	}
    }

    public boolean containsParam(String ident) {
        return param.contains(String2Id(ident));
    }
    
    public boolean containsVar(String ident) {
        return vars.contains(String2Id(ident));
    }

    public boolean containsArray(String ident) {
        return arrays.contains(String2Id(ident));
    }

    public int getParam(String ident) {
        return param.indexOf(String2Id(ident));
    }

    public int getVar(String ident) {
        return vars.indexOf(String2Id(ident));
    }

    public int getArray(String ident) {
        return arrays.indexOf(String2Id(ident));
    }

    public int getArrayOffset(String ident) {
        return getArrayOffset(String2Id(ident));
    }
    
    public List<Param> getParams(){
    	List<Param> ret = new ArrayList<Param>();
    	
    	for (Instruction ins:frame){
    		if(ins instanceof Param){
    			ret.add((Param)ins);
    		}
    	}
    	
    	return ret;
    }

    /**
     * Converts given id to name; returns null in case of error
     */
    public String Id2String(int id) {
        if (identifiers.get(id) != null) {
            return identifiers.get(id);
        }

        // Error
        return null;
    }

    /**
     * Converts given name to id; returns -1 in case of error
     */
    public int String2Id(String name) {
        int ret = -1;
        if (identifiers.contains(name)) {
            return identifiers.indexOf(name);
        } else {
            identifiers.add(name);
            ret = identifiers.size();
        }

        // Error
        return ret;
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Entry<T, E> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

	public String printParams() {
		String ret = "";
		boolean comma = false;
		for (Integer id:param){
			if (comma){ret+=", ";}else{comma=true;}
			ret += Id2String(id);
		}
		
		return ret;
	}

	public String printVars() {
		String ret = "";
		boolean comma = false;
		for (Integer id:vars){
			if (comma){ret+=", ";}else{comma=true;}
			ret += Id2String(id);
		}
		
		return ret;
	}

	public String printArrays() {
		String ret = "";
		boolean comma = false;
		for (Integer id:arrays){
			if (comma){ret+=", ";}else{comma=true;}
			ret += Id2String(id);
			//TODO add dimensions
		}
		
		return ret;
	}
}
