package models;
import java.sql.*;

/**
 * The class Connector has a single method (createConnection) to establish a connection to a certain database.
 * 
 * @author Francisco Rois Siso
 */
public class Connector {
	/**
	 * Constructor with no input parameters
	 */
	public Connector(){	
	}
	
	/**
	 * Method createConnection establishes a connection to the database indicated as input parameter, by using the data introduced as input parameters as well.
	 * 
	 * @param db_url URL of the database to access
	 * @param username Username to access the database, as String
	 * @param password Password to acces the database with the username indicated, as String
	 * @return connection An object Connection with the connection established to the certain database.
	 */
	public Connection createConnection(String db_url, String username, String password){
		Connection connection = null;
		try{
			// Create connection to the database
			connection = DriverManager.getConnection(db_url, username, password);
			System.out.println("\nConnection to "+db_url+" created");
		}
		catch(Exception e){
			System.out.println("ERROR: " + e.getMessage());
		}
		return connection;
	}
}
