package compiler.ir.cfg;

public class Range {

	public int begin;
	public int end;

    public Range() {
    	this.begin = -1;
    	this.end = -1;
	}

    public Range(int begin, int end) {
    	this.begin = begin;
    	this.end = end;
	}

	public void setRange(int begin, int end){
    	this.begin = begin;
    	this.end = end;
    }
    
    public void setBegin(int begin){
        this.begin = begin;
    }
    
    /**
     * This range overlaps other range.
     * 	- this range includes other.begin, or
     * 	- other range includes this.begin
     * (Note: adjacent ranges like [1,1] and [2,2] are considered to be overlapped)
     * @param other
     * @return
     */
    public boolean overlaps(Range other) {
    	
    	// this range includes other.begin
    	if ((other.begin - 1 >= this.begin) && (other.begin - 1 <= this.end)) {
    		return true;
    	}

    	// other range includes this.begin
    	if ((this.begin + 1 >= other.begin) && (this.begin + 1 <= other.end)) {
    		return true;
    	}

    	return false;
    }
    
    /**
     * Return a new merged range.
     * 	- newBegin = min(this.begin, other.begin)
     * 	- newEnd = max(this.end, other.end)
     * 
     * @param other
     * @return
     */
    public Range merge(Range other) {
    	int newBegin = Math.min(this.begin, other.begin);
    	int newEnd = Math.max(this.end, other.end);    	
    	return new Range(newBegin, newEnd);
    }
    
    @Override
    public String toString() {
    	return "[" + begin + " - " + end + "]";
    }
}







