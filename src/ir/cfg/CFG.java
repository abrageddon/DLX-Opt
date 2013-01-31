package ir.cfg;

import java.util.Iterator;

public class CFG {

	String label;
	
	public BasicBlock startBB;
	public BasicBlock exitBB;
	
	public BasicBlock currentBB;
	public BasicBlock currentJoinBB;

	public CFG(String lbl) {
		label = lbl;
		startBB = new BasicBlock("start");
		exitBB = new BasicBlock("exit");
		currentBB = startBB;
//		addBranch(startBB, exitBB);//FIXME not sure why this was in here. caused unconditional link from start of program to exit.
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

	public Iterator<BasicBlock> topDownIterator() {
		return new TopDownLiniarIterator();
	}	
	
	public Iterator<BasicBlock> bottomUpIterator() {
		return new BottomUpLiniarIterator();
	}
	
	public class TopDownLiniarIterator implements Iterator<BasicBlock> {

		private BasicBlock currentBB;
		
		public TopDownLiniarIterator() {
			 currentBB = null;
		}
		
		public boolean hasNext() {
			if (currentBB == null && startBB != null){
				return true;
			}
			return currentBB.next != null;
		}

		public BasicBlock next() {
			//not sure if this is the best fix but it was cutting off the start node
			if (currentBB == null){
				currentBB = startBB;
			}else{
				currentBB = currentBB.next;
			}
			return currentBB;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	public class BottomUpLiniarIterator implements Iterator<BasicBlock> {

		private BasicBlock currentBB;

		public BottomUpLiniarIterator() {
			 currentBB = exitBB;
		}

		public boolean hasNext() {
			return currentBB.prev != null;
		}

		public BasicBlock next() {
			return currentBB.prev;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
}
