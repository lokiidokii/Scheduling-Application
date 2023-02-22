/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;

/**
 *
 * @author LabUser
 */
public class LoginActivity {
/** File name that is file*/
    private static final String FILE_NAME = "login_activity.txt";

    /** Basic constructor*/
    public LoginActivity() {}

    /** Log attempt that tracks all login attempts
     CREDIT: A user on Stack Overflow helped me develop this snippet. It is my work with a few suggestions.*/
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