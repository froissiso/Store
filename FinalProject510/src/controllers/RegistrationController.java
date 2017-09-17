package controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.Connector;
import store.Market;
import store.PaymentMethod;
import store.PremiumUser;
import store.RegularUser;
import store.SemiPremiumUser;
import store.User;

/**
 * RegistratonController class is meant to control the registration of a new user in the database.
 * 
 * @author Francisco Rois Siso
 *
 */
public class RegistrationController {
	//This is the parent stage
	private Stage dialogStage;
	// data needed to connect to the database
	final static String DB_URL = "jdbc:mysql://www.papademas.net:3306/fp?autoReconnect=true&useSSL=false";
	final static String USERNAME = "dbfp";
	final static String PASSWORD = "510";

	@FXML private Button backButton;
	@FXML private Button continueButton;

	@FXML private TextField username; 
	@FXML private TextField password;
	@FXML private ChoiceBox usertype;
	@FXML private TextField name;
	@FXML private TextField address;
	@FXML private TextField email;
	@FXML private TextField phonenumber;
	@FXML private TextField idmarket;
	@FXML private TextField idorder;
	@FXML private Label registrationInfoLabel;

	@FXML private ChoiceBox paymentmethods;

	//Method to set the parent stage of the current view
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void initialize() {}

	/**
	 * Initializes and sets actions for buttons.
	 * @param loginManager
	 */
	public void init(final LoginManager loginManager) {
		dialogStage.setWidth(500);
		dialogStage.setHeight(500);
		dialogStage.show();

		//sessionLabel.setText(sessionID);
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				//loginManager.logout();
				close();
			}
		});
		continueButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent event){
				System.out.println(usertype.getValue().toString());
				if(registerNewUser(usertype.getValue().toString())){
					registrationInfoLabel.setText("User successfully registered.");
				}
				else{
					registrationInfoLabel.setText("An error occur. Please, review the fields and try again.");
				}
			}
		});
	}

	/**
	 * Registers the new user into the users' database with all its data. The process takes into consideration the type of user that is registering.
	 * It creates the table of users if it does not exist. 
	 * @param userType
	 * @return true if the operation was successful. False if not.
	 */
	private Boolean registerNewUser(String userType){
		int default_points = 1000;
		
		System.out.println(username.getText());
		// Create new User depending on the type selected
		User user = null;
		// But first generate list of payment methods
		PaymentMethod pm = new PaymentMethod("Default payment method", "no data", paymentmethods.getValue().toString(), false);
		List<PaymentMethod> pml = new ArrayList<>();
		pml.add(pm);
		switch (userType){
		case "Admin": // Regular user but with access to all the markets
			user = new RegularUser(username.getText(), password.getText(), usertype.getValue().toString(), name.getText(), 
					address.getText(), email.getText(), phonenumber.getText(), default_points, pml, new Market(1));
			break;
		case "Semipremium User": // Semipremium user with access to Medium market
			user = new SemiPremiumUser(username.getText(), password.getText(), usertype.getValue().toString(), name.getText(), 
					address.getText(), email.getText(), phonenumber.getText(), default_points, pml, new Market(2));
			break;
		case "Premium User": // Premium user with access to Big market
			user = new PremiumUser(username.getText(), password.getText(), usertype.getValue().toString(), name.getText(), 
					address.getText(), email.getText(), phonenumber.getText(), default_points, pml, new Market(3));
			break;
		default:// "Regular User"	
			user = new RegularUser(username.getText(), password.getText(), usertype.getValue().toString(), name.getText(), 
					address.getText(), email.getText(), phonenumber.getText(), default_points, pml, new Market(1));
			break;
		}
			
		// create connection to the database using the access data
		Connector connector = new Connector();
		Connection connection = connector.createConnection(DB_URL, USERNAME, PASSWORD);
		

		// daoModel object created in order to execute CRUD functions
		UserDAO udao = new UserDAO();
		udao.createTable(connection, "f_rois_users4");
		//dm.dropTable(connection, table_name);
		Boolean b = true;
		if(user!=null){
			b = udao.inserts(connection, user, "f_rois_users4");
		}
		else{
			System.out.println("null User");
		}
		//dm.deleteAllFromTable(connection, table_name);
		// Close connection to database
		try {
			connection.close();
			System.out.println("\nConnection to db closed");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return b;
	}
	
	/**
	 * Closes the registration window.
	 */
	private void close() {
		dialogStage.fireEvent(new WindowEvent(dialogStage,WindowEvent.WINDOW_CLOSE_REQUEST));
	}
}
