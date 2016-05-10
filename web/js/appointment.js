function getAppointment() {
    var scheduleStartDate = new Date(document.getElementById("startDate").value);
    scheduleStartDate = convertDate(scheduleStartDate);
    scheduleStartDate = "date,ge," + scheduleStartDate;

    var scheduleEndDate = new Date(document.getElementById("endDate").value);
    scheduleEndDate = convertDate(scheduleEndDate);
    scheduleEndDate = "date,le," + scheduleEndDate;

    var filter = document.getElementById("dropdown").value;

    var url = "api.php/appointments?";
    url += "filter[]=" + scheduleStartDate + "&";
    url += "filter[]=" + scheduleEndDate + "&";
    url += "filter[]=status,eq," + filter;

    $.getJSON(url, function(data) {
        console.log(data);
        $(".appointmentTable").remove();

        var table = "<table class='appointmentTable centered'><thead><tr><th data-field='date'>Date</th><th data-field='startTime'>Start Time</th><th data-field='endTime'>End Time</th></tr></thead><tbody>"

        var records = data.appointments.records;
        console.log(records);

        for (var i = 0; i < records.length; i++) {
            table += "<tr><td>" + records[i][0] + "</td>" +
            "<td>" + records[i][1] + "</td>" +
            "<td>" + records[i][2] + "</td></tr>";
        }

        table += "</tbody></table>";
        $(".appointments").append(table);
    })
}

function setAppointment() {
  var appointmentDate = new Date(document.getElementById("apptDate").value);
  appointmentDate = convertDate(appointmentDate);

  var startTime = document.getElementById("startTime").value;
  var endTime = document.getElementById("endTime").value;
  var start = convertTime(startTime);
  var end = convertTime(endTime);

  $.post("api.php/appointments",
      { date: appointmentDate, timeStart: start, timeEnd: end },
      function() {
        Materialize.toast('Appointment created!', 4000);
      }
  )
}
