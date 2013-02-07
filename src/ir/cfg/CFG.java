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
