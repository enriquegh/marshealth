# medical-mobile-app

## Synopsis

This is a basic test of our Medical Mobile App which for now is called "Test Project".
For the moment it currently has a very simple interface which involves one Activity called "Main Activity".
This Activity contains two EditText Views for Name and Major, one "Spinner" (which basically is a dropdown menu), a Send Button and a TextView wrapped in a ScrollView in order to be able to scroll the information.

##Running App

A few important things to do before making this work:
NOTE: This is implying that you are using a web service on your own computer connected to wi-fi.

1. Download MAMP, WAMP, LAMP, XAMP or any web service of your choice.
2. Download the php-crud-api specifically the "api.php" file and put it in the root of your web directory.
3. Have Android Studio downloaded with API 16 or higher installed.

###Configuring Web service
Make sure that your ports are not set to development ports.
This means your Apache port should be 80 and the SQL port should be 3306.
The SQL port can be changed and still work but I haven't found a way to include the web port and make it work.

###Creating a database
Also included in this repository is a file called medical_app.sql
This file will create a database and table exactly like the one I've been using.
You can restore a database by using phpMyAdmin which comes in MAMP and WAMP.

1. Open phpMyAdmin.
2. Create a database called "medical_app" and select it by clicking the database name in the list on the left of the screen.
3. Click the SQL link. This should bring up a new screen where you can either type in SQL commands, or upload your SQL file.
5. Use the browse button to find the database file.
6. Click Go button. This will upload the backup, execute the SQL commands and re-create your database.


###Modifying api.php
1. Open the api.php and go to the very bottom of the file and add the following code:
  $api = new MySQL_CRUD_API(array(
  'hostname'=>'localhost',
  'username'=>'root',
  'password'=>'root',
  'database'=>'localhost',
  'charset'=>'utf8'
  ));

###Modifying Android app
1. After opening Android project in Android Studio open the MainActivity.java file located in:
app/src/main/java/com/example/egonzalezh94/testproject/MainActivity.java
2. Change the string API_URL to whatever IP address your machine currently has.
3. After changing this, run the app, press search and a JSON list should pop up.


If for some reason something is not working or I didn't explain myself correctly, shoot me a text.



