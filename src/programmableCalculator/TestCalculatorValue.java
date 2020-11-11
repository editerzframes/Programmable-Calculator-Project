package programmableCalculator;

/**
 * <p> Title: TestCalculatorValue </p>
 * 
 * <p> Description: A component of the Calculator application </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2017 </p>
 * 
 * @author Lynn Robert Carter
 *         Sumit Singh and Shivam Singhal
 * 
 * @version 4.30	2017-10-18	Initial baseline 
 * 
 * @version 4.40    2019-02-13  Initial baseline for Project- 1(Data Structure)
 * 
// */

public class TestCalculatorValue {

	/**********
	 * This class roots the execution of the test of the CalculatorValue class.  The application 
	 * tests the class by invoking the class methods and checking the result to see if the results 
	 * are proper.
	 * 
	 */
	
	/*********************************************************************************************/
	
	/**********
	 * The check method compares an Expected String to an Actual String and returns true if the 
	 * Strings match and false otherwise.  In addition, the Strings are displayed to the console
	 * and a message is display stating whether or not there is a difference.  If there is a
	 * difference, the character at the point of the difference in the actual String is replaced
	 * with a "?" and both are displayed making it clear what character is the start of the
	 * difference
	 * 
	 * @param Expected	The String object of the expected value
	 * @param Actual		The String object of the actual value
	 */
	private static boolean check(String expected, String actual) {
		// Display the input parameters
		System.out.println("***Expected String");
		System.out.println(expected);
		System.out.println("***Actual String");
		System.out.println(actual);
		
		// Check to see if there is a difference
		int lesserLength = expected.length();
		if (lesserLength > actual.length()) lesserLength = actual.length();
		int ndx = 0;
		while (ndx < lesserLength && expected.charAt(ndx) == actual.charAt(ndx))
			ndx++;
		
		// Explain why the loop terminated and if there is a difference make it clear to the user
		if (ndx < lesserLength || lesserLength < expected.length() || lesserLength < actual.length()) {
			System.out.println("*** There is a difference!\n" + expected.substring(0, ndx) + "? <-----");
			return false;
		}
		System.out.println("*** There is no difference!\n");
		return true;
	}
	
	/*********************************************************************************************/
	
	/**********
	 * This main method roots the execution of this test.  The method ignores the program
	 * parameters.  After initializing several local variables, it performs a sequence of
	 * tests, displaying information accordingly and tallying the number of successes and
	 * failures.
	 * 
	 * @param args	Ignored by this application.
	 */
	public static void main(String[] args) {
		// Display the header message to the console and initialize local variables
		System.out.println("Test CalculatorValue Class\n");
		int numPassed = 0;
		int numFailed = 0;
		
		// 1. Perform a default constructor test
		CalculatorValue test = new CalculatorValue();						// Perform the test
		
		System.out.println("1. No input given");								// No input that was given

		// Check the actual output against the expected.  If they match, the test has been passed and display the proper
		// message and tally the result
		if (check("measuredValue = 0\nerrorMessage = \n", test.debugToString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		// If they do not match, display that there was a failure and tally that result
		else {
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();

		
		// 2. Perform a constructor test with a long
		test = new CalculatorValue(1234567890123456L,1234567890123456L);						// Perform the test
		
		System.out.println("2. Input: 1234567890123456L");	

		// Check the actual output against the expected.  If they match, the test has been passed and display the proper
		// message and tally the result
		if (check("measuredValue = 1234567890123456\nerrorMessage = \n", test.debugToString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		// If they do not match, display that there was a failure and tally that result
		else {
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();

		
		// 3. Perform a copy constructor test
		CalculatorValue t = new CalculatorValue(1234567890123456L,1234567890123456L);			// Set up the test
		t.setErrorMessage("The error message string");
		
		test = new CalculatorValue(t);										// Perform the test
		
		System.out.println("3. Input:\n1234567890123456L\nThe error message string\n");	

		// Check the actual output against the expected.  If they match, the test has been passed and display the proper
		// message and tally the result
		if (check("measuredValue = 1234567890123456\nerrorMessage = The error message string\n", test.debugToString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		// If they do not match, display that there was a failure and tally that result
		else {
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();

		
		// 4. Perform a constructor test with a string
		test = new CalculatorValue("1234567890123456","1234567890123456");						// Perform the test
		
		System.out.println("4. Input: \"1234567890123456\"");	

		// Check the actual output against the expected.  If they match, the test has been passed and display the proper
		// message and tally the result
		if (check("measuredValue = 1234567890123456\nerrorMessage = \n", test.debugToString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		// If they do not match, display that there was a failure and tally that result
		else {
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();

		
		// 5. Perform addition
		CalculatorValue left = new CalculatorValue("1","1");						// Set up the test
		CalculatorValue right = new CalculatorValue("2","2");
		
		left.add(right);														// Perform the test
		
		System.out.println("5. Addition Input: \n1\n2");	

		// Check the actual output against the expected.  If they match, the test has been passed and display the proper
		// message and tally the result
		if (check("measuredValue = 3\nerrorMessage = \n", left.debugToString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		// If they do not match, display that there was a failure and tally that result
		else {
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();

		
		// 6. Perform subtraction
		left = new CalculatorValue("1","1");										// Set up the test
		right = new CalculatorValue("2","2");
		
		left.sub(right);														// Perform the test
		
		System.out.println("5. Subtraction Input: \n1\n2");	

		// Check the actual output against the expected.  If they match, the test has been passed and display the proper
		// message and tally the result
		if (check("measuredValue = -1\nerrorMessage = \n", left.debugToString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		// If they do not match, display that there was a failure and tally that result
		else {
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();

		
		// 7. Perform multiplication
		left = new CalculatorValue("1","1");										// Set up the test
		right = new CalculatorValue("2","2");
		
		left.mpy(right);														// Perform the test
		
		System.out.println("7. Multiplication Input: \n1\n2");	

		// Check the actual output against the expected.  If they match, the test has been passed and display the proper
		// message and tally the result
		if (check("measuredValue = 2\nerrorMessage = \n", left.debugToString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		// If they do not match, display that there was a failure and tally that result
		else {
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();

		
		// 8. Perform division
		left = new CalculatorValue("1","1");										// Set up the test
		right = new CalculatorValue("2","2");
		
		left.div(right);														// Perform the test
		
		System.out.println("8. Division Input: \n1\n2");	

		// Check the actual output against the expected.  If they match, the test has been passed and display the proper
		// message and tally the result
		if (check("measuredValue = 0\nerrorMessage = \n", left.debugToString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		// If they do not match, display that there was a failure and tally that result
		else {
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();

		
		System.out.println("Number of tests passed: " + numPassed);
		System.out.println("Number of tests failed: " + numFailed);

	}
}
