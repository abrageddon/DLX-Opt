package front.symbolTable;

public class Symbol {

	public String ident;
	public SymbolKind kind;

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
		VAR, FUNCTION, PARAM;
	}
	
	public boolean isSSA() {
		return kind.equals(SymbolKind.VAR) ||
				kind.equals(SymbolKind.PARAM);
	}
}

