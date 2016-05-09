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

$(document).ready(function() {
  $('select').material_select();
  $('.parallax').parallax();
});

$('.timepicker').pickatime();
$(".button-collapse").sideNav();
