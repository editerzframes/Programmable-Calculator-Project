package lexer;

import java.util.Scanner;

public class Token {
	/**
	 * <p>
	 * Title: Token - A component of the Programmable Calculator Project
	 * </p>
	 *
	 * <p>
	 * Description: An entity object class that represent lexical objects
	 * </p>
	 *
	 * <p>
	 * Copyright: Copyright © 2019
	 * </p>
	 *
	 * @author Lynn Robert Carter
	 * @version 1.00	Baseline version 2019-01-08
	 */
	
	/**********************************************************************************************
	 * 
	 * The Token Class defines the kind of thinks that the lexer can find in a text file.
	 * 
	 * The following are the primary attributes for the Token Class
	 */

	/* The Kind enumeration partitions the various input tokens into these named items */
	public enum Kind {EOF, EOLN, RESERVED_WORD, IDENTIFIER, INTEGER, FLOAT, STRING, CHAR, SYMBOL, UNKNOWN};

	private Kind kind ;		/* Major token classification, e.g. kind = reserved word */
	private long code;		/* Within a major classification, this specifies the detail */
	private String text;	/* The actual text of the token e.g. the text of the token */

	/**********
	 * This fully parameterized constructor is used to establish each lexical token.  There is no
	 * default constructor defined because this class is completely determined by the Lexer Class
	 * 
	 * @param k	- This specifies the Kind of the token using the above enumeration
	 * @param c - This code provides detail about an element of a particular token Kind
	 * @param t - This String specifies the actual textual representation of the token
	 */
	public Token(Kind k, int c, String t) {
		kind = k;
		code = c;
		text = t;
	}
	
	/**********
	 * The following are the getters for the Token class.  There are no setters!
	 * 
	 * @return	The getters return the names attribute
	 */
	public Kind getTokenKind() {
		return kind;
	}
	
	public long getTokenCode() {
		return code;
	}
	
	public String getTokenText() {
		return text;
	}
	
	/**********
	 * This toString is used for debugging purposes.  It does not damage the object the way the
	 * Lexer's toString does!
	 */
	public String toString() {
		String result = "Token : Kind = ";
		switch (kind) {
		case EOF: 
			result += "<EOF>";
			break;
		case EOLN: 
			result += "<EOLN>";
			break;
		case RESERVED_WORD:
			result += "<Reserved Word>: code = " + code + "; text = \"" + text + "\"";
			break;
		case IDENTIFIER:
			result += "<Identifier>: code = " + code + "; text = \"" + text + "\"";
			break;
		case INTEGER:
			Scanner tmp = new Scanner(text);
			if (tmp.hasNextLong())
				code = tmp.nextLong();
			result += "<Integer>: code = " + code + "; text = \"" + text + "\"";
			tmp.close();
			break;
		case FLOAT:
			result += "<Float>: code = " + code + "; text = \"" + text + "\"";
			break;
		case STRING:
			result += "<String>: code = " + code + "; \"" + text + "\"";
			break;
		case CHAR:
			result += "<Char>: code = " + code + "; \'" + text + "\'";
			break;
		case SYMBOL:
			result += "<Symbol>: code = " + code + "; \"" + text + "\"";
			break;
		case UNKNOWN:
			result += "<Unknown>";
			break;
		}
		return result;
	}
}
