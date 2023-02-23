/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
 * Main Application Menu Controller class.
 *
 * @author HannahBergman
 */
public class SchedulingAppController implements Initializable {
    
    /*Scene variable.*/
    Parent scene;
    /*Stage variable.*/ 
    Stage stage;
    
    /*View Appointments Button.*/
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
    
    /*Click Appointments Button.
    * Takes user to ApptMenu.
    */
    @FXML
    void clickAppointmentsButton(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ApptMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    /*Click Customers Button.
    * Takes user to CustomerTable.
    */
    @FXML
    void clickCustomersButton(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/CustomerTable.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    /*Click Exit Button.
    * Leave Application.
    */
    @FXML
    void clickExit(ActionEvent event) {
        System.exit(0); //Exit application
    }
    
    /*Click Logout Button.
    * Takes user to LoginScreen.
    */
    @FXML
    void clickLoginMenu(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    /*Click Reports Button.
    * Takes user to FinalReports Menu.
    */
    @FXML
    void clickReportsButton(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/FinalReports.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Initializes the 15 min appointment reminder.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
          try {
            gets15MinApptReminder();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }    
    
    /*
    * Display a 15 min appointment alert to user.
    */
    public void gets15MinApptReminder() throws SQLException {
        LocalDateTime localStart = LocalDateTime.now();
        LocalDateTime localEnd = LocalDateTime.now().plusMinutes(15);
        Timestamp now = Timestamp.valueOf(localStart);
        Timestamp end = Timestamp.valueOf(localEnd);
        apptReminder(now, end);
    }

    /*
    * Search DB for coming appointments.
    *@param now current time
    *@param end within 15 min
    */
    public void apptReminder(Timestamp now, Timestamp end) throws SQLException {

        PreparedStatement apptIn15Min = JDBC.getConnection().prepareStatement(
            "SELECT * " +
                "FROM appointments " +
                "INNER JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID " +
                "WHERE Start BETWEEN ? AND ?");

        apptIn15Min.setTimestamp(1, now);
        apptIn15Min.setTimestamp(2, end);

        ResultSet appointmentResults = apptIn15Min.executeQuery();

        if(appointmentResults.next())  {
            Alerts.informationAlert("APPOINTMENT REMINDER  |  Your Appointment is Starting Soon!",
                    ("Appointment ID = "+ appointmentResults.getInt(("Appointment_ID")) + " is scheduled to start within 15 minutes. ") ,
                    ("Your appointment is with " +
                        appointmentResults.getString("Contact_Name") +
                        " to talk about " + appointmentResults.getString("Type") + " . It will be starting at " +
                        appointmentResults.getTimestamp("Start").toLocalDateTime() + "."));
            } else {
            Alerts.informationAlert("APPOINTMENT REMINDER  |  No Appointments Requiring Immediate Attention", "You have no appointments scheduled within the next 15 minutes.","");
        }
    }
}
