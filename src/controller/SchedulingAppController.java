package controller;

import helper.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.sql.*;
import java.time.LocalDateTime;
import model.Alerts;


/**
 * Main Application Menu Controller.
 *
 * @author HannahBergman
 */
public class SchedulingAppController implements Initializable {
    
    /**Scene variable.*/
    Parent scene;
    /**Stage variable.*/ 
    Stage stage;
    
    /**View Appointments Button.*/
    @FXML
    private Button appointmentsButton;
    /*View Customers Button.*/
    @FXML
    private Button customersButton;
    /*Exit Button.*/
    @FXML
    private Button exitButton;
    /*Logout Button.*/
    @FXML
    private Button logoutButton;
    /*View Reports Button.*/
    @FXML
    private Button reportsButton;
    
    /* BUTTON ACTIONS */
    
    /**Click Appointments Button.
    * @param event Take user to ApptMenu.
    */
    @FXML
    void clickAppointmentsButton(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ApptMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    /**Click Customers Button.
    * @param event Take user to CustomerTable.
    */
    @FXML
    void clickCustomersButton(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/CustomerTable.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    /**Click Exit Button.
    * @param event Leave Application.
    */
    @FXML
    void clickExit(ActionEvent event) {
        System.exit(0); //Exit application
    }
    
    /*Click Logout Button.
    * Takes user to LoginScreen (where they are logged out).
    */
    @FXML
    void clickLoginMenu(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    /** Click Reports Button.
    * @param event Takes user to FinalReports Menu.
    */
    @FXML
    void clickReportsButton(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/FinalReports.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Initialize 15 min appt reminder.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
          try {
            gets15MinApptReminder();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }    
    
    /**
    * Show 15 min appt reminder alert to user.
     * @throws java.sql.SQLException
    */
    public void gets15MinApptReminder() throws SQLException {
        LocalDateTime localStart = LocalDateTime.now();
        LocalDateTime localEnd = LocalDateTime.now().plusMinutes(15);
        Timestamp now = Timestamp.valueOf(localStart);
        Timestamp end = Timestamp.valueOf(localEnd);
        apptReminder(now, end);
    }

    /**
    * Search DB for coming appointments.
    *@param now = within 15 min
    *@param end = within 45 min
     * @throws java.sql.SQLException
    */
    public void apptReminder(Timestamp now, Timestamp end) throws SQLException {
        PreparedStatement apptIn15Min = JDBC.getConnection().prepareStatement(
            "SELECT * " +
            "FROM appointments " +
            "INNER JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID " +
            "WHERE Start BETWEEN ? AND ?");
       
        apptIn15Min.setTimestamp(1, now); //appt within 15 min
        apptIn15Min.setTimestamp(2, end); //appt within 45 min

        //Alert user whether or not they have an appointment within the next 15 minutes
        ResultSet appointmentResults = apptIn15Min.executeQuery();     
        //Appointment scheduled - alert user of Appt Id, who the appt is with, what the appt is about, and when it will be starting
        if(appointmentResults.next())  {
            Alerts.informationAlert("APPOINTMENT REMINDER  |  Your Appointment is Starting Soon!",
                    ("Appointment "+ appointmentResults.getInt(("Appointment_ID")) + " is scheduled to start within 15 minutes"),
                    ("Your appointment is with " +
                        appointmentResults.getString("Contact_Name") +
                        " to talk about " + appointmentResults.getString("Type") + ". It will be starting at exactly " +
                        appointmentResults.getTimestamp("Start").toLocalDateTime() + "."));
            }
          //No appointment scheduled 
          else {
            Alerts.informationAlert("APPOINTMENT REMINDER  |  No Appointments Requiring Immediate Attention", "You have no appointments scheduled within the next 15 minutes.","");
        }
    }
}
