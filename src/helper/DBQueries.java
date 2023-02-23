package helper;

import model.Alerts;
import java.sql.*;

/**
 *
 * @author HannahBergman
 */

/*Database Queries Class.*/
public class DBQueries {
    /* Insert customer into customer table.
    * @param customerName Customer Name
    * @param address Address
    * @param divisionID Division ID
    * @param Phone Phone Number
    * @param postalCode Postal Code 
    */
    public static int insertIntoCustomerTable(String customerName, String address, String Phone, String postalCode, int divisionID) throws SQLException {
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
    
     /* Update customer table values from the DB.
     *@param customerName Customer Name
     *@param address Address
     *@param divisionID Division ID
     *@param Phone Phone Number
     *@param postalCode Postal Code
     *@param customerID Customer ID
    */
    public static int updateCustomerTable(int customerID, String customerName, String address, String postalCode, String Phone, int divisionID) throws SQLException {

        String modifySQL = "UPDATE customers SET Customer_Name=?, Address=?, Phone=?, Postal_Code=?, Division_ID=? WHERE Customer_ID=?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(modifySQL);

        preparedStatement.setInt(6, customerID);
        preparedStatement.setString(1, customerName);
        preparedStatement.setString(2, address);
        preparedStatement.setString(3, Phone);
        preparedStatement.setString(4, postalCode);
        preparedStatement.setInt(5, divisionID);

        int rowsUpdates = preparedStatement.executeUpdate();

        if (rowsUpdates == 1) {
            Alerts.alertDisplays(7);
        }
        preparedStatement.close();
        return rowsUpdates;
    }

    /* Delete customer from the DB.
     *@param customerID customerID
    */
    public static int deleteFromCustomerTable(int customerID) throws SQLException {

        String modifySQL = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(modifySQL);

        preparedStatement.setInt(1, customerID);

        int rowsDeleted = preparedStatement.executeUpdate();

        preparedStatement.close();
        return rowsDeleted;
    }

    /* Delete appt from DB.
     @param appointmentId appointment ID
     */
    public static int deleteFromApptTable(int appointmentId) throws SQLException {

        String deleteAppointmentSQL = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(deleteAppointmentSQL);

        preparedStatement.setInt(1, appointmentId);

        int rowsDeletedForAppointments = preparedStatement.executeUpdate();

        preparedStatement.close();
        return rowsDeletedForAppointments;
    }

    /* Insert values into appt table.
     * @param userId User ID
     * @param type Appt Type
     * @param location Location
     * @param description Description
     * @param end End time
     * @param start Start time
     * @param title Title
     * @param contactId Contact ID
     * @param customerId Customer ID
    */
    public static int insertAppt(String title, String description, String location, String type, 
            Timestamp start, Timestamp end, int customerId, int userId, int contactId) throws SQLException {
        String insertApptSQL = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) " +
                               "VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(insertApptSQL);

        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setInt(7, customerId);
        ps.setInt(8, userId);
        ps.setInt(9, contactId);

        int rowsAffectedAppointmentInsert = ps.executeUpdate();

        ps.close();
        return rowsAffectedAppointmentInsert;
    }

    /* Update appts from the DB with appt ID.
     * @param appointmentID Appointment ID
     * @param customerId Customer ID
     * @param contactId Contact ID
     * @param title Title
     * @param start Start
     * @param end End
     * @param description Description
     * @param location Location
     * @param type Type
     * @param userId User ID
    */
    public static int updateAppointment(int appointmentID, String title, String description, String location, String type,
                                        Timestamp start, Timestamp end, int customerId, int userId, int contactId) throws SQLException {

        String updateApptSQL = "UPDATE appointments SET Title=?, Description=?, Location=?, Type=?, Start=?, End=?, " +
                                    "Customer_ID=?, User_ID=?, Contact_ID=? " +
                                "WHERE Appointment_ID=?";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(updateApptSQL);

        ps.setInt(10, appointmentID);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setInt(7, customerId);
        ps.setInt(8, userId);
        ps.setInt(9, contactId);

        int rowsAffectedAppointmentInsert = ps.executeUpdate();

        ps.close();
        return rowsAffectedAppointmentInsert;
    }
}

