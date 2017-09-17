package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import store.User;
import store.ProductWithImage;

/**
 * UpdatedMarketsDAO allows to perform CRUD operations for the entity UPDATEDMARKET.
 * It allows to create a new table, insert information in the table, read from the database in
 * several ways and also delete from it.
 * @author Francisco Rois Siso
 *
 */
public class UpdatedMarketsDAO {

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
					" (idUpdatedMarket INT not NULL AUTO_INCREMENT, " +
					" username VARCHAR(45), " +
					" productID INT, " +
					" price DOUBLE, " +
					" priceToPay DOUBLE, " + 
					" PRIMARY KEY ( idUpdatedMarket ))";

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
	 * Method inserts allows for a list of BankRecords objects to be passed to it and thus insert all record object
	 * field data consisting of the id, income and pep from the list into a database.
	 * @param connection Connection already established to a database.
	 * @param user User to be stored into the database.
	 * @param table_name Name of the table where the items are inserted.
	 */
	public void inserts(Connection connection, User user, String table_name, List<ProductWithImage> listUpdatedProductsWithImage){
		String sql = "";   
		try{
			// Execute a query
			System.out.println("Inserting updated market into the table...");
			Statement statement = connection.createStatement();
			
			String username = user.getUsername();
			int productID = 0;
			double price = 0.0;
			double priceToPay = 0.0;
			
			for(ProductWithImage p : listUpdatedProductsWithImage){
				productID = p.getIdProduct();
				price = p.getProductPrice();
				priceToPay = p.getRemainingPrice();
				
				sql = "INSERT INTO "+table_name+" (username,productID,price,priceToPay) "+
						"VALUES ("+"'"+username+"'"+", "+"'"+productID+"'"+", "+"'"+price+"'"+", "+"'"+priceToPay+"')";
				
				System.out.println("sql: "+sql);
				
				statement.executeUpdate(sql);
			}
			
			System.out.println("The market data has been saved for future sessions...");
			statement.close();

		}catch(SQLException se){
			//Handle errors for JDBC
			System.out.println(se.getMessage());
		}catch(Exception e){
			//Handle errors for Class.forName
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Checks if ther is a market saved by a certain user in the database.
	 * @param connection
	 * @param table_name
	 * @param username
	 * @return
	 */
	public boolean isThereAMarketSaved(Connection connection, String table_name, String username){
		Boolean b = false;
		ResultSet rs = null;
		try{
			// Execute a query
			Statement statement = connection.createStatement();

			// Select by id, income and pep and sort by pep in descending order in order to show first the premium users (pep = YES)
			String sql = "SELECT * FROM "+table_name+" WHERE username = '"+username+"'";
			rs = statement.executeQuery(sql);

			if(rs.next()){
				b = true;
			}
			
		}catch(SQLException se){
			//Handle errors for JDBC
			System.out.println(se.getMessage());
		}catch(Exception e){
			//Handle errors for Class.forName
			System.out.println(e.getMessage());
		}
		
		return b;
	}

	/**
	 * Read from the database and return a ResultSet with the data of a market saved by a particular user.
	 * @param connection
	 * @param table_name
	 * @param username
	 * @return
	 */
	public ResultSet obtainMapOfSavedProducts(Connection connection,String table_name,String username){
		ResultSet rs = null;
		try{
			// Execute a query
			Statement statement = connection.createStatement();

			// Select by id, income and pep and sort by pep in descending order in order to show first the premium users (pep = YES)
			String sql = "SELECT * FROM "+table_name+" WHERE username = '"+username+"'";
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
	 * Delete from database a market previously saved by a particular user.
	 * @param connection
	 * @param table_name
	 * @param username
	 */
	public void deleteSavedMarket(Connection connection, String table_name, String username){
		try{
			// Execute a query to delete all from the table
			System.out.println("Deleting saved market from table in database ...");
			Statement statement = connection.createStatement();

			String sql = "DELETE FROM "+ table_name + " WHERE username = '"+username+"'"; 

			statement.executeUpdate(sql);
			System.out.println("Deleted saved market from table in given database...");
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
}
