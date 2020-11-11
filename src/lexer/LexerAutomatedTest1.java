package lexer;


import java.util.Scanner;

import lexer.Token.Kind;

public class LexerAutomatedTest1 {
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
	private static Scanner testData = new Scanner("This is a test 123 while you test this, if it works let me know.\n" + 
			"This should find \"a string\".\n" + 
			"variable = constant + variable2 * 123.45;\n" + 
			"123.45E14 123E4\n" + 
			"\"the end");
	
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
		checkToken(" 1. Identifier - 4 chars", Kind.IDENTIFIER, -1, "This");
		checkToken(" 2. Identifier - 2 chars", Kind.IDENTIFIER, -1, "is");
		checkToken(" 3. Identifier - 1 char", Kind.IDENTIFIER, -1, "a");
		checkToken(" 4. Identifier - 4 chars", Kind.IDENTIFIER, -1, "test");
		checkToken(" 5. Integer - 3 chars", Kind.INTEGER, 123, "123");
		checkToken(" 6. Reserved Word", Kind.RESERVED_WORD, 1, "while");
		checkToken(" 7. Identifier - 3 chars", Kind.IDENTIFIER, -1, "you");
		checkToken(" 8. Identifier - 4 chars", Kind.IDENTIFIER, -1, "test");
		checkToken(" 9. Identifier - 4 chars", Kind.IDENTIFIER, -1, "this");
		checkToken("10. Symbol", Kind.SYMBOL, 2, ",");
		checkToken("11. Reserved Word", Kind.RESERVED_WORD, 0, "if");
		checkToken("12. Identifier - 2 chars", Kind.IDENTIFIER, -1, "it");
		checkToken("13. Identifier - 5 chars", Kind.IDENTIFIER, -1, "works");
		checkToken("14. Identifier - 3 chars", Kind.IDENTIFIER, -1, "let");
		checkToken("15. Identifier - 2 chars", Kind.IDENTIFIER, -1, "me");
		checkToken("16. Identifier - 4 chars", Kind.IDENTIFIER, -1, "know");
		checkToken("17. Symbol", Kind.SYMBOL, 1, ".");
		checkToken("18. End Of Line", Kind.EOLN, -1, "");
		checkToken("19. Identifier - 4 chars", Kind.IDENTIFIER, -1, "This");
		checkToken("20. Identifier - 6 chars", Kind.IDENTIFIER, -1, "should");
		checkToken("21. Identifier - 4 chars", Kind.IDENTIFIER, -1, "find");
		checkToken("22. String", Kind.STRING, -1, "a string");
		checkToken("23. Symbol", Kind.SYMBOL, 1, ".");
		checkToken("24. End Of Line", Kind.EOLN, -1, "");
		checkToken("25. Identifier - 8 chars", Kind.IDENTIFIER, -1, "variable");
		checkToken("26. Symbol", Kind.SYMBOL, 3, "=");
		checkToken("27. Identifier - 8 chars", Kind.IDENTIFIER, -1, "constant");
		checkToken("28. Symbol", Kind.SYMBOL, 6, "+");
		checkToken("29. Identifier - 9 chars", Kind.IDENTIFIER, -1, "variable2");
		checkToken("30. Symbol", Kind.SYMBOL, 8, "*");
		checkToken("31. Float - 6 chars", Kind.FLOAT, -1, "123.45");
		checkToken("32. Symbol", Kind.SYMBOL, 10, ";");
		checkToken("33. End Of Line", Kind.EOLN, -1, "");
		checkToken("34. Float - 9 chars", Kind.FLOAT, -1, "123.45E14");
		checkToken("35. Float - 5 chars", Kind.FLOAT, -1, "123E4");
		checkToken("36. End Of Line", Kind.EOLN, -1, "");
		checkToken("37. Symbol", Kind.SYMBOL, 11, "\"");
		checkToken("38. Identifier - 3 chars", Kind.IDENTIFIER, -1, "the");
		checkToken("39. Identifier - 3 chars", Kind.IDENTIFIER, -1, "end");
		checkToken("40. End Of Line", Kind.EOLN, -1, "");
		checkToken("41. End Of File", Kind.EOF, -1, "");
		
		// This displays the tallied results.
		System.out.println("\nNumber of tests passed: " + numberPassed);
		System.out.println("Number of tests failed: " + numberFailed);
	}

}
