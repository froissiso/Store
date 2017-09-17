package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * OrdersDAO allows to perform CRUD operations for the entity ORDER.
 * It allows to create a new table, insert a register in the table, read from the database and also delete a register from it.
 * @author Francisco Rois Siso
 *
 */
public class OrdersDAO {

	/**
	 * Creates table in the case that it does not exist yet.
	 * @param connection
	 * @param table_name
	 */
	public void createTable(Connection connection, String table_name){
		try{
			// Execute a query to create the table
			System.out.println("Creating table orders in database ...");
			Statement statement = connection.createStatement();

			String sql = "CREATE TABLE IF NOT EXISTS "+ table_name +
					" (idOrder INT not NULL AUTO_INCREMENT , " +
					" username VARCHAR(45), " +
					" products VARCHAR(255), " +
					" orderStatus VARCHAR(45), " +
					" PRIMARY KEY ( idOrder ))";

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
	 * Inserts order into the database.
	 * @param connection
	 * @param table_name
	 * @param us
	 * @param productsList
	 */
	public void insertOrder(Connection connection, String table_name,String us,List<Integer> productsList){
		String sql = "";   
		try{
			// Execute a query
			System.out.println("Inserting order into the table...");
			Statement statement = connection.createStatement();

			String username = us;
			String products = "";
			for(int i = 0;i<productsList.size()-1;i++){
				products += productsList.get(i);
				products += ", ";
			}
			products += productsList.get(productsList.size()-1);
			
			sql = "INSERT INTO "+table_name+" (products,username) "+
					"VALUES ("+"'"+products+"'"+", "+"'"+username+"')";

			System.out.println("sql: "+sql);

			statement.executeUpdate(sql);


			System.out.println("The order data has been saved for future sessions...");
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
	 * Reads order from the database.
	 * @param connection
	 * @param table_name
	 * @param username
	 * @return
	 */
	public ResultSet obtainOrderProductsFromDB(Connection connection,String table_name,String username){
		ResultSet rs = null;
		try{
			// Execute a query
			Statement statement = connection.createStatement();

			String sql = "SELECT * from "+table_name+" WHERE username = '"+username+"'";
			System.out.println("sql: "+sql);
			
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
	 * Delete order of a particular user from the database.
	 * @param connection
	 * @param table_name
	 * @param username
	 */
	public void deletePreviousOrder(Connection connection, String table_name, String username){
		try{
			// Execute a query to delete all from the table
			System.out.println("Deleting previous order from table in database ...");
			Statement statement = connection.createStatement();

			String sql = "DELETE FROM "+ table_name + " WHERE username = '"+username+"'"; 

			statement.executeUpdate(sql);
			System.out.println("Deleted previous order from table in given database...");
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
