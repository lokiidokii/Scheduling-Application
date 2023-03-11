package controller;

import helper.DBQueries;
import helper.DataProvider;
import helper.JDBC;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.scene.control.*;
import model.Alerts;

import model.CustomerInfo;

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
    private TextField addressTxtFld;
    /*Add New - Customer Id.*/
    @FXML
    private TextField idTextFld;
    /*Add New - Customer Name.*/
    @FXML
    private TextField nameTxtFld;
    /*Add New - Customer Phone Number. */
    @FXML
    private TextField phoneTxtFld;
    /*Add New - Customer Postal Code.*/
    @FXML
    private TextField postalTxtFld;
    
    /*Country Combo Box Dropdown.*/
    @FXML
    private ComboBox<String> countryComboBox;
     /*State Combo Box Dropdown*/
    @FXML
    private ComboBox<String> stateComboBox;
    
    /*Division ID From selected state*/
    public int stateDivisionID;
    
    /*Selected customer.*/
    CustomerInfo selectedCustomer;
    
    // BUTTONS
    
    /*Save Customer Updates Button*/
    @FXML
    private Button saveCustomerButton;
    /*Cancel Changes Button.*/
    @FXML
    private Button cancelCustomerButton;
    
    //Observable Lists
    /*Observable List for states.*/
    ObservableList<String> statesList = FXCollections.observableArrayList();
    /*Observable List for countries.*/
    ObservableList<String> countriesList = FXCollections.observableArrayList("United States", "Canada", "United Kingdom");
    
    /*
     * Fill in the fields with information to modify from the selected customer.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        countryComboBox.setItems(countriesList);
        selectedCustomer = CustomerTableController.getSelectedCustomer();

        idTextFld.setText(String.valueOf(selectedCustomer.getCustomerID()));
        nameTxtFld.setText(selectedCustomer.getCustomerName());
        addressTxtFld.setText(selectedCustomer.getAddress());
        postalTxtFld.setText(selectedCustomer.getPostalCode());
        phoneTxtFld.setText(selectedCustomer.getPhoneNumber());
        countryComboBox.setValue(selectedCustomer.getCountry());
        stateComboBox.setValue(selectedCustomer.getCity());
    }    
    
    // BUTTON ACTIONS
    
    /*Clicking on the country combobox dropdown*/
    @FXML
    void clickCountryComboBox(ActionEvent event) throws SQLException {
        String countrySelection = countryComboBox.getSelectionModel().getSelectedItem();

        //Populating US states into states combo box
        if(countrySelection.equals("United States")) {
            Statement statement = JDBC.getConnection().createStatement();
            String getStatesSQL = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = 1";
            ResultSet usStates = statement.executeQuery(getStatesSQL);

            while (usStates.next()) {
                String state = usStates.getString("Division");
                statesList.add(state);
                stateComboBox.setItems(statesList);
            }
            statement.close();

        } else if(countrySelection.equals("United Kingdom")) {
            Statement statement = JDBC.getConnection().createStatement();
            String getStatesSQL = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = 2";
            ResultSet ukCountries = statement.executeQuery(getStatesSQL);

            while (ukCountries.next()) {
                String ukCountry = ukCountries.getString("Division");
                statesList.add(ukCountry);
                stateComboBox.setItems(statesList);
            }
            statement.close();

        } else {
            Statement statement = JDBC.getConnection().createStatement();
            String getStatesSQL = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = 3";
            ResultSet caProvinces = statement.executeQuery(getStatesSQL);

            while (caProvinces.next()) {
                String province = caProvinces.getString("Division");
                statesList.add(province);
                stateComboBox.setItems(statesList);
            }
            statement.close();
        }  
    }

    /*Clicking cancel - go back to customer table.*/
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

     /* 
    * Change screen.
    * @param actionEvent Action event
    * @param resourcesString Screen link 
     */
    public void changeScreen(ActionEvent event, String resourcesString) throws IOException {
        //Resources example: "/view/mainMenu.fxml"
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(resourcesString));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    /*Click save - save customer modifications.*/
    @FXML
    public void clickSaveCustomer(ActionEvent event) throws SQLException, IOException {
        String customerId = idTextFld.getText();
        String customerName = nameTxtFld.getText();
        String customerAddress = addressTxtFld.getText();
        String customerPostalCode = postalTxtFld.getText();
        String customerPhone = phoneTxtFld.getText();
        
        DBQueries.updateCustomerTable(selectedCustomer.getCustomerID(), customerName, customerAddress, customerPostalCode, customerPhone, DataProvider.divisionID);
        Alerts.alertDisplays(6);
        changeScreen(event, "/view/customerTable.fxml");   
    }

    /*Click state combo box - select state/province to modify.*/
    @FXML
    void clickStateComboBox(ActionEvent event) throws SQLException {
       String stateSelected = stateComboBox.getSelectionModel().getSelectedItem();

        getAllStatesDivisionID(stateSelected);
        DataProvider.divisionID = stateDivisionID;
    }
    
    /* Get cities from Division ID.
    *@param comboBoxSelection Combo box selection
    */
    public void getAllStatesDivisionID(String comboBoxSelection) throws SQLException {
        Statement state = JDBC.getConnection().createStatement();
        String getAllStatesDivisionIDSQL = "SELECT Division_ID FROM first_level_divisions WHERE Division='" + comboBoxSelection + "'";
        ResultSet result = state.executeQuery(getAllStatesDivisionIDSQL);

        while(result.next()) {
            stateDivisionID = result.getInt("Division_ID");
        }
    }
}
