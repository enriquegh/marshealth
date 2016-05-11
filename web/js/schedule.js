function getSchedule() {
    $(".scheduleTable").remove();

    $.getJSON("../api.php/schedule", function(data) {
        console.log(data);
        var table = "<table class='scheduleTable centered'><thead><tr><th data-field='date'>Date</th><th data-field='openTime'>Open Time</th><th data-field='closeTime'>Close Time</th></tr></thead><tbody>"

        var records = data.schedule.records;
        console.log(records);

        for (var i = 0; i < records.length; i++) {
            console.log(records[i][1]);

            table += "<tr><td>" + records[i][1] + "</td>" +
            "<td>" + records[i][2] + "</td>" +
            "<td>" + records[i][3] + "</td></tr>";
        }

        table += "</tbody></table>";
        $(".schedule").append(table);
    })
}

function setSchedule() {
  var scheduleDate = new Date(document.getElementById("scheduleID").value);
  scheduleDate = convertDate(scheduleDate);

  var openTime = document.getElementById("openTime").value;
  var closeTime = document.getElementById("closeTime").value;
  var open = convertTime(openTime);
  var close = convertTime(closeTime);

  $.post("http://localhost/api.php/schedule",
      { date: scheduleDate, timeOpen: open, timeClose: close },
      function() {
        Materialize.toast('Schedule created!', 4000);
      }
  )
}
