package compiler.back.regAloc;

import compiler.ir.cfg.Range;

public class VirtualRegister extends Register {

	
	public RealRegister rReg;
	public int spillLocation;
	
	//	List<Range> ranges;
	
	/**
	 * The single range is used for the simplified linear scan register allocation. 
	 */
	public Range range;

	public VirtualRegister(int regNo) {
		super(regNo);
//		this.ranges = new ArrayList<Range>();
		this.range = new Range();
		this.spillLocation = -1;
	}

	public VirtualRegister(int reNo, RealRegister rReg) {
		this(reNo);
		this.rReg = rReg;
	}
	/*
	public void addRange(int from, int to) {
		// "the new range is merged if an overlapping range is present"
//		ranges.add(new Range(from, to));
		mergeOverlappingRanges();
	}
	*/

	public void setSingleRangeBegin(int begin) {
		if (begin < range.begin || range.begin == -1) {
			range.begin = begin;
		}
	}
	
	public void setSingleRangeEnd(int end) {
		if (end > range.end || range.end == -1) {
			range.end = end;
		}
		// if singleRange.end remains -1 it means that the value is not used
	}
	

	/**
	 * This method is called whenever a value is defined, when is the 
	 * output operand of a instruction.
	 * 
	 * @param begin
	 */
	/*	
	public void setRangeBegin(int begin) {
		// is not very clear from the paper, but it seems that
		// whenever this is called the register should have only
		// one range associated with it

		// current register is the output operand of a instruction and:
		if (ranges.size() >= 1) {
			// (1) it has at least one usage down the stream so:
			// "the start position of the first range is set to the current operation"
			ranges.get(0).setBegin(begin);
		} else {
			// (2) it has no usage down the stream
			// the right thing to do is not to assign this value to any register
			 System.err.println("r" + regNumber + " defined but not used!");
		}
	}
*/
	/*
	private void mergeOverlappingRanges() {
		// naive O(n^2) implementation, obviously not optimal
		boolean done;
		do {
			done = true;
			
			Range thisRange = null;
			Range otherRange = null;
			Range newRange = null;
			int thisIdx = 0;
			int otherIdx = 0;
		    outerloop:
			for(thisIdx = 0; thisIdx < ranges.size(); thisIdx++) {
				thisRange = ranges.get(thisIdx);
				for(otherIdx = 0; otherIdx < ranges.size(); otherIdx++) {				
					otherRange = ranges.get(otherIdx);
					if (!thisRange.equals(otherRange) &&
							thisRange.overlaps(otherRange)) {
						done = false;
						newRange = thisRange.merge(otherRange);
						break outerloop; // break, add merged range and test again
					}
				}
			}
			
			if (newRange != null) {
				ranges.remove(thisRange);
				ranges.remove(otherRange);
				ranges.add(newRange);				
			}
		} while(!done);

	}
*/
	@Override
	public String toString() {
		return "vr" + regNumber;
	}
/*
    public List<Range> getRanges() {
        return ranges;
    }
*/
}
