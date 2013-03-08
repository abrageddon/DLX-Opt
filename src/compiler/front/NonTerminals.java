package compiler.front;
import java.util.HashSet;

/**
 * Contains all non terminal symbols first sets.
 */
public enum NonTerminals{
	DESIGNATOR("designator", new HashSet<Tokens>() {
		private static final long serialVersionUID = 1L;
		{
			add(Tokens.IDENT);
		}
	}), FUNC_CALL("funcCall", new HashSet<Tokens>() {
		private static final long serialVersionUID = 1L;
		{
			add(Tokens.CALL);
		}
	}), FACTOR("factor", new HashSet<Tokens>() {
		private static final long serialVersionUID = 1L;
		{
			addAll(DESIGNATOR.firstSet);
			add(Tokens.NUMBER);			
			add(Tokens.L_PAREN);
			addAll(FUNC_CALL.firstSet);
		}
	}), TERM("term", new HashSet<Tokens>() {
		private static final long serialVersionUID = 1L;
		{
			addAll(FACTOR.firstSet);
		}
	}), EXPRESSION("expression", new HashSet<Tokens>() {
		private static final long serialVersionUID = 1L;
		{	
			addAll(TERM.firstSet);
		}
	}), RELATION("relation", new HashSet<Tokens>() {
		private static final long serialVersionUID = 1L;
		{
			addAll(EXPRESSION.firstSet);
		}
	}), ASSIGNMENT("assignment", new HashSet<Tokens>() {
		private static final long serialVersionUID = 1L;
		{
			add(Tokens.LET);
		}
	}), IF_STATEMENT("ifStatement", new HashSet<Tokens>() {
		private static final long serialVersionUID = 1L;
		{
			add(Tokens.IF);
		}
	}), WHILE_STATEMENT("whileStatement", new HashSet<Tokens>() {
		private static final long serialVersionUID = 1L;
		{
			add(Tokens.WHILE);
		}
	}), RETURN_STATEMENT("returnStatement", new HashSet<Tokens>() {
		private static final long serialVersionUID = 1L;
		{
			add(Tokens.RETURN);
		}
	}), STATEMENT("statement",new HashSet<Tokens>() {
		private static final long serialVersionUID = 1L;
		{
			addAll(ASSIGNMENT.firstSet);
			addAll(FUNC_CALL.firstSet);
			addAll(IF_STATEMENT.firstSet);
			addAll(WHILE_STATEMENT.firstSet);
			addAll(RETURN_STATEMENT.firstSet);
		}
	}), STAT_SEQUENCE("statSequence",new HashSet<Tokens>() {
		private static final long serialVersionUID = 1L;
		{
			addAll(STATEMENT.firstSet);
		}
	}), TYPE_DECL("typeDecl", new HashSet<Tokens>() {
		private static final long serialVersionUID = 1L;
		{
			add(Tokens.VAR);
			add(Tokens.ARRAY);
		}
	}), VAR_DECL("varDecl", new HashSet<Tokens>() {
		private static final long serialVersionUID = 1L;
		{
			addAll(TYPE_DECL.firstSet);
		}
	}),FUNC_DECL("funcDecl", new HashSet<Tokens>() {
		private static final long serialVersionUID = 1L;
		{
			add(Tokens.FUNCTION);
			add(Tokens.PROCEDURE);
		}
	}), FORMAL_PARAM("formalParams", new HashSet<Tokens>() {
		private static final long serialVersionUID = 1L;
		{
			add(Tokens.L_PAREN);
		}
	}), FUNC_BODY("funcDecl", new HashSet<Tokens>() {
		private static final long serialVersionUID = 1L;
		{
			addAll(VAR_DECL.firstSet);
		}
	}), COMPUTATION("computation", new HashSet<Tokens>() {
		private static final long serialVersionUID = 1L;
		{
			add(Tokens.MAIN);
		}
	});

	public final String lexeme;
	public final HashSet<Tokens> firstSet = new HashSet<Tokens>();

	NonTerminals(String lexeme, HashSet<Tokens> tokens) {
		this.lexeme = lexeme;
		firstSet.addAll(tokens);
	}
}
