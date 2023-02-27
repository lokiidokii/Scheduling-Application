package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Modify Customer Form.
 *
 * @author HannahBergman
 */
public class ModCustomerController implements Initializable {
    
    /* Scene variable*/
    Parent scene;
    /* Stage variable*/
     Stage stage;
    
    /*Add New - Customer Address.*/
    @FXML
    private TextField addCustomerAddress;
    /*Add New - Customer Id.*/
    @FXML
    private TextField addCustomerID;
    /*Add New - Customer Name.*/
    @FXML
    private TextField addCustomerName;
    /*Add New - Customer Phone Number. */
    @FXML
    private TextField addCustomerPhoneNum;
    /*Add New - Customer Postal Code.*/
    @FXML
    private TextField addCustomerPostalCode;
    
    /*Country Combo Box Dropdown.*/
    @FXML
    private ComboBox<String> countryComboBox;
     /*State Combo Box Dropdown*/
    @FXML
    private ComboBox<String> stateComboBox;
    
    /*Division ID From selected state*/
    public int stateDivisionID;
    
    // BUTTONS
    
    /*Save Customer Updates Button*/
    @FXML
    private Button saveNewCustomerButton;
    /*Cancel Changes Button.*/
    @FXML
    private Button cancelAddCustomerButton;
    
    //Observable Lists
    /*Observable List for states.*/
    ObservableList<String> statesList = FXCollections.observableArrayList();
    /*Observable List for countries.*/
    ObservableList<String> countriesList = FXCollections.observableArrayList("United States", "Canada", "United Kingdom");
    
    // BUTTON ACTIONS
    
    /*Clicking on the country combobox dropdown*/
    @FXML
    void clickCountryComboBox(ActionEvent event) {
        
    }

    @FXML
    void clickCustomerTable(ActionEvent event) throws IOException {
        //Alert user that data will be lost if they want to cancel
        Alert confirmCancel = new Alert(Alert.AlertType.CONFIRMATION);
        confirmCancel.setTitle("ARE YOU SURE?  |  Cancel Customer Modification");
        confirmCancel.setHeaderText("Are you sure you want to return to the Customer Information menu?");
        confirmCancel.setContentText("All text fields will be cleared and any data currently entered will be lost if not saved");
        Optional<ButtonType> cancelMod = confirmCancel.showAndWait();
        //If user presses OK to Cancel, they'll return to customerTable screen
        if(cancelMod.isPresent() && cancelMod.get() == ButtonType.OK) {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/customerTable.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    @FXML
    void clickSaveNewCustomer(ActionEvent event) {

    }

    @FXML
    void clickStateComboBox(ActionEvent event) {

    }
    /**
     * Initialize the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
