function addFilter() {
    var scheduleStartDate = new Date(document.getElementById("startDate").value);
    scheduleStartDate = scheduleStartDate.getFullYear() + '-' +
    ('00' + (scheduleStartDate.getMonth()+1)).slice(-2) + '-' +
    ('00' + scheduleStartDate.getDate()).slice(-2);

    scheduleStartDate = "date,ge," + scheduleStartDate;
    console.log(scheduleStartDate);
    document.getElementById("startDate").value = scheduleStartDate;

    var scheduleEndDate = new Date(document.getElementById("endDate").value);
    scheduleEndDate = scheduleEndDate.getFullYear() + '-' +
    ('00' + (scheduleEndDate.getMonth()+1)).slice(-2) + '-' +
    ('00' + scheduleEndDate.getDate()).slice(-2);

    scheduleEndDate = "date,le," + scheduleEndDate;
    document.getElementById("endDate").value = scheduleEndDate;

    return false;
}

function dateFormat() {
  var date = new Date(document.getElementById("scheduleID").value);

  date = date.getFullYear() + '-' +
  ('00' + (date.getMonth()+1)).slice(-2) + '-' +
  ('00' + date.getDate()).slice(-2);

  console.log(date);
  document.getElementById("scheduleID").value = date;
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
