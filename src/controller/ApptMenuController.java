package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;

import model.Alerts;
import model.Appointments;
import helper.DBQueries;
import helper.JDBC;
import java.net.URL;
import java.util.ResourceBundle;
import java.sql.SQLException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Appointment Menu Controller.
 *
 * @author HannahBergman
 */
public class ApptMenuController implements Initializable {

    /** Scene variable. */
    Parent scene;
    /** Stage variable. */
    Stage stage;
    
    /**Appointment Table - Appointment ID. */
    @FXML
    private TableColumn<Appointments, Integer> appointmentIDColumn;
    
    /**Appointment Table. */
    @FXML
    private TableView<Appointments> appointmentsTable;

    /**Filter Radio Button - by Month. */
    @FXML
    private RadioButton byMonthBtn;

    /**Filter Radio Button - by Week. */
    @FXML
    private RadioButton byWeekBtn;

    /**Appointment Table - Contact. */
    @FXML
    private TableColumn<Appointments, String> contactColumn;

    /**Appointment Table - Customer ID. */
    @FXML
    private TableColumn<Appointments, Integer> customerIDColumn;

    /**Appointment Table - Description. */
    @FXML
    private TableColumn<Appointments, String> descriptionColumn;

    /**Appointment Table - End Time. */
    @FXML
    private TableColumn<Appointments, LocalDateTime> endColumn;

    /**Toggle. */
    @FXML
    private ToggleGroup filterApptToggle;

    /**Appointment Table - Location. */
    @FXML
    private TableColumn<Appointments, String> locationColumn;

    /**Appointment Table - Start Time. */
    @FXML
    private TableColumn<Appointments, LocalDateTime> startColumn;

    /**Appointment Table - Title. */
    @FXML
    private TableColumn<Appointments, String> titleColumn;

    /**Appointment Table - Type. */
    @FXML
    private TableColumn<Appointments, String> typeColumn;

    /**Appointment Table - User ID. */
    @FXML
    private TableColumn<Appointments, Integer> userIDColumn;
    
    /**Observable list for appointments by month. */
    private ObservableList<Appointments> filterByMonthOL = FXCollections.observableArrayList();
    /**Observable list for appointments by week. */
    private ObservableList<Appointments> filterByWeekOL = FXCollections.observableArrayList();
    /**Selected Appointment. */
    public static Appointments selectedAppointment;
    
    /** Getter for selected appointments.
     * @return  */
    public static Appointments getSelectedAppointment() {
        return selectedAppointment;
    }
    
    // BUTTONS
    
    /**View All Appointments Button. */
    @FXML
    private Button viewAllAppointments;  
    /**Delete Appointment Button. */
    @FXML
    private Button deleteAppointment;
    /**Main Menu Button. */
    @FXML
    private Button mainMenuBtn;

    /*Modify Appointment Button.*/
    @FXML
    private Button modAppointment;
    
    /*Add Appointment Button.*/
    @FXML
    private Button addAppointment;

    // BUTTON ACTIONS
    
    /** Add appointment button.
    * Moves user to add appt screen.
    */
    @FXML
    void clickAddAppointment(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/addAppt.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Switch screens - specify which when using this.
     * @param event switch screens
     * @throws java.io.IOException
     */
    public void switchScreen (ActionEvent event, String resourcesString) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(resourcesString));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    /** 
     * Delete appointment button.
    * Deletes the appt from the DB, includes alert message warning user this action is permanent.
    */
    @FXML
    void clickDeleteAppointment(ActionEvent event) throws SQLException, IOException {
        Appointments selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();

        if(selectedAppointment == null) {
            //Make sure user selects an appointment to modify
            Alerts.displayAlert(9);
        } else 
            //Inform user that appointment will be permaently removed
        {
            Alert alertForDelete = new Alert(Alert.AlertType.CONFIRMATION);
            alertForDelete.setHeaderText("ARE YOU SURE?  |  CONFIRM APPOINTMENT DELETION");
            alertForDelete.setContentText("Pressing OK will cancel this appointment");
            Optional<ButtonType> removeAppointment = alertForDelete.showAndWait();
            //if ok button is clicked
            if(removeAppointment.isPresent() && removeAppointment.get() == ButtonType.OK) {
                //delete the selected appointment
                DBQueries.deleteFromApptTable(selectedAppointment.getAppointmentId());
                //Inform user appointment has now been deleted and therefore cancelled 
                Alerts.informationAlert("APPOINTMENT DELETED",
                        "Your appointment with the ID of " +  selectedAppointment.getAppointmentId() + " has been deleted",
                        "Your " + selectedAppointment.getType() + " meeting with " + selectedAppointment.getContactName() + " is cancelled");

                switchScreen(event, "/view/apptMenu.fxml");
            }
        }
    }

    /** Main menu button.
    * @param event Takes user back to the main Scheduling App menu screen.
    */
    @FXML
    void clickMainMenu(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/SchedulingApp.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** 
    * Modify appointment button.
    * @param event Takes user to the mod appt screen.
    */
    @FXML
    void clickModAppointment(ActionEvent event) throws IOException {
        selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();

        if(selectedAppointment == null) {
            Alert alertForModify = new Alert(Alert.AlertType.ERROR);
            alertForModify.setHeaderText("ERROR  |  PLEASE SELECT AN APPOINTMENT TO MODIFY");
            alertForModify.setContentText("Please select the appointment you want to modify");
            alertForModify.showAndWait();
        } else {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/modAppt.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    // FILTERING 
    
    /**
    * View all appointments button.
    * @parama event Takes user back to the original appt menu screen without filters. 
    */
    @FXML
    void clickViewAllAppointments(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/apptMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    /**
    * Filter by current week.
    */
    @FXML
    void clickFilterWeek(ActionEvent event) throws SQLException {
        filterByMonthOL.clear();
        filterByWeek().clear();
        appointmentsTable.setItems(filterByWeek());
    }
    
    /** 
     * Filter by week.
     * @return filtered week list
     * @throws java.sql.SQLException 
     */
    public ObservableList<Appointments> filterByWeek() throws SQLException {
        Statement weeklyAppointments = JDBC.getConnection().createStatement();
        String filterByWeekSql =
                "SELECT * " +
                "FROM appointments " +
                "INNER JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID " +
                "WHERE DATE(Start) = DATE(NOW()) " +
                "OR Start >= NOW() " +
                "AND  Start < DATE_ADD(CURRENT_DATE(), interval 7 day)";

        ResultSet filterResults = weeklyAppointments.executeQuery(filterByWeekSql);

        while(filterResults.next()) {
            Appointments appointments = new Appointments(
                    filterResults.getInt("Appointment_ID"),
                    filterResults.getString("Title"),
                    filterResults.getString("Description"),
                    filterResults.getString("Location"),
                    filterResults.getString("Contact_Name"),
                    filterResults.getString("Type"),
                    filterResults.getTimestamp("Start").toLocalDateTime(),
                    filterResults.getTimestamp("End").toLocalDateTime(),
                    filterResults.getInt("Customer_ID"),
                    filterResults.getInt("User_ID"));
            filterByWeekOL.add(appointments);
        }
        return filterByWeekOL;
    }
   
    /**
    * Filter by month.
     * @param monthSelected
     * @return selected month
    */
    public int monthSelection (String monthSelected) {
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
    
    /**
    * Filter by current month.
    */
    @FXML
    void clickFilterMonth(ActionEvent event) throws SQLException {
        filterByMonthOL.clear();
        filterByWeek().clear();
        appointmentsTable.setItems(filterByMonth());
    }

    /** 
     * Filter appointments by month.
     * @return appointments filtered by month
     * @throws java.sql.SQLException 
     */
    public ObservableList<Appointments> filterByMonth() throws SQLException {
        Month currentMonth = LocalDateTime.now().getMonth();
        String month = currentMonth.toString();
        int monthId = monthSelection(month);

        Statement monthlyAppointments = JDBC.getConnection().createStatement();
        String filterByMonthSql =
                "SELECT * " +
                "FROM appointments " +
                "INNER JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID " +
                "WHERE month(Start)=" + monthId;

        ResultSet filterResults = monthlyAppointments.executeQuery(filterByMonthSql);

        while(filterResults.next()) {
            Appointments appointments = new Appointments(
                    filterResults.getInt("Appointment_ID"),
                    filterResults.getString("Title"),
                    filterResults.getString("Description"),
                    filterResults.getString("Location"),
                    filterResults.getString("Contact_Name"),
                    filterResults.getString("Type"),
                    filterResults.getTimestamp("Start").toLocalDateTime(),
                    filterResults.getTimestamp("End").toLocalDateTime(),
                    filterResults.getInt("Customer_ID"),
                    filterResults.getInt("User_ID"));
            filterByMonthOL.add(appointments);
        }
        return filterByMonthOL;
    }
    
    /**
     * Fill appointments table with data.Lambda expression used: fill contact name in appointments table.
     * @param url url
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Appointments.getGetAllAppts().clear();
            appointmentsTable.setItems( Appointments.getGetAllAppts());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactColumn.setCellValueFactory(appointment -> new SimpleStringProperty(appointment.getValue().getContactName()));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIDColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
    }    
    
}
