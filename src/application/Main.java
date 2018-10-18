package application;

import java.sql.SQLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {


	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("MainStage.fxml"));
			primaryStage.setTitle("Reminder");
			primaryStage.setScene(new Scene(root));
			primaryStage.setResizable(true);
			primaryStage.setMaximized(true);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws ClassNotFoundException {

        try{
        	DataBase.createDatabase();
        //An exception is thrown here during initialization, if a table already exists
        }catch(SQLException e){			
        	System.out.println("SQL exception, probably cause table already exists");
        }catch(ClassNotFoundException e){
			e.printStackTrace();
		}
        Class.forName("application.Alarm");
		launch(args);

	}
}
