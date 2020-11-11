package programmableCalculator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import dictionary.Dictionary;
import expressionTreeBuilderEvaluator.ExpressionTreeBuilderEvaluator;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * <p> Title: ProgramUI Class. </p>
 * 
 * <p> Description: A component of a JavaFX demonstration application that displays a window to enter
 *                  various expressions and can allow to perform various actions</p>
 * 
 * <p> Copyright: Puneet Garg © 2019 </p>
 * 
 * @author  Puneet Garg
 * 
 * @version 4.90    2019-03-31 Project 1 - Part 2 with a expression window to compute the expressions with constants and variables.
 * 
 */

public class ProgramUI {


	private final double BUTTON_WIDTH = 60;
	
	Dictionary<String> d = new Dictionary<String>();
	
	// Pop-up window for expressions
		public static TextArea parser = new TextArea();
		public static TextArea exp_result = new TextArea();
		TextField exp_filename = new TextField();
		public static Label exp_error = new Label();
		Button exp_create = new Button("Create");
		Button exp_load = new Button("Load");
		Button exp_edit = new Button("Edit");
		Button exp_delete = new Button("Delete");
		private ComboBox<String> exp_list = new ComboBox<String>();
		Button exp_run = new Button("Run");
		Button exp_debug = new Button("Debug");
		String currentFile;
		Button exp_save = new Button("Save");
		File getfilename;
		Label exp_outlabel = new Label("Output:");
		
		
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
	 * Private local method to initialize the standard fields for a button
	 */
	private void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);		
	}
	/**********
	 * Private local method to initialize the standard fields for a text area
	 */
	private void setupTextAreaUI(TextArea a, String ff, double f, double w, double h, double x, double y, boolean e){
		a.setFont(Font.font(ff, f));
		a.setMinWidth(w);
		a.setMaxWidth(w);
		a.setMinHeight(h);
		a.setMaxHeight(h);
		a.setLayoutX(x);
		a.setLayoutY(y);		
	    a.setEditable(e);
		}
	
	/**********
	 * Private local method to initialize the standard fields for a label
	 */
	private void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);		
	}
	/*****
	 * This method establishes, defines, and sets up the GUI widgets for the Expression Window,
	 * links these widgets to the appropriate action methods in support of the GUI, and ties this
	 * stage to the primary stage that roots the user interface.
	 * 
	 */
	public void setUpDefinitionWindow1() {
		// Create new stage for this pop-up window
		Stage dialog = new Stage();

		// Make this window an Application Modal, which blocks all GUI requests to other windows
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.setTitle("Expression Window");
		  setupTextAreaUI(parser, "Arial", 14, 500, 255, 30, 70, false);
		  setupTextAreaUI(exp_result, "Times Roman New", 14, 500, 255, 30, 390, true);
		// Establish an error message for the second operand just above it with, left aligned		
		setupLabelUI(exp_error, "Times Roman New", 14, Calculator.WINDOW_WIDTH-10, Pos.BASELINE_LEFT, 30, 40);
		exp_error.setTextFill(Color.RED);
		  setupTextUI(exp_filename, "Arial", 14, 220, Pos.BASELINE_LEFT, 20, 10, true);
		  setupButtonUI(exp_create, "Arial", 14, BUTTON_WIDTH, Pos.BASELINE_LEFT, 250,10);
		  setupButtonUI(exp_load, "Arial", 14, BUTTON_WIDTH, Pos.BASELINE_LEFT, 520,10);
		  setupButtonUI(exp_edit, "Arial", 14, BUTTON_WIDTH, Pos.BASELINE_LEFT, 550,70);
		  exp_edit.setDisable(true);
		  setupButtonUI(exp_delete, "Arial", 14, BUTTON_WIDTH, Pos.BASELINE_LEFT, 550,120);
		  exp_list.setLayoutX(400); exp_list.setLayoutY(10);
		  setupButtonUI(exp_run, "Arial", 14, BUTTON_WIDTH, Pos.BASELINE_LEFT, 550,390);
		  setupButtonUI(exp_debug, "Arial", 14, BUTTON_WIDTH, Pos.BASELINE_LEFT, 550,430);
		  exp_run.setDisable(true);exp_debug.setDisable(true);
		  setupButtonUI(exp_save, "Arial", 14, BUTTON_WIDTH, Pos.BASELINE_LEFT, 550,290);
		  exp_save.setDisable(true);
		  setupLabelUI(exp_outlabel, "Arial", 14, Calculator.WINDOW_WIDTH-10, Pos.BASELINE_LEFT, 30, 360);

	       // adding files to the list
			  File[] files = new File("RespositoryOut/").listFiles();
			  //If this pathname does not denote a directory, then listFiles() returns null. 
			  for (File file : files) {
			      if (file.isFile()) {
			          exp_list.getItems().add(file.getName());
			      }
			  }
			 
		  
exp_load.setOnAction((event) -> {
	String theSelectedItemOP1 =  (String) exp_list.getSelectionModel().getSelectedItem();
	currentFile = theSelectedItemOP1;
 
    Dictionary<String> d= new Dictionary<String>();
    FileReader n = null;
      try {
	       n = new FileReader("RespositoryOut/" + theSelectedItemOP1);
           } catch (FileNotFoundException e1) {
	          e1.printStackTrace();
               }
           String c = null;
              try {
 	             parser.setText(d.load(n, c));
                 } catch (IOException e) {
	            e.printStackTrace();
                 }
   
    exp_edit.setDisable(false);exp_run.setDisable(false);exp_debug.setDisable(false);  
			 
			 
});
		  
 exp_create.setOnAction((event) -> { 
	 if(exp_filename.getText().trim().isEmpty()) {
		 exp_error.setText("Please Enter file name first");
	      	}
	      	else {
	      		
	      	 parser.setEditable(true);
	      	 parser.requestFocus();
	      	 exp_save.setDisable(false);
			   // Create a new Repository in the program
	      	File theDirectory = new File("RespositoryOut");
	      	if (!theDirectory.exists()) {       // checks whether the repository already exists or not
	      	theDirectory.mkdir();
	      	theDirectory.setReadable(true);
	      	theDirectory.setWritable(true);
	      	System.out.println("The directory was created");     // Message is displayed on console when the repository is created
	      	}
	      	//System.out.println(exp_filename.getText());
	  
	      	String fn =exp_filename.getText();
	      	currentFile = fn;
	      	exp_list.getItems().add(fn);
	      	File theDataFile = new File(fn);      // Create a new file
	      	if (theDataFile.exists())
	      	System.out.println("The Dictionary file already exists");
	      	else
	      	System.out.println("The Dictionary file does not exist");
	      	
	      	try {
				FileWriter file = new FileWriter( "RespositoryOut/" + fn);
			} catch (IOException e) {
				e.printStackTrace();
			}
	      	}
	      	
	 });
		  
 exp_delete.setOnAction((event) -> { 
			  String theSelectedItemOP1 =  (String) exp_list.getSelectionModel().getSelectedItem();
			  File file = new File("RespositoryOut/" + theSelectedItemOP1); 
			  
			  file.delete();
			  exp_list.getItems().remove(theSelectedItemOP1);
  });
 
 exp_edit.setOnAction((event) -> { 
	      parser.setEditable(true);
	      parser.requestFocus();
	      exp_save.setDisable(false);exp_run.setDisable(true);exp_debug.setDisable(true);
});
		  
  exp_save.setOnAction((event) -> { 
				String contentSave = parser.getText();
				 FileWriter n = null;
				
				 	try {
						n = new FileWriter("RespositoryOut/" + currentFile);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				
				 	try {
						d.save(n, (String)contentSave);
					} catch (IOException e) {
						e.printStackTrace();
					}
		             
	exp_run.setDisable(false);
	exp_debug.setDisable(false);
	exp_edit.setDisable(false);exp_save.setDisable(true);
  });
		  
  exp_debug.setOnAction((event) -> { 
	  exp_result.setText("");
			  if(parser != null) {
				  String theExpression = parser.getText();
				  Scanner theReader = new Scanner (theExpression);
				  ExpressionTreeBuilderEvaluator.debug(theExpression, theReader);
			  }
			  
  });
		  
  exp_run.setOnAction((event) -> { 
	  exp_result.setText("");
			  if(parser != null) {
				  String theExpression = parser.getText();
				  Scanner theReader = new Scanner (theExpression);
				  ExpressionTreeBuilderEvaluator.mainmethod(theExpression, theReader);
			  }
				
 });
		// Establish a new window pane and a TableView widget for that pane
		Pane thePane = new Pane();

		Scene dialogScene = new Scene(thePane, 630, 650);	// Define the window width and height


		// With all of the GUI elements defined and initialized, we add them to the window pane
		thePane.getChildren().addAll(parser, exp_create, exp_outlabel, exp_load, exp_edit, exp_delete, exp_error, exp_result, exp_list, exp_debug, exp_run, exp_filename, exp_save);
		
		// We set the scene into dialog for this window
		dialog.setScene(dialogScene);
		
		// We show the completed window to the user, making it possible for the user to start
		// clicking on the various GUI widgets in order to make things happen.
		dialog.show(); 
		
		}
}
