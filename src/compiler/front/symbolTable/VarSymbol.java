package compiler.front.symbolTable;

public class VarSymbol extends Symbol {
	
	public Type type;

	public VarSymbol(String ident, Type type) {
		super.ident = ident;
		super.kind = SymbolKind.VAR;
		this.type = type;
	}
	
	public VarSymbol(String ident) {
		super.ident = ident;
		super.kind = SymbolKind.VAR;
	}

	public boolean isSSA() {
		// arrays and globals are not SSA
		if (type instanceof ArrayType || scope == 0) {
			return false;
		}
		return true;
	}
}
