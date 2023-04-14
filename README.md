# Scheduling Program 

## Description
This application was built for an international company and was designed for the primary purpose of scheduling appointments for clients across different timezones.

I designed a schema using MySQL (Workbench) that contains tables for appointments, contacts (company employees), customers, country information, first level division information (depending on the country, division may mean state, province, territory, or region - this helps determine which language to set the Login & Main Menu screens of the program to, as well as the timezones based on location), and users (database access info - the employees set up to login & use the program).

Each table has its own functionality and purpose. The customer has the ability to schedule appointments with various contacts, assuming they are within business hours, and do not conflict with other appointments at that time. 

## Purpose 
Add, modify, and delete appointments for new/existing customers in different timezones and languages

## Build Info
### IDE: 
      Apache Netbeans 12.6
### JDK: 
      Java SE 17.0.6
### GUI:
      JavaFX Version: 17.0.1
      Gluon Scene Builder 17
### MySQL Connector Driver:
      Mysql-connector-java-8.1.23


