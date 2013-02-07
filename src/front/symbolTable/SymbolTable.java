package front.symbolTable;
import java.util.LinkedList;
import java.util.List;

public class SymbolTable {
	private LinkedList<LinkedList<Symbol>> scopes;
	private int currentScope = 0; //scope = 0 -> global scope

	public SymbolTable() {
		scopes = new LinkedList<LinkedList<Symbol>>();
		scopes.add(new LinkedList<Symbol>()); // initialize global scope
	}
	
	public void increaseScope() {
		++currentScope;
		scopes.add(new LinkedList<Symbol>());
	}
	
	public void decreaseScope() {
		if (currentScope == 0)
			throw new IllegalStateException("SymbolTable cannot decrease scope past 0!");
		--currentScope;
		scopes.removeLast();
	}
	
	public int getScope() {
		return currentScope;
	}
	
	
	public boolean insert(Symbol symbol) {
//		if (!resolve(symbol.ident, symbol.kind)) {
		if (!resolve(symbol, currentScope)) {
			scopes.get(currentScope).add(symbol);
			return true;
		}
		
		return false;
	}

	public boolean resolve(Symbol symbol, int scope) {
		for (Symbol sym : scopes.get(scope)) {
				if (sym.ident.equals(symbol.ident) &&
					(sym.kind.equals(symbol.kind))) {
					return true;
				}
			}
		
		return false;
	}

	public boolean resolve(Symbol symbol) {
		for (List<Symbol> sc : scopes) {
			for (Symbol sym : sc) {
				if (sym.ident.equals(symbol.ident) &&
					(sym.kind.equals(symbol.kind))) {
					return true;
				}
			}
		}
		
		return false;
	}

	public Symbol resolve(String ident, int scope) {
		
		for (Symbol sym : scopes.get(scope)) {
			if (sym.ident.equals(ident)) {
				return sym;
			}
		}
	
		return null;
	}

	public Symbol resolve(String ident) {
		
		for (List<Symbol> sc : scopes) {
			for (Symbol sym : sc) {
				if (sym.ident.equals(ident)) {
					return sym;
				}
			}
		}
		
		return null;
	}

//	public boolean resolveSymbol(Symbol symbol) {
//		
//		switch (symbol.kind) {
//		case VAR:
//			VarSymbol varSymbol = (VarSymbol) symbol;
//			
//			switch (varSymbol.type.kind) {
//			case VAR:
//				// check if it is in the table
//				break;
//			case ARRAY:
//				// check if it is in the table and dimension is right
//				break;
//			}
//			
//			break;
//		case FUNCTION:
//				
//			break;
//		case PARAM:
//			
//			break;
//		default:
//			break;
//		}
//		
//		return false;		
//	}
	
	public String print() {
		String text = "***** Symbol Table Contents *****\r\n";
		for (int i = 0; i <= currentScope; ++i) {
			for(Symbol s : scopes.get(i)) {
				for (int tabs = 0; tabs < i; ++tabs)
					text += "-->";
				text += "KIND: " + s.kind + "  ";
				text += "IDENT: " + s.ident + "  ";
				if (s instanceof VarSymbol)
					text += "TYPE: " + ( (VarSymbol) s).type + "  ";
				text += "\r\n";
			}
		}
		return text;
	}
}
