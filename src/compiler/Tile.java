package compiler;

import front.symbolTable.Symbol;
import ir.cfg.*;
import ir.instructions.*;

import java.util.ArrayList;
import java.util.HashSet;

public class Tile {
	public int tileNumber;
	public Tile parent;
	public ArrayList<Tile> children;
	
	public ArrayList<BasicBlock> blocks;
	public HashSet<Symbol> variables;

    public Tile(Tile parent, int tileNum) {
        tileNumber = tileNum;
        this.parent = parent;
        children = new ArrayList<Tile>();
        
        blocks = new ArrayList<BasicBlock>();
        variables = new HashSet<Symbol>();
    }

    public void addVariables(BasicBlock block){
        //TODO work with line numbers?
        if (block.isInstructionsEmpty()){return;}
        for(Instruction inst: block.getInstructions()){
            if(LoadValue.class.isAssignableFrom(inst.getClass())){
                variables.add( ((LoadValue)inst).symbol );
            }else if(StoreValue.class.isAssignableFrom(inst.getClass())){
                variables.add( ((StoreValue)inst).symbol );
            }else if(LoadAddress.class.isAssignableFrom(inst.getClass())){
                variables.add( ((LoadAddress)inst).symbol );
            }else if(Scalar.class.isAssignableFrom(inst.getClass())){
                variables.add( ((Scalar)inst).symbol );
            }
        }
    }
    
	public boolean contains(Variable var){
	    for (Symbol v:variables){
	        if (v.equals(var)){
	            return true;
	        }
	    }
		return false;//TODO verify
	}

	
	public int weight(){
		return 0;//TODO
	}

    public void addBlock(BasicBlock block) {
        blocks.add(block);
        addVariables(block);
        if (parent != null){
            parent.addBlock(block);
        }
    }

    public boolean contains(BasicBlock block) {
        if (blocks.contains(block)){
            return true;
        }
        return false;
    }

    public Tile smallestTileOf(BasicBlock block) {
        for (Tile t: children){
            if (t.contains(block)){
                return t.smallestTileOf(block);
            }
        }
        return this;
    }
}
