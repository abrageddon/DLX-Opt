package ir.cfg;

import ir.instructions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import compiler.PseudoRegister;
import compiler.Tile;
import compiler.TileTree;

public class CFG {

	public String label;
	
	public BasicBlock startBB;
	public BasicBlock exitBB;
	
	public BasicBlock currentBB;
	public BasicBlock currentJoinBB;
	
	//Reg Alloc
	public TileTree tileTree;

	// Store frame in the CFG since each CFG corresponds to a single function
	public List<Instruction> frame;
	
	public CFG(String lbl) {
		label = lbl;
		startBB = new BasicBlock("start");
		exitBB = new BasicBlock("exit");
		currentBB = startBB;
		
		tileTree = new TileTree();
		
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
    public void buildLiveRanges(){
        // BUILDINTERVALS
        // for each block b in reverse order do
        Iterator<BasicBlock> blockIterator = this.bottomUpIterator();
        HashSet<PseudoRegister> live;
        ArrayList<Interval> intervals;
        while (blockIterator.hasNext()) {
            BasicBlock b = blockIterator.next();
            // live = union of successor.liveIn for each successor of b
            live = new HashSet<PseudoRegister>();
            for (BasicBlock succ:b.succ){
                live.addAll(succ.liveIn);
            }

            // for each phi function phi of successors of b do
            for (BasicBlock succ:b.succ){
                for(Instruction i:succ.getInstructions()){
                    if(Phi.class.isAssignableFrom(i.getClass())){
                // live.add(phi.inputOf(b))
                        for (Instruction value: ((Phi)i).values ){
                            //TODO is correct?
                            live.add( new PseudoRegister( value.getInstrNumber() ) );
                        }
                    }
                }
            }
            
            // for each output operand opd in live do
//            for (Instruction i: b.getInstructions()){
//                // intervals[opd].addRange(b.from, b.to)
//                intervals.add(index, element)
//            }

            // for each operation op of b in reverse order do
                // for each output operand opd of op do
                    // intervals[opd].setFrom(op.id)
                    // live.remove(opd)
                // for each input operand opd of op do
                    // intervals[opd].addRange(b.from, op.id)
                    // live.add(opd)

            // for each phi function phi of b do
            for(Instruction i:b.getInstructions()){
                if(Phi.class.isAssignableFrom(i.getClass())){
                // live.remove(phi.output)
                    live.remove(new PseudoRegister( ((Phi)i).getInstrNumber() ));
                }
            }
            
            // if b is loop header then
            if(b.label.equals("while-cond")){
                // loopEnd = last block of the loop starting at b
//                BasicBlock loopEnd = 
                // for each opd in live do
                    // intervals[opd].addRange(b.from, loopEnd.to)
            }

            // b.liveIn = live
            b.liveIn = live;
        }
    }
    public void buildTileTree(){
        Iterator<BasicBlock> blockIterator = this.topDownIterator();
        Tile currentTile = tileTree.rootTile;
        int tileNumber = 0;
        
        if(!blockIterator.hasNext()){return;}

        BasicBlock currentBlock;
        BasicBlock nextBlock = blockIterator.next();
        while (blockIterator.hasNext()) {
            currentBlock = nextBlock;
            nextBlock = blockIterator.next();
            
            currentTile.addBlock(currentBlock);
            

            //Drop down a level when exiting loop or at exit block
            if (nextBlock.label.equals("while-next")){
                currentTile = currentTile.parent;
            }
            if (nextBlock.label.equals("exit")){
                currentTile = tileTree.rootTile;
            }
            
            //New tile for next block if leaving start or entering loop
            if (currentBlock.label.equals("start")){
                tileNumber++;
                Tile newTile = new Tile(currentTile, tileNumber);
                currentTile.children.add(newTile);
                currentTile=newTile;
            }
            if (nextBlock.label.equals("while-cond")){
                tileNumber++;
                Tile newTile = new Tile(currentTile, tileNumber);
                currentTile.children.add(newTile);
                currentTile=newTile;
            }
            
            
        }
        //Fixup
//        define t(n) to be the smallest tile which cent ains block n.
//        foreach edge e = (n, m) do
//          if n not-in t(m) and m not-in t(n) then
//              let a be the smallest tile containing both n and m
//              create a block na in a and in all tiles containing a
//              replace e with (n, na) and (na, m).
//          endif
//        endfor
//        while Exists e = (n, m) where m not-in parent(t(n)) do
//          create n’ in parent (t(n)) and all ancestor tiles
//          replace e with (n, n’) and (n’, m)
//        endwhile
//        while Exists e == (m, n) where m not-in parent(t(n)) do
//          create m’ in parent(t(n)) and all ancestor tiles
//          replace e with (n, m’) and (m’, m)
//        endwhile
        
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
