package ir.cfg;

import ir.instructions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class CFG {

	public String label;
	
	public BasicBlock startBB;
	public BasicBlock exitBB;
	
	public BasicBlock currentBB;
	public BasicBlock currentJoinBB;

	// Store frame in the CFG since each CFG corresponds to a single function
	public List<Instruction> frame;
	
	public CFG(String lbl) {
		label = lbl;
		startBB = new BasicBlock("start");
		exitBB = new BasicBlock("exit");
		currentBB = startBB;
		
//		currentBB = new BasicBlock("basic-block");
//		addBranch(startBB, currentBB);
//		addLinearLink(startBB, currentBB);
		
		frame = new ArrayList<Instruction>();
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
	
}
