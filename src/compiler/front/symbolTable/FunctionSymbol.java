package compiler.front.symbolTable;

import java.util.ArrayList;
import java.util.List;

public class FunctionSymbol extends Symbol {
	
	public List<ParamSymbol> formalParams;
	
	public FunctionSymbol(String ident, SymbolKind kind) {
		super.ident = ident;
		super.kind = kind;
		formalParams = new ArrayList<ParamSymbol>();
	}

	public boolean isSSA() {
		return false;
	}
}
