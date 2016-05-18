// Converts a 12 hour time to it's respective 24 hour value
//TODO: Bug in converting 12pm
function convertTime(time) {
    var timeSplit = time.split(" ");

    if (timeSplit[1] == "PM") {
      timeSplit = timeSplit[0].split(":");
      var fixedTime = parseInt(timeSplit[0]) + 12;
      fixedTime = fixedTime.toString() + ":" + timeSplit[1];
      return fixedTime;
    } else {
      return timeSplit[0];
    }
}

// Converts Javascript date object into a SQL date object
function convertDate(date) {
  date = date.getFullYear() + '-' +
  ('00' + (date.getMonth()+1)).slice(-2) + '-' +
  ('00' + date.getDate()).slice(-2);

  return date;
}

// Initialization for datepicker
$('.datepicker').pickadate({
  selectMonths: true, // Creates a dropdown to control month
  selectYears: 15 // Creates a dropdown of 15 years to control year
});

// Initialization for parallax on index.html
$(document).ready(function() {
  $('select').material_select();
  $('.parallax').parallax();
});

// Initialization for time picker and side nav bar
$('.timepicker').pickatime();
$(".button-collapse").sideNav();
