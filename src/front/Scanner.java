package front;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class Scanner {
	
	public String sourceFile;
	public Tokens currentToken;
	public String currentLexeme;
	private int nextChar; //contains the character (or -1 == EOF)
	
	private LineNumberReader in;
	
	public Scanner() {
		
	}

	public void open(String sourceFile) {
		try {
			in = new LineNumberReader(new FileReader(sourceFile));
			in.setLineNumber(1);
			nextChar = in.read(); 
		} catch (IOException e) {
			System.err.println("Scanner.open: Errors accessing source file " + sourceFile);
			e.printStackTrace();
		}		
	}
	
	public void close() {
		try {
			in.close();
		} catch (IOException e) {
			System.err.println("Scanner.close: Error closing source file " + sourceFile);
			e.printStackTrace();
		}
	}
	
	public Tokens getCurrentToken() {
		return currentToken;
	}

	public String getCurrentLexeme() {
		return currentLexeme;
	}
	
	public int getLineNumber() {
		return in.getLineNumber();
	}
	
	
	public void next() {
		currentToken = getNextToken();
	}
	
	/**
	 * Returns the next input token
	 * 
	 * @return
	 */
	public Tokens getNextToken() {
		// skipWhitespaceAndComments();
		skipWhitespace();
	    Tokens token = Tokens.SCAN_ERROR;
	    currentLexeme = "";
  
	    switch(nextChar) {

	     
	    case(-1) : return Tokens.EOF;
	    
	    //single chars
	    case(';') : token = Tokens.SEMI_COLON; 			break;
	    case(',') : token = Tokens.COMMA; 				break;
	    case('.') : token = Tokens.PERIOD; 				break;
	    case(')') : token = Tokens.R_PAREN; 			break;
	    case('(') : token = Tokens.L_PAREN; 			break;
	    case('{') : token = Tokens.L_BRACE; 			break;
	    case('}') : token = Tokens.R_BRACE; 			break;
	    case('[') : token = Tokens.L_SQ_BRKT;			break;
	    case(']') : token = Tokens.R_SQ_BRKT;			break;
	    case('+') : token = Tokens.ADD; 				break;
	    case('-') : token = Tokens.SUB; 				break;
	    case('*') : token = Tokens.MULT; 				break;
	    case('/') : token = Tokens.DIV; 				break;

	    //numbers
	    case('0') :
	    case('1') :
	    case('2') :
	    case('3') :
	    case('4') :
	    case('5') :
	    case('6') :
	    case('7') :
	    case('8') :
	    case('9') : return scanNumber();

	    // < // <= // <-
	    case('<') : getNextChar();
                  	if (nextChar != '=' && nextChar != '-') 
                  		return Tokens.LESS_THAN;
                  	if (nextChar == '=')
                  		token = Tokens.LESS_THAN_EQ;
                  	else if (nextChar == '-')
                  		token = Tokens.ASSIGN;
                  	break;
        // = // ==
      	case('=') : getNextChar();
    	   		  if (nextChar != '=') 
      				  return Tokens.SCAN_ERROR;
      			  token = Tokens.EQUAL;
    	   		  break;
        // !=
      	case('!') : getNextChar();
      			  	if (nextChar != '=') 
      			  		return Tokens.SCAN_ERROR;
      			  	token = Tokens.NOT_EQUAL;
      			  	break;
        //array
      	case('a') : return scanKeyword(Tokens.ARRAY);   
      	//call
      	case('c') : return scanKeyword(Tokens.CALL);
        //do
      	case('d') : return scanKeyword(Tokens.DO);   			  
      	//else
      	case('e') : return scanKeyword(Tokens.ELSE);
      	//fi  //function
      	case('f') : getNextChar();
      			  	if (nextChar == 'i') {
      			  		return scanKeyword(Tokens.FI, 2);
      			  	}
      			  	if (nextChar == 'u') {
      			  		return scanKeyword(Tokens.FUNCTION, 2);
      			  	}
      			  	return scanIdent();
      	//if
      	case('i') :  return scanKeyword(Tokens.IF);
      	//let
      	case('l') : return scanKeyword(Tokens.LET);
      	//main
      	case('m') : return scanKeyword(Tokens.MAIN);   
      	//od
      	case('o') : return scanKeyword(Tokens.OD);   
      	//print
      	case('p') : return scanKeyword(Tokens.PROCEDURE);   
      	//return
      	case('r') : return scanKeyword(Tokens.RETURN);   
      	//then
      	case('t') : return scanKeyword(Tokens.THEN);   
      	//var  
      	case('v') : return scanKeyword(Tokens.VAR);
        //while
      	case('w') : return scanKeyword(Tokens.WHILE);   
      	//default (ident)
      	default: if (isLetter())
    	       	  	return scanIdent();
      	         //else error falls through
	    }
	    //got the token. Advance to start of next token and return.
	    getNextChar();
	    return token;
	}
	
	//Scans in Numbers
	private Tokens scanNumber() {
		while (isNumber()) {
			getNextChar();	
		}
		return Tokens.NUMBER;
	}
	
	//Scans in Identifiers
	private Tokens scanIdent() {
		while (isNumber() || isLetter()) { 
			getNextChar(); 
		}
		return Tokens.IDENT;
	}
	

	// Scans in keywords, starting at the letter index indicated
	private Tokens scanKeyword(Tokens t, int startIndex) 
	{
		String s = t.lexeme; // the string we are trying to match
		
		//make sure string doesn't stop short of match
        for (int i = startIndex; i < s.length(); ++i)
        {
          getNextChar();
      	  if (nextChar != s.charAt(i))  //mismatch before end!
      		  return scanIdent();        
        }
        
        //make sure string doesn't keep going past
        getNextChar();
        if (!isLetter() && !isNumber()) 
      	  	return t; //just right
        
        //it kept going
        return scanIdent(); //too long
	}
	
	//assumes current letter already matched to token, starts on 2nd letter
	private Tokens scanKeyword(Tokens t) 
	{
		return scanKeyword(t, 1);
	}
	
	//Is it a number?
	private boolean isNumber() {
		return nextChar >= '0' && nextChar <= '9';
	}
	
	//Is it a letter?
	private boolean isLetter() {
		return nextChar >= 'a' && nextChar <= 'z';
	}
	
	//Is it whitespace?
	private boolean isWhitespace() {
		return (nextChar == 0 || nextChar == ' ' || nextChar == '\t' ||
				nextChar == '\n' || nextChar == '\r');
	}	
	
	//move cursor one space forward
	private void getNextChar()  {
		// add current character to the lexeme
		if (!isWhitespace()) {
			currentLexeme += (char)nextChar;
		}
		// advance the input cursor
		try {
			nextChar = in.read();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}		
	}
	

	//skips whitespace, will not move cursor if not already on whitespace
	private void skipWhitespace() 
	{
		while(isWhitespace())
			getNextChar();
	}

	/*		
	//skips whitespace AND comments
	private void skipWhitespaceAndComments() {
		do
			skipWhitespace();
		while(gotoCommentEnd());
			
	}
	//used by skipWhitespaceAndComments()
	private boolean gotoCommentEnd() {
		if (nextChar != '#')
			return false;		
		while(nextChar != '\r' && nextChar != '\n') 
			getNextChar();
		return true;
	}
*/
	
	// Helper methods
	
}
