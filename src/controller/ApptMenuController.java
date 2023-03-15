package controller;

import java.net.URL;
import java.util.ResourceBundle;
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
import model.Appointments;
import java.sql.SQLException;
import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;

import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author HannahBergman
 */
public class ApptMenuController implements Initializable {

    /* Scene variable*/
    Parent scene;
    /* Stage variable*/
    Stage stage;
    
    /*Appointment Table - Appointment ID.*/
    @FXML
    private TableColumn<?, ?> appointmentIDColumn;
    
    /*Appointment Table.*/
    @FXML
    private TableView<?> appointmentsTable;

    /*Filter Radio Button - by Month.*/
    @FXML
    private RadioButton byMonthBtn;

    /*Filter Radio Button - by Week.*/
    @FXML
    private RadioButton byWeekBtn;

    /*Appointment Table - Contact.*/
    @FXML
    private TableColumn<?, ?> contactColumn;

    /*Appointment Table - Customer ID.*/
    @FXML
    private TableColumn<?, ?> customerIDColumn;

    /*Appointment Table - Description.*/
    @FXML
    private TableColumn<?, ?> descriptionColumn;

    /*Appointment Table - End Time.*/
    @FXML
    private TableColumn<?, ?> endColumn;

    /*Toggle.*/
    @FXML
    private ToggleGroup filterApptToggle;

    /*Appointment Table - Location.*/
    @FXML
    private TableColumn<?, ?> locationColumn;

    /*Appointment Table - Start Time.*/
    @FXML
    private TableColumn<?, ?> startColumn;

    /*Appointment Table - Title.*/
    @FXML
    private TableColumn<?, ?> titleColumn;

    /*Appointment Table - Type.*/
    @FXML
    private TableColumn<?, ?> typeColumn;

    /*Appointment Table - User ID.*/
    @FXML
    private TableColumn<?, ?> userIDColumn;
    
    /*Observable list for appointments by month.*/
    private ObservableList<Appointments> filterByMonth = FXCollections.observableArrayList();
    /*Observable list for appointments by week.*/
    private ObservableList<Appointments> filterByWeek = FXCollections.observableArrayList();
    /*Selected Appointment.*/
    public static Appointments selectedAppointment;
    
    /* Getter for selected appointments.*/
    public static Appointments getSelectedAppointment() {
        return selectedAppointment;
    }
    
    // BUTTONS
    
    /*View All Appointments Button.*/
    @FXML
    private Button viewAllAppointments;
    
    /*Delete Appointment Button.*/
    @FXML
    private Button deleteAppointment;
    
    /*Main Menu Button.*/
    @FXML
    private Button mainMenuBtn;

    /*Modify Appointment Button.*/
    @FXML
    private Button modAppointment;
    
    /*Add Appointment Button.*/
    @FXML
    private Button addAppointment;

    // BUTTON ACTIONS
    @FXML
    void clickAddAppointment(ActionEvent event) {

    }

    @FXML
    void clickDeleteAppointment(ActionEvent event) {

    }

    @FXML
    void clickMainMenu(ActionEvent event) {

    }

    @FXML
    void clickModAppointment(ActionEvent event) {

    }

    @FXML
    void clickViewAllAppointments(ActionEvent event) {

    }

    /**
     * Fill appointment table with relevant info.
     * Lambda expression used: fill contact name in appointments table.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Appointments.getGetAllAppts().clear();
            appointmentsTable.setItems(Appointments.getGetAllAppts());
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
