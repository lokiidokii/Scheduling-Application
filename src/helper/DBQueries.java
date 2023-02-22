/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helper;

import model.Alerts;
import java.sql.*;

/**
 *
 * @author LabUser
 */
public class DBQueries {
/** Insert customer in to the database query
     @param customerName customer Name
     @param address address
     @param divisionID divisionID
     @param Phone Phone
     @param postalCode postalCode */
    public static int insertIntoCustomersTable(String customerName, String address, String Phone, String postalCode, int divisionID) throws SQLException {
        String sql = "INSERT INTO customers (Customer_Name, Address, Phone, Postal_Code, Division_ID) VALUES(?,?,?,?,?)";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        ps.setString(1, customerName);
        ps.setString(2, address);
        ps.setString(3, Phone);
        ps.setString(4, postalCode);
        ps.setInt(5, divisionID);

        int rowsAffected = ps.executeUpdate();

        ps.close();
        return rowsAffected;
    }
}

