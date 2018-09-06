package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.derby.iapi.sql.PreparedStatement;

public class DataBase {
	private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	public static final String JDBC_URL = "jdbc:derby:mydb;create=true";
	private static int id = 1;
	//public static final String JDBC_URL = "jdbc:derby://localhost:1527/mydb;create=true";
	private static Connection connection;

	public static void createDatabase() throws ClassNotFoundException, SQLException{
		Class.forName(DRIVER);
		connection = DriverManager.getConnection(JDBC_URL);
		//connection.createStatement().execute("update Events set id = 4 where SUBJECT = 'CPU'");



		//Set the id variable to the maximum ID value on the table
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select MAX(ID) from Events");
		if (resultSet.next()){
			setId(Integer.parseInt(resultSet.getString(1)));
			System.out.println("MAX id VALUE IS " + resultSet.getString(1));
		}

		connection.createStatement().execute("create table Events(id int not null, subject varchar(30), location varchar(30), starting_date varchar(15), starting_time varchar(10), ending_date varchar(15), ending_time varchar(10), importance varchar(25), comments varchar(150))");

		System.out.println("Success");
	}

	//public static void addToTable(String subj, String stDate, String stTime, String endDate, String endTime,  String loc, String imp, String com) throws SQLException{
	public static void addNewRow(int id, String subj,  String loc, String stDate, String stTime, String endDate, String endTime,  String imp, String com) throws SQLException, ClassNotFoundException{
		Class.forName(DRIVER);
		/* remove Class declaration ???*/Connection connection = DriverManager.getConnection(JDBC_URL);
		connection.createStatement().execute("insert into Events(id, subject, location, starting_date, starting_time, ending_date, ending_time, importance, comments) values("
				+  id + ", " + "'" + subj + "', " + "'" + loc + "', " + "'" + stDate + "', " + "'" + stTime + "', " + "'" + endDate + "', " + "'" + endTime + "', " + "'" + imp + "', " + "'" + com + "')");
	}

	public static void editTableRow(int id, String subj,  String loc, String stDate, String stTime, String endDate, String endTime,  String imp, String com) throws ClassNotFoundException, SQLException{
		Class.forName(DRIVER);
		connection = DriverManager.getConnection(JDBC_URL);
		connection.createStatement().execute("update Events set subject = '" + subj + "', location = '" + loc + "', starting_date = '" + stDate + "', starting_time = '" + stTime + "', ending_date = '" + endDate + "', ending_time = '" + endTime + "', importance = '" + imp + "', comments = '" + com + "' where id = " + id );
	}

	public static void deleteEvent(int id) throws SQLException{
		connection = DriverManager.getConnection(JDBC_URL);
		connection.createStatement().execute("delete from Events where id = " + id);
	}

	public static void viewTableContents(String table) throws SQLException{
		Connection connection = DriverManager.getConnection(JDBC_URL);
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select * from " + table);
		ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

		int columnCount = resultSetMetaData.getColumnCount();

		for (int i = 1; i <= columnCount; i++){
			System.out.format("%20s", resultSetMetaData.getColumnName(i) + "  |  ");
		}

		while (resultSet.next()){
			System.out.println("");
			for(int j = 1; j <= columnCount; j++){
				System.out.format("%20s", resultSet.getString(j) + "  |  ");

			}
		}
		System.out.println("");
		System.out.println("");
		if (statement != null)
			statement.close();
		if (connection != null)
			connection.close();
	}

	//returns the desired columned item according to the 'id' value given to the EventStageController class in order to be displayed on the stage
	public static String getTableItem(int id, String column) throws SQLException{
		String item = "";
		Connection connection = DriverManager.getConnection(JDBC_URL);
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("Select " + column + " from Events where ID = " +  id);
		if (resultSet.next()){
			item = resultSet.getString(1);
		}
		return item;
	}

	//returns the id of the event closest to the current Date-Time  to the Alarm class
	public static String getNextDateTime() throws SQLException{
		int id = 0;
		StringBuilder dateAndTime = new StringBuilder();
		Connection connection = DriverManager.getConnection(JDBC_URL);
		Statement statement = connection.createStatement();

		ResultSet resultSet = statement.executeQuery("Select id from Events order by starting_date ASC, starting_time ASC");
		if (resultSet.next()){
			id = resultSet.getInt(1);
		}

		resultSet = statement.executeQuery("Select starting_date from Events where ID = " + id);
		if (resultSet.next()){
			dateAndTime.append(resultSet.getString(1)).append(" ");
		}

		resultSet = statement.executeQuery("Select starting_time from Events where ID = " + id);
		if (resultSet.next()){
			dateAndTime.append(resultSet.getString(1));
		}

		return dateAndTime.toString();
	}

	//returns the subject of the event closest o the current Date-Time to the Alarm class
	public static String getEventSubject() throws SQLException{
		int id = 0;
		String subj = "";
		Connection connection = DriverManager.getConnection(JDBC_URL);
		Statement statement = connection.createStatement();

		ResultSet resultSet = statement.executeQuery("Select subject from Events order by starting_date ASC, starting_time ASC");
		if (resultSet.next()){
			subj = resultSet.getString(1);
		}

		return subj;
	}

	public static int getNumberOfRows() throws SQLException{
		int numOfRows = 0;

		Connection connection = DriverManager.getConnection(JDBC_URL);
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("Select count(*) from Events");

		if (resultSet.next()){
			numOfRows = resultSet.getInt(1);
		}
		return numOfRows;
	}

	public static Connection getConnection(){
		return connection;
	}

	public static int getId() {
		return id;
	}

	public static void setId(int id) {
		DataBase.id = id;
	}

}
