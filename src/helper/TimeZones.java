/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Alerts;

/**
 *
 * @author LabUser
 */
public class TimeZones {
     /** ObservableList for appointment times.*/
    private ObservableList<LocalTime> appointmentTimeList = FXCollections.observableArrayList();

    /** Generates all the times for the time combo boxes on the add and modify appointments screen
     @return appointmentTimeList*/
    public ObservableList<LocalTime> generateTimeList() {
        LocalTime appointmentTime = LocalTime.of(0,0);
        appointmentTimeList.add(appointmentTime);
        for(int i =0; i < 23; i++) {
            appointmentTime = appointmentTime.plusHours(1);
            appointmentTimeList.add(appointmentTime);
        }
        return appointmentTimeList;
    }

    public static boolean isOutsideBusinessHours(LocalDate selectedDate, LocalTime startTime, LocalTime endTime, ZoneId localZoneId) {
        ZonedDateTime startZDT = ZonedDateTime.of(LocalDateTime.of(selectedDate, startTime), localZoneId);
        ZonedDateTime endZDT = ZonedDateTime.of(LocalDateTime.of(selectedDate, endTime), localZoneId);

        ZonedDateTime estStartZDT = ZonedDateTime.ofInstant(startZDT.toInstant(), ZoneId.of("America/New_York"));
        ZonedDateTime estEndZDT = ZonedDateTime.ofInstant(endZDT.toInstant(), ZoneId.of("America/New_York"));

        if(estStartZDT.toLocalDateTime().toLocalTime().isBefore(LocalTime.of(8,0)) ||
                estStartZDT.toLocalDateTime().toLocalTime().isAfter(LocalTime.of(22,0))) {

            Alerts.errorAlert("Outside Business Hours", "Business hours are 8am - 10pm eastern",
                    "Please select a start time within business hours");
         return true;
        } else if(estEndZDT.toLocalDateTime().toLocalTime().isBefore(LocalTime.of(8,0)) ||
                estEndZDT.toLocalDateTime().toLocalTime().isAfter(LocalTime.of(22,0))) {

            Alerts.errorAlert("Outside Business Hours", "Business hours are 8am - 10pm eastern",
                    "Please select an end time within business hours");
            return true;
        }
        return false;
    }
}
