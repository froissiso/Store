package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import store.User;

/**
 * UserDAO allows to perform CRUD operations for the entity USER.
 * It allows to create a table, drop a table, delete from a table, insert information, and read
 * data in several ways.
 * @author Francisco Rois Siso
 *
 */
public class UserDAO {
	/**
	 * Method createTable creates a new table in the database, if it does not exist yet, using the connection already established. 
	 * @param connection Connection already established to a database
	 * @param table_name Name of the table to be created, as String
	 */
	public void createTable(Connection connection, String table_name){
		try{
			// Execute a query to create the table
			System.out.println("Creating table in database ...");
			Statement statement = connection.createStatement();

			String sql = "CREATE TABLE IF NOT EXISTS "+ table_name +
					//" (idUser INT not NULL , " +
					" (username VARCHAR(45), " +
					" password VARCHAR(45), " +
					" userType VARCHAR(45), " +
					" name VARCHAR(45), " +
					" address VARCHAR(45), " +
					" email VARCHAR(45), " +
					" phoneNumber VARCHAR(45), " +
					" points DOUBLE, " +
					" paymentMethods VARCHAR(45), " +
					" Market_idMarket INT NOT NULL, " +
					" PRIMARY KEY ( username ), " +
					" FOREIGN KEY (Market_idMarket) REFERENCES f_rois_markets(idMarket))";

			System.out.println("sql: "+sql);

			statement.executeUpdate(sql);
			System.out.println("Created table "+table_name+" in given database...");
			statement.close();
		}
		catch(SQLException se){
			//Handle errors for JDBC
			System.out.println(se.getMessage());
		}catch(Exception e){
			//Handle errors for Class.forName
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Method dropTable allows to drop a table from a database by using the connection provided.
	 * @param connection Connection already established to a database.
	 * @param table_name Name of the table to be dropped, as String.
	 */
	public void dropTable(Connection connection, String table_name){
		try{
			// Execute a query to drop the table from the database
			System.out.println("Droping table in database ...");
			Statement statement = connection.createStatement();

			String sql = "DROP TABLE "+ table_name; 

			statement.executeUpdate(sql);
			System.out.println("Droped table in given database...");
			statement.close();
		}
		catch(SQLException se){
			//Handle errors for JDBC
			System.out.println(se.getMessage());
		}catch(Exception e){
			//Handle errors for Class.forName
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Method deleteAllFromTable allows to delete all the rows of a certain database.
	 * @param connection Connection already established to a database.
	 * @param table_name Name of the table to be cleared, as String.
	 */
	public void deleteAllFromTable(Connection connection, String table_name){
		try{
			// Execute a query to delete all from the table
			System.out.println("Deleting all from table in database ...");
			Statement statement = connection.createStatement();

			String sql = "DELETE FROM "+ table_name; 

			statement.executeUpdate(sql);
			System.out.println("Deleted all rows from table in given database...");
			statement.close();
		}
		catch(SQLException se){
			//Handle errors for JDBC
			System.out.println(se.getMessage());
		}catch(Exception e){
			//Handle errors for Class.forName
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Inserts data from a particular user into the users' database.
	 * @param connection Connection already established to a database.
	 * @param user User to be stored into the database.
	 * @param table_name Name of the table where the items are inserted.
	 */
	public Boolean inserts(Connection connection, User user, String table_name){
		String sql = "";   
		try{
			// Execute a query
			System.out.println("Inserting user into the table...");
			Statement statement = connection.createStatement();
			
			String username = user.getUsername();
			String password = user.getPassword();
			String userType = user.getUserType();
			String name = user.getName();
			String address = user.getAddress();
			String email = user.getEmail();
			String phoneNumber = user.getPhoneNumber();
			String points = String.valueOf(user.getPoints());
			String paymentMethods = "";
			// get names of payment methods separated by commas
			for(int i = 0;i<user.getPaymentMethods().size();i++){
				paymentMethods += user.getPaymentMethods().get(i);
				if(i+1<user.getPaymentMethods().size())
					paymentMethods += ", ";
			}
			String Market_idMarket = String.valueOf(user.getMarket().getIdMarket());

			sql = "INSERT INTO "+table_name+" (username,password,userType,name,address,email,phoneNumber,points,paymentMethods,Market_idMarket) "+
					"VALUES ("+"'"+username+"'"+", "+"'"+password+"'"+", "+"'"+userType+"'"+", "+"'"+name+"'"+", "+"'"+address+"'"+", "+"'"+email+"'"+
					", "+"'"+phoneNumber+"'"+", "+"'"+points+"'"+", "+"'"+paymentMethods+"'"+", "+"'"+Market_idMarket+"')";
			
			System.out.println("sql: "+sql);
			
			statement.executeUpdate(sql);
			
			
			System.out.println("Inserted user into the table...");
			statement.close();
			return true;

		}catch(SQLException se){
			//Handle errors for JDBC
			System.out.println(se.getMessage());
			return false;
		}catch(Exception e){
			//Handle errors for Class.forName
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	/**
	 * Method getResultSet returns a ResultSet object containing the users access data (idUser, username and password).
	 * @param connection Connection already established to a database.
	 * @param table_name Table from which the data is retrieved to make the ResultSet.
	 * @return
	 */
	public ResultSet getUsersAccessData(Connection connection, String table_name){
		ResultSet rs = null;
		try{
			// Execute a query
			Statement statement = connection.createStatement();

			String sql = "SELECT username, password FROM "+table_name+"";
			rs = statement.executeQuery(sql);
					
		}catch(SQLException se){
			//Handle errors for JDBC
			System.out.println(se.getMessage());
		}catch(Exception e){
			//Handle errors for Class.forName
			System.out.println(e.getMessage());
		}
		return rs;
	}
	
	/**
	 * Retrieves data from a particular user, by its username.
	 * @param connection
	 * @param table_name
	 * @param username
	 * @return
	 */
	public ResultSet getRowByUsername(Connection connection, String table_name, String username){
		ResultSet rs = null;
		try{
			// Execute a query
			Statement statement = connection.createStatement();

			String sql = "SELECT * FROM "+table_name+" WHERE username = "+"'"+username+"'";
			rs = statement.executeQuery(sql);
					
		}catch(SQLException se){
			//Handle errors for JDBC
			System.out.println(se.getMessage());
		}catch(Exception e){
			//Handle errors for Class.forName
			System.out.println(e.getMessage());
		}
		return rs;
	}
}