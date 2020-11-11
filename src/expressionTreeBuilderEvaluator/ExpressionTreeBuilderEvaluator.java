package expressionTreeBuilderEvaluator;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;

import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import lexer.Lexer;
import lexer.Token;
import lexer.Token.Kind;
import programmableCalculator.CalculatorValue;
import programmableCalculator.DefinitionUserInterface;
import programmableCalculator.ProgramUI;

public class ExpressionTreeBuilderEvaluator {
	/**
	 * <p>
	 * Title: ExpressionTreeBuilderEvaluator - A demonstration of how to build and evaluate a tree
	 * </p>
	 *
	 * <p>
	 * Description: A controller object class that implements an expression parser, tree builder,
	 * and tree evaluator functions
	 * </p>
	 *
	 * <p>
	 * Copyright: Copyright © 2019
	 * </p>
	 *
	 * @author Lynn Robert Carter, Puneet Garg
	 * @version 1.00	Baseline version 2019-03-10
	 * 
	 * @version 4.90    2019-03-31 Project 1 - Part 2 with a expression window to compute the expressions with constants and variables.
	 * 
	 * @version 5.00    2019-05-05 Project 1 - Part 3 with a expression window to print and input the statement from the console 
	 *                                         and also use them to compute the results
	 * 
	 */
	
	/**********************************************************************************************
	 * 
	 * The ExpressionTreeBuilderEvaluator Class provides the functions that parses an input stream
	 * of lexical tokens, uses the structure to build an expression tree, and then uses that tree
	 * to compute the value of the expression.
	 * 
	 * The following are the primary followed by the secondary attributes for this Class
	 */

	
	private static Lexer lexer;
	
	private static Token current;
	private static Token next;
	
	// The following are the stacks that are used to transform the parse output into a tree
	private static Stack<ExprNode> exprStack = new Stack<>();
	private static Stack<Token> opStack = new Stack<>();
	
	private static CalculatorValue operand1 = new CalculatorValue(0, 0);
	private static CalculatorValue operand2 = new CalculatorValue(0, 0);
	private static CalculatorValue result1 = new CalculatorValue(0, 0);
	
	static String[] name = new String[100];
	static double[] value = new double[100];
	static int number=0;

    static Label varError = new Label("Please Enter file name");
    public static DefinitionUserInterface def = new DefinitionUserInterface();
    
	/**********
	 * The addSub Expression method parses a sequence of expression elements that are added
	 * together, subtracted from one another, or a blend of them.
	 * 
	 * @return	The method returns a boolean value indicating if the parse was successful
	 */
	
	public static boolean brackets() {
		

		// The method assumes the input is a sequence of terms separated by multiplication and/or 
		// division operators
		if (term() || term2()) {
			
			// Once an multiplication/division element has been found, it can be followed by a
			// sequence of multiplication or division operators followed by another 
			// multiplication/division element.  Therefore we start by looking for a "*" or a "/"
			while ((current.getTokenKind() == Kind.SYMBOL) && 
					((current.getTokenCode() == 4) 	// The "*" operator	
					 )) {		// The "/" operator
				
				// When you find a "*" or a "/", push it onto the operator stack
				opStack.push(current);
				
				// Advance to the next input token
				current = next;
				next = lexer.accept();
				
				// Look for the next multiplication/division element
				if (term() || term2()) {
					
					// If one is found, pop the two operands and the operator
					ExprNode expr2 = exprStack.pop();
					ExprNode expr1 = exprStack.pop();
					Token oper = opStack.pop();
					
					// Create an Expression Tree node from those three items and push it onto
					// the expression stack
					exprStack.push(new ExprNode(oper, true, expr1, expr2));
				}
				else {
					
					// If we get here, we saw a "*" or a "/", but it was not followed by a valid
					// multiplication/division element
					System.out.println("Parse error: a term was not found");
					return false;
				}
			}
			
			// Reaching this point indicates that we have processed the sequence of 
			// additive/subtractive elements
			return true;
		}
		else
			
			// This indicates that the first thing found was not a multiplication/division element
			return false;

}
	
	/**********
	 * The addSub Expression method parses a sequence of expression elements that are added
	 * together, subtracted from one another, or a blend of them.
	 * 
	 * @return	The method returns a boolean value indicating if the parse was successful
	 */
	public static boolean addSubExpr() {

		// The method assumes the input is a sequence of additive/subtractive elements separated
		// by addition and/or subtraction operators
		if (mpyDivExpr()) {
			
			// Once an additive/subtractive element has been found, it can be followed by a
			// sequence of addition or subtraction operators followed by another 
			// additive/subtractive element.  Therefore we start by looking for a "+" or a "-"
			while ((current.getTokenKind() == Kind.SYMBOL) && 
					((current.getTokenCode() == 6) ||		// The "+" operator
					 (current.getTokenCode() == 7))) {		// The "-" operator
				
				// When you find a "+" or a "-", push it onto the operator stack
				opStack.push(current);
				
				// Advance to the next input token
				current = next;
				next = lexer.accept();
				
				// Look for the next additive/subtractive element
				if (mpyDivExpr()) {
					
					// If one is found, pop the two operands and the operator
					ExprNode expr2 = exprStack.pop();
					ExprNode expr1 = exprStack.pop();
					Token oper = opStack.pop(); 
					
					// Create an Expression Tree node from those three items and push it onto
					// the expression stack
					exprStack.push(new ExprNode(oper, true, expr1, expr2));
				}
				else {
					
					// If we get here, we saw a "+" or a "-", but it was not followed by a valid
					// additive/subtractive element
					System.out.println("Parse error: A required additive/subtractive element was not found");
					return false;
				}
			}
			
			// Reaching this point indicates that we have processed the sequence of 
			// additive/subtractive elements
			return true;
		}
		else
			
			// This indicates that the first thing found was not an additive/subtractive element
			return false;
	}
	
	/**********
	 * The mpyDiv Expression method parses a sequence of expression elements that are multiplied
	 * together, divided from one another, or a blend of them.
	 * 
	 * @return	The method returns a boolean value indicating if the parse was successful
	 */
	private static boolean mpyDivExpr() {

		// The method assumes the input is a sequence of terms separated by multiplication and/or 
		// division operators
		if (brackets()) {
			
			// Once an multiplication/division element has been found, it can be followed by a
			// sequence of multiplication or division operators followed by another 
			// multiplication/division element.  Therefore we start by looking for a "*" or a "/"
			while ((current.getTokenKind() == Kind.SYMBOL) && 
					((current.getTokenCode() == 8) ||		// The "*" operator	
					 (current.getTokenCode() == 9))) {		// The "/" operator
				
				// When you find a "*" or a "/", push it onto the operator stack
				opStack.push(current);
				
				// Advance to the next input token
				current = next;
				next = lexer.accept();
				
				// Look for the next multiplication/division element
				if (brackets()) {
					
					// If one is found, pop the two operands and the operator
					ExprNode expr2 = exprStack.pop();
					ExprNode expr1 = exprStack.pop();
					Token oper = opStack.pop();
					
					// Create an Expression Tree node from those three items and push it onto
					// the expression stack
					exprStack.push(new ExprNode(oper, true, expr1, expr2));
				}
				else {
					
					// If we get here, we saw a "*" or a "/", but it was not followed by a valid
					// multiplication/division element
					System.out.println("Parse error: a term was not found");
					return false;
				}
			}
			
			// Reaching this point indicates that we have processed the sequence of 
			// additive/subtractive elements
			return true;
		}
		else
			
			// This indicates that the first thing found was not a multiplication/division element
			return false;
	}
	
	/**********
	 * The term Expression method parses constants.
	 * 
	 * @return	The method returns a boolean value indicating if the parse was successful
	 */
	private static boolean term() {
		
		 if (current.getTokenKind() == Kind.FLOAT || current.getTokenKind() == Kind.INTEGER) {
				// When you find one, push a corresponding expression tree node onto the stack
				exprStack.push(new ExprNode(current, false, null, null));
			
				current = next;
				
				next = lexer.accept();
				
				return true;
			}
		 
			// check that if there is a symbol having a token code 4
			else if(current.getTokenKind() == Kind.SYMBOL && current.getTokenCode() == 4)
			{
				
					current = next;  // if yes then skip it
					
					next = lexer.accept();  // and accept the next token
					
				addSubExpr(); 
					
				
				//check if there is symbol of token code 5
				if(current.getTokenKind() == Kind.SYMBOL && current.getTokenCode() == 5)
				{
					
						current = next; //skip the word
						next = lexer.accept(); 
					}
		
				return true;  
				
			}		

			//check if there is symbol of token code 7
				else if (current.getTokenKind() == Kind.SYMBOL && current.getTokenCode() == 7) {
							
										
					next = new Token(next.getTokenKind(), (int)next.getTokenCode(), current.getTokenText() + next.getTokenText());
								
				exprStack.push(new ExprNode(next, false, null, null)); // push the next value into the stack
			
		     	current = lexer.accept(); // accept the current values
				next = lexer.accept(); // accept the next values
								
							return true;  // return the true
						}
			return false;
		}
	
	

	/**********
	 * The term Expression method parses variables and constants from characters.
	 * 
	 * @return	The method returns a boolean value indicating if the parse was successful
	 */
	private static boolean term2() {
		
		if (current.getTokenKind() == Kind.IDENTIFIER) {
			// When you find one, push a corresponding expression tree node onto the stack
			exprStack.push(new ExprNode(current, false, null, null));
		
			current = next;
			
			next = lexer.accept();
			
			return true;
		}
			
			// check that if there is a symbol having a token code 4
			else if(current.getTokenKind() == Kind.SYMBOL && current.getTokenCode() == 4)
			{
				
					current = next;  // if yes then skip it
					
					next = lexer.accept();  // and accept the next token
					
				addSubExpr(); // call the method for the further computations 
					
				
				//check if there is symbol of token code 5
				if(current.getTokenKind() == Kind.SYMBOL && current.getTokenCode() == 5)
				{
					
						current = next; //skip the word
						next = lexer.accept(); 
					}
		
				return true;  
				
			}		

			//check if there is symbol of token code 7
				else if (current.getTokenKind() == Kind.SYMBOL && current.getTokenCode() == 7) {
							
										
					next = new Token(next.getTokenKind(), (int)next.getTokenCode(), current.getTokenText() + next.getTokenText());
								
					exprStack.push(new ExprNode(next, false, null, null)); // push the next value into the stack
			
					current = lexer.accept(); // accept the current values
					next = lexer.accept(); // accept the next values
								
					return true;  }
		
		
			return false;
			
	}

		
	
	
	/**********
	 * The compute method is passed a tree as an input parameter and computes the value of the
	 * tree based on the operator nodes and the value node in the tree.  Precedence is encoded
	 * into the tree structure, so there is no need to deal with it during the evaluation.
	 * 
	 * @param r - The input parameter of the expression tree
	 * 
	 * @return  - A double of the result of evaluating the expression tree
	 */
	public static double compute(ExprNode r) {

		// Check to see if this expression tree node is an operator.
		if ((r.getOp().getTokenKind() == Kind.SYMBOL) && ((r.getOp().getTokenCode() == 6)  ||
				(r.getOp().getTokenCode() == 7) || (r.getOp().getTokenCode() == 8) ||(r.getOp().getTokenCode() == 4) ||
				(r.getOp().getTokenCode() == 5)||
				(r.getOp().getTokenCode() == 9))) {

			// if so, fetch the left and right sub-tree references and evaluate them
			double leftValue = compute(r.getLeft());
			
			double rightValue = compute(r.getRight());
			
			operand1 = new CalculatorValue(leftValue, 0.0);
			operand2 = new CalculatorValue(rightValue, 0.0);
			
			// Give the value for the left and the right sub-trees, use the operator code
			// to select the correct operation
			double result = 0 ;
			switch ((int)r.getOp().getTokenCode()) {
			
			
			case 6: 
			    result1 = new CalculatorValue(operand1);
			    result1.add(operand2);
			    String re = result1.toString();
			    System.out.println(re);
			    result = Double.parseDouble(result1.toString());;
			    break;
			case 7: 
				result1 = new CalculatorValue(operand1);
				result1.sub(operand2);
				String re1 = result1.toString();
				System.out.println(re1);
				result = Double.parseDouble(result1.toString());;
				 break;
			case 8: 
				result1 = new CalculatorValue(operand1);
				result1.mpy(operand2);
				String re2 = result1.toString();
			    System.out.println(re2);
				result = Double.parseDouble(result1.toString());;
				break;
			case 9: 
				result1 = new CalculatorValue(operand1);
				result1.div(operand2);
				String re3 = result1.toString();
				System.out.println(re3);
				result = Double.parseDouble(result1.toString());
				break;
			}
			
			// Display the actual computation working from the leaves up to the root
			System.out.println("   " + result + " = " + leftValue + r.getOp().getTokenText() + rightValue);
			ProgramUI.exp_result.appendText("   " + result + " = " + leftValue + r.getOp().getTokenText() + rightValue + "\n");
			// Return the result to the caller
			return result;
		}
		
	
		// If the node is not an operator, determine what it is and fetch the value 
		else if (r.getOp().getTokenKind() == Kind.INTEGER) {
			Scanner convertInteger = new Scanner(r.getOp().getTokenText());
			Double result = convertInteger.nextDouble();
			convertInteger.close();
			return result;
		}
		else if (r.getOp().getTokenKind() == Kind.FLOAT) {
			Scanner convertFloat = new Scanner(r.getOp().getTokenText());
			Double result = convertFloat.nextDouble();
			convertFloat.close();
			return result;
		}
		
		// if the node is not an operator nor value, then determine if the character 
		// or word matches with the list in the dictionary
		else if (r.getOp().getTokenKind() == Kind.IDENTIFIER) {
			double mv = 0;
			String string = r.getOp().getTokenText();
			if(Arrays.asList(name).contains(string)) {
				int index = Arrays.asList(name).indexOf(string);
				
				return value[index];
			}
			
//			
			else {	
					
					DefinitionUserInterface a = new DefinitionUserInterface();
					File name = a.getTextFieldData();
					System.out.println(r.getOp().getTokenText());
					@SuppressWarnings("resource")
					Scanner scanner;
					try {
						scanner = new Scanner(name);
						while (scanner.nextLine() != null) {
							while (scanner.hasNextLine()) {
								try {
									String[] tokens = scanner.nextLine().split(" ");
								//	System.out.println(string);
									if (string.matches(tokens[0])) {
										System.out.println("Hello");
										 mv = Double.parseDouble(tokens[2]);
										 System.out.println(mv);
										
									}
									else {
										 //ProgramUI.exp_error.setText("the variables or constants dont exit in the dictionary");
										 
										}
								} catch (ArrayIndexOutOfBoundsException e) {
									e.printStackTrace();
								}
							}
							break;
						}
						
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
		}
					return mv;
			
		}
		// If it is not a recognized element, treat it as a value of zero
		else return 0.0;
	}

	/**********
	 * This is the mainline that drives the demonstration
	 * 
	 * @param theExpression this is the expression that is entered by the user
	 * @param theReader this is the scanner which scans the lines from the string
	 * 
	 */
	public static void mainmethod(String theExpression, Scanner theReader) {

		// Show the source of the expression
		System.out.println();
		System.out.println("The expression is: " + theExpression);
		System.out.println();
		String ab = theReader.nextLine();
		System.out.println(ab);
		 Scanner theReader1 = new Scanner (ab);
		lexer = new Lexer(theReader1);
		current = lexer.accept();
		next = lexer.accept();

		// Invoke the parser and the tree builder
		boolean isValid = addSubExpr();
	
		if (isValid) {
			
		
		// checks if there is a statement to be print on the console
		if(ab.contains("Print")) {
			System.out.println(compute2(ab));
			ProgramUI.exp_result.appendText(compute2(ab) + "\n");
			
			 if(theReader.hasNextLine()) {	
				 mainmethod(theExpression, theReader); 
			 }
		}
		
		// checks if there it is a statement to input the variable value
		else if(ab.contains("Input")) {
			
			ProgramUI.exp_result.requestFocus();
			
			ProgramUI.exp_result.setOnKeyPressed(event -> {
				   if(event.getCode() == KeyCode.ENTER){

				compute3(ab);
				ProgramUI.exp_result.appendText("\n");
				 if(theReader.hasNextLine()) {	
					 mainmethod(theExpression, theReader); 
				 }
					
			}});
		}
		
		// checks if there is a "=" sign to compute the result
		else if(ab.contains("=")) {
		
				compute4(ab);
				 if(theReader.hasNextLine()) {	
					 mainmethod(theExpression, theReader); 
				 }
				
		}
		
		else {	
			ExprNode theTree = exprStack.pop();
			System.out.println(theTree);
			
			// Evaluate the expression tree
			System.out.println("\nThe evaluation of the tree:");
			 ProgramUI.exp_result.appendText("\nThe evaluation of the tree:\n");
			
			System.out.println("\nThe resulting value is: " + compute(theTree));
			 ProgramUI.exp_result.appendText("\nThe resulting value is: " + compute(theTree) + "\n");
		
			 if(theReader.hasNextLine()) {	
				 mainmethod(theExpression, theReader); 
			 }
		}	 
			
		}
	}
	
	
	/**********
	 * The method is used when a "=" sign is identified in the line and 
	 * there's something to solve and then store the result into the dictionary file
	 * 
	 * @param r - The input string contains the line 
	 * 
	 */
	
	private static void compute4(String r) {
		
		String copy = r;
		int i = r.indexOf("=");
		r = r.substring(0, i-1);
		name[number] = r;
		r = copy.substring(i+1);
		System.out.println(r);
		Scanner theReader1 = new Scanner (r);
		lexer = new Lexer(theReader1);
		current = lexer.accept();
		next = lexer.accept();

		// Invoke the parser and the tree builder
		boolean isValid = addSubExpr();

		if (isValid) {
			ExprNode theTree = exprStack.pop();
			
			 ProgramUI.exp_result.appendText("\n" + name[number]+ "= " + compute(theTree) + "\n");
			 value[number] = compute(theTree);
			 
			 // check if the file has been loaded into the dictionary or not
			 if(DefinitionUserInterface.loadornot.getText().equals("File is not Loaded")) {
					varError.setText("Please Load or create a new file first");
					varError.setVisible(true);
					ProgramUI.exp_error.setText("Please Load or Create a new File first to save the variable" + "\n");
					ProgramUI.exp_error.setStyle("-fx-text-color: red;");
			 }
			
				
			else {
					
					varError.setText("Done");
				}
			 
			 // if the file is loaded the save the result into the file
			 if(varError.getText() == "Done") {
				 String result = Double.toString(value[number]);
					def.saveNewVar(name[number],result, "0.0", "km");}
		}
		number++;
	}

	/**********
	 * The compute method is passed a tree as an input parameter and computes the value of the
	 * tree based on the operator nodes and the value node in the tree.  Precedence is encoded
	 * into the tree structure, so there is no need to deal with it during the evaluation.
	 * 
	 * @param ab - The input string from the console to get the name of the variable
	 * 
	 */
	
	private static String compute3(String ab) {
		
		//get the name of the variable from the string
		ab = ab.substring(6);
		
		name[number] = ab;  // store name of variable into array
		
		enterEvent();
		
		number++;
		return null;
	}

	/**********
	 * The method is used to get the text from the console and store the value of variable
	 * which has been input by the user into the array
	 * 
	 */
	private static void enterEvent() {
		
		// scan the console
				   Scanner theReader1 = new Scanner (ProgramUI.exp_result.getText());
				   String scan = null;
			        while(theReader1.hasNextLine()) {
			            scan = theReader1.nextLine();       // get the last line
			            
			        }
			       System.out.println(scan);
			       //get the value from the console for the variable
			        double valuef = Double.parseDouble(scan);  
			        System.out.println(valuef);
			        value[number] = valuef; // store the value from console into array
			        System.out.println(value[number]);
			        
			   }
	

	/**********
	 * This method is used whenever the print statement is entered and 
	 * there's some text to print on the console
	 * 
	 * @param r - The string having the text to print 
	 * 
	 */
	
	private static String compute2(String r) {
		
	
	// if there is a value of variable that the user wants to print
		if(r.charAt(6) != '"' && r.charAt(r.length()-1) != '"' ) {
			r = r.substring(6, r.length());
			System.out.println(r);
			
			// checks the variable is user defined or not
			if(Arrays.asList(name).contains(r)) {
				int index = Arrays.asList(name).indexOf(r);
				String f = name[index] + " = " + value[index];
				return f;
			}
			else {
				
				// checks if the variable is from the dictionary file
				double mv;
				DefinitionUserInterface a = new DefinitionUserInterface();
				File name = a.getTextFieldData();
				@SuppressWarnings("resource")
				Scanner scanner;
				try {
					scanner = new Scanner(name);
					while (scanner.nextLine() != null) {
						while (scanner.hasNextLine()) {
							try {
								// print the value of the variable
								String[] tokens = scanner.nextLine().split(" ");
								if (r.matches(tokens[0])) {
									System.out.println("Hello");
									 mv = Double.parseDouble(tokens[2]);
									 System.out.println(mv);
									 String f = r + " = " + mv;
										return f;
								}
								else {
									// give the error message, if the text is neither a user defined nor from the file
									ProgramUI.exp_error.setText("Format is not correct or word doesn't exist"); 
									}
							} catch (ArrayIndexOutOfBoundsException e) {
								e.printStackTrace();
							}
						}
						break;
					}
					
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				
			}
		}
	
	// if there is some text to be print on the console
		else {
		int i = r.length();
		r = r.substring(7, i-1);
		return r;}
		
		return null;
		
	}

	/**********
	 * This is the method to check if the expression is valid or not
	 * @param theExpression this is the expression that is entered by the user
	 * @param theReader this is the scanner which scans the lines from the string
	 * 
	 */
	public static void debug(String theExpression, Scanner theReader) {

		// Show the source of the expression
		System.out.println();
		System.out.println("The expression is: " + theExpression);
		System.out.println();
		ProgramUI.exp_result.appendText("\n" + "The expression is: " + theExpression + "\n");
		
		// Set up the Scanner and the Lexer
		lexer = new Lexer(theReader);
		
		current = lexer.accept();
		next = lexer.accept();
		

		// Invoke the parser and the tree builder
		boolean isValid = addSubExpr();
		System.out.println("The expression is valid: " + isValid + "\n");
		ProgramUI.exp_result.appendText("The expression is valid: " + isValid + "\n");
			
		
	}

}
