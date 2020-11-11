package programmableCalculator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;

import dictionary.Dictionary;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import programmableCalculator.DefinitionUserInterface;
import programmableCalculator.DefinitionUserInterface.Quantity;


/**
 * <p>
 * Title: Dictionary Class - A component of the Dictionary system
 * </p>
 *
 * <p>
 * Description:this class follows the concept of CRUD to manage the definitions in the calculator.
 * </p>
 *
 * <p>
 * Copyright: Copyright Â© 2018-05-05
 * </p>
 *
 * @author Puneet Garg
 * @version 2.00 - Baseline for transition from Swing to JavaFX 2018-05-07
 * @version 2.01 - Installed in PKMT Tool (completely functioning) 2018-11-14
 * @version 2.02 - Creating an generic API and testing in PKMT 2019-02-05
 * @version 4.80    2019-02-26 Project 1 Part 1 with table view consist of definitions (Data Structures).
 */

public class ManageDefinitions <T>{

	Dictionary<String> dictionary;
	/**********************************************************************************************

	Constructor

	 **********************************************************************************************/

	/**********
	 * This is the default constructor.
	 */
	
	public ManageDefinitions(){
		return;
	}
	
	/**
	 * This method is used to create a new file at the given location with the data from the table view
	 * @param v this is the content that needs to be written in the file
	 * @param file this provides the name of the file that is entered by the user
	 */
	public void create(String v, File file) {
		dictionary = new Dictionary<String>();
		dictionary.create(v, file);
	}
	
/**
 * This method is to read the content from the file and load it into the table view.
 * 
 * @param Operand1  get the operand1 value
 * @param Operand1Errorterm  get the operand 1 error term
 * @param combobox  find the value from comboBox
 * @throws NoSuchElementException throws the exception if no element is found
 * @throws FileNotFoundException  throws the file not found exception
 */
	public void read(TextField Operand1, TextField Operand1Errorterm, ComboBox<String> combobox) throws NoSuchElementException, FileNotFoundException {
			DefinitionUserInterface a = new DefinitionUserInterface();
			File name = a.getTextFieldData();

			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(name);
			while (scanner.nextLine() != null) {
				while (scanner.hasNextLine()) {
					try {
						String[] tokens = scanner.nextLine().split(" ");
						if (Operand1.getText().matches(tokens[0])) {
							Operand1.setText(tokens[2]);
							Operand1Errorterm.setText(tokens[3]);
							combobox.getSelectionModel().select(tokens[4]);
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						e.printStackTrace();
					}
				}
				break;
			}
		}

	/**
	 * This method is used to update the entries in the table view in the already loaded file
	 * 
	 * @param v  the whole content from the table
	 * @param file  path of the file selected
	 */
	public void update(String v, String file) {
		
		 try {
	           
	            PrintWriter bf = new PrintWriter(file);
	            bf.flush();
	               bf.write(v);
	               bf.close();
	           } catch (IOException e) {
	               e.printStackTrace();
	           }
	}
	
	/**
	 * This method is used to delete the selected entry from the table view
	 * 
	 * @param ix  selected entry from table
	 * @param tableData tabledata from table
	 */
	public void delete(int ix, ObservableList<Quantity> tableData) {
		if (ix <= -1) return;
		tableData.remove(ix);
		
		if (ix != 0) {ix = ix -1;}
	}
	
}