package compiler;

import ir.cfg.*;
import front.symbolTable.*;

import java.util.ArrayList;

public class Tile {
	public ArrayList<BasicBlock> blocks;
	public Tile parent;
	public ArrayList<Tile> children;
	public ArrayList<Variable> variables;
	
	
	public boolean contains(Symbol var){
		return false;//TODO
	}
	public int weight(){
		return 0;//TODO
	}
}
//Tiles are visited in a bottom up fashion and a local interference graph is created and colored (using pseudo registers) for each tile.