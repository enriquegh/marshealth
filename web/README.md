# Web Application

Just going to talk about some of the functionality and specifications of the web application portion of the MARS Health Medical App. This web application serves as the Administration Console for the MARS system, and is primarily intended for use by the physician or staff of the clinic. There are 4 main functions of the web application, and they are separated into different web pages. We will take a more in depth look at each section, discuss their functionality, and how things were implemented.

Before we get to those, just some specifications about the web application itself. It was built using HTML, CSS, and Javascript. The CSS is using the MaterializeCSS library, which is a front-end framework based on the Material Design. In terms of Javascript, we are using jQuery for Ajax calls and parameter passing, as well as pickadate.js for the time and date pickers. This is currently online at [mars.cs.usfca.edu](http://mars.cs.usfca.edu/).

## Scheduling (scheduling.html, schedule.js)

Scheduling is implemented in scheduling.html and schedule.js. Scheduling is intended to allow the physican/office staff to create a schedule in which the office is open. Here the user can either get the current work schedule that is created, or add more dates to the current schedule. More documentation can be found in the comments of schedule.js.

## Appointments (appointment.html, appointment.js)

Similar to Scheduling, Appointments allow the staff/physicians to either create an appointment for a patient, or to see any appointments that are within a certain time frame that meet a certain criteria (Available slots, Confirmed slots, etc.). These are implemented in appointment.html and appointment.js. More documentation can be found in the comments of appointment.js.

## Messaging (message.html, IMsample.js)

Messaging is the area for patients and doctors to be able to communicate. On the phone app side, this looks like a traditional texting interface, whereas the web application side is similar to that of an instant messaging interface. The messaging is implemented using a Messaging API called [Sinch](https://www.sinch.com/). The current messaging system on the web application side is using the IMsample.js, which is a modified Javascript file from the SinchIMsample folder. In IMsample.js, the messaging client is created using our API key, and allows for a connection to the Sinch servers for messaging.

## Create User (createUser.html, registration.js)

User/patient creation is the primary function of this area, and once the form is filled out, a post HTTP request is sent to the SQL server to create a new patient. More documentation can be found in registration.js.

## Other/TODO's

There are things that are within this repository that were not completely implemented. Particularly, we have created a login page (login.html, login.js) that checks the credentials of a doctor, and once validated, it will redirect to the MARS health home page. Currently this is working, however, there needs to be a way for the login to remain once a doctor has logged in. Looking into this a bit, we discovered that something had to be modified in the cookies of the doctor's web browser.

Also, there is currently a bug in the convertTime() function in format.js. So it may be worth taking another look at this function.

Other than that, there are some MARS Health logos located under the img folder.
