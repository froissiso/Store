package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * ProductDAO allows to perform CRUD operations for the entity PRODUCT.
 * It allows to read the database in several ways and retrieve data of the products.
 * @author Francisco Rois Siso
 *
 */
public class ProductDAO {
	
	/**
	 * Method getResultSet returns a ResultSet object containing the correspondent products from the type of market's id specified.
	 * @param connection Connection already established to a database.
	 * @param table_name Table from which the data is retrieved to make the ResultSet.
	 * @return
	 */
	public ResultSet getMarketProducts(Connection connection, String table_name, int marketID){
		ResultSet rs = null;
		try{
			// Execute a query
			Statement statement = connection.createStatement();
			String sql = "";
			if(marketID == 0){ // Admin user. Access to all the markets.
				System.out.print("HERE");

				sql = "SELECT * from "+table_name+" WHERE (f_rois_markets_idMarket = '1' OR f_rois_markets_idMarket = '2' OR f_rois_markets_idMarket = '3')";
			}
			else{ // No admin user. Access to a particular market.
				System.out.print("HERE1");

				sql = "SELECT * from "+table_name+" WHERE f_rois_markets_idMarket = '"+String.valueOf(marketID)+"'";
			}
			
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
	 * Return ResultSet of Product by productID.
	 * @param connection
	 * @param table_name
	 * @param map
	 * @return
	 */
	public ResultSet obtainProductFromDB(Connection connection,String table_name,int productID){
		ResultSet rs = null;
		try{
			// Execute a query
			Statement statement = connection.createStatement();
			String sql = "SELECT * from "+table_name+" WHERE idProduct = '"+productID+"'";
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
