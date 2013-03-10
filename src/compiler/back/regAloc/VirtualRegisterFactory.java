package compiler.back.regAloc;

import java.util.ArrayList;
import java.util.List;

public class VirtualRegisterFactory {

	public static int regCnt = 1;//register 0 is reserved as ZERO
	public static List<VirtualRegister> virtualRegisters = new ArrayList<VirtualRegister>();

	public static void init() {
		VirtualRegisterFactory.regCnt = 1;
		VirtualRegisterFactory.virtualRegisters = new ArrayList<VirtualRegister>();
	}
	
	public static VirtualRegister newRegister(){
		VirtualRegister vReg = new VirtualRegister(regCnt++);
		virtualRegisters.add(vReg);
		return vReg;
	}

	public static void printAllVirtualRegisters() {
		System.out.println("Virtual Registers");
		for (VirtualRegister vr : VirtualRegisterFactory.virtualRegisters) {
			System.out.println(vr + " " + vr.ranges);
		}
	}


	/**
	 * Test the virtual register factory.
	 */
	public static void main(String[] args) {
		VirtualRegister v = VirtualRegisterFactory.newRegister();
		v.addRange(1, 1);
		v.addRange(2, 2);
		
		System.out.println(v.ranges);
	}
}
