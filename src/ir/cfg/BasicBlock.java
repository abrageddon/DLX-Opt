package ir.cfg;

import ir.instructions.Instruction;
import ir.instructions.Local;
import ir.instructions.Param;
import ir.instructions.Scalar;

import java.util.ArrayList;
import java.util.List;

public class BasicBlock {

    public String label;
    public Integer depth;
	
	public List<BasicBlock> succ;
	public List<BasicBlock> pred;
	
	public BasicBlock prev;
	public BasicBlock next;
	
	private List<Instruction> instructions;
	
	// entry SSA state and exit SSA state
	public List<Scalar> entryState;
	public List<Scalar> exitState;
	
	public BasicBlock(String lbl) {
		label = lbl;
		succ = new ArrayList<BasicBlock>();
		pred = new ArrayList<BasicBlock>();
		
		instructions = new ArrayList<Instruction>();
		
		entryState = new ArrayList<Scalar>();
		exitState = new ArrayList<Scalar>();
	}
	
	public void addInstruction(Instruction instr) {
		instructions.add(instr);
	}
	
	public List<Instruction> getInstructions() {
		return instructions;
	}
 	
	public Instruction getFirstInstruction() {
		return instructions.get(0);
	}
	
	public String toString() {
		return "|" + label + "|" + "[" + instructions + "]";
	}
	
}
