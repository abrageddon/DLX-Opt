package compiler.front.symbolTable;

public class ParamSymbol extends Symbol {
	
	public ParamSymbol(String ident) {
		super.ident = ident;
		super.kind = SymbolKind.PARAM;
	}

	public boolean isSSA() {
		// TODO 
		// As per the grammar it seems that arrays cannot be passed as parameters,
		// since there is no type marker,
		// however, there could be implemented a compiler mechanism to tag
		// the type of argument passed to a function and differentiate between
		// variables and arrays.
		
		return true;
	}
}
