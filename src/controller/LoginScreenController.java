package controller;

import helper.JDBC;
import helper.LoginActivity;

import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Locale;
import java.time.ZoneId;

import model.Alerts;
import model.Username;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * FXML Controller class
 *
 * @author HannahBergman
 */

/* Login Screen Controller.*/
public class LoginScreenController implements Initializable {

    /*Scene variable.*/
    Parent scene;
    /*Stage variable.*/
    Stage stage;
    
    /*Exit button.*/
     @FXML
    public Button exitButton;
    /*Login button*/
    @FXML
    public Button loginButton;
    /*Password Label*/
    @FXML
    public Label passwordLabel;
    /*Password Text Field*/
    @FXML
    public TextField passwordTextField;
    /*Username Label*/
    @FXML
    public Label usernameLabel;
    /*Username Text Field*/
    @FXML
    public TextField usernameTextField;
    /*Zone: */
    @FXML
    public Label zone;
    /*Empty Zone Label (For switching between zones)*/
    @FXML
    public Label zoneLabelBlank;
    
    /* OTHER VARIABLES */
    /* User ID Alert Box*/
    public static String titleForUserID; //USER ID NOT FOUND
    public static String headerForUserID; //Could not find User ID in DB
    public static String contentForUserID; //Re-enter correct username
    /* Login Alert Box */
    public static String titleForLogin; //UNABLE TO LOGIN
    public static String headerForLogin; //Incorrect username/pw
    public static String contentForLogin; //Re-enter username/pw
    
    /*Exit Button action.
    * Exit application when clicked.
    */
    @FXML
    public void clickExit(ActionEvent event) {
    System.exit(0); //Exit application
    }
    
    /*Login Button action*/
    @FXML
    public void clickLogin(ActionEvent actionEvent) throws IOException, SQLException {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        Username.username = username;
        //get username & password
        getUsername(username);
        getPassword(password);
                
     if (getUsername(username) && getPassword(password)) {
            LoginActivity.logAttempt(username, true, "Login Successful");
            getUserIdAndUsername(username);
            //Once logged in, move user to Scheduling App Menu
            stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/SchedulingApp.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        } else {
            //Prompt user to ensure login information is correct
            LoginActivity.logAttempt(username, false, "Login unsuccessful, please try again");
           Alerts.errorAlert(titleForLogin, headerForLogin, contentForLogin);
        }
    }

    /*
    * Match password entered with password from DB.
    *
    * @param password = password
    */
    private boolean getPassword(String password) throws SQLException {
        Statement statement = JDBC.getConnection().createStatement();
        //Lookup password in DB from users table where the password there is the same as the password entered by user
        String sqlPassword = "SELECT Password FROM users WHERE Password ='" + password + "'";
        ResultSet results = statement.executeQuery(sqlPassword);

        while(results.next()) {
            if(results.getString("Password").equals(password)) {
                return true;
            }
        }
        statement.close();
        return false;
        }  

    /* Match username entered with username from DB.
    *
    * @param username = username
    */
    private boolean getUsername(String username) throws SQLException {
        Statement statement = JDBC.getConnection().createStatement();
        String sqlUsername = "SELECT User_Name FROM users WHERE User_Name ='" + username + "'";
        ResultSet results = statement.executeQuery(sqlUsername);

        while(results.next()) {
            if(results.getString("User_Name").equals(username)) {
                return true;
            }
        }
        statement.close();
        return false;
    }

    /* Match UserID to username from DB. 
    *@param username = username
    */
    public static int getUserIdAndUsername(String username) throws SQLException {
        Statement statement = JDBC.getConnection().createStatement();
        //Lookup userId from username in users table wehre the username in the table matches the username the user entered
        String sqlUsername = "SELECT User_ID, User_Name FROM users WHERE User_Name ='" + username + "'";
        ResultSet results = statement.executeQuery(sqlUsername);

        while(results.next()) {
            if(results.getString("User_Name").equals(username)) {
                System.out.println(results.getInt("User_ID"));
                return results.getInt("User_ID");
            }
        }
        statement.close();
        Alerts.errorAlert(titleForUserID, headerForUserID, contentForUserID);
        return -1;
    }

    /**
     * Initialize the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ZoneId zone = ZoneId.systemDefault();
        zoneLabelBlank.setText(zone.toString());
        //get default locale
        Locale locale = Locale.getDefault();
        ResourceBundle rsBundle = ResourceBundle.getBundle("languages/language", locale);
        //Switching login menu language to French
        if(locale.getLanguage().equals("fr")) {
            this.usernameTextField.setPromptText(rsBundle.getString("usernamePrompt"));
            this.passwordTextField.setPromptText(rsBundle.getString("passwordPrompt"));
            this.usernameLabel.setText(rsBundle.getString("username"));
            this.passwordLabel.setText(rsBundle.getString("password"));
            this.loginButton.setText(rsBundle.getString("loginButtonText"));
            this.exitButton.setText(rsBundle.getString("exitButtonText"));
            titleForUserID = rsBundle.getString("titleForUserID");
            headerForUserID = rsBundle.getString("headerForUserID");
            contentForUserID = rsBundle.getString("contentForUserID");
            titleForLogin = rsBundle.getString("titleForLogin");
            headerForLogin = rsBundle.getString("headerForLogin");
            contentForLogin = rsBundle.getString("contentForLogin");
        } else {
            titleForUserID = rsBundle.getString("titleForUserID");
            headerForUserID = rsBundle.getString("headerForUserID");
            contentForUserID = rsBundle.getString("contentForUserID");
            titleForLogin = rsBundle.getString("titleForLogin");
            headerForLogin = rsBundle.getString("headerForLogin");
            contentForLogin = rsBundle.getString("contentForLogin");
    }    
    
}
}
