package front;

import front.symbolTable.FunctionSymbol;
import front.symbolTable.ParamSymbol;
import front.symbolTable.Symbol;
import front.symbolTable.Symbol.SymbolKind;
import front.symbolTable.SymbolTable;
import front.symbolTable.VarSymbol;
import front.symbolTable.ArrayType;
import front.symbolTable.Type;
import front.symbolTable.VarType;

public class Parser {

	private Scanner scanner;
	private SymbolTable symTable;
	private String sourceFile;
//	public Tokens sym; // Stores the current input from getSym()
	
	public Parser(String srcFile) {
		symTable = new SymbolTable();
		symTable.insert(new FunctionSymbol("inputnum"));
		symTable.insert(new FunctionSymbol("outputnum"));
		symTable.insert(new FunctionSymbol("outputnewline"));
		scanner = new Scanner();
		sourceFile = srcFile;
	}
	
	public void parse() {
		scanner.open(sourceFile);
		scanner.next(); // scan the input symbol
		computation();
	}
		
	public void printError(String str) {
		System.err.println("Parsing: " + sourceFile + " Error: " + str);
		System.err.println("Current token: " + scanner.currentToken + 
						   " Current lexemme: " + scanner.currentLexeme);
		new Exception().printStackTrace();
		System.exit(0);		
	}
	
	/**
	 * Recursive descent parser modular design using accept/expect functions.
	 * @throws IOException 
	 */
	private boolean accept(Tokens t) {
		if (scanner.currentToken == t) {
			scanner.next();
			return true;
		}
		return false;
	}

//	private boolean expect(Tokens t) throws ParserException {
	private boolean expect(Tokens t) {
		if (accept(t))
			return true;
		printError("Expected " + t.lexeme);
		return false;
	}

	private boolean peek(Tokens t) {
		if (scanner.currentToken == t) {
			return true;
		}
		return false;
	}

	private boolean assume(Tokens t) {
		if (peek(t))
			return true;
		printError("Expected " + t.lexeme);
		return false;
	}

	
	// computation = “main” { varDecl } { funcDecl } “{” statSequence “}” “.”
	public void computation() {
		expect(Tokens.MAIN);
		while(currentIsFirstOf(NonTerminals.VAR_DECL)) {
			varDecl();
		}
		while(currentIsFirstOf(NonTerminals.FUNC_DECL)) {
			funcDecl();
		}
		expect(Tokens.L_BRACE);
		symTable.increaseScope();
		statSequence();
		symTable.decreaseScope();
		expect(Tokens.R_BRACE);
		expect(Tokens.PERIOD);
	}
	
	// funcBody = { varDecl } “{” [ statSequence ] “}”
	private void funcBody() {
		while(currentIsFirstOf(NonTerminals.VAR_DECL)) {
			varDecl();
		}
		expect(Tokens.L_BRACE);
		if(currentIsFirstOf(NonTerminals.STAT_SEQUENCE)) {
			statSequence();
		}
		expect(Tokens.R_BRACE);
	}
	
	// formalParam = “(“ [ident { “,” ident }] “)”
	private void formalParam() {
		expect(Tokens.L_PAREN);
		if (peek(Tokens.IDENT)) {
			String ident = ident();
			insertSymbol(new ParamSymbol(ident));
			while (accept(Tokens.COMMA)) {
				ident = ident();
				insertSymbol(new ParamSymbol(ident));
			}
		}
		expect(Tokens.R_PAREN);
	}
	
	// funcDecl = (“function” | “procedure”) ident [formalParam] “;” funcBody “;” 
	private void funcDecl() {
		if(accept(Tokens.FUNCTION) || accept(Tokens.PROCEDURE)) {
			String ident = ident();
			insertSymbol(new FunctionSymbol(ident)); // TODO add params to function
			symTable.increaseScope();
			if(currentIsFirstOf(NonTerminals.FORMAL_PARAM)) {
				formalParam();
			}
			expect(Tokens.SEMI_COLON);
			funcBody();
			symTable.decreaseScope();
			expect(Tokens.SEMI_COLON);
		} else {
			printError("funcDecl parsing error");
		}
	}
	
	// varDecl = typeDecl ident { “,” ident } “;”
	private void varDecl() {
		Type type = typeDecl();
		String ident = ident();
		insertSymbol(new VarSymbol(ident, type));
		while (accept(Tokens.COMMA)) {
			ident = ident();
			insertSymbol(new VarSymbol(ident, type));
		}
		expect(Tokens.SEMI_COLON);
	}

	// typeDecl = “var” | “array” “[“ number “]” { “[“ number “]” }
	private Type typeDecl() {
//		String typeStr = null;
		if ( accept(Tokens.ARRAY)) {
			ArrayType type = new ArrayType();
//			typeStr = "array";
			expect(Tokens.L_SQ_BRKT);
//			typeStr += "[";
			String number = number();
//			typeStr += number;
			type.addDimension(number);
			expect(Tokens.R_SQ_BRKT);
//			typeStr += "]";
			while (accept(Tokens.L_SQ_BRKT)) {
//				typeStr += "[";
				number = number();
//				typeStr += number;
				type.addDimension(number);
				expect(Tokens.R_SQ_BRKT);
//				typeStr += "]";
			}
			return type;
		} else {
			expect(Tokens.VAR);
//			typeStr = "var";
			VarType type = new VarType();
			return type;
		}
//		return typeStr;
	}

	// statSequence = statement { “;” statement }
	private void statSequence() {
		statement();
		while (accept(Tokens.SEMI_COLON)) {
			statement();
		}		
	}
	
	// statement = assignment | funcCall | ifStatement | whileStatement | returnStatement
	private void statement() {
		if (currentIsFirstOf(NonTerminals.ASSIGNMENT)) {
			assignment();
		} else if (currentIsFirstOf(NonTerminals.FUNC_CALL)) {
			funcCall();
		} else if (currentIsFirstOf(NonTerminals.IF_STATEMENT)) {
			ifStatement();
		} else if (currentIsFirstOf(NonTerminals.WHILE_STATEMENT)) {
			whileStatement();
		} else if (currentIsFirstOf(NonTerminals.RETURN_STATEMENT)) {
			returnStatement();
		} else {
			printError("Statement parsing error");
		}
	}

	// returnStatement = “return” [ expression ] 
	private void returnStatement() {
		expect(Tokens.RETURN);
		if(currentIsFirstOf(NonTerminals.EXPRESSION)) {
			expression();
		}
	}

	// whileStatement = “while” relation “do” statSequence “od”
	private void whileStatement() {
		expect(Tokens.WHILE);
		relation();
		expect(Tokens.DO);
		statSequence();
		expect(Tokens.OD);
	}

	// ifStatement = “if” relation “then” statSequence [ “else” statSequence ] “fi”
	private void ifStatement() {
		expect(Tokens.IF);
		relation();
		expect(Tokens.THEN);
		statSequence();
		if (accept(Tokens.ELSE)) {
			statSequence();
		}
		expect(Tokens.FI);
	}

	// funcCall = “call” ident [ “(“ [expression { “,” expression } ] “)” ]
	private void funcCall() {
		// TODO check if function is defined; check number of parameters
		expect(Tokens.CALL);
		String ident = ident();
		tryResolve(ident, SymbolKind.FUNCTION);
		if (accept(Tokens.L_PAREN)) {
			if (currentIsFirstOf(NonTerminals.EXPRESSION)) {
				expression();
				while (accept(Tokens.COMMA)) {
					expression();	
				}
			}
			expect(Tokens.R_PAREN);
		}		
	}
	
	// assignment = “let” designator “<-” expression
	private void assignment() {
		expect(Tokens.LET);
		designator();
		expect(Tokens.ASSIGN);
		expression();
	}
	
	// relation = expression relOp expression
	private void relation() {
		expression();
		if (accept(Tokens.EQUAL) || accept(Tokens.NOT_EQUAL) ||
				accept(Tokens.LESS_THAN) || accept(Tokens.LESS_THAN_EQ) ||
				accept(Tokens.GRT_THAN) || accept(Tokens.GRT_THAN_EQ)) {
			expression();
		} else {
			printError("realation parsin error");
		}
	}
	
	// expression = term {(“+” | “-”) term}	
	private void expression() {
		term();
		while (accept(Tokens.ADD) || accept(Tokens.SUB)) {
			term();
		}
	}
	
	// term = factor { (“*” | “/”) factor}
	private void term() {
		factor();
		while (accept(Tokens.MULT) || accept(Tokens.DIV)) {
			factor();
		}
		
	}
	
	// factor = designator | number | “(“ expression “)” | funcCall
	private void factor() {
		if (currentIsFirstOf(NonTerminals.DESIGNATOR)) {
			designator();
		} else if (currentIsFirstOf(NonTerminals.FUNC_CALL)) {
			funcCall();
		} else if (accept(Tokens.L_PAREN)) {
			expression();
			expect(Tokens.R_PAREN);
		} else {
			expect(Tokens.NUMBER)	;
		}
	}
	
	// designator = ident{ "[" expression "]" }
	private void designator() {
		String ident = ident();
		tryResolve(ident, SymbolKind.VAR);
		while (accept(Tokens.L_SQ_BRKT)) {
			expression();
			expect(Tokens.R_SQ_BRKT);
		}
	}
	
	// Helper methods
	
	// ident is actually a terminal symbol, not a nonterminal
	// function added here just for expressiveness
	private String ident() {
		assume(Tokens.IDENT);
		String ident = scanner.currentLexeme;
		accept(Tokens.IDENT);
		return ident;
	}

	// number is actually a terminal symbol, not a nonterminal
	// function added here just for expressiveness
	private String number() {
		assume(Tokens.NUMBER);
		String number = scanner.currentLexeme;
		accept(Tokens.NUMBER);
		return number;
	}

	private boolean currentIsFirstOf(NonTerminals nonTerminal)
	{
		return (nonTerminal.firstSet.contains(scanner.currentToken));
	}
	
	private void insertSymbol(Symbol s) {
		if (!symTable.insert(s)) {
			printError("Symbol already defined " + s.ident);
		}
	}
	
	private void tryResolve(String ident, SymbolKind kind) {
		if (!symTable.resolve(ident, kind)) {
			printError("Symbol not found " + ident);
		}
	}

}










