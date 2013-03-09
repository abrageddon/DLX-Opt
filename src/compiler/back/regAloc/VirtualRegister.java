package compiler.back.regAloc;

import java.util.ArrayList;
import java.util.List;

import compiler.ir.cfg.Range;

public class VirtualRegister {

	public static int regCnt = 0;
	public int regNumber;
	List<Range> ranges;


	public VirtualRegister() {
		this.regNumber = regCnt++;
		this.ranges = new ArrayList<Range>();
	}

	public void addRange(int from, int to) {
		// "the new range is merged if an overlapping range is present"
		ranges.add(new Range(from, to));
		mergeOverlappingRanges();
	}

	public void setRangeBegin(int begin) {
		// "the start position of the first range is set to the current operation"
		// is not very clear from the paper, but it seems that
		// whenever this is called the register should have only
		// one range associated with it
		assert ranges.size() == 1;
		ranges.get(0).setBegin(begin);
	}

	
	
	private void mergeOverlappingRanges() {
		// naive O(n^2) implementation, obviously not optimal
		boolean done;
		do {
			done = true;
			for(Range thisRange : ranges) {
				for(Range otherRange : ranges) {
					if (!thisRange.equals(otherRange) &&
							thisRange.overlaps(otherRange)) {
						// removing and adding elements from a list while iterating
						// is safe here since we brake out of the iteration
						ranges.remove(thisRange);
						ranges.remove(otherRange);
						ranges.add(thisRange.merge(otherRange));
						done = false;
						break; // break and test again
					}
				}
			}			
		} while(!done);

	}

	public static void main(String[] args) {
		VirtualRegister v = new VirtualRegister();
		v.addRange(0, 3);
		v.addRange(0, 2);
		v.addRange(0, 1);
		
		System.out.println(v.ranges);
		
	}
	

	@Override
	public String toString() {
		return "r" + regNumber;
	}

}
