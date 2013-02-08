package front.symbolTable;

public class FunctionSymbol extends Symbol {
	
	public FunctionSymbol(String ident) {
		super.ident = ident;
		super.kind = SymbolKind.FUNCTION;
	}

	public boolean isSSA() {
		return false;
	}
}
