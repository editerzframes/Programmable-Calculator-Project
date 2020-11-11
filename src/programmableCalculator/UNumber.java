package programmableCalculator;

/**
 * <p> Title: UNumber Class, a component of the Unlimited Precision Math Package. </p>
 * 
 * <p> Description: A entity class that is being used for the unlimited precison and 
 * performing basic arthematic opertion over the numbers. </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2014 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.02a	Addition of code to support quality improvements, and the removal of 
 * 					the div2 method
 * 
 * @version 1.01	The initial version plus an improved add/sub, normalize, div, div2, 
 * 					lessThan, greaterThan, abs, and compareTo
 * 
 * @version 1.02    Two methods are added, get mantisa and get charachterstic for the square root
 *                  calculation 
 * 
 */

public class UNumber implements Comparable<UNumber> {
	
	/**
	 * An unlimited precision floating point value in this package is implemented using 
	 * 		<B>decimal</B> arithmetic. Values are stored in scientific notation (i.e. mantissa 
	 * 		x 10 to the characteristic power.  The "mantissa" is a sequence of 0 to 9 digits 
	 * 		with the assumed decimal point to the left of the Most Significant Digit (MSD) in 
	 * 		the mantissa.
	 * 
	 * The "characteristic" is an integer value.  This determines the location of the decimal 
	 * 		point and performs the equivalent of multiply the mantissa by ten raised to the 
	 * 		power of the characteristic
	 * 
	 * After any computation, the package ensures that the result is "normalised".  This means 
	 * that with the exception of a result value of zero, the MSD will be in the range "1" to "9".  
	 * We do this to speed future computations.  Normalisation discards leading zeros in the result 
	 * and for each zero from the left side that is removed, the characteristic is reduced by one 
	 * to compensate.
	 */
	
	protected byte d [];		// The mantissa as a sequence of digits, most significant digits (MSD)
							// is in d[0]. The implied decimal point is to the left of the MSD

	protected int dP;			// The characteristic as decimal power of 10 
	protected boolean s;		// The sign, true means positive, false is negative
	
	/**
	 * This default constructor sets up a 20 digit number with a value of zero
	 */
	public UNumber() {
		d = new byte[20];
		for (int i = 0; i < 20; i++) d[i] = 0;
		dP = 0;
		s = true;
	}
	
	/**********
	 * This constructor creates a variable length number based on an integer input value
	 * 
	 * @param v - number
	 */
	public UNumber(int v){
		// Establish the sign and make the working value positive (or zero)
		if (v>=0) s = true;
		else {s = false; v = -v;}
		
		// Determine the number of significant digits by iterative dividing by 10
		int numDigits = 0;
		int tempV = v;
		while (tempV > 0){
			tempV = tempV/10;
			numDigits++;
		}
		
		// Working from the least significant toward the most, extract the units
		// digit, save it into the array, and then divide by 10 to create the
		// next least significant digit as the units digit
		dP = numDigits;
		d = new byte[numDigits];
		while (numDigits > 0) {
			d[--numDigits] = (byte) (v % 10);	// This extracts the units digit
			v = v / 10;							// This gets the next least significant digit
		}
	}
	
	/**********
	 * This constructor creates a variable length number based on a long integer input value
	 * 
	 * @param v - number
	 */
	public UNumber(long v){
		// Establish the sign and make the working value positive (or zero)
		if (v>=0) s = true;
		else {s = false; v = -v;}
		
		// Determine the number of significant digits by iterative dividing by 10
		int numDigits = 0;
		long tempV = v;
		while (tempV > 0){
			tempV = tempV/10;
			numDigits++;
		}

		// Working from the least significant toward the most, extract the units
		// digit, save it into the array, and then divide by 10 to create the
		// next least significant digit as the units digit
		dP = numDigits;
		d = new byte[numDigits];
		while (numDigits > 0) {
			d[--numDigits] = (byte) (v % 10);	// This extracts the units digit
			v = v /10;							// This get the next least significant digit
		}
	}
	
	/**********
	 * This constructor creates a number based on a digit sequence in a string, a power of ten, 
	 * and a sign
	 * 
	 * @param str	The string specifies the digit sequence and it's length is the number of 
	 * 		significant digits
	 * 
	 * @param dec	This is the characteristic (the power of 10)
	 * 
	 * @param sign	The sign
	 */
	public UNumber(String str, int dec, boolean sign){
		// Use the length of the string to determine the size of the mantissa
		d = new byte[str.length()];
		
		// Define each element of the mantissa by means of this switch
		for (int i = 0; i < str.length(); i++) 
			switch (str.charAt(i)){
			case '0': d[i] = 0;
			break;
			case '1': d[i] = 1;
			break;
			case '2': d[i] = 2;
			break;
			case '3': d[i] = 3;
			break;
			case '4': d[i] = 4;
			break;
			case '5': d[i] = 5;
			break;
			case '6': d[i] = 6;
			break;
			case '7': d[i] = 7;
			break;
			case '8': d[i] = 8;
			break;
			case '9': d[i] = 9;
			break;
			}
		
		// The characteristic and sign are exactly the same as the parameters
		dP = dec;
		s = sign;
	}
	
	/**********
	 * This constructor creates a number based on a digit sequence in a string, a power of ten, a 
	 * sign, and a size constant.  If the size is larger than the constant sequence, the extra 
	 * positions are filled with zero.  If the size is less than the digit sequence, the excess 
	 * digits are discarded.
	 * 
	 * @param str	The string specifies the digit sequence and it's length is the number of 
	 * 		significant digits
	 * 
	 * @param dec	This is the characteristic (the power of 10)
	 * 
	 * @param sign	The sign
	 * 
	 * @param size	The number of significant digits
	 */
	public UNumber(String str, int dec, boolean sign, int size){
		// Use the fourth parameter to determine the size of the mantissa
		d = new byte[size];
		
		// Define each element of the mantissa by means of this switch
		for (int i = 0; i < Math.min(str.length(), size); i++) 
			switch (str.charAt(i)){
			case '0': d[i] = 0;
			break;
			case '1': d[i] = 1;
			break;
			case '2': d[i] = 2;
			break;
			case '3': d[i] = 3;
			break;
			case '4': d[i] = 4;
			break;
			case '5': d[i] = 5;
			break;
			case '6': d[i] = 6;
			break;
			case '7': d[i] = 7;
			break;
			case '8': d[i] = 8;
			break;
			case '9': d[i] = 9;
			break;
			}
		for (int i = Math.min(str.length(), size); i < size; i++) d[i] = 0;
		
		// The characteristic and sign are exactly the same as the parameters
		dP = dec;
		s = sign;
	}
	
	/**********
	 * This is a copy constructor
	 * 
	 * @param that  - number
	 */
	public UNumber(UNumber that){
		d = new byte[that.d.length];
		for (int i = 0; i < that.d.length; i++) d[i]=that.d[i];
		dP = that.dP;
		s = that.s;
	}
	
	/**********
	 * This is a copy constructor where the size of the new value is specified
	 * 
	 * @param size this defines the size of the new value
	 * @param that this is the object for UNumber
	 */
	public UNumber(UNumber that, int size){
		d = new byte[size];
		for (int i = 0; i < Math.min(that.d.length, size); i++) d[i]=that.d[i];
		for (int i = Math.min(that.d.length, size); i<size; i++)d [i]=0;
		dP = that.dP;
		s = that.s;
	}
	
	/**********
	 * This constructor creates a potentially unnormalized value (there maybe leading zeros) with 
	 * enough space in the mantissa array to hold all of the significant digits of both input 
	 * parameters at the same time.  It initializes the new number to the value of the first 
	 * parameter.  This constructor is used to align the decimal points of two numbers.
	 * 
	 * @param that		This parameter is used to initialize the number
	 * 
	 * @param another	This one helps determine the how many significant digits to use
	 */
	public UNumber(UNumber that, UNumber another){
		// left is the maximum number of digits to the left of the decimal point
		int left = Math.max(Math.max(that.dP, another.dP), 0);
		
		// right is the maximum number of digits to the right of the decimal point
		int right = Math.max(Math.max(that.d.length - that.dP, another.d.length - another.dP), 0);
		
		// The mantissa is allocated to be large enough to hold all of the digits
		d = new byte[left+right];
		
		// Initialize the number based on the value of the left parameter, "that"
		// Index is used to insert the digits into the mantissa array
		int index = 0;
		
		// The first loop fills in leading zeros (if needed) to align the decimal point
		for (int i=0; i<left-that.dP; i++) d[index++] = 0;
		
		// The second loop fills in the mantissa from the first parameter
		for (int i=0; i<that.d.length; i++) d[index++] = that.d[i];
		
		// The third loop fill in any trailing zeros (if needed) to fill the mantissa array
		for (int i=index; i<left+right; i++) d[i] = 0;
		
		// The characteristic and sign from the first parameter is used
		dP = left;
		s = that.s;
	}
	
	/**********
	 * This constructor takes a double and creates a UNumber from it.  The result is rounded to a 
	 * 14 significant digit result.
	 * 
	 * @param v - number
	 */public UNumber(double v){
		 // Allocate enough space to hold 14 significant digits plus one for rounding and one
		 // for loss of significance for normalization.
		 byte [] tempD = new byte [16];
		 
		 // Set the sign of the result and convert to a positive value for the conversion
		 s = true;
		 if (v < 0) {
			 v = -v;
			 s = false;
		 }
		 
		 // Use log base 10 to determine the characteristic
		 dP = ((int)Math.log10(v)) + 1;
		 
		 // Use the characteristic to adjust the mantissa to a value in the range from
		 // 0.10000000000000 to 0.99999999999999
		 double man = v/Math.pow(10.0, dP);
		 
		 // One by one, peel off the most-significant digit and place it into the byte array
		 for (int i =0; i < tempD.length; i++){
			 man *= 10;
			 byte digit = (byte)man;
			 tempD[i] = digit;
			 man -= digit;
		 }
		 
		 // If the last believable digit (the 16th) is greater than or equal to 5, round up the
		 // remaining 15 digits.
		 if (tempD[15]>=5){
			 // Round up
			 boolean carry = true;
			 for (int i=14; i>=0; i-- ) {
				 tempD[i] += (carry? 1:0);
				 if (tempD[i]>9){
					 carry = true;
					 tempD[i] -=10;
				 }
				 else carry = false;
			 }
			 // if the carry is true at the end of the loop, we have to deal with a carry off the
			 // left end (this tells up normalization is not required).
			 if (carry) {
				 for (int i=14; i>0; i-- ) tempD[i] = tempD[i-1];
				 tempD[0]=1;
				 this.dP++;
			 }
		 }
		 
		 // Establish the result to have 14 significant places
		 d = new byte [14];
		 
		 // If necessary, normalize the rounded 15 digit number... the value could still be 
		 // 0.0999999
		 if (tempD[0]==0) {
			 // This does the normalizing by eliminating one leading zero digit
			 for (int i = 0; i < d.length; i++) d[i] = tempD[i+1];
			 this.dP--;
		 }
		 else
			 // This is used when the value is already normalized
			 for (int i = 0; i < d.length; i++) d[i] = tempD[i];	
	 }
		
		/**********
		 * This implementation of toString converts the value to a String in scientific notation
		 * 
		 * @return result - formated result
		 */
		public String toString(){
			String result;
			if (s) result = "+0.";
			else result = "-0.";
			for (int i = 0; i < d.length; i++) result += d[i];
			result += "E";
			if (dP<0) result += dP;		// If the result is negative, the sign will automatically be 
										// inserted
			else result += "+" + dP;	// For positive values, we must manually insert the "+" sign
			return result;
		}
		
		/**********
		 * This implementation of toString converts the value to a String in decimal notation as
		 * long as the exponent is in the range +8 to -6
		 * 
		 * @return result - formated result
		 * 
		 */
		public String toStringDecimal(){
			if (dP > 8 || dP < -6) return toString();
			String result;
			if (s) result = "";
			else result = "-";
			if (dP > 0 && dP < d.length) {
				for (int i = 0; i < dP; i++) result += d[i];
				if (dP < d.length) result += ".";
				for (int i = dP; i < d.length; i++) result += d[i];
			}
			else if (dP > 0) {
				for (int i = 0; i < d.length; i++) result += d[i];
				for (int i = d.length; i < dP; i++) result += "0";
			}
			else {
				result += "0.";
				for (int i = dP; i < 0; i++) result += "0";
				for (int i = 0; i < d.length; i++) result += d[i];
			}
			return result;
		}
	
	/**********
	 * If the first digit of the UNumber is zero, the value must be zero as these values are 
	 * normalized.
	 * 
	 * @return	true if the value is zero, else false.
	 */
	public boolean isZero() {
		return this.d[0] == 0;
	}

	/**********
	 * This implementation of toString converts the value to a String in using a decimal notation
	 * 
	 * @return result - formated result
	 */
	public String toDecimalString(){
		// Establish the sign... 
		// We do not put a "+" on a positive value, but do put on a "-" for negative
		String result;
		if (s) result = "";
		else result = "-";
		
		// For values less than one, we will display one zero to the left of the decimal point
		if (dP <= 0) result += "0";
		
		// For values less than 1/10, we need to insert zeros between the decimal point and
		// the most significant digit
		if (dP < 0) {
			result += ".";										// Insert the decimal point
			for (int i = 0; i > dP; i--) result += "0";			// Insert the zeros
			for (int i = 0; i < d.length; i++) result += d[i];	// Insert the significant digits
		}
		else
		{
			// For values greater than 1/10, we insert the decimal point at the appropriate point
			// as we insert in the digits (if dP <= d.length)
			for (int i = 0; i < d.length; i++) {
				if (i == dP) result += ".";			// Insert the decimal point at the right place
				result += d[i];						// Insert the digits
			}
			// If dP > d.length, the decimal point is to the right of all of the significant 
			// digits so we must insert more zeros before we can insert the decimal point
			if (dP > d.length) 
				for (int i = d.length; i < dP; i++) result += "0";	// Insert the zeros
			if (dP >= d.length) result += ".";						// and then the decimal point
		}
		return result;
	}
		
	/**********
	 * This routine is used to display big numbers on the console, 80 characters per line with the
	 * characteristic displayed on the last line (which might be longer than 80 characters in length).
	 */
	public void displayBigNumber() {
		String str = (s? "  " : " -");
		int count = 2;
		for (int i = 0; i < d.length; i++){
			str += d[i];
			count++;
			if (count == 80) {
				System.out.println(str);
				count = 0;
				str = "";
			}
			if (i == dP - 1){
				str += ".";
				count++;
				if (count == 80) {
					System.out.println(str);
					count = 0;
					str = "";
				}
			}
		}
		System.out.println(str + "E" + dP);
	}
	
	/*********
	 * This routine displays just the first 10 significant digits of a UNumber followed
	 * by the characteristic to System.out (the console).
	 */
	public void displayShort() {
		if (!s) System.out.print("-");
		for (int i = 0; i < Math.min(d.length, 10); i++ ) System.out.print(d[i]);
		System.out.print("E");
		System.out.println(dP);
	}
	
	/**********
	 * This internal routine is used to normalize a UNumber
	 * 
	 * @param that	The value to be normalized
	 */
	private void normalize(UNumber that){
		if (that.d[0]==0){
			int zeroIndex = 1;		// Find where the first non-zero digit is in the number
			while (zeroIndex < that.d.length-1 && that.d[zeroIndex]==0) zeroIndex++;
			
			// if all digits are zero, do not normalize
			if (that.d.length - zeroIndex <= 0) return;
					
			// Create a new mantissa that has the appropriate number of significant digits (at 
			// least one)
			byte [] newD = new byte [that.d.length - zeroIndex];
			
			// Move the significant digits from the result mantissa array into the new mantissa 
			// array
			for (int i = zeroIndex; i < that.d.length; i++) newD[i - zeroIndex] = that.d[i];
			
			// Establish the normalized result
			that.d = newD;			// Replace the mantissa with the normalized (shortened) version
			that.dP -= zeroIndex;	// Adjust the characteristic accordingly
		}
		return;

	}
	
	/**********
	 * The Addition operation adds the second operand to this object's value  (this = this + that)
	 * The code implements the addition in a very simplistic manner, sacrificing speed and space 
	 * for a very simple algorithm that mirrors how people do addition by hand.
	 * 
	 * @param that	The second operand that is added to this object's value
	 */
	public void add(UNumber that){
		UNumber temp1;
		UNumber temp2;
		if(this.s == that.s) {
			
			// The signs are the same, so we can add the two unsigned numbers together and then 
			// copy the sign from either
			temp1 = new UNumber(this, that);	// Establish two working values that are decimal  
			temp2 = new UNumber(that, this);	// point aligned
			
			// Work from Least Significant Digit (LSD) toward Most Significant Digit (MSD) and 
			// add digit to digit
			boolean carry = false;
			for (int i = temp1.d.length-1; i>=0; i--){
				// If the previous digit addition had a carry, add it in to this digit
				temp1.d[i] += (byte)(temp2.d[i] + (carry? 1 : 0));
				if (temp1.d[i]>9) {		// If the resulting addition is > 9, there is a carry here
					temp1.d[i] -= 10;	// so reduce the result by 10.  It can't be more than 19 
					carry=true;			// (9 + 9 + 1 = 19) and set the carry for the next digit 
										// addition
				}
				else carry=false;		// If the digit addition is <= 9, there is no carry
			}
			
			// If there is a carry at the end of the loop, we need one more digit of significance
			if (carry){
				d = new byte[temp1.d.length+1];		// Establish a mantissa array that is one digit 
													// larger
				d[0]=1;								// If there was a carry, that new digit must be 
													// a "1"
				for (int i = 0; i<temp1.d.length; i++) d[i+1] = temp1.d[i];	// Copy over rest of 
													// the digits
				temp1.dP++;							// Increase the power of 10 by one
				temp1.d = d;						// Establish this new mantissa array as the 
													// result's mantissa
			}
		}
		
		else {
			// The signs differ, so we subtract the the negative value from the positive value
			// We will put the positive value in temp1 and the negative in temp2
			if (this.s) {						
				temp1 = new UNumber(this, that);
				temp2 = new UNumber(that, this);
			} else {
				temp2 = new UNumber(this, that);
				temp1 = new UNumber(that, this);
			}
			
			// Work from Least Significant Digit (LSD) toward Most Significant Digit (MSD) and 
			// subtract digit from digit.  We start off with no borrowing at the LSD
			boolean borrow = false;
			for (int i = temp1.d.length-1; i>=0; i--){ 
				// If the previous subtraction resulted in a borrow, we must subtract an additional 
				// 1 from this digit
				temp1.d[i] = (byte)(temp1.d[i] - temp2.d[i] - (borrow? 1: 0));
				if (temp1.d[i]<0){		// If the resulting subtraction is < 0, there is a borrow 
					temp1.d[i] += 10;	// here so add 10 to the result.  The result cannot be 
										// less than -10 (0 - 9 - 1 = -10)
					borrow = true;		// and set the borrow for the next digit subtraction
				}
				else borrow = false;	// If the digit subtraction is >= 0, there is no borrow
			}
						
			// if the borrow is true, the negative value was larger and we must invert the values 
			// and set the resulting sign to negative
			if (borrow){
				// For all but the LSD, we must subtract the digit from 9 (10 - digit - 1 for the 
				// next digit to the right borrow)
				for (int i = 0; i <temp1.d.length-1; i++) temp1.d[i] = (byte)(9 - temp1.d[i]);
				
				// For the LSD, we subtract the digit from 10, since there are no more digits that 
				// require a borrow
				temp1.d[temp1.d.length-1] = (byte)(10 - temp1.d[temp1.d.length-1]);
				
				// Resolve any carries working back from the LSD to the MSD
				for (int i = temp1.d.length-1; i > 0; i--)
					if (temp1.d[i]>=10){
						temp1.d[i]-=10;
						temp1.d[i-1]++;
					}
				
				// The resulting number is negative
				temp1.s = false;
			}
			else temp1.s = true;	// Since the borrow was false at the end, the positive value 
									// was larger, so then is the result
		}
		
		// The result of the addition (or subtraction) is in temp1, so we must first normalize it 
		// and then move it into this object's attributes
		normalize(temp1);
		this.d = temp1.d;
		this.dP = temp1.dP;
		this.s = temp1.s;
	}
	
	/**********
	 * The subtract operation negates the second operand and uses the add operator 
	 * (this = this - that)  The code uses a temporary for the second operand so the callers 
	 * second operand is not changed
	 * 
	 * @param that	The second operand that is subtracted from the value of this object's value
	 */
	public void sub(UNumber that){
		UNumber temp = new UNumber(that);
		temp.s = ! temp.s;
		this.add(temp);
	}
	
	/**********
	 * The multiply operation implements multiplication is the traditional, by hand-hand manner, 
	 * where each digit of the multiplicand is multiplied by each digit of the multiplier and 
	 * adding into the proper digit of the product using the index of the multiplier digit as the 
	 * base for the summation. Since we have unlimited number of digits, the intermediate results 
	 * can become arbitrarily large, so the carry must be resolved into the product after each digit 
	 * multiplication.
	 * 
	 * @param that	The multiplier 
	 */
	public void mpy(UNumber that){
		// The temporary product must be at least two digits longer than the result, one digit to 
		// deal with the possible loss of precision when normalizing is required, and one for 
		// rounding.
		byte [] product = new byte[this.d.length+(that.d.length>2?that.d.length:2)];
		byte [] multiplicand = this.d;
		byte [] multiplier = that.d;
		
		// If either the multiplicand or the multiplier is zero, 
		// the product is zero, so return a default zero
		if (multiplicand[0] == 0 || multiplier[0] == 0) {
			for (int i = 0; i < this.d.length; i++) this.d[i] = 0;
			dP = 0;
			s = true;	
			return;
		}
		
		// The product is not zero, so compute the product using the by-hand algorithm
		
		// Zero the product
		for (int p = 0; p < product.length; p++)
			product[p] = 0;
		
		// For each digit in the multiplier
		for (int r = multiplier.length - 1; r >= 0; r--) {
			
			// start with the proper digit in the product based on the location in the
			// multiplicand and multiplier
			int p = multiplicand.length + r;
			
			// Computer the product for each digit in the multiplicand and add it into the product
			for (int d = multiplicand.length-1; d >=0; d--) {
				product[p] += multiplicand[d] * multiplier[r];
		
				// Resolve any carries by ensuring each product digit is not larger than 9
				// (Since none of the digits were larger than 9, we can stop as soon as we find
				// one that is not larger than 9 as we resolve the carries working to the left.)
				int c=p;
				while (product[c] > 9) {
					product[c-1] += product[c] / 10;	// Add the carry to the digit to the left
					product[c--] %= 10;					// Use the unit's value for this digit
				}
				
				// use the next product digit to the left;
				p--;
				
			}
		}
		
		// We now have the product, but it may not be normalized.  The first digit of the product 
		// must not be zero since the product is not zero.  If at this point that leading digit is 
		// zero, then we must discard that one zero digit and adjust the size and the exponent 
		// down one.  (There can be at most one zero digit given the multiplicand and the multiplier 
		// were both non-zero and normalized.)
		boolean wasNormalized = false;
		if (product[0] == 0) {
			// The product must be normalized and transferred into this object
			for (int i = 0; i < product.length-1; i++) product[i] = product[i+1];
			// It is not necessary to set the LSD to zero, because it will not be used... two 
			// excess digits were set up in the product... one for normalization and one for 
			// rounding.
			product[product.length-1] = 0;
			
			// Signal that the value was normalized (needed to correct the characteristic)
			wasNormalized = true;
		}
		
		// Since the product is longer than the result array, we need to discard the excess digits 
		// and that leads us to wanting to round the result if the digit to the right of the LSD is 
		// five or larger.
		boolean roundingCarry=false;
		if (product[this.d.length] >= 5) {
			// Since the LSD was a 5 or larger, we must round and propagate the carry to the left.
			int i = d.length-1;
			product[i]++;
			while (i > 0 && product[i] > 9) {
				product[i-1]++;
				product[i--]-=9;
			}
			
			// If the carry propagates all the way from the LSD to the MSD and we carry off the end, 
			// all of those intermediate digits must have been "9"s before the rounding and they are 
			// now zeros.  
			if (i == 0 && product[0] > 9) {
				// Set flag so we know we are doing this so we can add one to the characteristic
				roundingCarry=true;
				// Set the MSD to 1 and the LSD to 0, since all the rest of them must be zeros
				product[0] = 1;
				product[d.length-1] = 0;
			}
		}

		// Compute the characteristic (the sum of the two powers of ten)
		this.dP += that.dP;
		if (wasNormalized) this.dP--;	// Adjust down if there was a leading zero in the product
		if (roundingCarry) this.dP++;	// Adjust up if the rounding resulting in a carry off the 
										// left end
		
		// The product must be transferred into this object
		for (int i = 0; i < this.d.length; i++) this.d[i] = product[i];

		// Compute the sign of the product
		this.s = this.s == that.s;
	}

	

	/**********
	 * The divide operation implements division by means of repeated subtraction, producing as 
	 * many significant digits in the quotient as in the dividend (this object) and the quotient 
	 * replaces the dividend.  This is a faster algorithm than the simpler Div2 below.  This 
	 * algorithm assumes the numbers are normalized.
	 * 
	 * @param that	The divisor 
	 */
	public void div(UNumber that){
		byte [] dividend = new byte[this.d.length+that.d.length+1];
		byte [] quotient = new byte[this.d.length+2];
		byte [] divisor = that.d;
		
		// Check for divide by zero and return close to infinite
		if (that.d[0] == 0) {
			for (int ndx = 0; ndx < this.d.length; ndx++) this.d[ndx] = 9;
			this.dP = 999999;
			this.s = true;
			return;
		}

		// Make working copy of the dividend, which is destroyed during the process
		for (int i = 0; i < this.d.length; i++) dividend[i] = this.d[i];
		for (int i = this.d.length; i < dividend.length; i++) dividend[i] = 0;

		// Initialize the quotient to zero, which will replace the dividend at the end of this 
		// process
		for (int i = 0; i < quotient.length; i++) quotient[i] = 0;

		// Digit by digit, subtract the divisor from the dividend until a borrow occurs, add it 
		// back in, shift the divisor right one digit, do it again, until all of the digits in the 
		// quotient have been filled in.
		for (int qIndex = 0; qIndex < quotient.length; qIndex++){
			boolean okToContinue = true;
			while (okToContinue && quotient[qIndex]<9){
				boolean borrow = false;
				for (int i = divisor.length-1; i>=0; i--){ 
					// If the previous subtraction resulted in a borrow, we must subtract an 
					// additional 1 from this digit
					dividend[i+qIndex] = (byte)(dividend[i+qIndex] - divisor[i] - (borrow? 1: 0));
					if (dividend[i+qIndex]<0){		// If the resulting subtraction is < 0, there 
													// is a borrow here
						dividend[i+qIndex] += 10;	// so add 10 to the result.  The result cannot 
													// be less than -10 (0 - 9 - 1 = -10)
						borrow = true;		// and set the borrow for the next digit subtraction
					}
					else borrow = false;	// If the digit subtraction is >= 0, there is no borrow
				}

				if (borrow){				// If a borrow is true at the end add back the divisor 
					if (qIndex > 0 && dividend[qIndex-1]>0) {
						dividend[qIndex-1]--;
						quotient[qIndex]++;
					}
					else {
						okToContinue = false;	// Stop the loop
						boolean carry = false;
						for (int i = divisor.length-1; i>=0; i--){ 
							// If the previous addition resulted in a carry, we must add an 
							// additional 1 to this digit
							dividend[i+qIndex] = 
									(byte)(dividend[i+qIndex] + divisor[i] + (carry? 1: 0));
							if (dividend[i+qIndex]>9){		// If the resulting subtraction is > 9, 
															// there is a carry here
								dividend[i+qIndex] -= 10;	// so subtract 10 to the result.  The 
															// result cannot be greater than 
															// 19 (9 + 9 + 1 = +19)
								carry = true;		// and set the carry for the next digit addition
							}
							else carry = false;	// If the digit addition is <= 9, there is no carry
						}
					}
				}
				else quotient[qIndex]++;		// If the subtraction was successful, count it
			}
		}
		
		this.dP = this.dP - that.dP + 1;
		this.s = this.s == that.s;
		
		// We now have the quotient, but it may not be normalized.  The first digit of the product 
		// must not be zero since the quotient is not zero.  If at this point that leading digit is 
		// zero, then we must discard that one zero digit and adjust the size and the exponent down 
		// one.  (There can be at most one zero digit given the multiplicand and the multiplier 
		// were both non-zero and normalized.)
		boolean wasNormalized = false;
		if (quotient[0] == 0) {
			// The product must be normalized and transferred into this object
			for (int i = 0; i < quotient.length-1; i++) quotient[i] = quotient[i+1];
			quotient[quotient.length-1] = 0;
			// signal that the value was normalized (needed to correct the characteristic)
			wasNormalized = true;
		}
		
		// Since the quotient is longer than the result array, we need to discard the excess 
		// digits and that leads us to wanting to round the result if the digit to the right of 
		// the LSD is five or larger.
		boolean roundingCarry=false;
		if (quotient[this.d.length] >= 5) {
			// Since the LSD was a 5 or larger, we must round and propagate the carry to the left.
			int i = d.length-1;
			quotient[i]++;
			while (i > 0 && quotient[i] > 9) {
				quotient[i-1]++;
				quotient[i--]-=9;
			}
			
			// If the carry propagates all the way from the LSD to the MSD and we carry off the 
			// end, all of those intermediate digits must have been "9"s before the rounding and 
			// they are now zeros.  
			if (i == 0 && quotient[0] > 9) {
				// Set flag so we know we are doing this so we can add one to the characteristic
				roundingCarry=true;
				// Set the MSD to 1 and the LSD to 0, since all the rest of them must be zeros
				quotient[0] = 1;
				quotient[d.length-1] = 0;
			}
		}

		// Compute the characteristic (the sum of the two powers of ten)
		if (wasNormalized) this.dP--;	// Adjust down if there was a leading zero in the quotient
		if (roundingCarry) this.dP++;	// Adjust up if the rounding resulting in a carry off the 
										//left end
		
		// The quotient must be transferred into this object
		for (int i = 0; i < this.d.length; i++) this.d[i] = quotient[i];
	}
	
	/**********
	 * This lessThan routine tries to avoid doing a subtraction in order to do things faster.  
	 * Therefore checks of sign bits, then the decimal points, and then the first digits are done 
	 * before falling into a subtract as a last resort.  If one value is negative and the other is 
	 * positive, there is no need to go any further.  If they are of the same size, but the 
	 * characteristics of the normalized values are not the same, there is no need to go any further.  
	 * Given the same size and the same characteristic for normalized numbers, if the first digit can 
	 * resolve the issue, try that.  Only in the case where the signs, characteristics, *and* the 
	 * first digits are the same do we resort to subtraction and a check of the sign bit.
	 * 
	 * @param that	The right operand in the "this lessThan that" relational test
	 * 
	 * @return boolean true if this is indeed less than that
	 */
	public boolean lessThan(UNumber that){
		if (this.s)
			if (that.s) {
				// Both operands are positive
				if (this.dP > that.dP) return false;
				else if (this.dP < that.dP) return true;
				else 
					// Both operands have the same magnitude
					if (this.d[0] < that.d[0]) return true;
					else if (this.d[0] > that.d[0]) return false;
					else {
						// same sign, magnitude, and first significant digit
						UNumber temp = new UNumber(this);
						temp.sub(that);
						if (temp.s) return false;
						return true;
					}				
			}
			else {
				// The left is positive and the right is negative
				return false;
			}
		else
			if (that.s) {
				// The left is negative and the right is positive
				return true;
			}
			else {
				// Both operands are negative
				if (this.dP > that.dP) return true;
				else if (this.dP < that.dP) return false;
				else 
					// Both operands have the same magnitude
					if (this.d[0] < that.d[0]) return false;
					else if (this.d[0] > that.d[0]) return true;
					else {
						// same sign, magnitude, and first significant digit
						UNumber temp = new UNumber(this);
						temp.sub(that);
						if (temp.s) return true;
						return false;
					}				
			}
	}

	/**********
	 * This greaterThan routine tries to avoid doing a subtraction in order to do things faster.  
	 * This code follows the same process as the less than code above
	 * 
	 * @param that	The right operand in the "this greaterThan that" relational test
	 * 
	 * @return boolean true if this is indeed greater than that
	 */
	public boolean greaterThan(UNumber that){
		if (this.s)
			if (that.s) {
				// Both operands are positive
				if (this.dP > that.dP) return true;
				else if (this.dP < that.dP) return false;
				else 
					// Both operands have the same magnitude
					if (this.d[0] < that.d[0]) return false;
					else if (this.d[0] > that.d[0]) return true;
					else {
						// same sign, magnitude, and first significant digit
						UNumber temp = new UNumber(this);
						temp.sub(that);
						if (temp.s) return true;
						return false;
					}				
			}
			else {
				// The left is positive and the right is negative
				return true;
			}
		else
			if (that.s) {
				// The left is negative and the right is positive
				return false;
			}
			else {
				// Both operands are negative
				if (this.dP > that.dP) return false;
				else if (this.dP < that.dP) return true;
				else 
					// Both operands have the same magnitude
					if (this.d[0] < that.d[0]) return true;
					else if (this.d[0] > that.d[0]) return false;
					else {
						// same sign, magnitude, and first significant digit
						UNumber temp = new UNumber(this);
						temp.sub(that);
						if (temp.s) return false;
						return true;
					}				
			}
	}
	
	/**********
	 * This absolute value routine sets the sign bit true which has the same effect of doing an 
	 * absolute value on this
	 */
	public void abs() {
		s = true;
	}
	
	/**********
	 * This compareTo routine satisfies the "Comparable" interface
	 */
	public int compareTo(UNumber that) {
		UNumber result = new UNumber(this);
		result.sub(that);
		if (result.s) return 1;
		result = new UNumber(that);
		result.sub(this);
		if (result.s) return -1;
		else return 0;
	}

	/**********
	 * Convert this UNumber to a Double, truncating the least significant digits
	 * 
	 * @return the Double equivalent of the UNumber
	 */
	public double getDouble() {
		int numDigits = 17;							// Doubles are not accurate beyond 17 digits
		if ( d.length < 17 ) numDigits = d.length;	// If the value has fewer, use just that many
		double result = 0;							// Initialize the result and then add the 
		double divisor = 1;							// the value of each digit into the result
		for (int ndx = 0; ndx < numDigits; ndx++) {	// The first digit is between 0/10 and 9/10
			divisor *= 10.0;						// and each following digit deals with the
			result = result + d[ndx]/divisor;		// subsequent negative powers of ten
		}
		return result * Math.pow(10.0, dP);			// Take the sum and multiply it by the 
	}												// appropriate power of ten using dP
	/**********
	 * Get the characteristic
	 * 
	 * @return the characteristic
	 */
	public int getCharacteristic () {
		return dP;
	}
	
	/**********
	 * Get the mantissa
	 * 
	 * @return the mantissa
	 */
	public String getMantissa () {
		String result = "";
		for (int ndx=0; ndx < d.length; ndx++) result += d[ndx];
		return result;
	}
}
