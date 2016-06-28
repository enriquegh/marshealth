# mars

## Synopsis

This is the alpha version of our Medical Mobile App with the name "MARS Health". This project currently consists of one android app, a web interface, a MySQL database with client and appointment data, as well as an API that ties the front-end with the back-end.

## Table of Contents

1. [Android](android/README.md)
2. [Web Interface](web/README.md)
3. [MySQL database](#mysql-database)
4. [API](#api)
5. [Things Completed](#things-completed)
6. [Things to be done](#things-to-be-done)
***


## MySQL database

Also included in this repository is a file called **[mars.sql](mars.sql)**
This file will create a database with all necessary tables, triggers, routines and status tables.

There are several ways on how to import the sql script but the one that was done by our team was by using phpMyAdmin. Feel free to populate the database in anyway but this is how it was done by us:

1. Open phpMyAdmin.
2. Create a database called "mars" and select it by clicking the database name in the list on the left of the screen.
3. Click the SQL link. This should bring up a new screen where you can either type in SQL commands, or upload your SQL file.
5. Use the browse button to find the database file.
6. Click Go button. This will upload the backup, execute the SQL commands and re-create your database.


***

## API

Currently the a php script developed by [mevdschee](https://github.com/mevdschee/php-crud-api). NOTE: Version used in this project is different from mevdschee's current version.

This API script allows us to use CRUD implementation to get relevant information from our MySQL server. This script converts all the tables on a given database into a route.

For example:

    GET www.example.com/api.php/clients

will return, in JSON format, all the current rows in the clients table.

In this project we set everything using localhost since the script is running on the server locally. The current settings on the script are the following:

        $api = new MySQL_CRUD_API(array(
        'hostname'=>'localhost',
        'username'=>'root',
        'password'=>'root',
        'database'=>'mars',
        'charset'=>'utf8'
        ));  
        $api->executeCommand();

This can be changed depending on future features and endeavors.

***

### Tables
The following, are all tables used in the project and explained.

#### clients
Holds the clients information and has the following columns:

`client_id` int(11) unsigned NOT NULL

`name` varchar(50) NOT NULL

`l_name` varchar(50) NOT NULL

`email` varchar(320) NOT NULL

`pwd` varchar(128) NOT NULL

`status` tinyint(2) unsigned NOT NULL

The status column field contains a FOREIGN KEY which references the client_status.
The available options are:

0 = inactive, 1 = active


NOTE: Password is currently stored as plain text.
A feature in the near future should be SALTing this password.

#### staff

Contains the staff members involved in the office, including doctors.
The members of this group will have access to the web interface and depending
on their status, will appear in the messaging portion of the Android appplication.

The columns on the staff table are:

`staff_id` int(11) unsigned NOT NULL

`name` varchar(50) NOT NULL

`l_name` varchar(50) NOT NULL

`username` varchar(50) NOT NULL

`pwd` varchar(128) NOT NULL

`position` tinyint(2) NOT NULL

As with the clients table, this table contains a column referenced by a staff_status table.

The options are:

0 = admin, 1 = doctors, 2 = staff

#### appointments

`date` date NOT NULL

`timeStart` time NOT NULL

`timeEnd` time NOT NULL

`status` tinyint(4) unsigned NOT NULL DEFAULT '0'

`patient_id` int(11) unsigned DEFAULT NULL

`appointment_id` int(11) NOT NULL

As with the clients table, this table contains a column referenced by a appointment_status table.

The options are:

0 = available, 1 = not_confirmed, 2 = confirmed, 3 = cancelled

#### schedule

`schedule_id` int(11) NOT NULL

`date` date NOT NULL

`timeOpen` time DEFAULT NULL

`timeClose` time DEFAULT NULL

`status` tinyint(2) unsigned NOT NULL DEFAULT '0'

As with the clients table, this table contains a column referenced by a schedule_status table.

The options are:

0 = open, 1 = closed

#### messages

This table is to store messages for a hypothetical 30 days in order to have client have messages in multiple devices.

`sinch_id` varchar(320) NOT NULL

`sender_id` varchar(320) NOT NULL

`recipient_id` varchar(320) NOT NULL

`messageText` varchar(500) NOT NULL

NOTE: This table is currently being used but this is the general layout of how the messages would be stored.

***

### Triggers

In order to maintain the schedule and appointments table in sync (i.e. for every hour set in the schedule, 4 15 minute appointments should be created) we use triggers to listen to the creation or update of any row in the schedule table.

The two triggers used are set on the schedule table and are:

**create_appts**

Waits for a new row to be created on schedule and calls the iterate_day() procedure.

**change_appts**

Waits for a current row's status to be updated to 1 (closed) and calls the cancel_day() procedure




***

### Procedures

A procedure is what is called when a triggers is activated.

The procedures used are:

**iterate_day(p1 TIME, p2 TIME)**

Iterates through two given hours and created fifteen minute interval rows on the appointments table setting the status to 0 (available)

**cancel_day(appt_day DATE)**

Sets all appointments with date equal to appt_day to status 3 (unavailable)

***

## Things completed

Android app with a login and appointment lookup based on a MySQL table.

Rudimentary messaging system between client (application) and staff (web interface).

MySQL schema with procedures, triggers and tables capable of storing clients, staff members as well as office schedules and available appointments.


***

## Things to be done/ Features to be implemented

- EMR system
- Tweaking Aysnctask classes to send information as a body parameters instead of passing in URL (Will prevent XSS)
- Not storing passwords as text
- Using SSL for all calls made to and from server.
- Have client be able to not only see available appointments but actually schedule an appointment
- Make a distinction between normal user and "premium" user
