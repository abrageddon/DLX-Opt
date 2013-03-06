package compiler;

import java.util.ArrayList;

import front.symbolTable.Symbol;

public class Variable {
	public Symbol var;
	public int startLine, endLine;
	//Interference map
	public ArrayList<Variable> interference;
	public int prefrence;
	//Register selected. Negative means Pseudo Register.
	public int register;
}
