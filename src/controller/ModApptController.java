package controller;

import helper.JDBC;
import helper.TimeZones;
import model.Appointments;
import model.Alerts;
import helper.DBQueries;

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
 * Modify Appointments Controller.
 *
 * @author HannahBergman
 */
public class ModApptController implements Initializable {
    /** Scene variable. */
    Parent scene;
    /** Stage variable. */
    Stage stage;
    
    /**Mod Appointment - Appointment ID. */
    @FXML
    private TextField apptIDTxtFld;
    /**Mod Appointment - Contact. */
    @FXML
    private ComboBox<String> contactComboBox;
    /**Mod Appointment - Customer. */
    @FXML
    private ComboBox<String> customerComboBox;
    /**Mod Appointment - Customer ID. */
    @FXML
    private TextField customerIDTxtFld;
    /**Mod Appointment - Date Picker. */
    @FXML
    private DatePicker datePicker;
    /**Mod Appointment - Description. */
    @FXML
    private TextField descriptionTxtFld;
    /**Mod Appointment - End Time. */
    @FXML
    private ComboBox<LocalTime> endTimeComboBox;
    /**Mod Appointment - Location. */
    @FXML
    private TextField locationTxtFld;
    /**Mod Appointment - Start Time. */
    @FXML
    private ComboBox<LocalTime> startTimeComboBox;
    /**Mod Appointment - Title. */
    @FXML
    private TextField titleTxtFld;
    /**Mod Appointment - Type. */
    @FXML
    private ComboBox<String> typeComboBox;
    /**Mod Appointment - User Id. */
    @FXML
    private ComboBox<Integer> userIDComboBox;
    /**Selected appointment. */
    private static Appointments selectedAppointment;
    /**Contact id. */
    private int contactId;
    /**Setter for Contact ID.
     * @param contactId */
    public void setContactID(int contactId) {
        this.contactId = contactId;
    }
    /**Observable List for current customers. */
    public static ObservableList<String> currentCustomersOL = FXCollections.observableArrayList();
    /**Observable List for contacts. */
    public static ObservableList<String> contactOL = FXCollections.observableArrayList();
    /**Observable List for users. */
    public static ObservableList<Integer> userIDOL = FXCollections.observableArrayList();
    /** Observable List for appointment types. */
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
    /** Take user back to Appt Menu Screen. 
     * @param event back to appt menu
     */
    @FXML
    void clickBackToApptMenu(ActionEvent event) throws IOException {
        Alert alertForMainMenu = new Alert(Alert.AlertType.CONFIRMATION);
        alertForMainMenu.setTitle("CONFIRMATION NEEDED  |  GO BACK TO APPOINTMENT MENU");
        alertForMainMenu.setHeaderText("Are you sure you want to return to your appointments?");
        alertForMainMenu.setContentText("Data not saved will be lost");
        Optional<ButtonType> MainMenuSelection = alertForMainMenu.showAndWait();

        if(MainMenuSelection.isPresent() && MainMenuSelection.get() == ButtonType.OK) {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/apptMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }
    
    /** Take user back to Main Menu Screen.
     * @param event back to main menu
     */
    @FXML
    void clickMainMenu(ActionEvent event) throws IOException {
        Alert alertForMainMenu = new Alert(Alert.AlertType.CONFIRMATION);
        alertForMainMenu.setTitle("CONFIRMATION NEEDED  |  GO BACK TO MAIN MENU");
        alertForMainMenu.setHeaderText("Are you sure you want to return to the Main Menu?");
        alertForMainMenu.setContentText("Data not saved will be lost");
        Optional<ButtonType> MainMenuSelection = alertForMainMenu.showAndWait();

        if(MainMenuSelection.isPresent() && MainMenuSelection.get() == ButtonType.OK) {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/SchedulingApp.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }
    
    /** Switch screens.
     * @param event switch screens
     * @param resourcesString link to different screen
     * @throws java.io.IOException
     */
    public void switchScreen(ActionEvent event, String resourcesString) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(resourcesString));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    /** Save Appt Modification.
     * @param event save mods
     */
    @FXML
    void clickSaveAppt(ActionEvent event) {
        try {
            int custID = Integer.parseInt(customerIDTxtFld.getText());
            int userID = userIDComboBox.getSelectionModel().getSelectedItem();
            int appointmentId = selectedAppointment.getAppointmentId();
            String titleInfo = titleTxtFld.getText();
            String descInfo = descriptionTxtFld.getText();
            String locationInfo = locationTxtFld.getText();
            String typeInfo = typeComboBox.getSelectionModel().getSelectedItem();
            LocalDate date = datePicker.getValue();
            LocalTime start = startTimeComboBox.getSelectionModel().getSelectedItem();
            LocalTime end = endTimeComboBox.getSelectionModel().getSelectedItem();
            Timestamp startTimestamp = Timestamp.valueOf(LocalDateTime.of(date, start));
            Timestamp endTimestamp = Timestamp.valueOf(LocalDateTime.of(date, end));

            boolean outsideBusinessHours = TimeZones.isOutsideBusinessHours(date, start, end, ZoneId.systemDefault());

            if(titleNotNull(titleInfo) && descriptionNotNull(descInfo) && typeNotNull(typeInfo) && locationNotNull(locationInfo) && startNotNull(startTimestamp) &&
                    endNotNull(endTimestamp) && dateNotNull(date) && customerNotNull(custID) &&
                    contactNotNull(contactId) && userIdNotNull(userID) && timeValidation(startTimestamp, endTimestamp) && !outsideBusinessHours) {

                Alerts.displayAlert(23);
                DBQueries.updateAppointment(appointmentId, titleInfo, descInfo, locationInfo, typeInfo,
                                                    startTimestamp, endTimestamp, custID, userID, contactId);

                switchScreen(event, "/view/apptMenu.fxml");
            }
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
            if(customerIDTxtFld.getText() == null) {
                Alerts.displayAlert(20);
            } else if(userIDComboBox.getSelectionModel().getSelectedItem() == null) {
                Alerts.displayAlert(21);
            } else if(datePicker.getValue() == null) {
                Alerts.displayAlert(17);
            } else if(startTimeComboBox.getSelectionModel().getSelectedItem() == null) {
                Alerts.displayAlert(18);
            } else if(endTimeComboBox.getValue() == null) {
                Alerts.displayAlert(19);
            } else if(customerComboBox.getValue() == null) {
                Alerts.displayAlert(20);
            }
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
                    "FROM appointments " +
                    "WHERE ('" + startZDT + "' BETWEEN Start AND End " +
                    "OR '" + endZDT + "' BETWEEN Start AND End) " +
                    "AND Appointment_ID !=" + apptIDTxtFld.getText();

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
    
    @FXML
    void populateApptID(ActionEvent event) {

    }

     /** Set contact ID field from the contact name selected in the contact combo box.
     * @throws java.sql.SQLException */
    public void getContactID() throws SQLException {
//        String contactName = contactComboBox.getSelectionModel().getSelectedItem();
//        Statement st = JDBC.getConnection().createStatement();
//        String sql = "SELECT Contact_ID FROM contacts WHERE Contact_Name='" + contactName + "'";
//        ResultSet resultSet = st.executeQuery(sql);
//
//        while(resultSet.next()){
//            int contactId = resultSet.getInt("Contact_ID");
//            setContactID(contactId);
//        }
//        st.close();
    }
    
    /** Get contact id from the contact name.
     * @throws java.sql.SQLException
     */
    public void getContactIDFromContact() throws SQLException {
        String contactName = contactComboBox.getSelectionModel().getSelectedItem();
        Statement st = JDBC.getConnection().createStatement();
        String sql = "SELECT Contact_ID FROM contacts WHERE Contact_Name='" + contactName + "'";
        ResultSet resultSet = st.executeQuery(sql);

        while(resultSet.next()){
            int contactId = resultSet.getInt("Contact_ID");
            setContactID(contactId);
        }
        st.close();
    }
    
     /** Get contact id from the contact name.
     * @param event get contact id
     * @throws java.sql.SQLException
     */
    public void selectContact(ActionEvent event) throws SQLException {
       getContactIDFromContact();
    }
    
    /** Set customer ID field from the customer selected in the customer combo box. 
    * @param event set contact id
    */
    @FXML
    void selectCustomer(ActionEvent event) throws SQLException {
        String contactName = contactComboBox.getSelectionModel().getSelectedItem();
        Statement st = JDBC.getConnection().createStatement();
        String sql = "SELECT Contact_ID FROM contacts WHERE Contact_Name='" + contactName + "'";
        ResultSet resultSet = st.executeQuery(sql);

        while(resultSet.next()){
            int contactId = resultSet.getInt("Contact_ID");
            setContactID(contactId);
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

    /**
     * Initializes the controller class.
     * @param url url
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selectedAppointment = ApptMenuController.getSelectedAppointment();

        apptIDTxtFld.setText(String.valueOf(selectedAppointment.getAppointmentId()));
        titleTxtFld.setText(selectedAppointment.getTitle());
        locationTxtFld.setText(selectedAppointment.getLocation());
        descriptionTxtFld.setText(selectedAppointment.getDescription());
        typeComboBox.setValue(selectedAppointment.getType());
        contactComboBox.setValue(selectedAppointment.getContactName());

        datePicker.setValue(selectedAppointment.getStart().toLocalDate());
        startTimeComboBox.setValue(selectedAppointment.getStart().toLocalTime());
        endTimeComboBox.setValue((selectedAppointment.getEnd().toLocalTime()));
        customerIDTxtFld.setText(String.valueOf(selectedAppointment.getCustomerId()));
        userIDComboBox.setValue(selectedAppointment.getUserId());
        try {
            getContactIDFromContact();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        TimeZones startTime = new TimeZones();
        startTimeComboBox.setItems(startTime.generateTimeList());

        TimeZones endTime = new TimeZones();
        ObservableList<LocalTime> endTimeList = endTime.generateTimeList();
        endTimeList.add(LocalTime.of(0, 0));

        endTimeComboBox.setItems(endTimeList);
        typeComboBox.setItems(typeOL);

        currentCustomersOL.clear();
        contactOL.clear();
        userIDOL.clear();

        try {
            Statement st = JDBC.getConnection().createStatement();
            String sql = "SELECT * FROM customers WHERE Customer_ID=" + selectedAppointment.getCustomerId();
            ResultSet rs = st.executeQuery(sql);

            if(rs.next()) {
                customerComboBox.setValue(rs.getString("Customer_Name"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            Statement populateExistingCustomers = JDBC.getConnection().createStatement();
            String sqlStatement = "SELECT * FROM customers";
            ResultSet result = populateExistingCustomers.executeQuery(sqlStatement);

            while(result.next()) {
                currentCustomersOL.add(result.getString("Customer_Name"));
                customerComboBox.setItems(currentCustomersOL);
            }
            populateExistingCustomers.close();

            Statement populateContactStatement = JDBC.getConnection().createStatement();
            String sqlContactStatement = "SELECT * FROM contacts";
            ResultSet contactResult = populateContactStatement.executeQuery(sqlContactStatement);

            while(contactResult.next()) {
                contactOL.add(contactResult.getString("Contact_Name"));
                contactComboBox.setItems(contactOL);
            }
            populateContactStatement.close();

            Statement populateUserIdStatement = JDBC.getConnection().createStatement();
            String sqlUserIdStatement = "SELECT * FROM users";
            ResultSet userIdResult = populateUserIdStatement.executeQuery(sqlUserIdStatement);

            while(userIdResult.next()) {
                userIDOL.add(userIdResult.getInt("User_ID"));
                userIDComboBox.setItems(userIDOL);
            }
            populateUserIdStatement.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }    
    
    // ERROR HANDLING
    /**Alert user if title is empty.
     *@param title Title
     * @return true
     */
    public boolean titleNotNull(String title) {
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
    public boolean descriptionNotNull(String desciption) {
        if (descriptionTxtFld.getText().isEmpty()) {
            Alerts.displayAlert(14);
            return false;
        }
        return true;
    }

    /**Alert user if type is unselected.
     *@param type Type
     * @return true
     */
    public boolean typeNotNull(String type) {
        if (typeComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.displayAlert(16);
            return false;
        }
        return true;
    }

    /**Alert user if location is empty
     @param location The text in the location
     * @return true
     */
    public boolean locationNotNull(String location) {
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
    public boolean startNotNull(Timestamp start) {
        if (startTimeComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.displayAlert(18);
            return false;
        }
        return true;
    }

    /**Alert user if end time is unselected.
     *@param end End time
     * @return 
     */
    public boolean endNotNull(Timestamp end) {
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
    public boolean dateNotNull(LocalDate date) {
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
    public boolean customerNotNull(int customerId) {
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
    public boolean userIdNotNull(int userId) {
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
    public boolean contactNotNull(int contact) {
        if (contactComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.displayAlert(22);
            return false;
        }
        return true;
    }
    
}


