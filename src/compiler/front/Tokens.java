package compiler.front;

//Tokens.java

public enum Tokens {
	ARRAY("array"), 
	CALL("call"),
	DO("do"),
	ELSE("else"),
	FI("fi"),
	FUNCTION("function"),
	IF("if"),
	LET("let"),
	MAIN("main"),
	OD("od"),
	PROCEDURE("procedure"), 
	RETURN("return"),
	THEN("then"),
	VAR("var"), 
	WHILE("while"), 

	SEMI_COLON(";"), 
	COMMA(","),
	PERIOD("."), 
	L_PAREN("("), 
	R_PAREN(")"), 
	L_BRACE("{"), 
	R_BRACE("}"),
	L_SQ_BRKT("["),
	R_SQ_BRKT("]"), 
	ADD("+"),
	SUB("-"), 
	MULT("*"), 
	DIV("/"), 
	
	ASSIGN("<-"),
	EQUAL("=="), 
	NOT_EQUAL("!="), 
	GRT_THAN(">"), 
	GRT_THAN_EQ(">="), 
	LESS_THAN("<"), 
	LESS_THAN_EQ("<="), 
	
	IDENT("ident"),
	NUMBER("number"),

//	SCAN_ERROR("SCAN_ERROR"),
	COMM_SLASH("//"),
	COMM_SHARP("#"),
	EOF("<EOF>");
	
	public final String lexeme; // used as a content holder for IDENT and NUMBER

	Tokens(String lexeme) {
		this.lexeme = lexeme;
	}
		
}