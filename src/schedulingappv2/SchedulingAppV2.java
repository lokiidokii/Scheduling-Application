package schedulingappv2;

import helper.JDBC;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Scheduling Application Main.
 *
 * @author HannahBergman
 */
public class SchedulingAppV2 extends Application {

    /**
     * Main start method.
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        /** Connecting to Client-Side DB via Netbeans. */
        /*Establishing aka OPening the DB connection.*/
        JDBC.openConnection(); //when ran, 'Connection successful!' should appear in output
        /*Start up JavaFX application.*/
        launch(args);
        /*Closing the DB connection.*/
        JDBC.closeConnection(); //when ran, 'Connection closed!' should appear in output
        
    }

    @Override
    public void start(Stage stage) throws Exception {
        /** Main Menu Load Screen. */ 
       Parent root = FXMLLoader.load(getClass().getResource("/view/loginScreen.fxml"));
       Scene scene = new Scene(root);
       stage.setScene(scene);
       stage.setTitle("Scheduling Application");
       stage.show();
    }
    
}
