function registerUser() {
  var firstName = document.getElementById("first_name").value;
  var lastName = document.getElementById("last_name").value;
  var password = document.getElementById("password").value;
  var emailAddress = document.getElementById("email").value;

  $.post("../api.php/clients",
      { name: firstName, l_name: lastName, email: emailAddress, pwd: password, status: 1 },
      function() {
        Materialize.toast('New user registered!', 4000);
      }
  )
}
