package helper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;

/** Login Activity Tracker.
 *
 * @author HannahBergman
 */
public class LoginActivity {
    /**File name.*/
    private static final String FILE_NAME = "login_activity.txt";

    /** Constructor. */
    public LoginActivity() {}

    /** Log attempt that tracks all login attempts. */
    public static void logAttempt(String username, boolean loggedIn, String message) {
        try (FileWriter fileWriter = new FileWriter(FILE_NAME, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
            printWriter.println("Username " + username + " was a" + (loggedIn ? " success!" : " failure.") + " " + message + " " + Instant.now().toString());
        } catch (IOException ex) {
            System.out.println("Log In Error: " + ex.getMessage());
        }
    }
}