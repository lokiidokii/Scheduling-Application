package controller;

import helper.DBQueries;
import helper.JDBC;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Optional;

import model.Alerts;
import model.CustomerInfo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Customer Table Controller class.
 *
 * @author HannahBergman
 */
public class CustomerTableController implements Initializable {
    
    /* Scene variable*/
    Parent scene;
    /* Stage variable*/
    Stage stage;
    
    // TABLE COLUMN INFO
    
    /**/
    @FXML
    public TableView<CustomerInfo> customerTable;
    
    /*Customer Table - Address.*/
    @FXML
    public TableColumn<CustomerInfo, String> customerTableAddressColumn;
    
    /*Customer Table - City.*/
    @FXML
    private TableColumn<?, ?> customerTableCityColumn;

    /*Customer Table - Country.*/
    @FXML
    public TableColumn<CustomerInfo, String> customerTableCountryColumn;

    /*Customer Table - Customer ID.*/
    @FXML
    public TableColumn<CustomerInfo, Integer> customerTableIDColumn;

    /*Customer Table - Name.*/
    @FXML
    public TableColumn<CustomerInfo, String> customerTableNameColumn;

    /*Customer Table - Phone.*/
    @FXML
    public TableColumn<CustomerInfo, String> customerTablePhoneColumn;

    /*Customer Table - Postal Code.*/
    @FXML
    public TableColumn<CustomerInfo, String> customerTablePostalCodeColumn;

    /*Customer Table - State.*/
    @FXML
    public TableColumn<CustomerInfo, String> customerTableStateColumn;
    
    // CUSTOMER INFO
    
    /*Selected customer.*/
    private static CustomerInfo selectedCustomer;
    
    /* Get selected customer.*/
    public static CustomerInfo getSelectedCustomer() {
        return selectedCustomer;
    }
    
    // BUTTONS
    
    /*Add New Customer Button.*/
    @FXML
    public Button addNewCustomerButton;

    /*Delete Customer Button.*/
    @FXML
    public Button deleteCustomerButton;

    /*Modify Customer Button.*/
    @FXML
    public Button modifyCustomerButton;

    /*Main Menu Button.*/
    @FXML
    public Button returnToMainMenu;
    
    // BUTTON ACTIONS

    /*Add Customer Button Action.
    * Takes user to addCustomer screen so they can add a new customer.
    */
    @FXML
    void clickAddCustomer(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddCustomer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /*Delete Customer.
    * 
    * Check if customer has an existing appointment.
    * If they do, inform user that they need to delete the appointment first.  
    */
        public static int getCustomerApptCount(int customerID) throws SQLException {
        Statement customerApptCount = JDBC.getConnection().createStatement();
        String modifySQL =
                "SELECT COUNT(Appointment_ID) AS Count FROM appointments INNER JOIN customers ON customers.Customer_ID = appointments.Customer_ID WHERE customers.Customer_ID=" + customerID;

        ResultSet apptCount = customerApptCount.executeQuery(modifySQL);

        if(apptCount.next() && apptCount.getInt("Count") > 0) {
            Alerts.errorAlert("Cannot Delete Customer", "Customer can't be deleted", "You must delete the existing appointments you have with this customer first.");
            return -1;
        }
        return 0;
    }
    
    /*Delete Customer button action.
    * .  
    */ 
    @FXML
    void clickDeleteCustomer(ActionEvent event) throws SQLException, IOException {
        //Get selected customer
        CustomerInfo selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        //Alert user if selected customer doesn't exist
        if(selectedCustomer == null) {
            Alerts.alertDisplays(9);
        } else if(getCustomerApptCount(selectedCustomer.getCustomerID()) == 0){
            Alert confirmCustomerDelete = new Alert(Alert.AlertType.CONFIRMATION);
            confirmCustomerDelete.setHeaderText("Are you sure you want to delete this customer?");
            confirmCustomerDelete.setContentText("This customer will be deleted from the database immediately. You can't undo this action.");
            Optional<ButtonType> deleteResult = confirmCustomerDelete.showAndWait();

            if(deleteResult.isPresent() && deleteResult.get() == ButtonType.OK) {
                DBQueries.deleteFromCustomerTable(selectedCustomer.getCustomerID());
                Alerts.alertDisplays(11);
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/CustomerTable.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
    }


    /*Return to Main Menu Button Action.
    * Takes user back to Main Menu.
    */
    @FXML
    void clickMainMenu(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/SchedulingApp.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /*Modify Customer Button Action.
    * Takes user to modCustomer screen so they can modify an existing customer.
    */
    @FXML
    void clickModifyCustomer(ActionEvent event) throws IOException {
        //Get selected custoemr to modify
         selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        //Make sure user has selected a customer to modify
        //If unselected, an alert will appear informing user to select customer
        if(selectedCustomer == null) {
            Alerts.errorAlert( "Cannot Modify Customer", "No customer selected", "Please choose a customer to modify.");
        } 
        //Else user will be taken to modCustomer screen 
        else {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/modCustomer.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }    
    
    /*
     * Fill customer table with data from the DB.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      try {
            CustomerInfo.getGetAllCustomers().clear();
            customerTable.setItems(CustomerInfo.getGetAllCustomers());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        customerTableIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerTableNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerTableAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerTableCityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        customerTableCountryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        customerTablePostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerTablePhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
    }    
    
}
