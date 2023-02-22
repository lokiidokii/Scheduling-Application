/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import javafx.scene.control.Alert;

/**
 *
 * @author HannahBergman
 */
public class Alerts {
    /** Error Alert.
     Lambda is used here to set the header of the error alert.
     @param title alert title.
     @param header header of alert.
     @param content content of alert message. 
     */
    public static void errorAlert(String title, String header, String content) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        final Runnable runnable = () -> error.setTitle(title);
        runnable.run();
        error.setHeaderText(header);
        error.setContentText(content);
        error.showAndWait();
    }

    /** Informational Alert.
    @param title alert title.
     @param header header of alert.
     @param content content of alert message.  
     */
    public static void informationAlert(String title, String header, String content) {
        Alert error = new Alert(Alert.AlertType.INFORMATION);
        error.setTitle(title);
        error.setHeaderText(header);
        error.setContentText(content);
        error.showAndWait();
    }

    /** Displays various alerts.
     @param alertType Alert type*/
    public static void alertDisplays(int alertType) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Alert alertForSave = new Alert(Alert.AlertType.INFORMATION);

        switch (alertType) {
            case 1:
                alert.setTitle("Error");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Name field is empty.");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Error");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Address field is empty.");
                alert.showAndWait();
                break;
            case 3:
                alert.setTitle("Error");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("City must be selected.");
                alert.showAndWait();
                break;
            case 4:
                alert.setTitle("Error");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Postal code field is empty.");
                alert.showAndWait();
                break;
            case 5:
                alert.setTitle("Error");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Phone number field is empty.");
                alert.showAndWait();
                break;
            case 6:
                alertForSave.setTitle("Saved Customer");
                alertForSave.setHeaderText("Customer Saved to the Database");
                alertForSave.setContentText("New customer has been saved");
                alertForSave.showAndWait();
                break;
            case 7:
                alertForSave.setTitle("Modified Customer");
                alertForSave.setHeaderText("Customer Modified ");
                alertForSave.setContentText("Customer has been modified and saved");
                alertForSave.showAndWait();
                break;
            case 8:
                alertForSave.setTitle("Deleted Customer");
                alertForSave.setHeaderText("Are you sure you want to delete this customer?");
                alertForSave.setContentText("Deleting the customer will remove them and their appointments");
                alertForSave.showAndWait();
                break;
            case 9:
                alert.setTitle("No Highlighted Customer");
                alert.setHeaderText("Customer was not highlighted");
                alert.setContentText("Please highlight a customer");
                alert.showAndWait();
                break;
            case 10:
                alert.setTitle("No Country Selected");
                alert.setHeaderText("Country field cannot me empty");
                alert.setContentText("Please select a country");
                alert.showAndWait();
                break;
            case 11:
                alert.setTitle("Customer Deleted");
                alert.setHeaderText("Customer was deleted from the the database successfully.");
                alert.setContentText("All customer information and appointments were deleted.");
                alert.showAndWait();
                break;
            case 12:
                alert.setTitle("User Id Not Found");
                alert.setHeaderText("Couldn't find User_Id from that username");
                alert.setContentText("Please enter a valid username");
                alert.showAndWait();
                break;
            case 13:
                alert.setTitle("Error");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Title field is empty.");
                alert.showAndWait();
                break;
            case 14:
                alert.setTitle("Error");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Description field is empty.");
                alert.showAndWait();
                break;
            case 15:
                alert.setTitle("Error");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Location field is empty.");
                alert.showAndWait();
                break;
            case 16:
                alert.setTitle("Error");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Type field is empty.");
                alert.showAndWait();
                break;
            case 17:
                alert.setTitle("Error");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Date picker is empty.");
                alert.showAndWait();
                break;
            case 18:
                alert.setTitle("Error");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Start time field is empty.");
                alert.showAndWait();
                break;
            case 19:
                alert.setTitle("Error");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("End time field is empty.");
                alert.showAndWait();
                break;
            case 20:
                alert.setTitle("Error");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Existing customer field is empty.");
                alert.showAndWait();
                break;
            case 21:
                alert.setTitle("Error");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("User ID field is empty.");
                alert.showAndWait();
                break;
            case 22:
                alert.setTitle("Error");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Contact is empty.");
                alert.showAndWait();
                break;
            case 23:
                alertForSave.setTitle("Saved Appointment");
                alertForSave.setHeaderText("Appointment Saved to the Database");
                alertForSave.setContentText("New Appointment has been saved");
                alertForSave.showAndWait();
                break;
            case 24:
                alert.setTitle("Error");
                alert.setHeaderText("Appointment Cannot End Before It Starts");
                alert.setContentText("End time must be after start time.");
                alert.showAndWait();
                break;
            case 25:
                alert.setTitle("Error");
                alert.setHeaderText("Appointment Cannot Start At the Same Time It Ends.");
                alert.setContentText("Please select a end time that is after the start time.");
                alert.showAndWait();
                break;
            case 26:
                alert.setTitle("Error");
                alert.setHeaderText("Appointment Collision");
                alert.setContentText("A customer already has an appointment with this time. Please select another time.");
                alert.showAndWait();
                break;
            case 28:
                alert.setTitle("Error");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Fields cannot be empty");
                alert.showAndWait();
                break;
            case 29:
                alert.setTitle("Error");
                alert.setHeaderText("Appointment Outside of Business Hours");
                alert.setContentText("Appointment must be within business hours.");
                alert.showAndWait();
                break;
        }
    }
}
