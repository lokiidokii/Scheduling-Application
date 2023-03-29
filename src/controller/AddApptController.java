package controller;

import model.Alerts;
import helper.DBQueries;
import helper.JDBC;
import helper.TimeZones;

import java.net.URL;
import java.time.LocalTime;
import java.sql.*;
import java.time.*;
import java.util.ResourceBundle;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


/**
 * Add Appointments Controller.
 *
 * @author HannahBergman
 */
public class AddApptController implements Initializable {
    /**Scene variable. */
    Parent scene;
    /**Stage variable. */
    Stage stage;
    
    /**Add Appointment - Appointment ID. */
    @FXML
    private TextField apptIDTxtFld;
    /**Add Appointment - Contact. */
    @FXML
    private ComboBox<String> contactComboBox;
    /**Add Appointment - Customer. */
    @FXML
    private ComboBox<String> customerComboBox;
    /**Add Appointment - Location. */
    @FXML
    private TextField locationTxtFld;
    /**Add Appointment - Customer ID. */
    @FXML
    private TextField customerIDTxtFld;
    /**Add Appointment - Date. */
    @FXML
    private DatePicker datePicker;
    /**Add Appointment - Description. */
    @FXML
    private TextField descriptionTxtFld;
    /**Add Appointment - End Time. */
    @FXML
    private ComboBox<LocalTime> endTimeComboBox;
    /**Add Appointment - Start Time. */
    @FXML
    private ComboBox<LocalTime> startTimeComboBox;
    /**Add Appointment - Title. */
    @FXML
    private TextField titleTxtFld;
    /**Add Appointment - Type. */
    @FXML
    private ComboBox<String> typeComboBox;
    /**Add Appointment - User ID. */
    @FXML
    private ComboBox<Integer> userIDComboBox;
    /**Contact ID. */
    private int contactId;
    /**Getter for Contact ID. 
     *@return contact id
     */
    public int getContactID() {
        return contactId;
    }
    /**Setter for Contact ID. 
     * @param contactId 
     */
    public void setContactID(int contactId) {
        this.contactId = contactId;
    }
    /**Observable List for current customers. */
    public static ObservableList<String> currentCustomersOL = FXCollections.observableArrayList();
    /**Observable List for contacts. */
    public static ObservableList<String> contactOL = FXCollections.observableArrayList();
    /**Observable List for user ids. */
    public static ObservableList<Integer> userIDOL = FXCollections.observableArrayList();
    /**Observable List for appointment types. */
    private ObservableList<String> typeOL = FXCollections.observableArrayList("In-Office", "Remote - Conference Call", "Remote - On-Site", "Remote - Video");
    
    // BUTTONS
    /**Main Menu Button. */
    @FXML
    private Button mainMenuButton;
    /**Save Button. */
    @FXML
    private Button saveButton;
    /**Back Button. */
    @FXML
    private Button backButton;
    
    
    // BUTTON ACTIONS
    /** Back button.
    * @param event Takes user to back to the Appt Menu screen.
    */
    @FXML
    void clickBackToApptMenu(ActionEvent event) throws IOException {
        Alert confirmBackToApptMenu = new Alert(Alert.AlertType.CONFIRMATION);
        confirmBackToApptMenu.setTitle("CONFIRMATION NEEDED  |  GO BACK TO APPOINTMENT MENU");
        confirmBackToApptMenu.setHeaderText("Are you sure you want to return to your appointments?");
        confirmBackToApptMenu.setContentText("Data not saved will be lost");
        Optional<ButtonType> okBackToApptMenu = confirmBackToApptMenu.showAndWait();

        if(okBackToApptMenu.isPresent() && okBackToApptMenu.get() == ButtonType.OK) {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/apptMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        
    }
    
    /** Make sure appointment start and end times are valid.
     * @param end End time
     * @param start Start time
     * @return 
     */
    public boolean timeValidation(Timestamp start, Timestamp end) {

        boolean endTimeIsBeforeStart = end.before(start);
        boolean endTimeIsEqualToStart = end.equals(start);

        if(endTimeIsBeforeStart) {
            Alerts.displayAlert(24);
            return false;
        } else if(endTimeIsEqualToStart) {
            Alerts.displayAlert(25);
            return false;
        }

        ZonedDateTime startZDT = ZonedDateTime.of(start.toLocalDateTime(), ZoneId.systemDefault());
        ZonedDateTime endZDT = ZonedDateTime.of(end.toLocalDateTime(), ZoneId.systemDefault());

        ZonedDateTime utcStartZDT = ZonedDateTime.ofInstant(startZDT.toInstant(), ZoneId.of("UTC"));
        ZonedDateTime utcEndZDT = ZonedDateTime.ofInstant(endZDT.toInstant(), ZoneId.of("UTC"));

        Timestamp startUTC = Timestamp.valueOf(utcStartZDT.toLocalDateTime());
        Timestamp endUTC = Timestamp.valueOf(utcEndZDT.toLocalDateTime());

        try {
            Statement appointmentTimeConflictCheck = JDBC.getConnection().createStatement();

            String checkForAppointmentTimeOverlap =
                    "SELECT * " +
                    "FROM scheduleapp.appointments " +
                    "WHERE ('" + startZDT + "' BETWEEN Start AND End " +
                    "OR '" + endZDT + "' BETWEEN Start AND End)";

            ResultSet apptOverlap = appointmentTimeConflictCheck.executeQuery(checkForAppointmentTimeOverlap);

            if(apptOverlap.next()) {
                Alerts.displayAlert(26);
                return false;
            }
        } catch (SQLException se) {
            se.getMessage();
        }
        return true;
    }

    /** Save appointment button.
    * @param event Save new appointment.
    */
    @FXML
    void clickSaveAppt(ActionEvent event) {
        try {
            String titleInfo = titleTxtFld.getText();
            String descInfo = descriptionTxtFld.getText();
            String locationInfo = locationTxtFld.getText();
            int contactInfo = contactId;
            String typeInfo = typeComboBox.getSelectionModel().getSelectedItem();
            int custID = Integer.parseInt(customerIDTxtFld.getText());
            int userID = userIDComboBox.getSelectionModel().getSelectedItem();
            LocalDate date = datePicker.getValue();
            LocalTime start = startTimeComboBox.getSelectionModel().getSelectedItem();
            LocalTime end = endTimeComboBox.getSelectionModel().getSelectedItem();
            Timestamp startTimestamp = Timestamp.valueOf(LocalDateTime.of(date, start));
            Timestamp endTimestamp = Timestamp.valueOf(LocalDateTime.of(date, end));

            boolean checkOutsideBusinessHours = TimeZones.isOutsideBusinessHours(date, start, end, ZoneId.systemDefault());

            if (titleNotEmpty(titleInfo) && descriptionNotEmpty(descInfo) && typeNotEmpty(typeInfo) && locationNotEmpty(locationInfo) && startTimeNotEmpty(startTimestamp) &&
                    endTimeNotEmpty(endTimestamp) && dateNotEmpty(date) && customerNotEmpty(custID) &&
                    contactNotEmpty(contactId) && userIdNotEmpty(userID) && timeValidation(startTimestamp, endTimestamp) && !checkOutsideBusinessHours) {

                DBQueries.insertAppt(titleInfo, descInfo, locationInfo, typeInfo, startTimestamp, endTimestamp, custID, userID, contactInfo);
                Alerts.displayAlert(23);
                switchScreen(event, "/view/apptMenu.fxml");
            }

        } catch (Exception e) {
            if (customerIDTxtFld.getText() == null) {
                Alerts.displayAlert(20);
            } else if (userIDComboBox.getSelectionModel().getSelectedItem() == null) {
                Alerts.displayAlert(21);
            } else if (datePicker.getValue() == null) {
                Alerts.displayAlert(17);
            } else if (startTimeComboBox.getSelectionModel().getSelectedItem() == null) {
                Alerts.displayAlert(18);
            } else if (endTimeComboBox.getValue() == null) {
                Alerts.displayAlert(19);
            } else if (customerComboBox.getValue() == null) {
                Alerts.displayAlert(20);
            }
        }
    }
    
    /** Main menu button.
    * @param event Takes user to Scheduling App (Main Menu) screen.
    */
    @FXML
    void clickMainMenu(ActionEvent event) throws IOException {
        Alert confirmBackToMainMenu = new Alert(Alert.AlertType.CONFIRMATION);
        confirmBackToMainMenu.setTitle("CONFIRMATION NEEDED  |  GO BACK TO MAIN MENU");
        confirmBackToMainMenu.setHeaderText("Are you sure you want to return to the Main Menu?");
        confirmBackToMainMenu.setContentText("Data not saved will be lost");
        Optional<ButtonType> okBackToMainMenu = confirmBackToMainMenu.showAndWait();

        if(okBackToMainMenu.isPresent() && okBackToMainMenu.get() == ButtonType.OK) {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/SchedulingApp.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * Switch screens - specify which when using this.
     * @param event Switch screens
     * @param resourcesString Link to different screen
     * @throws java.io.IOException
     */
    public void switchScreen (ActionEvent event, String resourcesString) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(resourcesString));
        stage.setScene(new Scene(scene));
        stage.show();
    }    

    // COMBO BOX ACTIONS

    /** Set contact ID field from the contact name selected in the contact combo box. */
    @FXML
    void selectContact(ActionEvent event) throws SQLException {
        String contactName = contactComboBox.getSelectionModel().getSelectedItem();
        Statement st = JDBC.getConnection().createStatement();
        String sql = "SELECT Contact_ID FROM contacts WHERE Contact_Name='" + contactName + "'";
        ResultSet contactIdResultSet = st.executeQuery(sql);

        while(contactIdResultSet.next()){
            int contactId = contactIdResultSet.getInt("Contact_ID");
            setContactID(contactId);
        }
        st.close();
    }

    /** Set customer ID field from the customer selected in the customer combo box. */
    @FXML
    void selectCustomer(ActionEvent event) throws SQLException {
        String customerName = customerComboBox.getSelectionModel().getSelectedItem();
        Statement st = JDBC.getConnection().createStatement();
        String sql = "SELECT Customer_ID FROM customers WHERE Customer_Name='" + customerName + "'";
        ResultSet customerIdResultSet = st.executeQuery(sql);

        while(customerIdResultSet.next()){
            customerIDTxtFld.setText(String.valueOf(customerIdResultSet.getInt("Customer_ID")));
        }
        st.close();
    }

    @FXML
    void selectDate(ActionEvent event) {

    }

    @FXML
    void selectEndTime(ActionEvent event) {

    }

    @FXML
    void selectStartTime(ActionEvent event) {

    }

    @FXML
    void selectType(ActionEvent event) {

    }

    @FXML
    void selectUserID(ActionEvent event) {

    }  
    
    @FXML
    void populateApptID(ActionEvent event) {

    }
    
    /**
     * Fill combo boxes with existing information.
     * @param url url
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentCustomersOL.clear();
        contactOL.clear();
        userIDOL.clear();

        TimeZones startTime = new TimeZones();
        startTimeComboBox.setItems(startTime.generateTimeList());
        startTimeComboBox.getSelectionModel().selectFirst();

        TimeZones endTime = new TimeZones();
        ObservableList<LocalTime> endTimeList = endTime.generateTimeList();
        endTimeList.add(LocalTime.of(0, 0));

        endTimeComboBox.setItems(endTimeList);
        endTimeComboBox.getSelectionModel().selectFirst();

        typeComboBox.setItems(typeOL);

        try {
            Statement allCustomers = JDBC.getConnection().createStatement();
            String sqlStatement = "SELECT * FROM customers";
            ResultSet allCustomersRS = allCustomers.executeQuery(sqlStatement);

            while(allCustomersRS.next()) {
                currentCustomersOL.add(allCustomersRS.getString("Customer_Name"));
                customerComboBox.setItems(currentCustomersOL);
            }
            allCustomers.close();

            Statement allContacts = JDBC.getConnection().createStatement();
            String sqlContactStatement = "SELECT * FROM contacts";
            ResultSet allContactsRS = allContacts.executeQuery(sqlContactStatement);

            while(allContactsRS.next()) {
                contactOL.add(allContactsRS.getString("Contact_Name"));
                contactComboBox.setItems(contactOL);
            }
            allContacts.close();

            userIDOL.clear();

            Statement allUserIds = JDBC.getConnection().createStatement();
            String sqlUserIdStatement = "SELECT * FROM users";
            ResultSet allUserIdsRS = allUserIds.executeQuery(sqlUserIdStatement);

            while(allUserIdsRS.next()) {

                userIDOL.add(allUserIdsRS.getInt("User_ID"));
                userIDComboBox.setItems(userIDOL);
            }
            allUserIds.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }  
    
    // ERROR HANDLING
    /**Alert user if title is empty.
     *@param title Title
     * @return true
     */
    public boolean titleNotEmpty(String title) {
        if (titleTxtFld.getText().isEmpty()) {
            Alerts.displayAlert(13);
            return false;
        }
        return true;
    }

    /**Alert user if description is empty.
     * @param desciption
     * @return true
     */
    public boolean descriptionNotEmpty(String desciption) {
        if (descriptionTxtFld.getText().isEmpty()) {
            Alerts.displayAlert(14);
            return false;
        }
        return true;
    }

    /** Alert user if type is unselected
     *@param type Type
     * @return true
     */
    public boolean typeNotEmpty(String type) {
        if (typeComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.displayAlert(16);
            return false;
        }
        return true;
    }

    /** Alert user if location is empty
     * @param location Location
     * @return true
     */
    public boolean locationNotEmpty(String location) {
        if (locationTxtFld.getText().isEmpty()) {
            Alerts.displayAlert(15);
            return false;
        }
        return true;
    }

    /**Alert user if start time is unselected.
     *@param start Start time
     * @return true
     */
    public boolean startTimeNotEmpty(Timestamp start) {
        if (startTimeComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.displayAlert(18);
            return false;
        }
        return true;
    }

    /** Alert user if end time is unselected.
     *@param end End time
     * @return true
     */
    public boolean endTimeNotEmpty(Timestamp end) {
        if (endTimeComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.displayAlert(19);
            return false;
        }
        return true;
    }

    /**Alert user if date hasn't been selected.
     *@param date date
     * @return true
     */
    public boolean dateNotEmpty(LocalDate date) {
        if (datePicker.getValue() == null) {
            Alerts.displayAlert(17);
            return false;
        }
        return true;
    }

    /**Alert user if customer_id is blank
     *@param customerId customer_id
     * @return true
     */
    public boolean customerNotEmpty(int customerId) {
        if (customerComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.displayAlert(20);
            return false;
        }
        return true;
    }

    /**Alert user if user_id is blank
     *@param userId User_ID
     * @return true
     */
    public boolean userIdNotEmpty(int userId) {
        if (userIDComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.displayAlert(21);
            return false;
        }
        return true;
    }

    /**Alert user if contact is unselected
     * @param contact contact
     * @return true
     */
    public boolean contactNotEmpty(int contact) {
        if (contactComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.displayAlert(22);
            return false;
        }
        return true;
    }
    
}
