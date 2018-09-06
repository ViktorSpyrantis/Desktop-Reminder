package application;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;



public class MainStageController {

	private static ObservableList<Events> eventsList = FXCollections.observableArrayList();
	private static int id = 0;
	private static boolean newEvent = false;		//a boolean value to help the EventStageController class to check which button was pressed
	private static boolean isRowSelected = false;
	PopUpWindow popUpWindow = new PopUpWindow();

	@FXML
	private TableView<Events> tableView = new TableView<>();;
	@FXML
	private TableColumn<Events, String> subjectColumn;// = new TableColumn<>("Subject");
	@FXML
	private TableColumn<Events, String> startingDateColumn;// = new TableColumn<>("Date");
	@FXML
	private TableColumn<Events, String> startingTimeColumn;// = new TableColumn<>("Time");
	@FXML
	private TableColumn<Product, String> locationColumn;// = new TableColumn<>();
	@FXML
	private TableColumn<Product, String> importanceColumn;// = new TableColumn<>();




	@FXML
	void initialize() throws SQLException{

		subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
		startingDateColumn.setCellValueFactory(new PropertyValueFactory<>("startingDate"));
		startingTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startingTime"));
		locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
		importanceColumn.setCellValueFactory(new PropertyValueFactory<>("importance"));

		setTableView();

	}


	public void addEventClick(){
		setNewEvent(true);
		try {
			Stage stage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("EventStage.fxml"));
			root.getStyleClass().clear();
			root.getStyleClass().add("application.css");
			stage.setTitle("Add Event");
			stage.setScene(new Scene(root,600,400));
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void editEventClick(){
		Events ev = tableView.getSelectionModel().getSelectedItem();
		setId(ev.getId());

		setNewEvent(false);
		try {
			Stage stage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("EventStage.fxml"));
			root.getStyleClass().clear();
			root.getStyleClass().add("application.css");
			stage.setTitle("Edit Event");
			stage.setScene(new Scene(root,600,400));
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void eraseEventClick() throws SQLException, IOException {
		Events ev = tableView.getSelectionModel().getSelectedItem();
		//tableView.refresh();
		PopUpWindow.deleteEventPopUp("Do you wish to delete the selected event?");
		if(PopUpWindow.getWillErase()) {
			System.out.println("fffff" + PopUpWindow.getWillErase());
			DataBase.deleteEvent(ev.getId());
			tableView.getItems().remove(ev);
		};


	}

	public void setTableView() throws SQLException{
		System.out.println("hey bb");
		//tableView.refresh();
		for ( int i = 0; i<tableView.getItems().size(); i++) {
			tableView.getItems().clear();
		}
		eventsList.clear();
		Connection connection = DriverManager.getConnection(DataBase.JDBC_URL);
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select * from Events");
		ResultSetMetaData meta = resultSet.getMetaData();
		final int columnCount = meta.getColumnCount();
		List<List<String>> rowList = new LinkedList<List<String>>();

		eventsList.clear();

		while (resultSet.next()){

		    List<String> columnList = new LinkedList<String>();
		    rowList.add(columnList);
		    Events event = new Events();
		    for (int column = 1; column <= columnCount; column++){
		        String value = resultSet.getString(column);
		        columnList.add(String.valueOf(value));
		        //System.out.println(value);
		        StringBuilder columnName = new StringBuilder(meta.getColumnName(column));
		        //System.out.println(columnName);

		        switch(columnName.toString()){
		        	case "ID":
		        		event.setId(Integer.parseInt(value));
		        		break;
		        	case "SUBJECT":
		        		event.setSubject(value.toString());
		        		break;
		        	case "LOCATION":
		        		event.setLocation(value.toString());
		        		break;
		        	case "STARTING_DATE":
		        		event.setStartingDate(value.toString());
		        		break;
		        	case "STARTING_TIME":
		        		event.setStartingTime(value.toString());
		        		break;
		        	case "ENDING_DATE":
		        		event.setEndingDate(value.toString());
		        		break;
		        	case "ENDING_TIME":
		        		event.setEndingTime(value.toString());
		        		break;
		        	case "IMPORTANCE":
		        		event.setImportance(value.toString());
		        		break;
		        	case "COMMENTS":
		        		event.setComments(value.toString());
		        		break;
		        }

		        //System.out.print(event.getEndingDate());
		        //System.out.println("   " + columnName);
		        columnName.setLength(0);				//erasing the contents of the StringBuilder
		    }
		    tableView.getItems().addAll(event);
		    eventsList.add(event);

		}

		// add the rowList to the request.

		tableView.setItems(eventsList);			//Sets the default products (laptop/popcorn etc...)
	}
/*
	public void addToTableView(int lastId) throws SQLException {
		tableView.refresh();
		Connection connection = DriverManager.getConnection(DataBase.JDBC_URL);
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select * from Events where id = " + lastId);
		ResultSetMetaData meta = resultSet.getMetaData();
		final int columnCount = meta.getColumnCount();
		List<List<String>> rowList = new LinkedList<List<String>>();

		while (resultSet.next()){
		//resultSet.next();
		//if (resultSet.last()){
		    List<String> columnList = new LinkedList<String>();
		    rowList.add(columnList);
		    Events event = new Events();
		    for (int column = 1; column <= columnCount; column++){
		        String value = resultSet.getString(column);
		        columnList.add(String.valueOf(value));
		        //System.out.println(value);
		        StringBuilder columnName = new StringBuilder(meta.getColumnName(column));
		        //System.out.println(columnName);

		        switch(columnName.toString()){
	        		case "ID":
	        			event.setId(Integer.parseInt(value));
	        			break;
		        	case "SUBJECT":
		        		event.setSubject(value.toString());
		        		break;
		        	case "LOCATION":
		        		event.setLocation(value.toString());
		        		break;
		        	case "STARTING_DATE":
		        		event.setStartingDate(value.toString());
		        		break;
		        	case "STARTING_TIME":
		        		event.setStartingTime(value.toString());
		        		break;
		        	case "ENDING_DATE":
		        		event.setEndingDate(value.toString());
		        		break;
		        	case "ENDING_TIME":
		        		event.setEndingTime(value.toString());
		        		break;
		        	case "IMPORTANCE":
		        		event.setImportance(value.toString());
		        		break;
		        	case "COMMENTS":
		        		event.setComments(value.toString());
		        		break;
		        }

		        //System.out.print(event.getEndingDate());
		        //System.out.println("   " + columnName);
		        columnName.setLength(0);				//erasing the contents of the StringBuilder
		    }
		    tableView.getItems().addAll(event);
		    eventsList.add(event);

		}

		// add the rowList to the request.
		tableView.setItems(eventsList);
	}
*/

	@FXML
    private void viewContentsClick() throws SQLException{
    	DataBase.viewTableContents("Events");
    }

	@FXML
	private void alarmButtonClick(){

	}

	public static boolean isNewEvent() {
		return newEvent;
	}

	private static void setNewEvent(boolean newEvent) {
		MainStageController.newEvent = newEvent;
	}

	public static int getId() {
		return id;
	}

	private static void setId(int id) {
		MainStageController.id = id;
	}






}
