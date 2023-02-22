/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import helper.JDBC;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author LabUser
 */
public class CustomerInfo {
   /** Customer ID*/
    private int customerID;
    /** Customer Name*/
    private String customerName;
    /** Address*/
    private String address;
    /** City*/
    private String city;
    /** Postal Code*/
    private String postalCode;
    /** Phone number*/
    private String phoneNumber;
    /** Country*/
    private String country;
    /** Observable List of Customer called allCustomersList*/
    public static ObservableList<CustomerInfo> allCustomersList = FXCollections.observableArrayList();

    /** Customer constructor
     @param customerID
     @param customerName
     @param address
     @param city
     @param country
     @param postalCode
     @param phoneNumber */
    public CustomerInfo (int customerID, String customerName, String address, String city, String postalCode, String phoneNumber, String country) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
    }
    /** Getter for customer ID
     @return customerID*/
    public int getCustomerID() {
        return customerID;
    }

    /** Setter for customer id*/
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /** Getter for customer Name
     @return customer Name*/
    public String getCustomerName() {
        return customerName;
    }

    /** Setter for customerName*/
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /** Getter for address
     @return address*/
    public String getAddress() {
        return address;
    }

    /** Setter for address*/
    public void setAddress(String address) {
        this.address = address;
    }

    /** Getter for city
     @return city*/
    public String getCity() {
        return city;
    }

    /** Setter for city*/
    public void setCity(String city) {
        this.city = city;
    }

    /** Getter for Postal Code
     @return Postal Code*/
    public String getPostalCode() {
        return postalCode;
    }

    /** Setter for Postal Code*/
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /** Getter for Phone number
     @return Phone number*/
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /** Setter for Phone number*/
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /** Getter for country
     @return country*/
    public String getCountry() {
        return country;
    }

    /** Setter for country*/
    public void setCountry(String country) {
        this.country = country;
    }

    /** Getter for all customers from the database
     @return allCustomersList*/
    public static ObservableList<CustomerInfo> getGetAllCustomers() throws SQLException {

        Statement statement = JDBC.getConnection().createStatement();
        String customerInfoSQL = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, customers.Postal_Code, customers.Phone, countries.Country, first_level_divisions.*" +
                                "FROM customers " +
                                "INNER JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID " +
                                "INNER JOIN countries ON first_level_divisions.COUNTRY_ID = countries.Country_ID";

        ResultSet customerInfoResults = statement.executeQuery(customerInfoSQL);

        while(customerInfoResults.next()) {
            CustomerInfo customer = new CustomerInfo(customerInfoResults.getInt("Customer_ID"),
                                            customerInfoResults.getString("Customer_Name"),
                                            customerInfoResults.getString("Address"),
                                            customerInfoResults.getString("Division"),
                                            customerInfoResults.getString("Postal_Code"),
                                            customerInfoResults.getString("Phone"),
                                            customerInfoResults.getString("Country"));
            allCustomersList.add(customer);
        }
        return allCustomersList;
    }
}
