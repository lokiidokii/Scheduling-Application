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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Final Reports Controller.
 *
 * @author HannahBergman
 */
public class FinalReportsController implements Initializable {
    /** Scene variable. */
    Parent scene;
    /** Stage variable. */
    Stage stage;
    
    /**Reports - Changing Label 1, appts by month/type. */
    @FXML
    private Label changingLabel1;
    /**Reports - Changing Label 2, appts by customer. */
    @FXML
    private Label changingLabel2;
    
    /**Reports Table. */
    @FXML
    private TableView<Appointments> reportTable;
    /**Reports - Appointment ID. */
    @FXML
    private TableColumn<Appointments, Integer> apptIDColumn;
    /**Reports - Contact. */
    @FXML
    private TableColumn<Appointments, String> contactColumn;
    /**Reports - Customer ID. */
    @FXML
    private TableColumn<Appointments, Integer> customerIDColumn;
    /**Reports - Description. */
    @FXML
    private TableColumn<Appointments, String> descriptionColumn;
    /**Reports - End Time. */
    @FXML
    private TableColumn<Appointments, LocalDateTime> endTimeColumn;
    /**Reports - Location. */
    @FXML
    private TableColumn<Appointments, String> locationColumn;
    /**Reports - Start Time. */
    @FXML
    private TableColumn<Appointments, LocalDateTime> startTimeColumn;
    /**Reports - Title. */
    @FXML
    private TableColumn<Appointments, String> titleColumn;
    /**Reports - Type. */
    @FXML
    private TableColumn<Appointments, String> typeColumn;
    /**Reports - User ID. */
    @FXML
    private TableColumn<Appointments, Integer> userIDColumn;
    
    // COMBO BOXES
    /**Reports - Type Combo Box. */
    @FXML
    private ComboBox<String> typeComboBox;
    /**Reports - Month Combo Box. */
    @FXML
    private ComboBox<String> monthComboBox;
    /**Reports - Contact Combo Box. */
    @FXML
    private ComboBox<String> contactComboBox;
    /**Reports - Customer Combo Box. */
    @FXML
    private ComboBox<String> customerComboBox;
    
    /**Contact id. */
    private int contactId;
//    /*Setter for Contact ID*/
//    public void setContactID(int contactId) {
//        this.contactId = contactId;
//    }
    /**Observable List for current customers. */
    public ObservableList<String> currentCustomersOL = FXCollections.observableArrayList();
    /**Observable List for contacts. */
    public ObservableList<String> contactOL = FXCollections.observableArrayList();
    /**Observable List for users. */
    public ObservableList<String> monthsOL = FXCollections.observableArrayList("JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER");
    /**Observable List for filling table by appointment. */
    public ObservableList<Appointments> apptScheduleOL = FXCollections.observableArrayList();
    /**Observable List for appointment types. */
    public ObservableList<String> typeOL = FXCollections.observableArrayList("In-Office", "Remote - Conference Call", "Remote - On-Site", "Remote - Video");
    
    /** Switch screens.
     * @param event switch screen
     * @param resourcesString link to different screen
     */
    public void switchScreen(ActionEvent event, String resourcesString) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(resourcesString));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    // BUTTONS
    /** Main Menu Button. */
    @FXML
    private Button mainMenuBtn;
    /** Search Appointments Button. */
    @FXML
    private Button searchAppointmentsBtn;
    /** Search Customers Appointments Button. */
    @FXML
    private Button searchCustApptsBtn;
    
    // BUTTON ACTIONS
    
    /** Take user back to Appt Menu Screen. 
     * @param event take user to main menu
     */
    @FXML
    void clickMainMenu(ActionEvent event) throws IOException, SQLException {
        Alert alertForMainMenu = new Alert(Alert.AlertType.CONFIRMATION);
        alertForMainMenu.setTitle("CONFIRMATION NEEDED  |  GO BACK TO MAIN MENU");
        alertForMainMenu.setHeaderText("Are you sure you want to return to the Main Menu?");
        Optional<ButtonType> MainMenuSelection = alertForMainMenu.showAndWait();

        if(MainMenuSelection.isPresent() && MainMenuSelection.get() == ButtonType.OK) {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/SchedulingApp.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
    }
    }
    
    /** Find month/type in database.
    * @param type Type
    * @param month Month
    */
     void searchAppts(int monthId, String type) throws SQLException {
        Statement appointmentStatement = JDBC.getConnection().createStatement();
        String searchByMonthAndType = "SELECT COUNT(Appointment_ID) AS Count FROM appointments WHERE month(Start)=" + monthId + " AND Type='" + type + "'";
        ResultSet appointmentCount = appointmentStatement.executeQuery(searchByMonthAndType);

        while(appointmentCount.next()) {
            changingLabel1.setText(String.valueOf(appointmentCount.getInt("Count")));
        }
    }
     
    /** Allow user to search by month and type.
     * Throw an error alert if they don't select an option from combo box. 
     */
    @FXML
    void clickSearchAppts(ActionEvent event) throws SQLException {
        if(monthComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.errorAlert("PLEASE SELECT MONTH", "Please select a month from the dropdown", "");
        } else if(typeComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.errorAlert("PLEASE SELECT TYPE", "Please select a type from the dropdown", "");
        } else {
            String monthSelected = monthComboBox.getSelectionModel().getSelectedItem();
            int monthId = monthSelection(monthSelected);
            String type = typeComboBox.getSelectionModel().getSelectedItem();
            searchAppts(monthId, type);
        }
    }

    /** Fill contact table with data. 
     * @event combo box selection
     */
    @FXML
    void selectContact(ActionEvent event) throws SQLException {
         getApptSchedule();
        try {
            getApptSchedule().clear();
            reportTable.setItems(getApptSchedule());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        apptIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIDColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
    }

    // ATTEMPT TO GET CUSTOMER COMBO BOX TO WORK WITH SEPARATE BUTTON
//    public void searchCustomer(String customerName) throws SQLException {
//        Statement getCustomerCount = JDBC.getConnection().createStatement();
//        String customerCountSQL = "SELECT COUNT(contactName) FROM appointments";
//        ResultSet customerCount = getCustomerCount.executeQuery(customerCountSQL);
//        
//        while(customerCount.next()) {
//            changingLabel2.setText(String.valueOf(customerCount.getInt("Count")));
//        }
//    }
//    
//    @FXML
//    public void clickSearchCustAppts(ActionEvent event) throws SQLException {
//           if(customerComboBox.getSelectionModel().getSelectedItem() == null) {
//            Alerts.errorAlert("PLEASE SELECT CUSTOMER", "Please select a customer from the dropdown", "");
//        } else {
//            String customerName = customerComboBox.getSelectionModel().getSelectedItem();
//            searchCustomer(customerName);
//        }
////    }

    /** Search DB for # of customer appointments and display total in changing label. 
     * @event combo box selection
     */
    @FXML
    void selectCustomer(ActionEvent event) throws SQLException {
        String customerName = customerComboBox.getSelectionModel().getSelectedItem();
        Statement getCustomerCount = JDBC.getConnection().createStatement();
        String customerCountSQL = "SELECT COUNT(Customer_Name) AS 'Total' FROM appointments " +
                                "INNER JOIN customers " +
                                "ON appointments.Customer_ID = customers.Customer_ID " +
                                "WHERE Customer_Name='" + customerName + "'";
        ResultSet rs = getCustomerCount.executeQuery(customerCountSQL);
        while(rs.next()) {
            changingLabel2.setText(rs.getString("Total"));
    }
    }
    
    @FXML
    void selectMonth(ActionEvent event) {
      
    }

    @FXML
    void selectType(ActionEvent event) {

    }

    /**
     * Initialize combo boxes of Final Reports screen.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            fillCustomers();
            fillContacts();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        monthComboBox.setItems(monthsOL);
        typeComboBox.setItems(typeOL);
    }    
    
    /** Fill contact list combo box. */
    public void fillContacts() throws SQLException {
        Statement loadContactStatement = JDBC.getConnection().createStatement();
        String loadContactNameSQL = "SELECT * FROM contacts";
        ResultSet loadContactResults = loadContactStatement.executeQuery(loadContactNameSQL);

        while(loadContactResults.next()) {
            contactOL.add(loadContactResults.getString("Contact_Name"));
            contactComboBox.setItems(contactOL);
        }
    }

     /** Fill customer combo box. */
    public void fillCustomers() throws SQLException {

        Statement loadCustomersStatement = JDBC.getConnection().createStatement();
        String loadCustomerNameSQL = "SELECT * FROM customers";
        ResultSet loadCustomerResults = loadCustomersStatement.executeQuery(loadCustomerNameSQL);

        while(loadCustomerResults.next()) {
            currentCustomersOL.add(loadCustomerResults.getString("Customer_Name"));
            customerComboBox.setItems(currentCustomersOL);
        }
    }

     /** Fill month combo box with list of months. 
      * Note: months need to be capitalized 
      */
    public int monthSelection(String monthSelected) {
        int monthId;
        switch(monthSelected){
            case "FEBRUARY":
                monthId = 2;
                break;
            case "MARCH":
                monthId = 3;
                break;
            case "APRIL":
                monthId = 4;
                break;
            case "MAY":
                monthId = 5;
                break;
            case "JUNE":
                monthId = 6;
                break;
            case "JULY":
                monthId = 7;
                break;
            case "AUGUST":
                monthId = 8;
                break;
            case "SEPTEMBER":
                monthId = 9;
                break;
            case "OCTOBER":
                monthId = 10;
                break;
            case "NOVEMBER":
                monthId = 11;
                break;
            case "DECEMBER":
                monthId = 12;
                break;
            default:
                monthId = 1;
        }
        return monthId;
    }
    
    /** Fill the schedule table with correlating info.
     @return appointments
     */
    public ObservableList<Appointments> getApptSchedule() throws SQLException {
        String contactName = contactComboBox.getSelectionModel().getSelectedItem();

        Statement statement = JDBC.getConnection().createStatement();
        String appointmentInfoSQL = "SELECT appointments.*, contacts.* " +
                                "FROM appointments " +
                                "INNER JOIN contacts " +
                                "ON appointments.Contact_ID = contacts.Contact_ID " +
                                "WHERE Contact_Name='" + contactName + "'";

        ResultSet appointmentResults = statement.executeQuery(appointmentInfoSQL);

        while(appointmentResults.next()) {
            Appointments appointments = new Appointments(appointmentResults.getInt("Appointment_ID"),
                    appointmentResults.getString("Title"),
                    appointmentResults.getString("Description"),
                    appointmentResults.getString("Location"),
                    appointmentResults.getString("Contact_Name"),
                    appointmentResults.getString("Type"),
                    appointmentResults.getTimestamp("Start").toLocalDateTime(),
                    appointmentResults.getTimestamp("End").toLocalDateTime(),
                    appointmentResults.getInt("Customer_ID"),
                    appointmentResults.getInt("User_ID"));
            apptScheduleOL.add(appointments);
        }
        return apptScheduleOL;
    }
}
        

