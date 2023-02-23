package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;

/**
 * Customer Table Controller class.
 *
 * @author HannahBergman
 */
public class CustomerTableController implements Initializable {
    
    /*Customer Table - Address.*/
    @FXML
    private TableColumn<?, ?> customerTableAddressColumn;

    /*Customer Table - Country.*/
    @FXML
    private TableColumn<?, ?> customerTableCountryColumn;

    /*Customer Table - Customer ID.*/
    @FXML
    private TableColumn<?, ?> customerTableIDColumn;

    /*Customer Table - Name.*/
    @FXML
    private TableColumn<?, ?> customerTableNameColumn;

    /*Customer Table - Phone.*/
    @FXML
    private TableColumn<?, ?> customerTablePhoneColumn;

    /*Customer Table - Postal Code.*/
    @FXML
    private TableColumn<?, ?> customerTablePostalCodeColumn;

    /*Customer Table - State.*/
    @FXML
    private TableColumn<?, ?> customerTableStateColumn;
    
    /*Add New Customer Button.*/
    @FXML
    private Button addNewCustomerButton;

    /*Delete Customer Button.*/
    @FXML
    private Button deleteCustomerButton;

    /*Modify Customer Button.*/
    @FXML
    private Button modifyCustomerButton;

    /*Main Menu Button.*/
    @FXML
    private Button returnToMainMenu;

    /*Add Customer Button Action.
    * 
    */
    @FXML
    void clickAddCustomer(ActionEvent event) {

    }

    @FXML
    void clickDeleteCustomer(ActionEvent event) {

    }

    @FXML
    void clickMainMenu(ActionEvent event) {

    }

    @FXML
    void clickModifyCustomer(ActionEvent event) {

    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
