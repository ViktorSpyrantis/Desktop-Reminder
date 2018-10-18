package application;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EventStageController {

	@FXML
	private TextField subjectTF = new TextField();
	@FXML
	private TextField locationTF = new TextField();
	@FXML
	private DatePicker startingDatePicker = new DatePicker(LocalDate.now());
	@FXML
	private TextField startingHourTF = new TextField();
	@FXML
	private TextField startingMinTF = new TextField();
	@FXML
	private ComboBox<String> startingTimeCB = new ComboBox<>();
	@FXML
	private DatePicker endingDatePicker = new DatePicker(LocalDate.now());
	@FXML
	private TextField endingHourTF = new TextField();
	@FXML
	private TextField endingMinTF = new TextField();
	@FXML
	private ComboBox<String> endingTimeCB = new ComboBox<>();
	@FXML
	private ComboBox<String> importanceCB = new ComboBox<>();
	@FXML
	private TextArea commentsTA = new TextArea();


	MainStageController mainCon = new MainStageController();
	ErrorCheck errorCheck = new ErrorCheck();
	Date currentDate = new Date();
	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");



	@FXML
    private void initialize() throws IOException, SQLException {
		startingTimeCB.getItems().addAll("am", "pm");
		endingTimeCB.getItems().addAll("am", "pm");
		importanceCB.getItems().addAll("low", "medium", "high");
		startingDatePicker.getEditor().setEditable(false);
		startingDatePicker.setDisable(false);

		//sets a default value on all the ComboBoxes when a new event is to be created
		if(MainStageController.isNewEvent()){
			startingTimeCB.getSelectionModel().selectFirst();
			endingTimeCB.getSelectionModel().selectFirst();
			importanceCB.getSelectionModel().selectFirst();
			startingDatePicker.setValue(getDate(formatter.format(currentDate)));
			endingDatePicker.setValue(getDate(formatter.format(currentDate)));
		}


		//sets the time TextFields's values to be numeric and 2-digit only
		setTimeFormatter(startingHourTF);
		setTimeFormatter(startingMinTF);
		setTimeFormatter(endingHourTF);
		setTimeFormatter(endingMinTF);

		//sets the location and subject TextFields's values to a max size of 30
		setLocAndSubjFormatter(subjectTF);
		setLocAndSubjFormatter(locationTF);

		//sets the comments TextArea's value to a max size of 150
		setCommentsFormatter(commentsTA);


		//Setting all the FXML items when 'Edit Event' button is pressed
		if(!MainStageController.isNewEvent()){

			int id = MainStageController.getId();
			System.out.println("ID =  --------" + id);
			subjectTF.setText(DataBase.getTableItem(id, "subject"));
			locationTF.setText(DataBase.getTableItem(id, "location"));

			startingDatePicker.setValue(getDate(DataBase.getTableItem(id, "STARTING_DATE")));
			String startingTime = DataBase.getTableItem(id, "starting_time");
			String startingHour = startingTime.substring(0, startingTime.indexOf(':'));
			startingHourTF.setText(startingHour);
			String startingMin = startingTime.substring(startingTime.indexOf(":") + 1);
			startingMin = startingMin.substring(0, startingMin.indexOf(" "));
			startingMinTF.setText(startingMin);
			startingTimeCB.setValue(startingTime.substring(startingTime.length() - 2));

			endingDatePicker.setValue(getDate(DataBase.getTableItem(id, "ENDING_DATE")));
			String endingTime = DataBase.getTableItem(id, "ending_time");
			String endingHour = endingTime.substring(0, endingTime.indexOf(':'));
			endingHourTF.setText(endingHour);
			String endingMin = endingTime.substring(endingTime.indexOf(":") + 1);
			endingMin = endingMin.substring(0, endingMin.indexOf(" "));
			endingMinTF.setText(endingMin);
			endingTimeCB.setValue(endingTime.substring(endingTime.length() - 2));

			importanceCB.setValue(DataBase.getTableItem(id, "importance"));
			commentsTA.setText(DataBase.getTableItem(id, "comments"));
		}

    }


	
	//the 'Save' button will add a new event to the DataBase table if the 'Add Event' button is pressed and it will modify an existing event if the 'Edit Event' button is pressed

	public void saveButtonClick() throws SQLException, ClassNotFoundException, IOException, ParseException{
		Stage stage = (Stage) startingTimeCB.getScene().getWindow();
		boolean errorsExist;// = false;

		//checking for any errors on the values
		errorsExist = errorCheck.checkForErrors(subjectTF.getText(), locationTF.getText(), startingDatePicker.getValue(), startingHourTF.getText(), startingMinTF.getText(), startingTimeCB.getValue(), endingDatePicker.getValue(), endingHourTF.getText(), endingMinTF.getText(), endingTimeCB.getValue());

		//if there are not any errors on the data values the database is altered accordingly to the button which was pressed
		if(!errorsExist){
			if(MainStageController.isNewEvent()){
				int currentId = DataBase.getId();
				DataBase.addNewRow(currentId + 1, subjectTF.getText(), locationTF.getText(), startingDatePicker.getValue().toString(), startingHourTF.getText() + ":" + startingMinTF.getText() + " " + startingTimeCB.getValue(), endingDatePicker.getValue().toString(), endingHourTF.getText() + ":" + endingMinTF.getText() + " " +endingTimeCB.getValue(), importanceCB.getValue(), commentsTA.getText() + "");
				DataBase.setId(currentId + 1);			//id value must be incremented by 1
				DataBase.viewTableContents("Events");

				stage.close();
				//mainCon.addToTableView(DataBase.getId());		//the new table row is added to the tableView
				mainCon.setTableView();
			}else{
				int id = MainStageController.getId();
				DataBase.editTableRow(id, subjectTF.getText(), locationTF.getText(), startingDatePicker.getValue().toString(), startingHourTF.getText() + ":" + startingMinTF.getText() + " " + startingTimeCB.getValue(), endingDatePicker.getValue().toString(), endingHourTF.getText() + ":" + endingMinTF.getText() + " " +endingTimeCB.getValue(), importanceCB.getValue(), commentsTA.getText() + "");
				DataBase.viewTableContents("Events");
				mainCon.setTableView();
				stage.close();
			}
		}
	}

	private static LocalDate getDate(String dateString){
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate localDate = LocalDate.parse(dateString, formatter);
	    return localDate;
	}

	
	//the method that makes a textfield accept 2-character and numeric values only
	private void setTimeFormatter(TextField tf){
		tf.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	            if (!newValue.matches("\\d*")) {
	            	tf.setText(newValue.replaceAll("[^\\d]", ""));
	            }
	            if (tf.getText().length() > 2) {
	                String s = tf.getText().substring(0, 2);
	                tf.setText(s);
	            }
	        }
	    });
	}

	private void setLocAndSubjFormatter(TextField tf){
		tf.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

	            if (tf.getText().length() > 30) {
	                String s = tf.getText().substring(0, 30);
	                tf.setText(s);
	            }
	        }
	    });
	}

	private void setCommentsFormatter(TextArea tf){
		tf.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

	            if (tf.getText().length() > 150) {
	                String s = tf.getText().substring(0, 150);
	                tf.setText(s);
	            }
	        }
	    });
	}
}
