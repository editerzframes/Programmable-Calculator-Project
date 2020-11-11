package lexer;

import java.util.InputMismatchException;
import java.util.Scanner;
import lexer.Token.Kind;

public class Lexer {
	/**
	 * <p>
	 * Title: Lexer - A component of the Programmable Calculator Project
	 * </p>
	 *
	 * <p>
	 * Description: A controller object class that implements the scanner functions
	 * </p>
	 *
	 * <p>
	 * Copyright: Copyright © 2019
	 * </p>
	 *
	 * @author Lynn Robert Carter
	 * @version 1.00	Baseline version 2019-01-08
	 * @version 1.01	Align FLOAT and INTEGER with Java definition and correct handling of ";" 2019-01-09
	 */
	
	/**********************************************************************************************
	 * 
	 * The Lexer Class provides the functions similar to the Scanner class from the java.util
	 * library.
	 * 
	 * The following are the primary followed by the secondary attributes for the Lexer Class
	 */
	private Token currentToken;			// The current token - The most recently accepted token
	private Token nextToken;			// The next token - It has not yet been accepted
	
	private String textLine;			// The text line that holds the input line being processed
	private int ndx;					// The index into the line for the next token to be found
	private Scanner reader;				// This is the input scanner the provides the file input
	
	private String tokenString;			// This is the string for the token being assembled
	private int tokenCode;				// This is the code for the specific kind of token
	private boolean decimalFound;		// This flag records if a decimal point has been found
	private boolean exponentFound;		// This flag records if an exponent has been found
	private boolean firstLineRead;		// This flag suppresses an EOLN on the first line
	private boolean eofFound;			// This flag enables a final <EOLN> before the <EOF>.
		
	/**********
	 * This constructor creates a new Lexer object given a Scanner object has been given to it.
	 * The constructor initializes the internal state and "primes the pump" to get the first input
	 * token read into the "nextToken" attribute. This Lexer is built on the model of two Tokens,
	 * currentToken and nextToken followed by the rest of the input that has yet to be "lexed".
	 * Once a Token has been accepted, it moved from nextToken to currentToken and a new nextToken
	 * is "lexed". The process allows a parser to work with the currentToken and ask questions 
	 * about the nextToken as part of the parsing process.  This kind of lexer makes it easier to
	 * write parsers by hand, but does not practically increase the set of languages that could be
	 * parsed.
	 * 
	 * @param r		The only input parameter is a java.util Scanner Class object for reading input
	 */
	public Lexer(Scanner r) {
		firstLineRead = false;			// Set up to read the first line properly
		eofFound = false;				// Assume the file is not empty
		reader = r;						// Capture the Scanner object for future calls
		textLine = "";					// Set the input line to empty
		ndx = 1;						// Point to a non-existent character to force input
		currentToken = null;			// There is no current token, it must be "accepted"!
		nextToken = lexNextToken();		// We do fetch the very first token for the nextToken
	}
	
	/**********
	 * This method accepts the nextToken to be the currentToken, fetches a new nextToken, and
	 * returns the just accepted Token for processing by a parser.
	 * 
	 * @return	the just accepted Token to the caller
	 */
	public Token accept() {
		currentToken = nextToken;
		nextToken = lexNextToken();
		return currentToken; 
	}
	
	/**********
	 * This method returns a copy of the Token that was most recently accepted.
	 * 
	 * @return	the most recently accepted Token to the caller
	 */
	public Token getCurrentToken() {
		return currentToken;
	}
	
	/**********
	 * This method returns a copy of the Token that will be accepted next. The use of this Token is
	 * primarily to help determine the proper way to parse and process the currentToken.
	 * 
	 * @return	the Token that will be accepted next to the caller
	 */
	public Token getNextToken() {
		return nextToken;
	}
	
	/*********************************************************************************************/
	/**********************************************************************************************
	 * 
	 *  The following set of "has" and "next" methods is patterned after the Scanner class and is
	 *  used by a caller to create a parser-like solutions to the input processing needs of their
	 *  application.
	 *  
	 */

	/**********
	 * Return true if the nextToken has a kind attribute of EOF (end of file)
	 * 
	 * @return	true or false based on the contents of the kind attribute
	 */
	public boolean hasNextEOF() {
		return nextToken.getTokenKind() == Kind.EOF;
	}
	
	/**********
	 * Return the nextToken if it has a kind attribute of EOF (end of file), otherwise, it throws
	 * an InputMismatchException
	 * 
	 * @return	true or false based on the contents of the kind attribute
	 * @throws	InputMismatchException if the nextToken is not an EOF kind of Token
	 */
	public Token nextEOF() {
		if (nextToken.getTokenKind() == Kind.EOF)
			return accept();
		else {
			System.out.println("*** Error *** From Lexer: EOF not found!");
			throw new InputMismatchException("From Lexer: EOF not found!");
		}
	}

	/*********************************************************************************************/
	/**********
	 * Return true if the nextToken has a kind attribute of RESERVED_WORD (e.g. if and while)
	 * 
	 * @return	true or false based on the contents of the kind attribute
	 */
	public boolean hasNextRESERVED_WORD() {
		return nextToken.getTokenKind() == Kind.RESERVED_WORD;
	}
	
	/**********
	 * Return the nextToken if it has a kind attribute of RESERVED_WORD (e.g. if and while), 
	 * otherwise, it throws an InputMismatchException
	 * 
	 * @return	true or false based on the contents of the kind attribute
	 * @throws	InputMismatchException if the nextToken is not a RESERVED_WORD kind of Token
	 */
	public Token nextRESERVED_WORD() {
		if (nextToken.getTokenKind() == Kind.RESERVED_WORD)
			return accept();
		else {
			System.out.println("*** Error *** From Lexer: RESERVED_WORD not found!");
			throw new InputMismatchException("From Lexer: RESERVED_WORD not found!");
		}
	}

	/**********
	 * Return true if the nextToken has a kind attribute of RESERVED_WORD (e.g. if and while) AND
	 * the text of the RESERVED_WORD matches the passed-in String parameter
	 * 
	 * @param	rw	this specifies the reserve word for which we are looking
	 * @return	true or false based on the contents of the kind attribute and a matching string
	 */	
	public boolean hasNextRESERVED_WORD(String rw) {
		return nextToken.getTokenKind() == Kind.RESERVED_WORD &&
				nextToken.getTokenText().equals(rw);
	}
	
	/*********************************************************************************************/
	/**********
	 * Return true if the nextToken has a kind attribute of IDENTIFIER (e.g. sum)
	 * 
	 * @return	true or false based on the contents of the kind attribute
	 */
	public boolean hasNextIDENTIFIER() {
		return nextToken.getTokenKind() == Kind.IDENTIFIER;
	}
	
	/**********
	 * Return the nextToken if it has a kind attribute of IDENTIFIER (e.g. sum), otherwise, it 
	 * throws an InputMismatchException
	 * 
	 * @return	true or false based on the contents of the kind attribute
	 * @throws	InputMismatchException if the nextToken is not an IDENTIFIER kind of Token
	 */
	public Token nextIDENTIFIER() {
		if (nextToken.getTokenKind() == Kind.IDENTIFIER)
			return accept();
		else {
			System.out.println("*** Error *** From Lexer: IDENTIFIER not found!");
			throw new InputMismatchException("From Lexer: IDENTIFIER not found!");
		}
	}

	/**********
	 * Return true if the nextToken has a kind attribute of IDENTIFIER (e.g. sum) AND the text of
	 * the IDENTIFIER matches the passed-in String parameter
	 * 
	 * @param	ident	this specifies the identifier for which we are looking
	 * @return	true or false based on the contents of the kind attribute and a matching string
	 */	
	public boolean hasNextIDENTIFIER(String ident) {
		return nextToken.getTokenKind() == Kind.IDENTIFIER && 
				nextToken.getTokenText().equals(ident);
	}

	/*********************************************************************************************/
	/**********
	 * Return true if the nextToken has a kind attribute of INTEGER (e.g. 138)
	 * 
	 * @return	true or false based on the contents of the kind attribute
	 */
	public boolean hasNextINTEGER() {
		return nextToken.getTokenKind() == Kind.INTEGER;
	}
	
	/**********
	 * Return the nextToken if it has a kind attribute of INTEGER (e.g. 138), otherwise, it throws
	 * an InputMismatchException
	 * 
	 * @return	true or false based on the contents of the kind attribute
	 * @throws	InputMismatchException if the nextToken is not an INTEGER kind of Token
	 */
	public Token nextINTEGER() {
		if (nextToken.getTokenKind() == Kind.INTEGER)
			return accept();
		else {
			System.out.println("*** Error *** From Lexer: INTEGER not found!");
			throw new InputMismatchException("From Lexer: INTEGER not found!");
		}
	}

	/**********
	 * Return true if the nextToken has a kind attribute of INTEGER (e.g. sum) AND the text of
	 * the INTEGER matches the passed-in String parameter
	 * 
	 * @param	integer	this specifies the integer for which we are looking
	 * @return	true or false based on the contents of the kind attribute and a matching string
	 */	
	public boolean hasNextINTEGER(String integer) {
		return nextToken.getTokenKind() == Kind.INTEGER && 
				nextToken.getTokenText().equals(integer);
	}
	
	/*********************************************************************************************/
	/**********
	 * Return true if the nextToken has a kind attribute of FLOAT (e.g. 138.75)
	 * 
	 * @return	true or false based on the contents of the kind attribute
	 */
	public boolean hasNextFLOAT() {
		return nextToken.getTokenKind() == Kind.FLOAT;
	}
	
	/**********
	 * Return the nextToken if it has a kind attribute of FLOAT (e.g. 138.75), otherwise, it throws
	 * an InputMismatchException
	 * 
	 * @return	true or false based on the contents of the kind attribute
	 * @throws	InputMismatchException if the nextToken is not a FLOAT kind of Token
	 */
	public Token nextFLOAT() {
		if (nextToken.getTokenKind() == Kind.FLOAT)
			return accept();
		else {
			System.out.println("*** Error *** From Lexer: FLOAT not found!");
			throw new InputMismatchException("From Lexer: FLOAT not found!");
		}
	}

	/**********
	 * Return true if the nextToken has a kind attribute of FLOAT (e.g. sum) AND the text of
	 * the FLOAT matches the passed-in String parameter (Given all of the potentially equal
	 * valued textual representations, this is probably *not* a good way to deal with the
	 * need to accept a specific input value of type FLOAT.)
	 * 
	 * @param	f	this specifies the floiat for which we are looking
	 * @return	true or false based on the contents of the kind attribute and a matching string
	 */	
	public boolean hasNextFLOAT(String f) {
		return nextToken.getTokenKind() == Kind.FLOAT && 
				nextToken.getTokenText().equals(f);
	}
	
	/*********************************************************************************************/
	/**********
	 * Return true if the nextToken has a kind attribute of STRING (e.g. "text")
	 * 
	 * @return	true or false based on the contents of the kind attribute
	 */
	public boolean hasNextSTRING() {
		return nextToken.getTokenKind() == Kind.STRING;
	}
	
	/**********
	 * Return the nextToken if it has a kind attribute of STRING (e.g. "text"), otherwise, it 
	 * throws an InputMismatchException
	 * 
	 * @return	true or false based on the contents of the kind attribute
	 * @throws	InputMismatchException if the nextToken is not a STRING kind of Token
	 */
	public Token nextSTRING() {
		if (nextToken.getTokenKind() == Kind.STRING)
			return accept();
		else {
			System.out.println("*** Error *** From Lexer: STRING not found!");
			throw new InputMismatchException("From Lexer: STRING not found!");
		}
	}

	/**********
	 * Return true if the nextToken has a kind attribute of STRING (e.g. "text") AND the text of
	 * the STRING matches the passed-in String parameter
	 * 
	 * @param	str	this specifies the string for which we are looking
	 * @return	true or false based on the contents of the kind attribute and a matching string
	 */	
	public boolean hasNextSTRING(String str) {
		return nextToken.getTokenKind() == Kind.STRING && 
				nextToken.getTokenText().equals(str);
	}

	/*********************************************************************************************/
	/**********
	 * Return true if the nextToken has a kind attribute of CHAR (e.g. 'a')
	 * 
	 * @return	true or false based on the contents of the kind attribute
	 */
	public boolean hasNextCHAR() {
		return nextToken.getTokenKind() == Kind.CHAR;
	}
	
	/**********
	 * Return the nextToken if it has a kind attribute of CHAR (e.g. 'a'), otherwise, it 
	 * throws an InputMismatchException
	 * 
	 * @return	true or false based on the contents of the kind attribute
	 * @throws	InputMismatchException if the nextToken is not a CHAR kind of Token
	 */
	public Token nextCHAR() {
		if (nextToken.getTokenKind() == Kind.CHAR)
			return accept();
		else {
			System.out.println("*** Error *** From Lexer: CHAR not found!");
			throw new InputMismatchException("From Lexer: CHAR not found!");
		}
	}

	/**********
	 * Return true if the nextToken has a kind attribute of CHAR (e.g. 'a') AND the text of
	 * the CHAR matches the passed-in String parameter
	 * 
	 * @param	ch	this specifies the character for which we are looking
	 * @return	true or false based on the contents of the kind attribute and a matching string
	 */	
	public boolean hasNextCHAR(String ch) {
		return nextToken.getTokenKind() == Kind.CHAR && 
				nextToken.getTokenText().equals(ch);
	}
	
	/*********************************************************************************************/
	/**********
	 * Return true if the nextToken has a kind attribute of SYMBOL (e.g. =)
	 * 
	 * @return	true or false based on the contents of the kind attribute
	 */
	public boolean hasNextSYMBOL() {
		return nextToken.getTokenKind() == Kind.SYMBOL;
	}
	
	/**********
	 * Return the nextToken if it has a kind attribute of SYMBOL (e.g. =), otherwise, it throws
	 * an InputMismatchException
	 * 
	 * @return	true or false based on the contents of the kind attribute
	 * @throws	InputMismatchException if the nextToken is not a SYMBOL kind of Token
	 */
	public Token nextSYMBOL() {
		if (nextToken.getTokenKind() == Kind.SYMBOL)
			return accept();
		else {
			System.out.println("*** Error *** From Lexer: SYMBOL not found!");
			throw new InputMismatchException("From Lexer: SYMBOL not found!");
		}
	}

	/**********
	 * Return true if the nextToken has a kind attribute of SYMBOL (e.g. =) AND the text of
	 * the SYMBOL matches the passed-in String parameter
	 * 
	 * @param	symbol	this specifies the symbol for which we are looking
	 * @return	true or false based on the contents of the kind attribute and a matching string
	 */	
	public boolean hasNextSYMBOL(String symbol) {
		return nextToken.getTokenKind() == Kind.SYMBOL && 
				nextToken.getTokenText().equals(symbol);
	}
	
	/*********************************************************************************************/
	/**********
	 * Return true if the nextToken has a kind attribute of UNKNOWN (e.g. ?)
	 * 
	 * @return	true or false based on the contents of the kind attribute
	 */
	public boolean hasNextUNKNOWN() {
		return nextToken.getTokenKind() == Kind.UNKNOWN;
	}
	
	/**********
	 * Return the nextToken if it has a kind attribute of UNKNOWN (e.g. ?), otherwise, it throws
	 * an InputMismatchException
	 * 
	 * @return	true or false based on the contents of the kind attribute
	 * @throws	InputMismatchException if the nextToken is not a UNKNOWN kind of Token
	 */
	public Token nextUNKNOWN() {
		if (nextToken.getTokenKind() == Kind.UNKNOWN)
			return accept();
		else {
			System.out.println("*** Error *** From Lexer: UNKNOWN not found!");
			throw new InputMismatchException("From Lexer: UNKNOWN not found!");
		}
	}
	
	/*********************************************************************************************/
	/**********************************************************************************************
	 * 
	 *  The following set of methods are the internal support methods that the class uses to do the
	 *  heavy "lifting" required to implement the Class' API.
	 *  
	 */

	/*****
	 * This private method defines what is meant by "whitespace" for the lexer.  Whitespace are
	 * those characters that are used to separate tokens when they appear on the same line. (Not
	 * all tokens need to be separated by "whitespace".
	 * 
	 * The current definition of "whitespace" is blanks and tabs
	 */
	private void skipWhiteSpace() {
		while (ndx < textLine.length() && 
				(textLine.charAt(ndx) == ' ' || textLine.charAt(ndx) == '\t')) ndx++;
	}
	
	/*****
	 * This method defines what we mean by the term <IDENTIFIER>. The method returns true if the
	 * text following the nextToken is an <IDENTIFIER> and the attribute tokenString is set to
	 * the actual set of characters.
	 * 
	 * This definition is a string of visible character that must start with an alphabetic
	 * character (upper or lower case) followed by zero or more alphanumeric characters or '$' or
	 * '_' characters.
	 * 
	 * @return	true if the characters waiting to be lexed are indeed an <IDENTIFIER>
	 */
	private boolean nextIsIdentifier() {
		if ((ndx < textLine.length()) && 
			((textLine.charAt(ndx) >= 'a' && textLine.charAt(ndx) <= 'z') ||
			 (textLine.charAt(ndx) >= 'A' && textLine.charAt(ndx) <= 'Z'))){
			tokenString = "";
			while ((ndx < textLine.length()-1)
					&&
				   ((textLine.charAt(ndx) >= 'a' && textLine.charAt(ndx) <= 'z') ||
				    (textLine.charAt(ndx) >= 'A' && textLine.charAt(ndx) <= 'Z') ||
				    (textLine.charAt(ndx) >= '0' && textLine.charAt(ndx) <= '9') ||
				    textLine.charAt(ndx) == '$' || textLine.charAt(ndx) == '_')) {
				tokenString += textLine.charAt(ndx);
				ndx++;
			}
			return true;
		}
		else return false;
	}
	
	/*****
	 * This method defines what we mean by the term <RESERVED_WORD>. The method returns true if the
	 * text following the nextToken is an <RESERVED_WORD> and the attribute tokenString is set to
	 * the actual set of characters.
	 * 
	 * This definition is a string of visible character that must start with an alphabetic
	 * character (upper or lower case) followed by zero or more alphanumeric characters or '$' or
	 * '_' characters AND it must match one of the predefined set of <RESERVED_WORD>s. Right now,
	 * there are only two of them: "if" and "while".
	 * 
	 * @return	true if the characters waiting to be lexed are indeed an <RESERVED_WORD>
	 */
	private boolean nextIsReservedWord() {
		if (tokenString.equals("if")) {
			tokenCode = 0;
			return true;
		}
		if (tokenString.equals("while")) {
			tokenCode = 1;
			return true;
		}
		
		return false;
	}
	
	/*****
	 * This method defines what we mean by the term exponent in a FLOAT. The method returns true if
	 * the text following the nextToken is an FLOAT and the next part of the token is a valid 
	 * exponent.
	 * 
	 * This definition is a string of visible character that must start with an 'E' (upper or lower 
	 * case) followed by an optional sign character which is, in turn, followed by one or more 
	 * digits.
	 * 
	 * @return	true if the characters waiting to be lexed are indeed an exponent part of a FLOAT
	 */
	private boolean nextIsExponent() {
		
		// The exponent part *must* start with an 'E' or an 'e'
		if (textLine.charAt(ndx) != 'E' && textLine.charAt(ndx) != 'e') 
			return false;
		
		// We found the 'E' or an 'e', so we start gathering what we believe to be an exponent
		String exponentPart = "" + textLine.charAt(ndx++);
		
		// Check to see if there is an optional sign.  If so, add it to the exponent
		if (textLine.charAt(ndx) == '+' || textLine.charAt(ndx) == '-')
			exponentPart += textLine.charAt(ndx++);
		
		// The next character must be one of more digits
		if (textLine.charAt(ndx) >= '0' && textLine.charAt(ndx) <= '9') {
			
			// If we get here, there is at least one numeric digit, so the following loop can be
			// used to gather up all of the digits.
			while (textLine.charAt(ndx) >= '0' && textLine.charAt(ndx) <= '9') 
				exponentPart += textLine.charAt(ndx++);
			
			// When the loop ends, we append the exponent part onto the numeric prefix
			tokenString += exponentPart;
			exponentFound = true;
			return true;
		}
		
		// If we get here, we found an 'E' or and 'e' followed by an optional sign, but that was
		// not followed by one or more digits, so it is *not* a valid exponent part
		return false;
	}
	
	/*****
	 * This method defines what we mean by the term numeric, which could be an <INTEGER> or a
	 * <FLOAT>. The method returns true if the text following the nextToken is an <INTEGER> or a
	 * <FLOAT>.
	 * 
	 * @return	true if the characters waiting to be lexed are indeed an exponent part of a number
	 */
	private boolean nextIsNumeric() {
		decimalFound = false;
		exponentFound = false;
		tokenString = "";
		//
		// This portion deals with a value with a leading numeric digit. 
		if ((ndx < textLine.length()) && (textLine.charAt(ndx) >= '0' && textLine.charAt(ndx) <= '9')) {
			tokenString += textLine.charAt(ndx++);
			while (textLine.charAt(ndx) >= '0' && textLine.charAt(ndx) <= '9')
				tokenString += textLine.charAt(ndx++);
			if (textLine.charAt(ndx) == '.') {
				
				// This processes values with a decimal point after one or more digits
				decimalFound = true;
				tokenString += textLine.charAt(ndx++);
				while (textLine.charAt(ndx) >= '0' && textLine.charAt(ndx) <= '9')
					tokenString += textLine.charAt(ndx++);
				
				// This processes an exponent after the decimal point and zero or more digits
				// So, values such as 1.E10 and 1.234E10 would be processed by the following
				if (nextIsExponent())
					return true;
				
				// This return processes values such as 1. and 1.234
				return true;
			}
			// This processes values such as 123E-10 (Values with no decimal points)
			if (nextIsExponent())
				return true;
			
			// This processes values such as 123
			return true;
		}
		
		// This portion deals with a value where the first character is a decimal point. It must be
		// followed by at least one numeric digit.
		else if (textLine.charAt(ndx) == '.') {
			if (textLine.charAt(ndx+1) >= '0' && textLine.charAt(ndx) <= '9') {
				
				// Once we know that there is at least one numeric digits, the rest is just like
				// what we did above.
				tokenString += textLine.charAt(ndx++);
				decimalFound = true;
				while (textLine.charAt(ndx) >= '0' && textLine.charAt(ndx) <= '9') 
					tokenString += textLine.charAt(ndx++);
				
				// This processes an exponent after the decimal point and zero or more digits
				// So, values such as .5E10 and .503E10 would be processed by the following
				if (nextIsExponent())
					return true;
				
				// This return processes values such as .5 and .523
				return true;
			}
			
			// If we get here, we have something that start with a decimal point, but it is not
			// follow by a digit... so it is not numeric
			return false;
		}
		
//		// This portion deals with a value where the first character is a decimal point. It must be
//		// followed by at least one numeric digit.
//		else if (textLine.charAt(ndx) == '-') {
//			if (textLine.charAt(ndx+1) >= '0' && textLine.charAt(ndx) <= '9') {
//				
//				// Once we know that there is at least one numeric digits, the rest is just like
//				// what we did above.
//				tokenString += textLine.charAt(ndx++);
//				decimalFound = true;
//				while (textLine.charAt(ndx) >= '0' && textLine.charAt(ndx) <= '9') 
//					tokenString += textLine.charAt(ndx++);
//				
//				// This processes an exponent after the decimal point and zero or more digits
//				// So, values such as .5E10 and .503E10 would be processed by the following
//				if (nextIsExponent())
//					return true;
//				
//				// This return processes values such as .5 and .523
//				return true;
//			}
//			
//			// If we get here, we have something that start with a decimal point, but it is not
//			// follow by a digit... so it is not numeric
//			return false;
//		}
		else
			
			// If we get here, we have something that does not start with a digit or a decimal
			// point, so it is not numeric
			return false;
	}
	
	/*****
	 * This method defines what we mean by the term <STRING>. The method returns true if the text
	 * following the nextToken starts with a quote, has a matching final quote on the same line,
	 * and any internal quotes must be preceded by a '\' character.
	 * 
	 * @return	true if the characters waiting to be lexed are indeed <STRING>
	 */
	private boolean nextIsString() {
		if (textLine.charAt(ndx) == '\"') {
			ndx++;
			int start = ndx;
			
			// This loop keeps going until the end of the line or a closing quote is found
			while (ndx < textLine.length() && textLine.charAt(ndx) != '\"') {
				
				// If a backslash is found and the next character is a quote, it is treated
				// as part of the string, so we skip it so the loop control does not see it
				if (textLine.charAt(ndx) == '\\' && textLine.charAt(ndx+1) == '\"') ndx++;
				ndx++;
			}
			
			// There are two ways to get here... the end of the line or a matching ending quote
			if (ndx < textLine.length() && textLine.charAt(ndx) == '\"') {
				
				// We found the matching ending quote, so it is a <STRING>
				tokenString = textLine.substring(start, ndx++);
				return true;
			}
			else {
				
				// We did not find a matching ending quote, so this is *not* a <STRING>
				ndx = start - 1;
				return false;
			}
		}
		else
			
			// The starting character is not a quote, so this can't be a <STRING>
			return false;
	}
	
	/*****
	 * This method defines what we mean by the term <CHAR>. The method returns true if the text
	 * following the nextToken starts with an apostrophe, has a matching final apostrophe on the
	 * same line, and an internal apostrophe must be preceded by a '\' character.
	 * 
	 * The method checks to make sure that a backslash is followed by one of the valid lower case
	 * characters signaling a valid escape sequence.
	 * 
	 * @return	true if the characters waiting to be lexed are indeed <CHAR>
	 */
	private boolean nextIsChar() {
		if (textLine.charAt(ndx) == '\'') {
			ndx++;
			if (textLine.charAt(ndx) == '\\')
				
				// This part checks for the valid escape sequences
				if (textLine.charAt(ndx+1) == '\'') {
					ndx+=2;
					tokenString = "\'";
				} else if (textLine.charAt(ndx+1) == '\"') {
					ndx+=2;
					tokenString = "\"";
				} else if (textLine.charAt(ndx+1) == '\t') {
					ndx+=2;
					tokenString = "\t";
				} else if (textLine.charAt(ndx+1) == '\n') {
					ndx+=2;
					tokenString = "\n";
				} else if (textLine.charAt(ndx+1) == '\b') {
					ndx+=2;
					tokenString = "\b";
				} else if (textLine.charAt(ndx+1) == '\f') {
					ndx+=2;
					tokenString = "\f";
				} else {
					
					// If we get here, the '\' we found was *not* followed by a valid escape
					// character, so we treat is as if the user has entered "\\"
					ndx++;
					tokenString = "\\";
				}
			
			// This portion of the code checks to make sure there is a matching closing apostrophe
			if (textLine.charAt(ndx) == '\'') {
				
				// If so, all is good, it's a <CHAR>
				ndx++;
				return true;
			}
			
			// If not, then the apostrophe we saw before was not part of a <CHAR>
			else
				return false;
		}
		else
			return false;
	}
	
	/*****
	 * This internal method is used to perform the lexical analysis of the input
	 * 
	 * @return	a token, even if it is an unknown token consisting of just a character.
	 */
	public Token lexNextToken() {
		
		// Start by skipping any white space
		skipWhiteSpace();
		
		// If there are no more character on the line and no more input lines, signal an EOF
		if (ndx >= textLine.length() && !reader.hasNextLine())
			if (eofFound)
				return new Token(Kind.EOF,-1,"");
			else {
				eofFound = true;
				return new Token(Kind.EOLN,-1,"");
			}
				
		// If we are at the end of a line *and* there are more lines of input, keep going until
		// we reach a non-whitespace character
		if (ndx >= textLine.length() && reader.hasNextLine()) {
			textLine = reader.nextLine().trim() + " ";
			ndx = 0;
			if (firstLineRead)
				return new Token(Kind.EOLN,-1,"");
			else
				firstLineRead = true;
		}
		
		// Since .trim() may not remove what we consider to be whitespace, we call this routine
		// again!
		skipWhiteSpace();
		
		// We check for each of the various tokens that could be in the input
		if (nextIsIdentifier())
			if (nextIsReservedWord())
				
				// Lexically, a <RESERVE_WORD> is an <IDENTIFIER> so we have to check to see if the
				// text of the <IDENTIFIER> matches one of the <RESERVE_WORD>s
				return new Token(Kind.RESERVED_WORD, tokenCode, tokenString);
			else
				return new Token(Kind.IDENTIFIER, -1, tokenString);
		
		// Similarly, a <FLOAT> can look like an <INTEGER>
		if (nextIsNumeric())
			if (decimalFound)
				return new Token(Kind.FLOAT, -1, tokenString);
			else if (exponentFound)
				return new Token(Kind.FLOAT, -1, tokenString);
			else
				return new Token(Kind.INTEGER, Integer.parseInt(tokenString), tokenString);
		
		// <STRING> and <CHAR> processing simple
		if (nextIsString()) 
			return new Token(Kind.STRING, -1, tokenString);
		if (nextIsChar()) 
			return new Token(Kind.CHAR, -1, tokenString);
		
		// This next block checks for the recognized symbol characters
		if (textLine.charAt(ndx) == '.') {
			ndx++;
			return new Token(Kind.SYMBOL, 1, ".");
		}
		if (textLine.charAt(ndx) == ',') {
			ndx++;
			return new Token(Kind.SYMBOL, 2, ",");
		}
		if (textLine.charAt(ndx) == '=') {
			ndx++;
			return new Token(Kind.SYMBOL, 3, "=");
		}
		if (textLine.charAt(ndx) == '(') {
			ndx++;
			return new Token(Kind.SYMBOL, 4, "(");
		}
		if (textLine.charAt(ndx) == ')') {
			ndx++;
			return new Token(Kind.SYMBOL, 5, ")");
		}
		if (textLine.charAt(ndx) == '+') {
			ndx++;
			return new Token(Kind.SYMBOL, 6, "+");
		}
		if (textLine.charAt(ndx) == '-') {
			ndx++;
			return new Token(Kind.SYMBOL, 7, "-");
		}
		if (textLine.charAt(ndx) == '*') {
			ndx++;
			return new Token(Kind.SYMBOL, 8, "*");
		}
		if (textLine.charAt(ndx) == '/') {
			ndx++;
			return new Token(Kind.SYMBOL, 9, "/");
		}
		if (textLine.charAt(ndx) == ';') {
			ndx++;
			return new Token(Kind.SYMBOL, 10, ";");
		}
		if (textLine.charAt(ndx) == '\"') {
			ndx++;
			return new Token(Kind.SYMBOL, 11, "\"");
		}
		
		
		// If it is none of the above, then it is an unknown token character as it is removed from
		// the input in a <UNKNOWN> token so the lexer can work on the next character
		return new Token(Kind.UNKNOWN, -1, textLine.substring(ndx++));
	}
	
	/**********
	 * This toString method is used to transform the actual input into debugging String.  Once this
	 * routine is used, all of the input will have been processed, so this is not a particularly
	 * useful debugging tool.  It for this reason, we say that this toString *damages* the object,
	 * so only use this method when you want to look at the sequence of tokens and have no desire 
	 * to actually do anything with them!
	 */
	public String toString() {
		String result = "";
		Token t = nextToken;
		while (t.getTokenKind() != Kind.EOF) {
			result += t;
			t = lexNextToken();
		}
		return result + t;
	}
}
