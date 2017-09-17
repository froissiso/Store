package controllers;

import java.io.IOException;
import java.util.logging.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import store.User;

/** 
 * Class Login Manager manages the control flow for logins and shows the different screens of the program.
 * 
 * @author Francisco Rois Siso
 */
public class LoginManager {
  private Scene scene;

  public LoginManager(Scene scene) {
    this.scene = scene;
  }

  /**
   * Callback method invoked to notify that a user has been authenticated.
   * Will show the main application screen.
   */ 
  public void authenticated(String sessionID,User user) {
	  //User user = new User();
	  showMainView(sessionID,user);
  }

  /**
   * Callback method invoked to notify that a user has logged out of the main application.
   * Will show the login application screen.
   */ 
  public void logout() {
    showLoginScreen();
  }
  
  public void register(){
	  showRegistrationView();
  }
  
  /**
   * Show login screen.
   */
  public void showLoginScreen() {
    try {
      FXMLLoader loader = new FXMLLoader(
        getClass().getResource("/views/login.fxml")
      );
      scene.setRoot((Parent) loader.load());
      LoginController controller = 
        loader.<LoginController>getController();
      controller.initManager(this);
    } catch (IOException ex) {
      Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Show main view.
   * 
   * @param sessionID
   * @param user
   */
  private void showMainView(String sessionID, User user) {
    try {
      FXMLLoader loader = new FXMLLoader(
        getClass().getResource("/views/mainview.fxml")
      );
      scene.setRoot((Parent) loader.load());
      MainViewController controller = 
        loader.<MainViewController>getController();
      controller.initSessionID(this, sessionID, user);
    } catch (IOException ex) {
      Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  /**
   * Show registration view.
   */
  private void showRegistrationView(){
	  try {
			//Create a loader for the UI components
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/registrationview.fxml"));
			//Inflate the view using the loader
          StackPane root = (StackPane) loader.load();
          Stage stage = new Stage();
          //Set window title
          stage.setTitle("Add Bank");
          //Create a scene with the inflated view
          Scene scene = new Scene(root);
          //point to style sheet
          //scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
          //Set the scene to the stage
          stage.setScene(scene);
          //Get the controller instance from the loader
          RegistrationController controller = loader.getController();
          //Set the parent stage in the controller
          controller.setDialogStage(stage);
          //Show the view
          //stage.show();
          controller.init(this);
		} catch(Exception e) {
			System.out.println("Error occured while inflating view: " + e);
		}
	  }
}
