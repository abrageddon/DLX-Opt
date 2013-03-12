package compiler.back.regAloc;

public class RealRegister extends Register {

	public boolean free;
	
	public RealRegister(int regNo) {
		super(regNo);
		this.free = true;
	}
	
	@Override
	public String toString() {
		return "r" + regNumber;
	}

	
}
