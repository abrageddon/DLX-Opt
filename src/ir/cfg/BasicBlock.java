package ir.cfg;

//import ir.Instruction;

import java.util.ArrayList;
import java.util.List;

public class BasicBlock {

	public String label;
	
	public List<BasicBlock> succ;
	public List<BasicBlock> pred;
	
	public BasicBlock prev;
	public BasicBlock next;
	
//	public List<Instruction> instructions;
	
	public BasicBlock(String lbl) {
		label = lbl;
//		instructions = new ArrayList<Instruction>();
		succ = new ArrayList<BasicBlock>();
		pred = new ArrayList<BasicBlock>();
	}
	
	public String toString() {
		return "|" + label + "|";
	}
	
}
