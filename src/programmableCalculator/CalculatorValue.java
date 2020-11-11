package programmableCalculator;

import java.util.Scanner;
import programmableCalculator.UserInterface;

/**
 * <p> Title: CalculatorValue Class. </p>
 * 
 * <p> Description: A component of a JavaFX demonstration application that performs computations and also imple
 *                  implements the FSM to display error for any wrong entry</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2017 </p>
 * 
 * @author Lynn Robert Carter
 *         Sumit Singh and Shivam Singhal
 *         Puneet Garg
 * 
 * @version 4.30	2018-02-23 Double float implementation of the CalculatorValue class 
 * 
 * @version 4.40    2018-03-23 Double float implementation of the CalculatorValue class with error terms
 * 
 * @version 4.50    2018-10-06 The implementation of a calculator showing result value and error term in single 
 * 							   text field. 
 * 
 * @version 4.60    2018-11-24 Implementing the selecting the unit, so that calculation can be done by using the units.
 *
 * @version 4.70    2019-02-13 Baseline for Project 1(Data Structures).
 * 
 * @version 4.80    2019-02-26 Project 1 - Part 1 with the list of constants and variables (Data Structures).
 * 
 * @version 4.90    2019-03-31 Project 1 - Part 2 with a expression window to compute the expressions with constants and variables.
 * 
 */
public class CalculatorValue {
	
	/**********************************************************************************************
    
	Attributes
	
	**********************************************************************************************/
	  static DefinitionUserInterface data = new DefinitionUserInterface();
	
	// These are the major values that define a calculator value
	private double measuredValue = 0.00;
	private double errorTerm = 0.50;
	
	UNumber mValue = new UNumber();
	static UNumber eValue = new UNumber();
	UNumber two = new UNumber(2);	
	
	String errorMessage = "";
	
	double operand1LB = 0.00;
	double operand2LB = 0.00;
	double operand1UB = 0.00;
	double operand2UB = 0.00;
	double resultLB = 0.00;
	double resultUB = 0.00;
	
	public static String measuredValueErrorMessage = "";	// The alternate error message text
	public static String measuredValueInput = "";		// The input being processed
	public static int measuredValueIndexofError = -1;		// The index where the error was located
	public static int state = 0;						// The current state value
	public static int nextState = 0;					// The next state value
	public static boolean finalState = false;			// Is this state a final state
	private static String inputLine = "";				// The input line
	private static char currentChar;						// The current character in the line
	private static int currentCharNdx;					// The index of the current character
	private static boolean running;						// The flag that specifies if it is running
	
	private static int index_op1;
	private static int index_op2;
	
	private static String unit = "";
	
	private String[] operand_units = { "m", "km","s", "min", "h", "day","No Unit"};
    		
	public static double converssionTable[][] = {
			//m       //Km       //sec   //min     //hours         //days                   //No Unit

/*m*/		{ 1       , 0.001    ,1     , 1        ,1               ,1                        ,1     },
/*Km*/		{ 1000    , 1        ,1     , 1        ,1               ,1       				  ,1     },
/*sec*/		{1        ,1         ,1     ,0.0166667 ,0.00027777833333,1.157409722208333465e-5  ,1     },
/*min*/     {1        ,1         ,60    ,1         ,0.0166666999998 ,06.9444583332500006527e-4,1     },
/*hours*/   {1        ,1         ,3600  ,60        ,1               ,0.041666749999500006518  ,1     },
/*days*/    {1        ,1         ,86400 ,1440      ,24              ,1                        ,1     },
/*No Unit*/	{1        ,1         ,1     ,1         ,1               ,1                        ,1	 }};
	
	
	private boolean lookUpTableAddSub[][] = 
			//m     //Km    //mil   //sec   //min   //hours  //days  //No Unit

/*m*/	   {{true 	,true 	,false	,false   ,false   ,false  ,false},
/*Km*/		{true 	,true 	,false	,false   ,false   ,false  ,false},
/*sec*/		{false	,false	,true 	,true    ,true    ,true   ,false},
/*min*/		{false	,false	,true 	,true    ,true    ,true   ,false},
/*hour*/	{false	,false	,true 	,true    ,true    ,true   ,false},
/*days*/    {false	,false	,true 	,true    ,true    ,true   ,false},
/*No Unit*/	{false 	,false 	,false 	,false   ,false   ,false  ,true}};
			   	
	
	 
    static String[] name1 ;
    String[] corv1;
    String[] mv1 ;
    String[] ev1 ;
    String[] u1;
    static int i;
    static Boolean tf = true;
	/**********************************************************************************************

	Constructors
	
	**********************************************************************************************/

	/*****
	 * This is the default constructor
	 */
	public CalculatorValue() {
	}

	/*****
	 * This constructor creates a calculator value based on a long integer. For future calculators, it
	 * is best to avoid using this constructor.
	 */
	public CalculatorValue(double v, double e) {
		measuredValue = v;
		errorTerm = e;
	}
	

	/*****
	 * This copy constructor creates a duplicate of an already existing calculator value
	 */
	public CalculatorValue(CalculatorValue v) {
		measuredValue = v.measuredValue;
		errorMessage = v.errorMessage;
	}

	/*****
	 * This copy constructor creates a duplicate of an already existing calculator value
	 * @return 
	 */
	public double getErrorTerm() {
		return errorTerm;
	}
	
	/*****
	 * This copy constructor creates a duplicate of an already existing calculator value
	 * @return 
	 */
	public double getmeasuredValue() {
		return measuredValue;
	}
	
	
	/*****
	 * This constructor creates a calculator value from a string... Due to the nature
	 * of the input, there is a high probability that the input has errors, so the 
	 * routine returns the value with the error message value set to empty or the string 
	 * of an error message.
	 */
	public CalculatorValue(String s, String e) {
		measuredValue = 0.00;
		
		if (s.length() == 0) {							     	// If there is nothing there,
			errorMessage = "***Error*** Input is empty";		// signal an error	
			return;												
		}
		
														
		// If the first character is a plus sign, ignore it.
		int start = 0;										// Start at character position zero
		boolean negative = false;							// Assume the value is not negative
		 
		switch(s.charAt(start)) { 
		case 1: start++;                  //Switch case is used to check sign only once as break will stops
				negative =true;           // after one check and two consecutive signs will be considered as
				break;                    //an invalid input
		 }
		
		/*****
		 * Here a scanner is created for the digits, to see if the next token is a valid or not, here numbers starting 
		 * with "+" are invalid as they don't have any influence over the number, they remain as it is 
		 *
		 */
		// See if the user-entered string can be converted into an double value
		Scanner tempScanner = new Scanner(s.substring(start));// Create scanner for the digits
		
		if (!tempScanner.hasNextDouble()) {					// See if the next token is a valid
			errorMessage = "***Error*** Invalid value"; 		
			tempScanner.close();								
			return;												
		}
		
		// Convert the user-entered string to a integer value and see if something else is following it
		measuredValue = tempScanner.nextDouble();				// Convert the value and check to see
		
		if (tempScanner.hasNext()) {							// that there is nothing else is 
			errorMessage = "***Error*** Excess data"; 		// following the value.  If so, it
			tempScanner.close();								// is an error.  Therefore we must
			measuredValue = 0;								// return a zero value.
			return;													
		}
		
		if (s.charAt(0) == '+') {
			errorMessage = "***Error*** Invalid value"; 		
			tempScanner.close();								
			return;
			
		}
		tempScanner.close();
		
		
		errorMessage = "";
		if (negative)										// Return the proper value based
			measuredValue = -measuredValue;					// on the state of the flag that
		
		
		// Checking Error term
		Scanner tempScannerET = new Scanner(e);         // Create scanner for the digits
		
		if (!tempScannerET.hasNextDouble()) {					// See if the next token is a valid
			errorMessage = "***Error*** Invalid value"; 		
			tempScannerET.close();								
			return;												
		}
		
		// Convert the user-entered string to a integer value and see if something else is following it
		errorTerm = tempScannerET.nextDouble();				// Convert the value and check to see
		
		if (tempScannerET.hasNext()) {							// that there is nothing else is 
			errorMessage = "***Error*** Excess data"; 		// following the value.  If so, it
			tempScannerET.close();								// is an error.  Therefore we must
			errorTerm = 0;								// return a zero value.
			return;													
		}
		
		tempScannerET.close();
		
	}

	/**********************************************************************************************

	Getters and Setters
	
	**********************************************************************************************/
	
	/*****
	 * This is the start of the getters and setters
	 * 
	 * Get the error message
	 */
	public String getErrorMessage(){
		return errorMessage;
	}

	
	/*****
	 * Set the current value of a calculator value to a specific long integer
	 */
	public void setValue(double v){
		measuredValue = v;
	}
	
	/*****
	 * Set the current value of a calculator error message to a specific string
	 */
	public void setErrorMessage(String m){
		errorMessage = m;
	}
	
	/*****
	 * Set the current value of a calculator value to the value of another (copy)
	 */
	public void setValue(CalculatorValue v){
		measuredValue = v.measuredValue;
		errorMessage = v.errorMessage;
	}
		
	/**********************************************************************************************

	The toString() Methods
	
	**********************************************************************************************/
	
	public String toString() {
		return mValue.toStringDecimal();
	}
	
	
	public static String toStringErrTerm() {
		return eValue.toStringDecimal();
		
	}
	
	public String debugToString() {
		return "measuredValue = " + measuredValue + "\nerrorMessage = " + errorMessage + "\n";
	}
	
	public String debugToStringErrTerm() {
		return "errorTerm = " + errorTerm + "\n";
	}

	
	public void setIndexofUnits(int ndx1, int ndx2) {
		index_op1 = ndx1;
		index_op2 = ndx2;
	}
	
	
	public boolean setUnitcheck() {	
		return lookUpTableAddSub[index_op1][index_op2];
	}
			
	public static String unitOfResult() {
		return unit;
	}
	
	public void setUnitAddSub() {
		if(index_op1 < index_op2)
			unit = operand_units[index_op1];
		
		else
			unit = operand_units[index_op2];
	}
	
		
	/**********
	 * This private method matches the number of significant digits  in the new estimate and the previous 
	 * estimate, and see if the number of significant digits that were similar in the previous run are same 
	 * or not. If they are same then the further iterations are stopped and the last estimate  is declared 
	 * as the square root of the number, if they aren't same then one more iteration is done.This method 
	 * returns the number of significant digits that matches in new estimate and the previous estimate 
	 * 
	 * @param newGuess - This is the new estimate
	 * @param oldGuess - This is the previous estimate
	 * @param maxMatchingDigits - Number of same significant digits in the last iteration
	 * 
	 * @return maxMatchingDigits - Number of same significant digits in the current iteration
	 */

	public static int howManyDigitsMatch(UNumber newGuess, UNumber oldGuess, int maxMatchingDigits) {
	// If the characteristics are not the same, the digits in the mantissa do not matter
	if (newGuess.getCharacteristic() != oldGuess.getCharacteristic()) return 0;

	// The characteristic is the same, so fetch the mantissas so we can compare them
	String newGuessStr = newGuess.getMantissa();
	String oldGuessStr = oldGuess.getMantissa();
	// Set the upper limit;
	int maxIterations = maxMatchingDigits;

	/* No need to do this because we are working with the mantissa, so there are not decimal points
	for (int ndx = 0; ndx<15; ndx++) {
		if (newGuessStr.charAt(ndx) == '.') {
			String start = newGuessStr.substring(0, ndx);
			String rest = newGuessStr.substring(ndx+1);
			newGuessStr = start + rest;
			break;
		}
	}

	for (int ndx = 0; ndx<maxMatchingDigits; ndx++) 
		if (oldGuessStr.charAt(ndx) == '.') {
			String start = oldGuessStr.substring(0, ndx);
			String rest = oldGuessStr.substring(ndx+1);
			oldGuessStr = start + rest;
			break;
		}
	*/

	// Loop through the digits as long as they match
	for (int ndx = 0; ndx < maxIterations; ndx++) {
		if (oldGuessStr.charAt(ndx) != newGuessStr.charAt(ndx)) return ndx;
	}

	// If the loop completes, we consider all 15 to match
	return maxMatchingDigits;
	}
	/**********************************************************************************************

	The computation methods
	
	**********************************************************************************************/
	

	/*******************************************************************************************************
	 * The following methods implement computation on the calculator values.  These routines assume that the
	 * caller has verified that things are okay for the operation to take place.  These methods understand
	 * the technical details of the values and their reputations, hiding those details from the business 
	 * logic and user interface modules.
	 * 
	 * All methods uses the UNumber library for computation purposes. The addition, subtraction, multiplication 
	 * and division method uses the upper and lower bound technique for finding the measured value and the 
	 * error term and the square root uses the Newton's method for computation of square root.
	 */

	public void add(CalculatorValue v) {
		
		UNumber linkingValue = new UNumber();      
		linkingValue = new UNumber(linkingValue,25);
		
		UNumber mValue1 = new UNumber(measuredValue);      // Saving the value of operand 1 into an object
		mValue1 = new UNumber(mValue1,25);
		
		UNumber mValue2 = new UNumber(v.measuredValue);    // Saving the value of operand 2 into an object
		mValue2 = new UNumber(mValue2,25);
		
		UNumber eT1 = new UNumber(errorTerm);  // Saving the value of operand 1 error term into an object
		eT1 = new UNumber(eT1,25);
		
		UNumber eT2 = new UNumber(v.errorTerm);  // Saving the value of operand 1 error term into an object
		eT2 = new UNumber(eT2,25);
			
		if(index_op1 > index_op2) {
			linkingValue = new UNumber(converssionTable[index_op1][index_op2]); 
			mValue1.mpy(linkingValue);
			eT1.mpy(linkingValue);

		}

		else {
			linkingValue = new UNumber(converssionTable[index_op2][index_op1]); 
			mValue2.mpy(linkingValue);
			eT2.mpy(linkingValue);
		}
		
		UNumber temp1 = new UNumber(mValue1);    // Saving the value of operand 1 into an object
		temp1 = new UNumber(temp1,25);
		
		UNumber temp2 = new UNumber(mValue2);    // Saving the value of operand 1 into an object
		temp2 = new UNumber(temp2,25);
	
		// Finding the operand 1 range 
		mValue1.sub(eT1);       // Finding the lower bound of operand 1
		temp1.add(eT1);         // Finding the upper bound of operand 1
		
		// Finding the operand 2 range 
		mValue2.sub(eT2);       // Finding the lower bound of operand 2
		temp2.add(eT2);         // Finding the upper bound of operand 2
		
		// Finding the range
		mValue1.add(mValue2);   // adding lower bounds
		temp1.add(temp2);       // adding upper bounds
		
		UNumber temp3 = new UNumber(mValue1);   // Saving the lower bound 
		temp3 = new UNumber(temp3,25);          
		
		UNumber temp4 = new UNumber(temp1);     // Saving the upper bound 
		temp4 = new UNumber(temp4,25);
		
		// Finding average of the range
		mValue1.add(temp1);  mValue1.div(two);
		
		// Finding error term of the range
		temp4.sub(temp3);	temp4.div(two);
		
		mValue = new UNumber(mValue1);    // Saving the result 
		
		eValue = new UNumber(temp4);      // Saving the error term value
		
		errorMessage = "";
		
		setUnitAddSub();
	}


	/*****
	 * Algorithm for subtraction  
	 * @param v
	 */
	public void sub(CalculatorValue v) {
		
		UNumber linkingValue = new UNumber();      
		linkingValue = new UNumber(linkingValue,25);
		
		UNumber mValue1 = new UNumber(measuredValue);      // Saving the value of operand 1 into an object
		mValue1 = new UNumber(mValue1,25);
		
		UNumber mValue2 = new UNumber(v.measuredValue);    // Saving the value of operand 2 into an object
		mValue2 = new UNumber(mValue2,25);
		
		UNumber eT1 = new UNumber(errorTerm);  // Saving the value of operand 1 error term into an object
		eT1 = new UNumber(eT1,25);
		
		UNumber eT2 = new UNumber(v.errorTerm);  // Saving the value of operand 1 error term into an object
		eT2 = new UNumber(eT2,25);
		
		if(index_op1 > index_op2) {
			linkingValue = new UNumber(converssionTable[index_op1][index_op2]); 
			mValue1.mpy(linkingValue);
			eT1.mpy(linkingValue);

		}

		else {
			linkingValue = new UNumber(converssionTable[index_op2][index_op1]); 
			mValue2.mpy(linkingValue);
			eT2.mpy(linkingValue);
		}
		
		UNumber temp1 = new UNumber(mValue1);    // Saving the value of operand 1 into an object
		temp1 = new UNumber(temp1,25);
		
		UNumber temp2 = new UNumber(mValue2);    // Saving the value of operand 1 into an object
		temp2 = new UNumber(temp2,25);
		
		// Finding the operand 1 range 
		mValue1.sub(eT1);       // Finding the lower bound of operand 1
		temp1.add(eT1);         // Finding the upper bound of operand 1
		
		// Finding the operand 2 range 
		mValue2.sub(eT2);       // Finding the lower bound of operand 2
		temp2.add(eT2);         // Finding the upper bound of operand 2
		
		// Finding the range
		mValue1.sub(mValue2);   // subtracting lower bounds
		temp1.sub(temp2);       // subtracting upper bounds
		
		UNumber temp3 = new UNumber(mValue1);   // Saving the lower bound 
		temp3 = new UNumber(temp3,25);          
		
		UNumber temp4 = new UNumber(temp1);     // Saving the upper bound 
		temp4 = new UNumber(temp4,25);
		
		// Finding average of the range
		mValue1.add(temp1);  mValue1.div(two);
		
		// Finding error term of the range
		temp4.sub(temp3);	temp4.div(two);
		
		mValue = new UNumber(mValue1);    // Saving the result 
		
		eValue = new UNumber(temp4);      // Saving the error term value
		
		errorMessage = "";
				
		setUnitAddSub();
	}
	
	/*****
	 * Algorithm for multiplication  
	 * @param v
	 */

	public void mpy(CalculatorValue v) {
		UNumber linkingValue = new UNumber();      
		linkingValue = new UNumber(linkingValue,25);
		
		UNumber mValue1 = new UNumber(measuredValue);      // Saving the value of operand 1 into an object
		mValue1 = new UNumber(mValue1,25);
		
		UNumber mValue2 = new UNumber(v.measuredValue);    // Saving the value of operand 2 into an object
		mValue2 = new UNumber(mValue2,25);
		
		UNumber eT1 = new UNumber(errorTerm);  // Saving the value of operand 1 error term into an object
		eT1 = new UNumber(eT1,25);
		
		UNumber eT2 = new UNumber(v.errorTerm);  // Saving the value of operand 2 error term into an object
		eT2 = new UNumber(eT2,25);
		
		if(index_op1 > index_op2) {
			linkingValue = new UNumber(converssionTable[index_op1][index_op2]); 
			mValue1.mpy(linkingValue);
			eT1.mpy(linkingValue);

		}

		else {
			linkingValue = new UNumber(converssionTable[index_op2][index_op1]); 
			mValue2.mpy(linkingValue);
			eT2.mpy(linkingValue);
		}
		
		UNumber temp1 = new UNumber(mValue1);    // Saving the value of operand 1 into an object
		temp1 = new UNumber(temp1,25);
		
		UNumber temp2 = new UNumber(mValue2);    // Saving the value of operand 1 into an object
		temp2 = new UNumber(temp2,25);
		
		temp1.mpy(temp2);     // Finding the product of operand 1 and operand 2
		
		// Finding error term
		eT1.div(mValue1);     // Finding the error fraction of operand 1 
		
		eT2.div(mValue2);     // Finding the error fraction of operand 2 
		
		eT1.add(eT2);		  // Adding the error fractions
		
		eT1.mpy(temp1);       // Multiplying the error fraction with the product 
			
		mValue = new UNumber(temp1);    // Saving the product 
		
		eValue = new UNumber(eT1);      // Saving the error term value
		
		errorMessage = "";
				
		if(index_op1 != index_op2)
			if(index_op1 >1 || index_op2 >1)
				unit = operand_units[index_op1] + "." +operand_units[index_op2];
			else
				if(index_op1 >  index_op2)
					unit = operand_units[index_op2] + "\u00B2";
				else 
					unit = operand_units[index_op1] + "\u00B2";
		
		else 
			unit = operand_units[index_op1] + "\u00B2";
		
	}
	
	/*****
	 * Algorithm for division  
	 * @param v
	 */

	public void div(CalculatorValue v) {
		UNumber linkingValue = new UNumber();      
		linkingValue = new UNumber(linkingValue,25);
		
		UNumber mValue1 = new UNumber(measuredValue);      // Saving the value of operand 1 into an object
		mValue1 = new UNumber(mValue1,25);
		
		UNumber mValue2 = new UNumber(v.measuredValue);    // Saving the value of operand 2 into an object
		mValue2 = new UNumber(mValue2,25);
		
		UNumber eT1 = new UNumber(errorTerm);  // Saving the value of operand 1 error term into an object
		eT1 = new UNumber(eT1,25);
		
		UNumber eT2 = new UNumber(v.errorTerm);  // Saving the value of operand 1 error term into an object
		eT2 = new UNumber(eT2,25);
		

		if(index_op1 > index_op2) {
			linkingValue = new UNumber(converssionTable[index_op1][index_op2]); 
			mValue1.mpy(linkingValue);
			eT1.mpy(linkingValue);

		}

		else {
			linkingValue = new UNumber(converssionTable[index_op2][index_op1]); 
			mValue2.mpy(linkingValue);
			eT2.mpy(linkingValue);
		}
	
		UNumber temp1 = new UNumber(mValue1);    // Saving the value of operand 1 into an object
		temp1 = new UNumber(temp1,25);
		
		UNumber temp2 = new UNumber(mValue2);    // Saving the value of operand 1 into an object
		temp2 = new UNumber(temp2,25);
		
		temp1.div(temp2);     // Findi Quotient of operand 1 and operand 2
		
		// Finding error term
		eT1.div(mValue1);     // Finding the error fraction of operand 1 
		
		eT2.div(mValue2);     // Finding the error fraction of operand 2 
		
		eT1.add(eT2);		  // Adding the error fractions
		
		eT1.mpy(temp1);       // Multiplting the error fraction with the product 
			
		mValue = new UNumber(temp1);    // Saving the product 
		
		eValue = new UNumber(eT1);      // Saving the error term value
		
		errorMessage = "";	
		
		if(index_op1 != index_op2)
			if(index_op1 >1 || index_op2 >1)
				if(index_op1 == 6)
					unit = operand_units[index_op2] + "\u207B" + "\u00B9";
				else if(index_op2 == 6)
					unit =  operand_units[index_op1];
				else
					unit = operand_units[index_op1] +"/" + operand_units[index_op2];
				
			else
				if(index_op1 >  index_op2)
					unit = "No unit";
				else 
					unit = "No unit";
		
		else 
			unit = "No unit";
		
	}
	
	/*****
	 * For square root only one operand is required, thus operation is only performed over 
	 * measuredValue and operand1 error term only
	 * 
	 * @param v
	 */
	public void sqrt(CalculatorValue v){    
		
		int digitsMatch = 0;									// This used to hold the number of matching significant digits

		int numSigDigits = 25;									// This is for computing results using UNumber values

		UNumber half = new UNumber(1.5);				// This is the constant 2.0
		half = new UNumber(half, numSigDigits  );
		
		UNumber newGuess = new UNumber(measuredValue);	// Compute the the first estimate
		newGuess = new UNumber(newGuess, numSigDigits  );
		
		UNumber mValue1 = new UNumber(measuredValue);      // Saving the value of operand into an object
		mValue1 = new UNumber(mValue1,25);
		
		UNumber eT = new UNumber(errorTerm);
		eT = new UNumber(eT,25);
		eT.div(newGuess);
		
		newGuess.div(two);							
			// Compute the the first estimate
	
		UNumber oldGuess;								    // Temporary value for determining when to terminate the loop
		
		UNumber temp;                                       // Declaring an object to temporary store the value of the input number       
		
		digitsMatch = 0;									// This used to hold the number of matching significant digits
					
		do {		
			oldGuess = newGuess;                             // Save the old guess
			
			temp = new UNumber(measuredValue);	              // Stores the value of the input because on performing
			temp = new UNumber(temp, numSigDigits );          // direct function it will change the original value
			                                                
			                                                             
			//Computing new guess
			
			// Dividing the the input with old guess value
			temp.div(oldGuess);                             
			
			// Adding the old guess value to the result
			temp.add(oldGuess);
			
			// Dividing whole answer with 2
			temp.div(two);
			
			// Transferring the result value into the new guess
			newGuess = temp;
			
			digitsMatch = howManyDigitsMatch(newGuess, oldGuess, numSigDigits);	// Determine how many digits match
						
		} while (digitsMatch < numSigDigits);			// Determine if the old and the new guesses are "close enough"
  		
		mValue = new UNumber(newGuess);     // Saving the square root of the number
		
		// Finding error term
		eT.div(mValue1);         // Finding the error fraction
		
		eT.mpy(half);            // Multiplying the error fracction with the power (1/2 or 0.5)
		
		eT.mpy(mValue);          // Multiplying the error fraction with the square root
		
		eValue = new UNumber(eT);   // Saving the error term value
		
		errorMessage = ""; 
		
		unit = "\u221A" + operand_units[index_op1];
		
	}
	private static String displayInput(String input, int currentCharNdx) {
		// Display the entire input line
		String result = input + "\n";

		// Display a line with enough spaces so the up arrow point to the point of an error
		for (int ndx=0; ndx < currentCharNdx; ndx++) result += " ";

		// Add the up arrow to the end of the second line
		return result + "\u21EB";				// A Unicode up arrow with a base
	}

	public void getTableSize(int p, String[] name, String[] corv, String[] mv, String[] ev, String[] u){
	
		i = p;
		name1 = new String[i];
		corv1 = new String[i];
		mv1 = new String[i];
		ev1 = new String[i];
		u1 = new String[i];
		
		name1 = name;
		corv1 = corv;
		mv1 = mv;
		ev1 = ev;
		u1 = u;
	}

	private static void moveToNextCharacter() {
		currentCharNdx++;
		if (currentCharNdx < inputLine.length())
			currentChar = inputLine.charAt(currentCharNdx);
		else {
			currentChar = ' ';
			running = false;
		}
	}

	/**********
	 * This method is a mechanical transformation of a Finite State Machine diagram into a Java
	 * method.
	 * 
	 * @param input		The input string for the Finite State Machine
	 * @return			An output string that is empty if every things is okay or it will be
	 * 						a string with a help description of the error follow by two lines
	 * 						that shows the input line follow by a line with an up arrow at the
	 *						point where the error was found.
	 */
	public static String checkMeasureValue(String input) {
		if(input.length() <= 0) return "";
		// The following are the local va2riable used to perform the Finite State Machine simulation
		state = 0;							// This is the FSM state number
		inputLine = input;					// Save the reference to the input line as a global
		currentCharNdx = 0;					// The index of the current character
		currentChar = input.charAt(0);		// The current character from the above indexed position

		// The Finite State Machines continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state

		measuredValueInput = input;			// Set up the alternate result copy of the input
		running = true;						// Start the loop
		

		// The Finite State Machines continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state
		while (running) {
			// The switch statement takes the execution to the code for the current state, where
			// that code sees whether or not the current character is valid to transition to a
			// next state
			switch (state) {
			case 0: 
				// State 0 has three valid transitions.  Each is addressed by an if statement.
				
				// This is not a final state
				finalState = false;
				
				// If the current character is in the range from 1 to 9, it transitions to state 1
				if (currentChar >= '0' && currentChar <= '9') {
					nextState = 1;
					break;
				}
				// If the current character is a decimal point, it transitions to state 3
				else if (currentChar == '.') {
					nextState = 3;
					break;					
				}
				
				else if(currentChar =='-') {
					nextState = 0;
					break;
				}
				
				else if(currentChar >= 'a' && currentChar <= 'z' || currentChar >= 'A' && currentChar <= 'Z') {
					nextState = 8;
					break;
				}
				
				// If it is none of those characters, the FSM halts
				else 
					running = false;
				
				// The execution of this state is finished
				break;
			
			case 1: 
				// State 1 has three valid transitions.  Each is addressed by an if statement.
				
				// This is a final state
				finalState = true;
				
				// In state 1, if the character is 0 through 9, it is accepted and we stay in this
				// state
				if (currentChar >= '0' && currentChar <= '9') {
					nextState = 1;
					break;
				}
				
				// If the current character is a decimal point, it transitions to state 2
				else if (currentChar == '.') {
					nextState = 2;
					break;
				}
				// If the current character is an E or an e, it transitions to state 5
				else if (currentChar == 'E' || currentChar == 'e') {
					nextState = 5;
					break;
				}
				// If it is none of those characters, the FSM halts
				else
					running = false;
				
				// The execution of this state is finished
				break;			
				
			case 2: 
				// State 2 has two valid transitions.  Each is addressed by an if statement.
				
				// This is a final state
				finalState = true;
				
				// If the current character is in the range from 1 to 9, it transitions to state 1
				if (currentChar >= '0' && currentChar <= '9') {
					nextState = 2;
					break;
				}
				// If the current character is an 'E' or 'e", it transitions to state 5
				else if (currentChar == 'E' || currentChar == 'e') {
					nextState = 5;
					break;
				}

				// If it is none of those characters, the FSM halts
				else 
					running = false;

				// The execution of this state is finished
				break;
	
			case 3:
				// State 3 has only one valid transition.  It is addressed by an if statement.
				
				// This is not a final state
				finalState = false;
				
				// If the current character is in the range from 1 to 9, it transitions to state 1
				if (currentChar >= '0' && currentChar <= '9') {
					nextState = 4;
					break;
				}

				// If it is none of those characters, the FSM halts
				else 
					running = false;

				// The execution of this state is finished
				break;

			case 4: 
				// State 4 has two valid transitions.  Each is addressed by an if statement.
				
				// This is a final state
				finalState = true;
				
				// If the current character is in the range from 1 to 9, it transitions to state 4
				if (currentChar >= '0' && currentChar <= '9') {
					nextState = 4;
					break;
				}
				// If the current character is an 'E' or 'e", it transitions to state 5
				else if (currentChar == 'E' || currentChar == 'e') {
					nextState = 5;
					break;
				}

				// If it is none of those characters, the FSM halts
				else 
					running = false;

				// The execution of this state is finished
				break;

			case 5: 
                 //State 5 has two valid transitions.  Each is addressed by an if statement.
				
				
				finalState = false;
				
				// If the current character is in the range from 1 to 9, it transitions to state 4
				if (currentChar >= '0' && currentChar <= '9') {
					nextState = 7;
					break;
				}
				// If the current character is an 'E' or 'e", it transitions to state 5
				else if (currentChar == '+' || currentChar == '-') {
					nextState = 6;
					break;
				}

				// If it is none of those characters, the FSM halts
				else 
					running = false;

				// The execution of this state is finished
				break;

			case 6: 
                 //State 6 has one valid transitions.  It is addressed by an if statement.
				
				
				finalState = false;
				
				// If the current character is in the range from 1 to 9, it transitions to state 4
				if (currentChar >= '0' && currentChar <= '9') {
					nextState = 7;
					break;
				}
				
				// If it is none of those characters, the FSM halts
				else 
					running = false;

				// The execution of this state is finished
				break;

			case 7: 
                  //State 7 has one valid transitions.  It is addressed by an if statement.
				
				// This is a final state
				finalState = true;
				
				// If the current character is in the range from 1 to 9, it transitions to state 4
				if (currentChar >= '0' && currentChar <= '9') {
					nextState = 7;
					break;
				}
				 
				// If it is none of those characters, the FSM halts
				else 
					running = false;

				// The execution of this state is finished
				break;
				
			case 8: 
                //State 8 has one valid transitions.  It is addressed by an if statement.
				
				// This is a final state
				finalState = true;
			
				for(int e = 0; e<i; e++) {
					String textfc1 = UserInterface.text_Operand1.getText();
					String textfc2 = UserInterface.text_Operand2.getText();
				String array = name1[e];
					if(name1[e] != null) {
				
				
						if(textfc1.equals(array) || textfc2.equals(array)) {
						tf = false;
							System.out.println("Word found");
							measuredValueErrorMessage = " ";
							return measuredValueErrorMessage + displayInput(input, currentCharNdx);
						}
						}
					}
					if (tf==false) {
					tf = true;
					nextState = 8;
					break;
				}
				 
				// If it is none of those characters, the FSM halts
				else 
					running = false;

				// The execution of this state is finished
				break;
				
			}
			
			
			
			if (running) {
				
				// When the processing of a state has finished, the FSM proceeds to the next character
				// in the input and if there is one, it fetches that character and updates the 
				// currentChar.  If there is no next character the currentChar is set to a blank.
				moveToNextCharacter();
				
				// Move to the next state
				state = nextState;

			}
			// Should the FSM get here, the loop starts again

		}

		measuredValueIndexofError = currentCharNdx;		// Copy the index of the current character;
		
		// When the FSM halts, we must determine if the situation is an error or not.  That depends
		// of the current state of the FSM and whether or not the whole string has been consumed.
		// This switch directs the execution to separate code for each of the FSM states.
		switch (state) {
		case 0:
			// State 0 is not a final state, so we can return a very specific error message
			measuredValueIndexofError = currentCharNdx;		// Copy the index of the current character;
			measuredValueErrorMessage = "The first character must be a digit or a decimal point.";
			return "The first character must be a \"+\" sign, digit a decimal point.";

		case 1:
			// State 1 is a final state, so we must see if the whole string has been consumed.
			if (currentCharNdx<input.length()) {
				// If not all of the string has been consumed, we point to the current character
				// in the input line and specify what that character must be in order to move
				// forward.
				measuredValueErrorMessage = "This character may only be an \"E\", an \"e\", a digit, "
						+ "a \".\", or it must be the end of the input.\n";
				return measuredValueErrorMessage + displayInput(input, currentCharNdx);
			}
			else {
				measuredValueIndexofError = -1;
				measuredValueErrorMessage = "";
				return measuredValueErrorMessage;
			}

		case 2:
		case 4:
			// States 2 and 4 are the same.  They are both final states with only one possible
			// transition forward, if the next character is an E or an e.
			if (currentCharNdx<input.length()) {
				measuredValueErrorMessage = "This character may only be an \"E\", an \"e\", a digit or it must"
						+ " be the end of the input.\n";
				return measuredValueErrorMessage + displayInput(input, currentCharNdx);
			}
			// If there is no more input, the input was recognized.
			else {
				measuredValueIndexofError = -1;
				measuredValueErrorMessage = "";
				return measuredValueErrorMessage;
			}
		case 3:
		case 6:
			// States 3, and 6 are the same. None of them are final states and in order to
			// move forward, the next character must be a digit.
			measuredValueErrorMessage = "This character may only be a digit.\n";
			return measuredValueErrorMessage + displayInput(input, currentCharNdx);

		case 8:
			// States 8 is the state where we check if the word in the textfield matches any entry in the definition
			// list. if it matches the definitions in the list, then it will continue otherwise it will display an error
			
			if (currentCharNdx<input.length()) {
				measuredValueErrorMessage = "This word doesn't exist in the list.\n";
				return measuredValueErrorMessage + displayInput(input, currentCharNdx);
				}
				else {
				measuredValueIndexofError = -1;
				measuredValueErrorMessage = "";
				return measuredValueErrorMessage;
						}
						
		case 7:
			// States 7 is similar to states 3 and 6, but it is a final state, so it must be
			// processed differently. If the next character is not a digit, the FSM stops with an
			// error.  We must see here if there are no more characters. If there are no more
			// characters, we accept the input, otherwise we return an error
			if (currentCharNdx<input.length()) {
				measuredValueErrorMessage = "This character may only be a digit.\n";
				return measuredValueErrorMessage + displayInput(input, currentCharNdx);
			}
			else {
				measuredValueIndexofError = -1;
				measuredValueErrorMessage = "";
				return measuredValueErrorMessage;
			}

		case 5:
			// State 5 is not a final state.  In order to move forward, the next character must be
			// a digit or a plus or a minus character.
			measuredValueErrorMessage = "This character may only be a digit, a plus, or minus "
					+ "character.\n";
			return measuredValueErrorMessage + displayInput(input, currentCharNdx);
			
					
		default:
			return "";
		}
		
	}
	
	public static String errorTermErrorMessage = "Error Term recognition has not been implements";
	public static String errorTermInput = "";			// The input being processed
	public static int errorTermIndexofError = -1;		// The index where the error was located
	private static int errorTermState = 0;
	private static int errorTermCurrentCharNdx;
	private static char errorTermCurrentChar;
	public static String errorTermInputLine = "";
	public static boolean errorTermRunning;
	public static boolean errorTermFinalState = false;
	private static int errorTermNextState = 0;

	private static String errorTermDisplayInput(String input, int errorTermCurrentCharNdx) {
		// Display the entire input line
		String result = input + "\n";

		// Display a line with enough spaces so the up arrow point to the point of an error
		for (int ndx=0; ndx < errorTermCurrentCharNdx; ndx++) result += " ";

		// Add the up arrow to the end of the second line
		return result + "\u21EB";				// A Unicode up arrow with a base
	}

	private static void errorTermmoveToNextCharacter() {
		errorTermCurrentCharNdx++;
		if (errorTermCurrentCharNdx < errorTermInputLine.length())
			errorTermCurrentChar = errorTermInputLine.charAt(errorTermCurrentCharNdx);
		else {
			errorTermCurrentChar = ' ';
			errorTermRunning = false;
		}
	}

	/**********
	 * This method is a mechanical transformation of a Finite State Machine diagram into a Java
	 * method.
	 * 
	 * @param input		The input string for the Finite State Machine
	 * @return			An output string that is empty if every things is okay or it will be
	 * 						a string with a help description of the error follow by two lines
	 * 						that shows the input line follow by a line with an up arrow at the
	 *						point where the error was found.
	 */
	public static String checkErrorTerm(String input) {
		if(input.length() <= 0) return "";
		// The following are the local variable used to perform the Finite State Machine simulation
		errorTermState = 0;							// This is the FSM state number
		errorTermInputLine = input;					// Save the reference to the input line as a global
		errorTermCurrentCharNdx = 0;					// The index of the current character
		errorTermCurrentChar = input.charAt(0);		// The current character from the above indexed position

		// The Finite State Machines continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state

		errorTermInput = input;			// Set up the alternate result copy of the input
		errorTermRunning = true;						// Start the loop

		// The Finite State Machines continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state
		while (errorTermRunning) {
			// The switch statement takes the execution to the code for the current state, where
			// that code sees whether or not the current character is valid to transition to a
			// next state
			switch (errorTermState) {
			case 0: 
				// State 0 has three valid transitions.  Each is addressed by an if statement.
				
				// This is not a final state
				errorTermFinalState = false;
				
				// If the current character is in the range from 1 to 9, it transitions to state 1
				if (errorTermCurrentChar >= '1' && errorTermCurrentChar <= '9') {
					errorTermNextState = 1;
					break;
				}
				// If the current character is a decimal point, it transitions to state 3
				else if (errorTermCurrentChar == '.') {
					errorTermNextState = 3;
					break;					
				}
				
				else if (errorTermCurrentChar == '0') {
					errorTermNextState = 8;
					break;
				}
				
				if (errorTermCurrentChar >= 'a' && errorTermCurrentChar <= 'z' || errorTermCurrentChar >= 'A' && errorTermCurrentChar <= 'Z') {
					errorTermNextState = 10;
					break;
				}
				// If it is none of those characters, the FSM halts
				else 
					errorTermRunning = false;
				
				// The execution of this state is finished
				break;
			
			case 1: 
				// State 1 has three valid transitions.  Each is addressed by an if statement.
				
				// This is a final state
				errorTermFinalState = true;
				
				// In state 1, if the character is 0 through 9, it is accepted and we stay in this
				// state
				if (errorTermCurrentChar == '0') {
					errorTermNextState = 1;
					break;
				}
				
				// If the current character is a decimal point, it transitions to state 2
				else if (errorTermCurrentChar == '.') {
					errorTermNextState = 2;
					break;
				}
				// If the current character is an E or an e, it transitions to state 5
				else if (errorTermCurrentChar == 'E' || errorTermCurrentChar == 'e') {
					errorTermNextState = 5;
					break;
				}
				// If it is none of those characters, the FSM halts
				else
					errorTermRunning = false;
				
				// The execution of this state is finished
				break;			
				
			case 2: 
				// State 2 has two valid transitions.  Each is addressed by an if statement.
				
				// This is a final state
				errorTermFinalState = true;
				
				// If the current character is in the range from 1 to 9, it transitions to state 1
				if (errorTermCurrentChar == 'E' || errorTermCurrentChar == 'e') {
					errorTermNextState = 5;
					break;
				}
				
				// If it is none of those characters, the FSM halts
				else 
					errorTermRunning = false;

				// The execution of this state is finished
				break;
	
			case 3:
				// State 3 has only one valid transition.  It is addressed by an if statement.
				
				// This is not a final state
				errorTermFinalState = false;
				
				// If the current character is in the range from 1 to 9, it transitions to state 1
				if (errorTermCurrentChar >= '1' && errorTermCurrentChar <= '9') {
					errorTermNextState = 4;
					break;
				}

				else if (errorTermCurrentChar == '0') {
					errorTermNextState = 3;
					break;
					
				}
				// If it is none of those characters, the FSM halts
				else 
					errorTermRunning = false;

				// The execution of this state is finished
				break;

			case 4: 
				// State 4 has one valid transitions.  Each is addressed by an if statement.
				
				// This is a final state
				errorTermFinalState = true;
				
				
				if (errorTermCurrentChar == 'E' || errorTermCurrentChar == 'e') {
					errorTermNextState = 5;
					break;
				}

				// If it is none of those characters, the FSM halts
				else 
					errorTermRunning = false;

				// The execution of this state is finished
				break;

			case 5:
                // State 5 has two valid transition.  These are addressed by an if statement.
				
				// This is not a final state
				errorTermFinalState = false;
				
				if(errorTermCurrentChar =='+' || errorTermCurrentChar =='-') {
					errorTermNextState = 6;
					break;
				}
				
				else if (errorTermCurrentChar >= '0' && errorTermCurrentChar <= '9') {
					errorTermNextState = 7;
					break;
				}
				
				// If it is none of those characters, the FSM halts
				else 
					errorTermRunning = false;

				// The execution of this state is finished
				break;

			case 6: 
                // State 6 has only one valid transition.  It is addressed by an if statement.
				
				// This is not a final state				
				errorTermFinalState = false;
				
				if (errorTermCurrentChar >= '0' && errorTermCurrentChar <= '9') {
					errorTermNextState = 7;
					break;
				}
				
				// If it is none of those characters, the FSM halts
				else 
					errorTermRunning = false;

				// The execution of this state is finished
				break;

			case 7: 
                // State 7 has only one valid transition.  It is addressed by an if statement.
				
				// This is not a final state				
				errorTermFinalState = true;
				
				if (errorTermCurrentChar >= '0' && errorTermCurrentChar <= '9') {
					errorTermNextState = 7;
					break;
				}
				
				// If it is none of those characters, the FSM halts
				else 
					errorTermRunning = false;

				// The execution of this state is finished
				break;
			
			case 8:
                // State 8 has only one valid transition.  It is addressed by an if statement.
				
				// This is not a final state				
				errorTermFinalState = false;
				if (errorTermCurrentChar == '.') {
					errorTermNextState = 9;
					break;
				}
				
				// If it is none of those characters, the FSM halts
				else 
					errorTermRunning = false;

				// The execution of this state is finished
				break;
			
			
			case 9:
                // State 9 has only one valid transition.  It is addressed by an if statement.
				
				// This is not a final state
				errorTermFinalState = false;
				if (errorTermCurrentChar == '0') {
					errorTermNextState = 9;
					break;
				}
				
				else if (errorTermCurrentChar >= '1' && errorTermCurrentChar <= '9') {
					errorTermNextState = 4;
					break;
				}
				
				// If it is none of those characters, the FSM halts
				else 
					errorTermRunning = false;

				// The execution of this state is finished
				break;
				
			case 10:
                // State 9 has only one valid transition.  It is addressed by an if statement.
				
				// This is not a final state
				errorTermFinalState = true;
				if (errorTermCurrentChar >= 'a' && errorTermCurrentChar <= 'z' || errorTermCurrentChar >= 'A' && errorTermCurrentChar <= 'Z' ||  errorTermCurrentChar >= '0' && errorTermCurrentChar <= '9') {
					errorTermNextState = 10;
					break;
				}
				
				// If it is none of those characters, the FSM halts
				else 
					errorTermRunning = false;

				// The execution of this state is finished
				break;
				
			}	
			
			if (errorTermRunning) {
				// When the processing of a state has finished, the FSM proceeds to the next character
				// in the input and if there is one, it fetches that character and updates the 
				// errorTermCurrentChar.  If there is no next character the errorTermCurrentChar is set to a blank.
				errorTermmoveToNextCharacter();
				
				// Move to the next state
				errorTermState = errorTermNextState;

			}
			// Should the FSM get here, the loop starts again

		}

		errorTermIndexofError = errorTermCurrentCharNdx;		// Copy the index of the current character;
		
		// When the FSM halts, we must determine if the situation is an error or not.  That depends
		// of the current state of the FSM and whether or not the whole string has been consumed.
		// This switch directs the execution to separate code for each of the FSM states.
		switch (errorTermState) {
		case 0:
			// State 0 is not a final state, so we can return a very specific error message
			errorTermIndexofError = errorTermCurrentCharNdx;		// Copy the index of the current character;
			errorTermErrorMessage = "The first character must be a digit or a decimal point.";
			return "The first character must be a digit or a decimal point.";

		case 1:
			// State 1 is a final state, so we must see if the whole string has been consumed.
			if (errorTermCurrentCharNdx<input.length()) {
				// If not all of the string has been consumed, we point to the current character
				// in the input line and specify what that character must be in order to move
				// forward.
				errorTermErrorMessage = "This character may only be an \"E\", an \"e\", a digit, "
						+ "a \".\", or it must be the end of the input.\n";
				return errorTermErrorMessage + errorTermDisplayInput(input, errorTermCurrentCharNdx);
			}
			else {
				errorTermIndexofError = -1;
				errorTermErrorMessage = "";
				return errorTermErrorMessage;
			}

		case 2:
		case 4:
			// States 2 and 4 are the same.  They are both final states with only one possible
			// transition forward, if the next character is an E or an e.
			if (errorTermCurrentCharNdx<input.length()) {
				errorTermErrorMessage = "This character may only be an \"E\", an \"e\", or it must"
						+ " be the end of the input.\n";
				return errorTermErrorMessage + errorTermDisplayInput(input, errorTermCurrentCharNdx);
			}
			// If there is no more input, the input was recognized.
			else {
				errorTermIndexofError = -1;
				errorTermErrorMessage = "";
				return errorTermErrorMessage;
			}
		case 6:
			// State 6. None of them are final states and in order to
			// move forward, the next character must be a digit.
			errorTermErrorMessage = "This character may only be a digit.\n";
			return errorTermErrorMessage + errorTermDisplayInput(input, errorTermCurrentCharNdx);

		case 3:
		case 9:
		case 7:
			// States 3, 7 and 9 has similar valid transitions, but 7 is a final state, so it must be
			// processed differently. If the next character is not a digit, the FSM stops with an
			// error.  We must see here if there are no more characters. If there are no more
			// characters, we accept the input, otherwise we return an error
			if (errorTermCurrentCharNdx<input.length()) {
				errorTermErrorMessage = "This character may only be a digit.\n";
				return errorTermErrorMessage + errorTermDisplayInput(input, errorTermCurrentCharNdx);
			}
			else {
				errorTermIndexofError = -1;
				errorTermErrorMessage = "";
				return errorTermErrorMessage;
			}
			
		case 5:
			// State 5 is not a final state.  In order to move forward, the next character must be
			// a digit or a plus or a minus character.
			errorTermErrorMessage = "This character may only be a digit, a plus, or minus "
					+ "character.\n";
			return errorTermErrorMessage + errorTermDisplayInput(input, errorTermCurrentCharNdx);
		
		case 8:
			// States 8 is not a final states with only one possible
			  errorTermErrorMessage = "This character can only be \".\" \n";
			  return errorTermErrorMessage + errorTermDisplayInput(input, errorTermCurrentCharNdx);
			
		case 10:
			// State 1 is a final state, so we must see if the whole string has been consumed.
			if (errorTermCurrentCharNdx<input.length()) {
				// If not all of the string has been consumed, we point to the current character
				// in the input line and specify what that character must be in order to move
				// forward.
				errorTermErrorMessage = "This is invalid\n";
				return errorTermErrorMessage + errorTermDisplayInput(input, errorTermCurrentCharNdx);
			}
			else {
				errorTermIndexofError = -1;
				errorTermErrorMessage = "";
				return errorTermErrorMessage;
			}
		default:
			return "";
		}
	}
	
}
