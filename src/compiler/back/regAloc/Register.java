package compiler.back.regAloc;

public abstract class Register {

	public int regNumber;

	public Register() {
		
	}
	public Register(int regNo) {
		this.regNumber = regNo;
	}
}
