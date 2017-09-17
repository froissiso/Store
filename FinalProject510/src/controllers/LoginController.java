package controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.UserDAO;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Connector;
import store.Market;
import store.PaymentMethod;
import store.PremiumUser;
import store.RegularUser;
import store.SemiPremiumUser;
import store.User;

/** 
 * LoginController class controls the login screen. It performs different queries to the database in order to
 * check that the user credentials are correct and authorized. If the user is authorized, the MainView is launched.
 * It also allows to access the RegisterView, in order to register in the database.
 */
public class LoginController {
	// data needed to connect to the database
	final static String DB_URL = "jdbc:mysql://www.papademas.net:3306/fp?autoReconnect=true&useSSL=false";
	final static String USERNAME = "dbfp";
	final static String PASSWORD = "510";

	@FXML private TextField user;
	@FXML private TextField password;
	@FXML private Button loginButton;
	@FXML private Button registerButton;
	@FXML private Label infoLabel;

	/**
	 * Initiates the manager and set the actions for the buttons.
	 * @param loginManager
	 */
	public void initManager(final LoginManager loginManager) {
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				String sessionID = authorize();
				if (sessionID != null) {
					infoLabel.setText("User successfully authorized.");
					User authenticated_user = getUserFromDBByUsername(user.getText());
					loginManager.authenticated(sessionID,authenticated_user);
				}
				else{
					infoLabel.setText("The username and password are not correct. Please, try again.");
				}
			}
		});

		registerButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				loginManager.register();

			}
		});

	}

	/**
	 * Check authorization credentials by accessing the users' database.
	 * If authorized a session ID is generated. If not, null is returned.
	 * 
	 */   
	private String authorize() {
		// check authorization by looking at the users' database
		// Map <username,password> extracted from database users
		Map<String,String> userpassword_map = getMapFromDB();

		return  userpassword_map.containsKey(user.getText())&&userpassword_map.get(user.getText()).equals(password.getText())
				? generateSessionID() 
						: null;
	}

	private static int sessionID = 0;

	private String generateSessionID() {
		sessionID++;
		return "session " + sessionID;
	}

	/**
	 * Extracts a map with the list of users in the database and the correspondent passwords.
	 */
	private Map<String,String> getMapFromDB(){
		// create connection to the database using the access data
		Connector connector = new Connector();
		Connection connection = connector.createConnection(DB_URL, USERNAME, PASSWORD);


		// daoModel object created in order to execute CRUD functions
		UserDAO udao = new UserDAO();
		Map<String,String> map = new HashMap<>();
		
		// Extract data from result set
		try {
			ResultSet rs = udao.getUsersAccessData(connection, "f_rois_users4");
			while(rs.next()){
				//Retrieve by column name
				String username  = rs.getString("username");
				String password = rs.getString("password");

				//Display values
				System.out.print("Username: " + username);
				System.out.println(", Password: " + password);
				
				// Introduce pair in the map
				map.put(username, password);
			}
		} catch (SQLException e1) {
			System.out.println(e1.getMessage());
		}

		// Close connection to database
		try {
			connection.close();
			System.out.println("\nConnection to db closed");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return map;
	}

	
	/**
	 * Retrieve from database all the data from a user that has been successfully authorized to access the system.
	 * @param u_authenticated
	 * @return
	 */
	private User getUserFromDBByUsername(String u_authenticated){
		// create connection to the database using the access data
		Connector connector = new Connector();
		Connection connection = connector.createConnection(DB_URL, USERNAME, PASSWORD);

		// daoModel object created in order to execute CRUD functions
		UserDAO udao = new UserDAO();

		User u = null;

		// Extract data from result set
		try {
			ResultSet rs = udao.getRowByUsername(connection, "f_rois_users4", u_authenticated);
			while(rs.next()){
				//Retrieve by column name
				String username  = rs.getString("username");
				String password = rs.getString("password");
				String userType = rs.getString("userType");
				String name = rs.getString("name");
				String address = rs.getString("address");
				String email = rs.getString("email");
				String phoneNumber = rs.getString("phoneNumber");
				int points = rs.getInt("points");
				String paymentMethods = rs.getString("paymentMethods");
				int Market_idMarket = rs.getInt("Market_idMarket");

				//Display values
				System.out.print("Username: " + username);
				System.out.print(", Password: " + password);
				System.out.print(", Usertype: " + userType);
				System.out.print(", name: " + name);
				System.out.print(", address: " + address);
				System.out.print(", email: " + email);
				System.out.print(", phonenumber: " + phoneNumber);
				System.out.print(", points: " + points);
				System.out.print(", paymentmethods: " + paymentMethods);
				System.out.print(", idMarket: " + Market_idMarket);

				// Create new User depending on the type selected
				User user = null;
				// list needed to be created by splitting the String by commas
				PaymentMethod pm = new PaymentMethod("Default payment method", "no data", paymentMethods, false);
				List<PaymentMethod> pml = new ArrayList<>();
				pml.add(pm);
				switch (userType){
				case "Admin":
					user = new RegularUser(username, password, userType, name, 
							address, email, phoneNumber, points, pml, new Market(1));
					break;
				case "Semipremium User":
					user = new SemiPremiumUser(username, password, userType, name, 
							address, email, phoneNumber, points, pml, new Market(Market_idMarket));
					break;
				case "Premium User":
					user = new PremiumUser(username, password, userType, name, 
							address, email, phoneNumber, points, pml, new Market(Market_idMarket));
					break;
				default:// "Regular User"	
					
					//temporal
					//pml.add(new PaymentMethod("Default payment method", "no data", "Credit Card", false));
					user = new RegularUser(username, password, userType, name, 
							address, email, phoneNumber, points, pml, new Market(Market_idMarket));
					//temporal
					//user = new RegularUser("user1", "user1", "Regular User", "User 1", 
					//"Address 1", "Email 1", "Phone number 1", default_points, pml, new Market(), new Order());
					break;
				}
				u = user;
			}
		} catch (SQLException e1) {
			System.out.println(e1.getMessage());
		}


		// Close connection to database
		try {
			connection.close();
			System.out.println("\nConnection to db closed");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return u;
	}
}
