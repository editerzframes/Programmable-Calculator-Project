package programmableCalculator;


import java.io.File;
import java.io.IOException;
import java.util.Comparator;

import dictionary.Dictionary;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DefinitionUserInterface {
	/**
	 * <p>
	 * Title: DefinitionUserInterface - A component of the Programmable Calculator Project
	 * </p>
	 *
	 * <p>
	 * Description: A controller object class that implements an editable TableView UI
	 * </p>
	 *
	 * <p>
	 * Copyright: Copyright © 2019
	 * </p>
	 *
	 * @author Lynn Robert Carter, Puneet Garg
	 * @version 1.00	Baseline version 2019-01-26
	 * @version 2.00	Interface with function for the definitions of constants and 
	 *                  variables in the table view 2019-02-26
	 */

	/**********************************************************************************************
	 * 
	 * The DefinitionUserInterface Class provides an experimental platform upon which the user interface
	 * for the a pop-up window implementation of the Variable / Constant Definition Table can be
	 * developed and experiments can be run without dealing with the rest of the Calculator.
	 * 
	 * The following are the primary followed by the secondary attributes for the DefinitionDemo
	 * Class
	 */

	private Label lbl_EditingGuidance = 					// A Label used to guide the user
			new Label("Editing a Table Cell!  When finished, press <enter> or <return> to commit the change.");

	private static boolean whenSorting = false;			// A flag to signal when to ignore case

	private final ObservableList<Quantity> tableData =	// The list of values being defined
			FXCollections.observableArrayList(
					);

	String v = "\n";
	String re = "";
	
	TableColumn<Quantity, String> col_NameValue;
	TableColumn<Quantity, String> col_IsConstantValue;
	TableColumn<Quantity, String> col_MeasureValue;
	TableColumn<Quantity, String> col_ErrorValue;
	TableColumn<Quantity, String> col_UnitsValue;
	
	@SuppressWarnings("rawtypes")
	ManageDefinitions<?> md = new ManageDefinitions();
	Dictionary<String> dictionary = new Dictionary<String>();
	
	TableView<Quantity> table = new TableView<>();
	File getfilename;
	int q;
	CalculatorValue cv = new CalculatorValue();
	
	
	  
    String[] name ;
    String[] corv;
    String[] mv;
    String[] ev;
    String[] u;
    
    // label to show an error if the file is loaded or not
    public static Label loadornot = new Label("File is not Loaded");
    public static TextField filename = new TextField();
    
	/**********
	 * Private local method to initialize the standard fields for a text field
	 */
	private void setupTextUI(TextField t, String ff, double f, double w, Pos p, double x, double y, boolean e){
		t.setFont(Font.font(ff, f));
		t.setMinWidth(w);
		t.setMaxWidth(w);
		t.setAlignment(p);
		t.setLayoutX(x);
		t.setLayoutY(y);		
		t.setEditable(e);
	}
	
	/**********
	 * Private local method to initialize the standard fields for a label
	 */
	private void setupLabelUI(Label l, String ff, double f, Pos p, double x, double y){
		l.setFont(Font.font(ff, f));
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);		
	}

	/**********
	 * This inner class is used to define the various fields required by the variable/constant
	 * definitions.
	 * 
	 * @author lrcarter
	 *
	 */
	public static class Quantity {	
		private final SimpleStringProperty nameValue;			// The name of the value
		private final SimpleStringProperty isConstantValue;		// Specifies if this is a constant
		private final SimpleStringProperty measureValue;		// The measured value
		private final SimpleStringProperty errorTermValue;		// Error term, if there is one
		private final SimpleStringProperty unitsValue;			// Units, if there is one

		/*****
		 * This fully-specified constructor establishes all of the fields of a Quantity object
		 * 
		 * @param n - A String that specifies the name of the constant or variable
		 * @param c - A String that serves as a T or F flag as to where or not this is a constant
		 * @param m - A String that specifies the measured value / value, if there is one
		 * @param e - A String that specifies the error term, if there is one
		 * @param u - A String that specifies the units definition, if there is one
		 */
		public Quantity(String n, String c, String m, String e, String u) {
			this.nameValue = new SimpleStringProperty(n);
			this.isConstantValue = new SimpleStringProperty(c);
			this.measureValue = new SimpleStringProperty(m);
			this.errorTermValue = new SimpleStringProperty(e);
			this.unitsValue = new SimpleStringProperty(u);
		}

		/*****
		 * This getter gets the value of the variable / constant name field - If the whenSorting
		 * flag is true, this method return the String converted to lower case - otherwise, it 
		 * return the String as is
		 * 
		 * NOTE: Be very careful with the name, especially the capitalization as this code 
		 * generates method calls to these routines given the name of the field, it follows
		 * this naming pattern.
		 * 
		 * @return	String - of the name of the variable / constant
		 */
		public String getNameValue() {
			if (whenSorting)
				return nameValue.get().toLowerCase();
			else
				return nameValue.get();
		}

		/*****
		 * This Setter sets the value of the variable / constant name field
		 * 
		 * NOTE: Be very careful with the name, especially the capitalization as this code 
		 * generates method calls to these routines given the name of the field, it follows
		 * this naming pattern.
		 * 
		 * @param n this sets the value in the definitions
		 */
		public void setNameValue(String n) {
			nameValue.set(n);
		}

		/*****
		 * This getter gets the value of the isConstant flag field - If this field is true, the
		 * item being defined is a constant and the calculator will not be allowed to alter the
		 * value (but the calculator's user may editing the value of this item).
		 * 
		 * NOTE: Be very careful with the name, especially the capitalization as this code 
		 * generates method calls to these routines given the name of the field, it follows
		 * this naming pattern.
		 * 
		 * @return	String - Either a "T" or an "F" String
		 */
		public String getIsConstantValue() {
			return isConstantValue.get();
		}

		/*****
		 * This Setter sets the value of the isConstant flag field - If the parameter c starts
		 * with a "T" or a "t", the field is set to "T", else it is set to "F". 
		 * 
		 * NOTE: Be very careful with the name, especially the capitalization as this code 
		 * generates method calls to these routines given the name of the field, it follows
		 * this naming pattern.
		 * 
		 * @param c	String - The first letter is used to determine if this is a "T" or "F"
		 */
		public void setIsConstantValue(String c) {
			if (c.startsWith("T") || c.startsWith("t"))
				isConstantValue.set("T");
			else
				isConstantValue.set("F");
		}

		/*****
		 * This getter gets the value of the measureValue field.
		 * 
		 * NOTE: Be very careful with the name, especially the capitalization as this code 
		 * generates method calls to these routines given the name of the field, it follows
		 * this naming pattern.
		 * 
		 * @return	String - A String of the measuredValue specification is returned
		 */
		public String getMeasureValue() {
			return measureValue.get();
		}

		/*****
		 * This Setter sets the value of the measuredValue field 
		 * 
		 * NOTE: Be very careful with the name, especially the capitalization as this code 
		 * generates method calls to these routines given the name of the field, it follows
		 * this naming pattern.
		 * 
		 * @param m	String - The value is assumed to be a value numeric string. It must be
		 * checked before this routine is used.
		 */
		public void setMeasureValue(String m) {
			measureValue.set(m);
		}

		/*****
		 * This getter gets the value of the errorTerm field.
		 * 
		 * NOTE: Be very careful with the name, especially the capitalization as this code 
		 * generates method calls to these routines given the name of the field, it follows
		 * this naming pattern.
		 * 
		 * @return	String - A String of the errorTerm specification is returned
		 */
		public String getErrorTermValue() {
			return errorTermValue.get();
		}

		/*****
		 * This Setter sets the value of the errorTerm field 
		 * 
		 * NOTE: Be very careful with the name, especially the capitalization as this code 
		 * generates method calls to these routines given the name of the field, it follows
		 * this naming pattern.
		 * 
		 * @param e	String - The value is assumed to be a value numeric string. It must be
		 * checked before this routine is used.
		 */
		public void setErrorTermValue(String e) {
			errorTermValue.set(e);
		}

		/*****
		 * This getter gets the value of the units field.
		 * 
		 * NOTE: Be very careful with the name, especially the capitalization as this code 
		 * generates method calls to these routines given the name of the field, it follows
		 * this naming pattern.
		 * 
		 * @return	String - A String of the units specification is returned
		 */
		public String getUnitsValue() {
			return unitsValue.get();
		}

		/*****
		 * This Setter sets the value of the unitsValue field 
		 * 
		 * NOTE: Be very careful with the name, especially the capitalization as this code 
		 * generates method calls to these routines given the name of the field, it follows
		 * this naming pattern.
		 * 
		 * @param u	String - The value is assumed to be a value units string. It must be
		 * checked before this routine is used.
		 */
		public void setUnitsValue(String u) {
			unitsValue.set(u);
		}
	}

	/*****
	 * This method establishes, defines, and sets up the GUI widgets for the Definition Window,
	 * links these widgets to the appropriate action methods in support of the GUI, and ties this
	 * stage to the primary stage that roots the user interface.
	 * 
	 */
public void setUpDefinitionWindow() {
	// Create new stage for this pop-up window
	Stage dialog = new Stage();

	// Make this window an Application Modal, which blocks all GUI requests to other windows
	dialog.initModality(Modality.APPLICATION_MODAL);
	dialog.setTitle("Definition Table");
	//dialog.initOwner(primaryStage);

	// Establish a new window pane and a TableView widget for that pane
	Pane thePane = new Pane();
	
	Scene dialogScene = new Scene(thePane, 630, 600);	// Define the window width and height

	// Label the first operand error term just, right aligned
	setupLabelUI(loadornot, "Arial", 18, Pos.BASELINE_LEFT, 400, 500);		
    loadornot.setStyle("-fx-text-fill: red; -fx-font-size: 16;");
	  
	// Establish a button to open the file content in the table
	Button btn_open = new Button("Open");
	setupButton(btn_open, 100, 150, 550);
	 btn_open.setDisable(true);
	
	 
	// Establish a button to close this pop-up window
	Button btn_Close = new Button("Close");
	setupButton(btn_Close, 100, 20, 15);
	btn_Close.setOnAction((event) -> { 

		dialog.close(); });

	// Establish a button to add a new row into the TableView into the set of definitions
	Button btn_Add = new Button("Add a new Item");
	setupButton(btn_Add, 150, 140, 15);		
	Button btn_Delete = new Button("Delete an Item");
	btn_Add.setOnAction((event) -> { 

		// Create a new row after last row in the table'
		Quantity q = new Quantity("?", "?", "?", "?", "?");
		tableData.add(q);
		btn_Delete.setDisable(false);
		int row = tableData.size() - 1;

		// Select the row that was just created
		table.requestFocus();
		table.getSelectionModel().select(row);
		table.getFocusModel().focus(row);
	});

	
	setupTextUI(filename, "Arial", 14, 300, Pos.BASELINE_LEFT, 20, 500, true);
	
	// Establish a button to load the file in the table
		Button btn_load = new Button("Load");
		setupButton(btn_load, 100, 20, 550);
		btn_load.setOnAction((event) -> { 

		    FileChooser fileChooser = new FileChooser();

            // Set extension filter
            FileChooser.ExtensionFilter extFilter = 
                    new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);

            // Show open file dialog
            File file = fileChooser.showOpenDialog(dialog);
            if (file != null) {
              System.out.println(file.getPath());
              filename.setText(file.getPath());
            }
        btn_open.setDisable(false);
        getfilename = file;
        
		});
		
	// event handling to load and enter the contents of file in the table
		 btn_open.setOnAction((event) -> { 
			 loadornot.setText("File Loaded");
			 loadornot.setStyle("-fx-text-fill: green; -fx-font-size: 16;");
		      try {
				dictionary.loadFile(getfilename, tableData);
			} catch (IOException e) {
				e.printStackTrace();
			}
        // open the file content and load them
		      openFile();
			 });
		
	// Establish a button to delete a row in the TableView into the set of definitions
	setupButton(btn_Delete, 150, 310, 15);		

	// If there is no data in the table, then disable the Delete Button else enable it
	if (tableData.size()<=0)
		btn_Delete.setDisable(true);
	else
		btn_Delete.setDisable(false);

	// This button handler deals with the various cases that arise when deleting a table row
	btn_Delete.setOnAction((event) -> {
		// Get selected row and delete
		int ix = table.getSelectionModel().getSelectedIndex();
		md.delete(ix, tableData);
		if (table.getItems().size() == 0) {
			btn_Delete.setDisable(true);
			return;
		}
		table.requestFocus();
		table.getSelectionModel().select(ix);
		table.getFocusModel().focus(ix);
	});

	// Make the table editable and position it in the pop-up window
	table.setEditable(true);
	table.setLayoutX(20);
	table.setLayoutY(60);

	//**********//
	// Define each of the columns in the table view and set up the handlers to support editing

	// This is the column that support the Name column. When the name of a definition is changed
	// this code will cause the table of data to be re-sorted and rearranged so the rows will 
	// shown in the table as sorted.
	col_NameValue = new TableColumn<Quantity, String>("Variable/Constant\nName");
	col_NameValue.setMinWidth(130);
	col_NameValue.setCellValueFactory(new PropertyValueFactory<>("nameValue"));
	col_NameValue.setCellFactory(TextFieldTableCell.<Quantity>forTableColumn());

	// When one starts editing a Name column, a message is displayed giving guidance on how to
	// commit the change when done.
	col_NameValue.setOnEditStart((CellEditEvent<Quantity, String> t) -> {
		lbl_EditingGuidance.setVisible(true);
	});

	// When the user commits the change, the editing guidance message is once again hidden and
	// the system sorts the data in the table so the data will always appear sorted in the table
	col_NameValue.setOnEditCommit(
			(CellEditEvent<Quantity, String> t) -> {
				((Quantity) t.getTableView().getItems().get(
						t.getTablePosition().getRow())
						).setNameValue(t.getNewValue());
				whenSorting = true;
				tableData.sort(Comparator.comparing(Quantity::getNameValue));
				whenSorting = false;
				lbl_EditingGuidance.setVisible(false);
			});

	//**********//
	// This is the column that supports the IsConstantValue field.  
	col_IsConstantValue = new TableColumn<Quantity, String>("Is a\nConstant");
	col_IsConstantValue.setMinWidth(75);
	col_IsConstantValue.setCellValueFactory(new PropertyValueFactory<>("isConstantValue"));
	col_IsConstantValue.setCellFactory(TextFieldTableCell.<Quantity>forTableColumn());

	// When one starts editing the IsConstantValue column, a message is displayed giving 
	// guidance on how to commit the change when done.
	col_IsConstantValue.setOnEditStart((CellEditEvent<Quantity, String> t) -> {
		lbl_EditingGuidance.setVisible(true);
	});	

	// When the user commits the change, the editing guidance message is once again hidden and
	// the system sorts the data in the table so the data will always appear sorted in the table
	col_IsConstantValue.setOnEditCommit(
			(CellEditEvent<Quantity, String> t) -> {
				((Quantity) t.getTableView().getItems().get(
						t.getTablePosition().getRow())
						).setIsConstantValue(t.getNewValue());
				lbl_EditingGuidance.setVisible(false);
			});

	//**********//
	// This is the column that supports the MeasureValue field.  
	col_MeasureValue = new TableColumn<Quantity, String>("Measure or Value");
	col_MeasureValue.setMinWidth(175);
	col_MeasureValue.setCellValueFactory(new PropertyValueFactory<>("measureValue"));
	col_MeasureValue.setCellFactory(TextFieldTableCell.<Quantity>forTableColumn());

	// When one starts editing the MeasureValue column, a message is displayed giving 
	// guidance on how to commit the change when done.
	col_MeasureValue.setOnEditStart((CellEditEvent<Quantity, String> t) -> {
		lbl_EditingGuidance.setVisible(true);
	});	

	// When the user commits the change, the editing guidance message is once again hidden and
	// the system sorts the data in the table so the data will always appear sorted in the table
	col_MeasureValue.setOnEditCommit(
			(CellEditEvent<Quantity, String> t) -> {
				((Quantity) t.getTableView().getItems().get(
						t.getTablePosition().getRow())
						).setMeasureValue(t.getNewValue());
				lbl_EditingGuidance.setVisible(false);
			});

	//**********//
	// This is the column that supports the ErrorTermValue field.  
	col_ErrorValue = new TableColumn<Quantity, String>("Error Term");
	col_ErrorValue.setMinWidth(100);
	col_ErrorValue.setCellValueFactory(new PropertyValueFactory<>("errorTermValue"));
	col_ErrorValue.setCellFactory(TextFieldTableCell.<Quantity>forTableColumn());

	// When one starts editing the ErrorTermValue column, a message is displayed giving 
	// guidance on how to commit the change when done.
	col_ErrorValue.setOnEditStart((CellEditEvent<Quantity, String> t) -> {
		lbl_EditingGuidance.setVisible(true);
	});			

	// When the user commits the change, the editing guidance message is once again hidden and
	// the system sorts the data in the table so the data will always appear sorted in the table
	col_ErrorValue.setOnEditCommit(
			(CellEditEvent<Quantity, String> t) -> {
				((Quantity) t.getTableView().getItems().get(
						t.getTablePosition().getRow())
						).setErrorTermValue(t.getNewValue());
				lbl_EditingGuidance.setVisible(false);
			});

	//**********//
	// This is the column that supports the UnitsValue field.  
	col_UnitsValue = new TableColumn<Quantity, String>("Units");
	col_UnitsValue.setMinWidth(100);
	col_UnitsValue.setCellValueFactory(new PropertyValueFactory<>("unitsValue"));
	col_UnitsValue.setCellFactory(TextFieldTableCell.<Quantity>forTableColumn());

	// When one starts editing the UnitsValue column, a message is displayed giving 
	// guidance on how to commit the change when done.
	col_UnitsValue.setOnEditStart((CellEditEvent<Quantity, String> t) -> {
		lbl_EditingGuidance.setVisible(true);
	});			

	// When the user commits the change, the editing guidance message is once again hidden and
	// the system sorts the data in the table so the data will always appear sorted in the table
	col_UnitsValue.setOnEditCommit(
			(CellEditEvent<Quantity, String> t) -> {
				((Quantity) t.getTableView().getItems().get(
						t.getTablePosition().getRow())
						).setUnitsValue(t.getNewValue());
				lbl_EditingGuidance.setVisible(false);
			});

	//**********//
	// The follow sets up the editing guidance text,. positions it below the table, sets the
	// text red, and hides the text so it is only shown during the edit process.
	lbl_EditingGuidance.setMinWidth(600);
	lbl_EditingGuidance.setLayoutX(20);
	lbl_EditingGuidance.setLayoutY(470);
	lbl_EditingGuidance.setTextFill(Color.RED);
	lbl_EditingGuidance.setVisible(false);

	// The right-most three columns are grouped into a single column as they define the value
	// elements of the definition.
	TableColumn<Quantity, String> col_ValueGroup = new TableColumn<Quantity, String>("Value");
	col_ValueGroup.getColumns().add(col_MeasureValue);
	col_ValueGroup.getColumns().add(col_ErrorValue);
	col_ValueGroup.getColumns().add(col_UnitsValue);

	// As we are setting up the GUI, we begin by sorting the data with which we start
	whenSorting = true;
	tableData.sort(Comparator.comparing(Quantity::getNameValue));
	whenSorting = false;

	// This loads the data from the ObservableList into the table, so the TableView code can
	// display it and provide all of the functions that it provides
	table.setItems(tableData);

	// This calls add the three major column titles into the table.  Notice that the right most
	// column is a composite of the three value fields (measure, error term, and units)
	table.getColumns().add(col_NameValue);
	table.getColumns().add(col_IsConstantValue);
	table.getColumns().add(col_ValueGroup);
	
	// button to save the entries in the file 
	Button btn_Save = new Button("Save");
	setupButton(btn_Save, 100, 500, 550);	
	
	btn_Save.setOnAction((event) -> {
		 FileChooser fileChooser = new FileChooser();
		 
         //Set extension filter for text files
         FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
         fileChooser.getExtensionFilters().add(extFilter);

         //Show save file dialog
         File file1 = fileChooser.showSaveDialog(dialog);
         loadornot.setText("File Loaded");
		 loadornot.setStyle("-fx-text-fill: green; -fx-font-size: 16;");
		 
         filename.setText(file1.getAbsolutePath());
         if (file1 != null) {
             saveTextToFile();
         }
        
         md.create(v, file1);
         openFile();
   
	});
	
	// button to update content in the existing loaded file 
	Button btn_update = new Button("Update Existing File");
	setupButton(btn_update, 100, 350, 550);	
	
	btn_update.setOnAction((event) -> {
		saveTextToFile();
		String tftext = filename.getText();
		
		md.update(v, tftext);
	});
	
	 q = table.getItems().size();
	  openFile();
	System.out.println(q);
	// With all of the GUI elements defined and initialized, we add them to the window pane
	thePane.getChildren().addAll(btn_Close, filename, btn_open, btn_load, btn_Add, btn_update, loadornot, btn_Delete, table, btn_Save, lbl_EditingGuidance);
	
	// We set the scene into dialog for this window
	dialog.setScene(dialogScene);
	
	// We show the completed window to the user, making it possible for the user to start
	// clicking on the various GUI widgets in order to make things happen.
	dialog.show(); 
	
	}


/**
 * This method saves the data in the text file with the path chosen by the user.
 */
private void saveTextToFile() {
		 q = table.getItems().size();
		String name;
		String corv;
		String mv;
		String ev;
		String u;
		for(int i = 0; i<q; i++) {
			if(col_NameValue.getCellObservableValue(i).getValue() != null) {
				name = col_NameValue.getCellObservableValue(i).getValue();
				v +=  name;
                System.out.print(col_NameValue.getCellObservableValue(i).getValue());}else {continue;}
			if(col_IsConstantValue.getCellObservableValue(i).getValue() != null) {
				corv = col_IsConstantValue.getCellObservableValue(i).getValue();
				v += " " + corv;
				System.out.print(col_IsConstantValue.getCellObservableValue(i).getValue());}else {continue;}
			if(col_MeasureValue.getCellObservableValue(i).getValue() != null) {
				mv = col_MeasureValue.getCellObservableValue(i).getValue();
				v += " " + mv;
				System.out.print(col_MeasureValue.getCellObservableValue(i).getValue());}else {continue;}
			if(col_ErrorValue.getCellObservableValue(i).getValue() != null) {
				ev = col_ErrorValue.getCellObservableValue(i).getValue();
				v += " " + ev;
				System.out.print(col_ErrorValue.getCellObservableValue(i).getValue());}else {continue;}
			if(col_UnitsValue.getCellObservableValue(i).getValue() != null) {
				u = col_UnitsValue.getCellObservableValue(i).getValue();
				v += " " + u;
				System.out.print(col_UnitsValue.getCellObservableValue(i).getValue());}else {continue;}
			v += " "+ "\n";
			System.out.println("");
	}
}

/**
 * This method return the method content and provides the entries to the arrays
 * @param p this is the number of values in the table list.
 * @param name1 this is the names of the constants or variables
 * @param corv1 this defines whether the value is a constant or variable
 * @param mv1 this provides the measure value of the constant or variable
 * @param ev1 this provides the error term value of constant or variable
 * @param u1 this provides the unit of the constant or variable
 * 
 */
public void getTableSize(int p, String[] name1, String[] corv1, String[] mv1, String[] ev1, String[] u1){
		
		p = q;
		name1 = new String[q];
		corv1 = new String[q];
		mv1 = new String[q];
		ev1 = new String[q];
		u1 = new String[q];
		
		name1 = name;
		corv1 = corv;
		mv1 = mv;
		ev1 = ev;
		u1 = u;
	}

/**
 * This method provides the file from the path given in the textfield.
 * 
 * @return name of the file
 */
public File getTextFieldData() {
	File file = new File(filename.getText());
	return file;
}


	
/**
 * This method helps to save the new variable from the result into the existing loaded file
 * 
 * @param name - name of the variable
 * @param mv - measured value from the result textfield
 * @param ev - error term from the result
 * @param u - units of the result
 */
public void saveNewVar(String name, String mv, String ev, String u) {
		
		saveTextToFile();
		String tftext = filename.getText();
		v +=  name;
		
	    v += " " + "F";
			
		v += " " + mv;
				
	    v += " " + ev;
			
		v += " " + u;
				
		md.update(v, tftext);
	
	}
	
/**
 * This methods opens the content from the text file and loads them into the table.
 * 
 */
public void openFile(){
		  q = table.getItems().size();
		     
	     name = new String[q];
	     corv = new String[q];
	     mv = new String[q];
	     ev = new String[q];
	     u = new String[q];
		
		for(int i = 0; i<q; i++) {
			if(col_NameValue.getCellObservableValue(i).getValue() != null) {
				name[i] = col_NameValue.getCellObservableValue(i).getValue();
				re +=  name[i];
                System.out.print(name[i]);}else {continue;}
			if(col_IsConstantValue.getCellObservableValue(i).getValue() != null) {
				corv[i] = col_IsConstantValue.getCellObservableValue(i).getValue();
				re += " " + corv[i];
				System.out.print(corv[i]);}else {continue;}
			if(col_MeasureValue.getCellObservableValue(i).getValue() != null) {
				mv[i] = col_MeasureValue.getCellObservableValue(i).getValue();
				re += " " + mv[i];
				System.out.print(mv[i]);}else {continue;}
			if(col_ErrorValue.getCellObservableValue(i).getValue() != null) {
				ev[i] = col_ErrorValue.getCellObservableValue(i).getValue();
				re += " " + ev[i];
				System.out.print(ev[i]);}else {continue;}
			if(col_UnitsValue.getCellObservableValue(i).getValue() != null) {
				u[i] = col_UnitsValue.getCellObservableValue(i).getValue();
				re += " " + u[i];
				System.out.print(u[i]);}else {continue;}
			re += " "+ "\n";
			System.out.println("");
	   
		}
	
		// calls the method in the calculator value to provide the values from the table
	    cv.getTableSize(q, name, corv, mv, ev, u);  
	    getTableSize(q, name, corv, mv, ev, u);
		
	}

	
	/*****
	 * The setupButton method is used to factor out recurring text in order to speed the coding and
	 * make it easier to read the code.
	 * 
	 * @param b	- Button that specifies which button is being set up
	 * @param w - int that specifies the minimum width of the button
	 * @param x - int that specifies the left edge of the button in the window
	 * @param y - int that specifies the upper edge of the button in the window
	 */
	private void setupButton(Button b, int w, int x, int y) {
		b.setMinWidth(w);
		b.setAlignment(Pos.BASELINE_CENTER);
		b.setLayoutX(x);
		b.setLayoutY(y);
	}

/**
 * this methods returns the whole tables entries
 * @return return the table
 */
	public boolean get_table() {
		if(tableData.isEmpty())
			;
		return false;
	}

}
