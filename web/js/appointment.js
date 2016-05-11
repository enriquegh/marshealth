function getAppointment() {
    var scheduleStartDate = new Date(document.getElementById("startDate").value);
    scheduleStartDate = convertDate(scheduleStartDate);
    scheduleStartDate = "date,ge," + scheduleStartDate;

    var scheduleEndDate = new Date(document.getElementById("endDate").value);
    scheduleEndDate = convertDate(scheduleEndDate);
    scheduleEndDate = "date,le," + scheduleEndDate;

    var filter = document.getElementById("dropdown").value;

    var url = "../api.php/appointments?";
    url += "filter[]=" + scheduleStartDate + "&";
    url += "filter[]=" + scheduleEndDate + "&";
    url += "filter[]=status,eq," + filter;

    $.getJSON(url, function(data) {
        $(".appointmentTable").remove();

        var table = "<table class='appointmentTable centered'><thead><tr><th data-field='date'>Date</th><th data-field='startTime'>Start Time</th><th data-field='endTime'>End Time</th><th data-field='patient'>Patient</th></tr></thead><tbody>"


        var records = data.appointments.records;
        console.log(records);

        for (var i = 0; i < records.length; i++) {
            table += "<tr><td>" + records[i][0] + "</td>" +
            "<td>" + records[i][1] + "</td>" +
            "<td>" + records[i][2] + "</td>";

            if (records[i][4] != undefined) {
                var patientURL = "../api.php/clients?filter=client_id,eq," + records[i][4];
                name;

                $.getJSON(patientURL, function(patientData) {
                    console.log(patientData);
                    name = patientData.clients.records[0][1] + " " + patientData.clients.records[0][2];
                    //console.log(table);
                })
                console.log(table);

                table += "<td>" + name + "</td>";
            } else {
                table += "<td>N/A</td>";
            }

            table += "</tr>";
        }

        table += "</tbody></table>";
        $(".appointments").append(table);
    })
}

function scheduleAppointment() {
  var firstName = document.getElementById("first_name").value;
  var lastName = document.getElementById("last_name").value;

  var url = "../api.php/clients?"
  url += "filter[]=name,eq," + firstName + "&";
  url += "filter[]=l_name,eq," + lastName;

  // Check if patient name is valid
  $.getJSON(url, function(data) {
      console.log(data);

      var patient = data.clients.records[0];

      if (patient == undefined) {
          Materialize.toast('Invalid patient. Please try again.', 4000);
          Materialize.toast('(Check spelling and make sure first and last name is capitalized! e.g. John Doe)', 4000);
      } else {

          var appointmentDate = new Date(document.getElementById("apptDate").value);
          appointmentDate = convertDate(appointmentDate);

          var startTime = document.getElementById("startTime").value;
          var endTime = document.getElementById("endTime").value;
          var start = convertTime(startTime);
          var end = convertTime(endTime);

          var timeUrl = "../api.php/appointments?"
          timeUrl += "filter[]=date,eq," + appointmentDate + "&";
          timeUrl += "filter[]=timeStart,ge," + start + "&";
          timeUrl += "filter[]=timeEnd,le," + end;

          console.log(appointmentDate, start, end);

          $.getJSON(timeUrl, function(timeData) {
              console.log(timeData);

              var timeSlots = timeData.appointments.records;
              var appts =[];

              for(var i = 0; i < timeSlots.length; i++) {
                  appts.push(timeSlots[i][5]);
              }

              console.log(appts);

              for(var t = 0; t < appts.length; t++) {
                  var apptURL = "../api.php/appointments/" + appts[t]

                  $.ajax({
                      url: apptURL,
                      method: 'PUT',
                      data: {patient_id: patient[0], status: 2}
                  })
              }

              Materialize.toast('Appointment scheduled!', 4000);

          })
      }

  })
}
