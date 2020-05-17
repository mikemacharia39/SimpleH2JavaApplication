package testConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.h2.tools.Server;

public class TestH2Connection {

	static Connection conn;
	static String connectionName;
	Statement statement;
	Server server;
	public void setConnection() {
	
		//connectionName = "jdbc:h2:tcp://localhost/file:C:/Program Files/SimpleSolution/ssposh2db";
		//connectionName = "jdbc:h2:file:C:/Program Files/Java/testH2";
		//connectionName = "jdbc:h2:tcp://localhost/~/H2Databases/testH2";
		//connectionName = "jdbc:h2:~/H2Databases/ssposh2db";
		
		try {
			server = Server.createTcpServer().start();
			
			Class.forName("org.h2.Driver").newInstance();
			conn = DriverManager.getConnection(connectionName, "root", "h2password");
			
			System.out.println("Successfully connected!!");
			
			statement = conn.createStatement();
			
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void closeConnection() {
		try {
			if(!conn.isClosed()) {
				conn.close();
				server.stop();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create table statement
	 */
	private void runCreateStatement() {
		String createStatement = "CREATE TABLE IF NOT EXISTS testCountries ("
				+ "countryID int(11) NOT NULL AUTO_INCREMENT, "
				+ "abbr varchar(5) NOT NULL, "
				+ "name varchar(80) NOT NULL, "
				+ "countryName varchar(80) NOT NULL, "
				+ "ISO varchar(3) DEFAULT NULL, "
				+ "numCode smallint(6) DEFAULT NULL, "
				+ "phonecode int(5) NOT NULL, "
				+ "PRIMARY KEY (countryID))";
		
		try {
			statement.executeUpdate(createStatement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Insert
	 */
	private void runInsertStatement() {
		String createInsertQuery = ""
				+ "INSERT INTO `testCountries` (`countryID`, `abbr`, `name`, `countryName`, `ISO`, `numCode`, `phoneCode`) VALUES" + 
				"(NULL, 'AL', 'ALBANIA', 'Albania', 'ALB', 8, 355);";
		
		try {
			statement.executeUpdate(createInsertQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Select H2 Data
	 */
	private void runSelectStatement() {
		String selectDataQuery = "SELECT * FROM testCountries where countryID IN (2,3,5,6)";
		
		try {
			ResultSet rs = statement.executeQuery(selectDataQuery);
			String results = "Country Name \t ISO \t phoneCode\t \n";
			while(rs.next()) {
				results += rs.getString("countryName") + "\t\t" + rs.getString("ISO") + "\t" + rs.getString("phoneCode") + "\n";
			}
			System.out.println(results);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static void main(String[] args) {
		
		TestH2Connection testConn = new TestH2Connection();
		
		testConn.setConnection();
		
		testConn.runCreateStatement();
		
		testConn.runInsertStatement();
		
		testConn.runSelectStatement();
		
		testConn.closeConnection();
	}
}
