package front.symbolTable;

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
		if (type instanceof ArrayType) {
			return false;
		}
		return true;
	}
}
