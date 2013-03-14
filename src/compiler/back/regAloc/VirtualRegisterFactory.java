package compiler.back.regAloc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class VirtualRegisterFactory {

	public static final int INCRESING_START = 0;
	public static final int INCRESING_END = 1;

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

	public static VirtualRegister newRegister(RealRegister rReg){
		VirtualRegister vReg = new VirtualRegister(regCnt++, rReg);
		virtualRegisters.add(vReg);
		return vReg;
	}
	
	public static void removeRegister(VirtualRegister vReg){
		virtualRegisters.remove(vReg);
	}

	public static void printAllVirtualRegisters() {
		System.out.println("Virtual Registers");
		Collections.sort(VirtualRegisterFactory.virtualRegisters, new Comparator<VirtualRegister>() {
			public int compare(VirtualRegister o1, VirtualRegister o2) {
				return o1.regNumber < o2.regNumber ? -1 : o1.regNumber == o2.regNumber ? 0 : 1;
			}
		});
		System.out.println("VirtR" + " \t" + "Map" + " \t" + "Interval");
		for (VirtualRegister vr : VirtualRegisterFactory.virtualRegisters) {
			//			System.out.println(vr + " " + vr.ranges);
			System.out.println(vr + " \t" + (vr.rReg == null ? "off " + vr.spillLocation : vr.rReg) + " \t" + vr.range);
//			System.out.println(vr + " \t" + "off " + vr.spillLocation + "\t" + vr.rReg + " \t" + vr.range);
		}
	}

	/**
	 * Test the virtual register factory.
	 */
	public static void main(String[] args) {
		//		VirtualRegister v = VirtualRegisterFactory.newRegister();
		//		v.addRange(1, 1);
		//		v.addRange(2, 2);

		//		System.out.println(v.ranges);
	}

}
