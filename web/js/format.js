// Filter for Grabbing Appointments
function getApptFilter() {
    var scheduleStartDate = new Date(document.getElementById("startDate").value);
    scheduleStartDate = convertDate(scheduleStartDate);
    scheduleStartDate = "date,ge," + scheduleStartDate;

    var scheduleEndDate = new Date(document.getElementById("endDate").value);
    scheduleEndDate = convertDate(scheduleEndDate);
    scheduleEndDate = "date,le," + scheduleEndDate;

    document.getElementById("startDate").value = scheduleStartDate;
    document.getElementById("endDate").value = scheduleEndDate;
}

// Filter for setting appointments
function setApptFilter() {
  var date = new Date(document.getElementById("apptDate").value);
  date = convertDate(date);

  var startTime = document.getElementById("startTime").value;
  var endTime = document.getElementById("endTime").value;
  var start = convertTime(startTime);
  var end = convertTime(endTime);

  document.getElementById("apptDate").value = date;
  document.getElementById("startTime").value = start;
  document.getElementById("endTime").value = end;
}

function setSchedFilter() {
  var date = new Date(document.getElementById("scheduleID").value);
  date = convertDate(date);

  var openTime = document.getElementById("openTime").value;
  var closeTime = document.getElementById("closeTime").value;
  var open = convertTime(openTime);
  var close = convertTime(closeTime);

  document.getElementById("scheduleID").value = date;
  document.getElementById("openTime").value = open;
  document.getElementById("closeTime").value = close;
}

function convertTime(time) {
    var timeSplit = time.split(" ");

    if (timeSplit[1] == "PM") {
      timeSplit = timeSplit[0].split(":");
      console.log(timeSplit);
      var fixedTime = parseInt(timeSplit[0]) + 12;
      fixedTime = fixedTime.toString() + ":" + timeSplit[1];
      console.log(fixedTime);
      return fixedTime;
    } else {
      return time;
    }
}

function convertDate(date) {
  date = date.getFullYear() + '-' +
  ('00' + (date.getMonth()+1)).slice(-2) + '-' +
  ('00' + date.getDate()).slice(-2);

  return date;
}

$('.datepicker').pickadate({
  selectMonths: true, // Creates a dropdown to control month
  selectYears: 15 // Creates a dropdown of 15 years to control year
});

$('.timepicker').pickatime();

$(".button-collapse").sideNav();
$(document).ready(function(){
    $('.parallax').parallax();
});
