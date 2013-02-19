package ir.cfg;

import ir.instructions.Instruction;
import ir.instructions.Scalar;

import java.util.ArrayList;
import java.util.List;

public class BasicBlock {

    public String label;
    public Integer depth;//for VCG
	
	public List<BasicBlock> succ;
	public List<BasicBlock> pred;
	
	public BasicBlock prev;
	public BasicBlock next;
	
	private List<Instruction> instructions;
	
	// entry SSA state and exit SSA state
	public List<Instruction> entryState;
	public List<Instruction> exitState;
	
	public BasicBlock(String lbl) {
		label = lbl;
		succ = new ArrayList<BasicBlock>();
		pred = new ArrayList<BasicBlock>();
		
		instructions = new ArrayList<Instruction>();
		
		entryState = new ArrayList<Instruction>();
		exitState = new ArrayList<Instruction>();
	}
	
	public void prependInstruction(Instruction instr) {
		instructions.add(0, instr);
	}
	
	public void appendInstruction(Instruction instr) {
		instructions.add(instr);
	}
	
	public void removeInstruction(Instruction instr) {
		instructions.remove(instr);
	}
	
	public void removeInstructions(List<Instruction> instrList) {
		instructions.removeAll(instrList);
	}
	
	public List<Instruction> getInstructions() {
		return instructions;
	}
    
    public Instruction getFirstInstruction() {
        return instructions.get(0);
    }
    
    public Boolean isInstructionsEmpty() {
        return instructions.isEmpty();
    }
	
	public String toString() {
		return "|" + label + "|" + "[" + instructions + "]";
	}
	
}
