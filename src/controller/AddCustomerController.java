package controller;

import helper.DBQueries;
import helper.DataProvider;
import helper.JDBC;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.sql.*;
import model.Alerts;
import model.CustomerInfo;

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

/*
 * Add Customer Controller class.
 *
 * @author HannahBergman
 */
public class AddCustomerController implements Initializable {
    /* Scene variable*/
    Parent scene;
    /* Stage variable*/
    Stage stage;
    
    /*Add New - Customer Address*/
    @FXML
    private TextField addCustomerAddress;
    /*Add New - Customer ID*/
    @FXML
    private TextField addCustomerID;
    /*Add New - Customer Name*/
    @FXML
    private TextField addCustomerName;
    /*Add New - Customer Phone Number*/
    @FXML
    private TextField addCustomerPhoneNum;
    /*Add New - Customer Postal Code*/
    @FXML
    private TextField addCustomerPostalCode;
    /*Add New - State Combo Box*/
    @FXML
    private ComboBox<String> stateComboBox;
    /*Add New - Country Combo Box*/
    @FXML
    private ComboBox<String> countryComboBox;
    /*Division ID From selected state*/
    public int stateDivisionID;

    
    // BUTTONS
    
    /*Cancel Add Customer Button*/
    @FXML
    private Button cancelAddCustomerButton;
    /*Save New Customer Button*/
    @FXML
    private Button saveNewCustomerButton;
    
    //Observable Lists
    /*Observable List for states.*/
    ObservableList<String> statesList = FXCollections.observableArrayList();
    /*Observable List for countries.*/
    ObservableList<String> countriesList = FXCollections.observableArrayList("United States", "Canada", "United Kingdom");
    
    // BUTTON ACTIONS
    
    /*Countries combo box dropdown.*/
    @FXML
    public void clickCountryComboBox(ActionEvent event) throws SQLException {
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
    
    
    /*Cancel Button.
    * Go back to customer table.
    */
    @FXML
    public void clickCustomerTable(ActionEvent event) throws IOException {
        //Alert user that data will be lost if they want to cancel
        Alert confirmCancel = new Alert(Alert.AlertType.CONFIRMATION);
        confirmCancel.setTitle("ARE YOU SURE?  |  Cancel Customer Addition");
        confirmCancel.setHeaderText("Are you sure you want to return to the Customer Information menu?");
        confirmCancel.setContentText("All text fields will be cleared and any data currently entered will be lost if not saved");
        Optional<ButtonType> cancelAdd = confirmCancel.showAndWait();
        //If user presses OK to Cancel, they'll return to customerTable screen
        if(cancelAdd.isPresent() && cancelAdd.get() == ButtonType.OK) {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/customerTable.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }
     
    /* Save new customer.
    * Update database with new customer values then return back to customer table.
    */
    @FXML
    void clickSaveNewCustomer(ActionEvent event) throws IOException, SQLException {
       int customerID = 0;
        for (CustomerInfo customer : CustomerInfo.getGetAllCustomers()) {
            if (customer.getCustomerID() > customerID) {
                customerID = customer.getCustomerID();
            }
        }
        String addName = addCustomerName.getText();
        String addAddress = addCustomerAddress.getText();
        String addPostalCode = addCustomerPostalCode.getText();
        String addPhoneNum = addCustomerPhoneNum.getText();
        String addState = stateComboBox.getSelectionModel().getSelectedItem();
        String addCountry = countryComboBox.getSelectionModel().getSelectedItem();

        if (nameFieldFilled(addName) && addressFieldFilled(addAddress) && postalCodeFieldFilled(addPostalCode) && phoneNumFieldFilled(addPhoneNum) && countryPicked(addCountry) && statePicked(addState)) {

            DBQueries.insertIntoCustomerTable(addName, addAddress, addPhoneNum, addPostalCode, DataProvider.divisionID);
            Alerts.alertDisplays(6);
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/customerTable.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    
    /* Fill the country Combo Box.*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        countryComboBox.setItems(countriesList);
    }    

    /*States/provinces combo box dropdown.*/
    @FXML
    public void clickStateComboBox(ActionEvent event) throws SQLException {
        String stateSelected = stateComboBox.getSelectionModel().getSelectedItem();

        getAllStatesDivisionID(stateSelected);
        DataProvider.divisionID = stateDivisionID;
        System.out.println(stateDivisionID);
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

    /* Check if the name field is empty and alert user to fill out if null.
    * @param name 
    */
    public boolean nameFieldFilled(String name) {
        if (addCustomerName.getText().isEmpty()) {
            Alerts.alertDisplays(1);
            return false;
        }
        return true;
    }

    /* Check if the address field is empty and alert user to fill out if null.
    * @param address 
    */
    public boolean addressFieldFilled(String address) {
        if (addCustomerAddress.getText().isEmpty()) {
            Alerts.alertDisplays(2);
            return false;
        }
        return true;
    }

    /* Check if the postal code field is empty and alert user to fill out if null.
    * @param postalCode 
    */
    public boolean postalCodeFieldFilled(String postalCode) {
        if (addCustomerPostalCode.getText().isEmpty()) {
            Alerts.alertDisplays(4);
            return false;
        }
        return true;
    }

    /* Check if the phone number field is empty and alert user to fill out if null.
    * @param phone 
    */
    public boolean phoneNumFieldFilled(String phone) {
        if (addCustomerPhoneNum.getText().isEmpty()) {
            Alerts.alertDisplays(5);
            return false;
        }
        return true;
    }

    /* Check if the country combo box is empty and alert user to select country if null.
    * @param country
    */
    public boolean countryPicked(String country) {
        if (countryComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.alertDisplays(10);
            return false;
        }
        return true;
    }

    /* Check if the state (state/province) combo box is empty and alert user to select state if null.
    * @param state 
    */
    public boolean statePicked (String state) {
        if (stateComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.alertDisplays(3);
            return false;
        }
        return true;
    }
}

