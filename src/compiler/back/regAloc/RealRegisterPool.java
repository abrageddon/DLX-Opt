package compiler.back.regAloc;

import java.util.ArrayList;
import java.util.List;

public class RealRegisterPool {

	public static final int MAX_REGS = 8;
	public static List<RealRegister> regs = new ArrayList<RealRegister>();
	
	static {
		for(int regNo = 1; regNo < MAX_REGS+1; regNo++) {
			regs.add(new RealRegister(regNo));
		}
	}
	
	public static RealRegister getFreeRegister() {
		for(RealRegister rReg : regs) {
			if (rReg.free == true) {
				rReg.free = false;
				return rReg;
			}
		}
		return null;
	}
	
	public static void freeRegister(RealRegister rReg) {
		rReg.free = true;
	}
	
}
