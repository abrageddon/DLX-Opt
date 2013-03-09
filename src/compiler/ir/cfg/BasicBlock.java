package compiler.ir.cfg;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;

import compiler.back.regAloc.VirtualRegister;
import compiler.ir.instructions.*;



public class BasicBlock {

    public String label;
    public Integer depth;//for VCG
    public HashSet<BasicBlock> semiDom;
    public BasicBlock iDom;
	
	public List<BasicBlock> succ;
	public List<BasicBlock> pred;
	
	public BasicBlock prev;
	public BasicBlock next;
	
	private List<Instruction> instructions;
	
	public HashSet<VirtualRegister> liveIn;
	
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
		
		liveIn = new HashSet<VirtualRegister>();
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
    
	public ListIterator<Instruction> getInstructionsIterator() {
		return instructions.listIterator();
	}
	
	public ListIterator<Instruction> getReverseInstructionsIterator() {
		return instructions.listIterator(instructions.size());
	}
	
	//TODO have separate iterator for phis and non-phis, and maybe a combined iterator for whenever needed
	
	public List<Phi> getPHIs() {
		// TODO could cache all PHIs in a different list (would that be safe?)
		List<Phi> PHIs = new ArrayList<Phi>();
		for(Instruction inst : instructions) {
			if (inst instanceof Phi) {
				PHIs.add((Phi) inst);
			}
		}
		return PHIs;
	}
	
    public Instruction getFirstInstruction() {
        return instructions.get(0);
    }
    
    public Boolean isInstructionsEmpty() {
        return instructions.isEmpty();
    }
	
    /**
     * Begin instruction number.
     * 
     * @return
     */
    public int begin(){
        return instructions.get(0).getInstrNumber();
    }
    
    /**
     * End instruction number.
     * @return
     */
    public int end(){
        return instructions.get(instructions.size() - 1).getInstrNumber();
    }
    
	public String toString() {
		return "|" + label + "|" + "[" + instructions + "]";
	}
}
