package front.symbolTable;

public class ParamSymbol extends Symbol {
	
	public ParamSymbol(String ident) {
		super.ident = ident;
		super.kind = SymbolKind.PARAM;
	}
}
