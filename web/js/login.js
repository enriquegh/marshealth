// Grabs login information, attempts to grab a user with those credentials
// If successful --> redirect to home page
// If unsuccessful --> pop up error message
function validateLogin() {
  var user_name = document.getElementById("username").value;
  var password = document.getElementById("pwd").value;

  var url = "../api.php/staff?"
  url += "filter[]=username,eq," + user_name + "&";
  url += "filter[]=pwd,eq," + password;

  $.getJSON(url, function(data) {

      var profile = data.staff.records[0];

      if (profile == undefined) {
          Materialize.toast('Invalid User Name or Password. Try again.', 4000);
      } else {
          Materialize.toast('Logging in!', 4000);

          setTimeout(function(){
            window.location.href = 'index.html';
          }, 3 * 1000);
      }
  })

}
