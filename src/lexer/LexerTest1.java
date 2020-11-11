package lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LexerTest1 {
	private static Scanner keyboard = new Scanner(System.in);

	private static Scanner theReader = null;
	
	private static String fileName = null;

	public static void main(String[] args) {

		while (theReader == null) {
			System.out.println();
			System.out.println("Please enter the name of the data file.");
			System.out.println();
			System.out.print("Data file: ");
			fileName = keyboard.nextLine();
			try {
				theReader = new Scanner (new File(fileName));
			} catch (FileNotFoundException e) {
				System.out.println();
				System.out.println("File <" + fileName + "> was not found.");
			}
		}

		System.out.println("The File <" + fileName + "> is ready for input.");
		
		Lexer l = new Lexer(theReader);
		System.out.println(l);

	}

}
