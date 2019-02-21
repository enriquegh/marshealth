// Grabs all values from the page, sends a HTTP POST request, and pops a toast message when successful
function registerUser() {
  var firstName = document.getElementById("first_name").value;
  var lastName = document.getElementById("last_name").value;
  var password = document.getElementById("password").value;
  var emailAddress = document.getElementById("email").value;

  $.post("http://mars.health.com/api.php/clients",
      { name: firstName, l_name: lastName, email: emailAddress, pwd: password, status: 1 },
      function() {
        Materialize.toast('New user registered!', 4000);
      }
  )
}
