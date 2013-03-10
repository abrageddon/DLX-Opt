package compiler.ir.cfg;

public class Range {

	public int begin;
	public int end;
	
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
     * 
     * @param other
     * @return
     */
    public boolean overlaps(Range other) {
    	
    	// this range includes other.begin
    	if ((other.begin >= this.begin) && (other.begin <= this.end)) {
    		return true;
    	}

    	// other range includes this.begin
    	if ((this.begin >= other.begin) && (this.begin <= other.end)) {
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







