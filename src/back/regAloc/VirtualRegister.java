package back.regAloc;

public class VirtualRegister {

	public static int regCnt = 0;
	public int regNumber;
	
	public VirtualRegister() {
		this.regNumber = regCnt++;
	}
	
	@Override
	public String toString() {
		return "r" + regNumber;
	}
	
}
