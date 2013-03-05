package front.symbolTable;

import java.util.ArrayList;
import java.util.List;

public class FunctionSymbol extends Symbol {
	
	public List<ParamSymbol> formalParams;
	
	public FunctionSymbol(String ident) {
		super.ident = ident;
		super.kind = SymbolKind.FUNCTION;
		formalParams = new ArrayList<ParamSymbol>();
	}

	public boolean isSSA() {
		return false;
	}
}
