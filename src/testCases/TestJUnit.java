package testCases;


import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import programmableCalculator.BusinessLogic;
import programmableCalculator.CalculatorValue;


public class TestJUnit {
	
	/* This is the link to the business logic */
	public BusinessLogic perform = new BusinessLogic();
	CalculatorValue result;
	

	  @Test
	    public void testTheMethodAdd(){

		    result = new CalculatorValue();                  // It displays the value of operand1.
		    CalculatorValue value2 = new CalculatorValue();
		    result.add(value2);                                 // add the values using the method add
		
		    //check for not null value
		    assertNotNull(result);
	    }
	
}

