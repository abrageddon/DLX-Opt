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
		addBranch(startBB, exitBB);
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
	
		return label + "\n" + liniarPassPrint();
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
			 currentBB = startBB;
		}
		
		public boolean hasNext() {
			return currentBB.next != null;
		}

		public BasicBlock next() {
			currentBB = currentBB.next;
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
