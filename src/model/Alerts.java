package model;

import javafx.scene.control.Alert;

/**
 *
 * @author HannahBergman
 */
public class Alerts {
    /** Lambda expression used: set header of the error alert.
     * @param title alert title.
     * @param header header of alert.
     * @param content content of alert message. 
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
    * @param title alert title.
    * @param header header of alert.
    * @param content content of alert message.  
    */
    public static void informationAlert(String title, String header, String content) {
        Alert error = new Alert(Alert.AlertType.INFORMATION);
        error.setTitle(title);
        error.setHeaderText(header);
        error.setContentText(content);
        error.showAndWait();
    }

    /** Displays various alerts.
    *  @param variedAlert Alert type
    */
    public static void displayAlert(int variedAlert) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Alert alertForSave = new Alert(Alert.AlertType.INFORMATION);

        switch (variedAlert) {
            case 1:
                alert.setTitle("FIELD EMPTY  |  NAME REQUIRED");
                alert.setHeaderText("Name field is empty");
                alert.setContentText("Please enter name");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("FIELD EMPTY  |  ADDRESS REQUIRED");
                alert.setHeaderText("Address field is empty");
                alert.setContentText("Please enter valid address");
                alert.showAndWait();
                break;
            case 3:
                alert.setTitle("FIELD EMPTY  |  STATE/PROVINCE REQUIRED");
                alert.setHeaderText("No state/province selected");
                alert.setContentText("Please select your state/province from teh dropdown");
                alert.showAndWait();
                break;
            case 4:
                alert.setTitle("FIELD EMPTY  |  POSTAL CODE REQUIRED");
                alert.setHeaderText("Postal code field is empty");
                alert.setContentText("Please enter postal code");
                alert.showAndWait();
                break;
            case 5:
                alert.setTitle("FIELD EMPTY  |  PHONE NUMBER REQUIRED");
                alert.setHeaderText("Phone number field is empty");
                alert.setContentText("Please enter phone number with dashes, ex: 123-456-7890");
                alert.showAndWait();
                break;
            case 6:
                alertForSave.setTitle("SAVE SUCCESSFUL |  NEW CUSTOMER ADDED");
                alertForSave.setHeaderText("Customer information has been saved to the database");
                alertForSave.setContentText("New customer has been added");
                alertForSave.showAndWait();
                break;
            case 7:
                alertForSave.setTitle("SAVE SUCCESSFUL  |  CUSTOMER MODIFIED");
                alertForSave.setHeaderText("Customer information has been saved to the database");
                alertForSave.setContentText("Existing customer has been modified");
                alertForSave.showAndWait();
                break;
            case 8:
                alertForSave.setTitle("ARE YOU SURE  |  DELETE CUSTOMER");
                alertForSave.setHeaderText("Delete this customer?");
                alertForSave.setContentText("This is action is permanent and will delete the customer's saved information and any associated appointments you have with them");
                alertForSave.showAndWait();
                break;
            case 9:
                alert.setTitle("PLEASE SELECT A CUSTOMER");
                alert.setHeaderText("Please select a customer");
                alert.setContentText("Highlight the customer you'd like to modify/delete by clicking on the row they're associated with");
                alert.showAndWait();
                break;
            case 10:
                alert.setTitle("FIELD EMPTY  |  COUNTRY REQUIRED");
                alert.setHeaderText("No Country Selected");
                alert.setContentText("Please select a country from the dropdown");
                alert.showAndWait();
                break;
            case 11:
                alert.setTitle("CUSTOMER SUCCESSFULLY DELETED");
                alert.setHeaderText("The customer has been deleted from the database");
                alert.setContentText("All customer information and appointments have been permanently removed");
                alert.showAndWait();
                break;
            case 12:
                alert.setTitle("ERROR  |  USER ID NOT FOUND");
                alert.setHeaderText("Could not find User_Id from that username");
                alert.setContentText("Please enter a valid username");
                alert.showAndWait();
                break;
            case 13:
                alert.setTitle("ERROR  |  FIELD EMPTY");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Title field is empty");
                alert.showAndWait();
                break;
            case 14:
                alert.setTitle("ERROR  |  FIELD EMPTY");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Description field is empty");
                alert.showAndWait();
                break;
            case 15:
                alert.setTitle("ERROR  |  FIELD EMPTY");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Location field is empty");
                alert.showAndWait();
                break;
            case 16:
                alert.setTitle("ERROR  |  FIELD EMPTY");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Type field is empty");
                alert.showAndWait();
                break;
            case 17:
                alert.setTitle("ERROR  |  FIELD EMPTY");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Date picker is empty");
                alert.showAndWait();
                break;
            case 18:
                alert.setTitle("ERROR  |  FIELD EMPTY");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Start time field is empty");
                alert.showAndWait();
                break;
            case 19:
                alert.setTitle("ERROR  |  FIELD EMPTY");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("End time field is empty");
                alert.showAndWait();
                break;
            case 20:
                alert.setTitle("ERROR  |  FIELD EMPTY");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Existing customer field is empty");
                alert.showAndWait();
                break;
            case 21:
                alert.setTitle("ERROR  |  FIELD EMPTY");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("User ID field is empty");
                alert.showAndWait();
                break;
            case 22:
                alert.setTitle("ERROR  |  FIELD EMPTY");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Contact is empty");
                alert.showAndWait();
                break;
            case 23:
                alertForSave.setTitle("APPOINTMENT SUCCESSFULLY SAVED");
                alertForSave.setHeaderText("New appointment added");
                alertForSave.setContentText("Your appointment has been saved to the Database");
                alertForSave.showAndWait();
                break;
            case 24:
                alert.setTitle("ERROR  |  TIME CONFLICT");
                alert.setHeaderText("Appointment end time cannot be before start time");
                alert.setContentText("Please select a new time for your appointment");
                alert.showAndWait();
                break;
            case 25:
                alert.setTitle("ERROR  |  TIME CONFLICT");
                alert.setHeaderText("Appointment start time cannot be at the same time it ends");
                alert.setContentText("Please select a new time for your appointment");
                alert.showAndWait();
                break;
            case 26:
                alert.setTitle("ERROR  |  TIME CONFLICT");
                alert.setHeaderText("Appointment overlaps with an existing appointment");
                alert.setContentText("Please select a new time for your appointment");
                alert.showAndWait();
                break;
            case 28:
                alert.setTitle("ERROR  |  FIELD EMPTY");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Please ensure all fields are filled");
                alert.showAndWait();
                break;
            case 29:
                alert.setTitle("ERROR  |  TIME CONFLICT");
                alert.setHeaderText("Appointment is outside of operating hours");
                alert.setContentText("Please select a new time for your appointment");
                alert.showAndWait();
                break;
        }
    }
}
