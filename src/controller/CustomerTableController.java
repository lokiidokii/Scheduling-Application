package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    public TableView<Customer> customerTable;
    
    /*Customer Table - Address.*/
    @FXML
    public TableColumn<Customer, ?> customerTableAddressColumn;

    /*Customer Table - Country.*/
    @FXML
    public TableColumn<?, ?> customerTableCountryColumn;

    /*Customer Table - Customer ID.*/
    @FXML
    public TableColumn<?, ?> customerTableIDColumn;

    /*Customer Table - Name.*/
    @FXML
    public TableColumn<?, ?> customerTableNameColumn;

    /*Customer Table - Phone.*/
    @FXML
    public TableColumn<?, ?> customerTablePhoneColumn;

    /*Customer Table - Postal Code.*/
    @FXML
    public TableColumn<?, ?> customerTablePostalCodeColumn;

    /*Customer Table - State.*/
    @FXML
    public TableColumn<?, ?> customerTableStateColumn;
    
    /* Getter for selected customer.*/
    public static Customer getSelectedCustomer() {
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

    /*Delete Customer Button Action.
    * Deletes selected customer if conditions are met.
    */
    @FXML
    void clickDeleteCustomer(ActionEvent event) {
       
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
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/modCustomer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
