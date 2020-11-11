package lexer;


import java.util.Scanner;

import lexer.Token.Kind;

public class LexerAutomatedTest2 {
	/**
	 * <p>
	 * Title: LexerAutomatedTest1 - A component of the Programmable Calculator Project
	 * </p>
	 *
	 * <p>
	 * Description: An controller object class that tests the Lexer and Node classes
	 * </p>
	 *
	 * <p>
	 * Copyright: Copyright © 2019
	 * </p>
	 *
	 * @author Lynn Robert Carter
	 * @version 1.00	Baseline version 2019-01-09
	 */
	
	
	/**********************************************************************************************
	 * 
	 * The following are the attributes for this Class
	 */
	
	// This holds the string that provided the input data for the test
	private static Scanner testData = new Scanner("8*3+2");
	
	// This is the lexer object that does the lexing
	private static Lexer lexer = new Lexer(testData);
	
	// This is the Token object that holds the result of the lexing
	private static Token currentToken;
	
	// These variable hold the count of the number of passes and failed tests cases
	private static int numberPassed = 0;
	private static int numberFailed = 0;
	
	/*****
	 * The method checks what the lexer actually produces against what the developer believes it
	 * should produce.
	 * 
	 * @param t		This String is the title for the test
	 * @param k		This is the Kind of token object that was found
	 * @param c		This is the Code that distinguishes amount some of the Kinds 
	 * @param s		This is the String that should be found
	 * @return		The method returns true if the input matches the expectation
	 */
	private static boolean checkToken(String t, Kind k, long c, String s) {
		// Fetch the next token
		currentToken = lexer.accept();

		// Display the title and the expected values
		System.out.print(t + "; Kind: " + k + "; Code: " + c + "; text: " + s + "; ");
		
		// Compare the expected with the actual values
		if (currentToken.getTokenKind() == k && currentToken.getTokenCode() == c && currentToken.getTokenText().equals(s)) {
			// They match so say so, tally the success, and return true
			System.out.println("*** Pass");
			numberPassed++;
			return true;
		}
		
		// At least one of the three aspects failed This display what was expected and what was found
		System.out.print("*** Failed: ");
		if (currentToken.getTokenKind() != k) 
			System.out.print("Token.Kind was: " + currentToken.getTokenKind() + "; Should have been: " + k + "; ");
		if (currentToken.getTokenCode() != c) 
			System.out.print("Token.Code was: " + currentToken.getTokenCode() + "; Should have been: " + c + "; ");
		if (currentToken.getTokenText() != s) 
			System.out.print("Token.Text was: " + currentToken.getTokenText() + "; Should have been: " + s + ";");
		
		// This tallies the failure and returns false
		numberFailed++;
		return false;
	}
	
	/*****
	 * This main method establishes a Lexer Class object and then tests the class by calling the
	 * checkToken method to see if the Lexer processes the provided input the way the designer
	 * believes it should.
	 * 
	 * @param args	The Main Method's argument is ignored by this application.
	 */
	public static void main(String[] args) {

		// The following are the hand coded test based on the developers understanding
		checkToken(" 1. Identifier - 4 chars", Kind.INTEGER, 8, "8");
		checkToken(" 2. Identifier - 2 chars", Kind.SYMBOL, 8, "*");
		checkToken(" 3. Identifier - 1 char", Kind.INTEGER, 3, "3");
		checkToken(" 4. Identifier - 4 chars", Kind.SYMBOL, 6, "+");
		checkToken(" 5. Integer - 3 chars", Kind.INTEGER, 2, "2");
	
		checkToken("40. End Of Line", Kind.EOLN, -1, "");
		checkToken("41. End Of File", Kind.EOF, -1, "");
		
		// This displays the tallied results.
		System.out.println("\nNumber of tests passed: " + numberPassed);
		System.out.println("Number of tests failed: " + numberFailed);
	}

}
