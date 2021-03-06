package compiler.front.symbolTable;


public abstract class Symbol {

	public String ident;
	public SymbolKind kind;

	
	// the scope in which the symbol is declared (0 is global)
	public int scope;
	
	// field used for SSA state vectors
	public int slot;
	
	
	
	public SymbolKind getSymbolKind() {
		return kind;
	}

	public static boolean canCoexist(Symbol s1, Symbol s2)
	{
		if (!s1.ident.equals(s2.ident)) 
			return true; 
		//they have the same name
		if (s1.getSymbolKind().equals(s2.getSymbolKind())) 
			return false; 
		//they have differing types
		return true;
	}

	public enum SymbolKind {
		VAR, PARAM, FUNCTION, PROCEDURE;
	}
	
	abstract public boolean isSSA();
	
	public String toString(){
		return ident;
	}
}

