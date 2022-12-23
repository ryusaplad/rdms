let userMessage = document.querySelector(".userMessage");
let passMessage = document.querySelector(".passMessage");
let accTypeMessage = document.querySelector(".accTypeMessage");
let userError = document.querySelector(".userError");
let passError = document.querySelector(".passError");
let accTypeError = document.querySelector(".accTypeError");
function usernameChangerBySelection() {
  let accountType = document.getElementById("accType");
  let getSelected = accountType.options[accountType.selectedIndex].value;
  if (getSelected == "Student") {
    document.getElementById("username").placeholder = "Student Id";
  } else if (getSelected == "Teacher") {
    document.getElementById("username").placeholder = "Teacher Id";
  } else if (getSelected == "Facilitator") {
    document.getElementById("username").placeholder = "Facilitator Id";
  } else if (getSelected == "Administrator") {
    document.getElementById("username").placeholder = "Admin Id";
  }
}
$(document).ready(function () {
  function validate() {
    let accountType = document.getElementById("accType");
    let user = document.getElementById("username").value.toLowerCase();
    let pass = document.getElementById("password").value.toLowerCase();
    let getSelected = accountType.options[accountType.selectedIndex].value;
    if (user.length <= 0) {
      displayMessage(userMessage, userError, 1, "Username");
      return false;
    } else {
      hideMessage(userMessage, userError);
    }
    if (pass.length <= 0) {
      displayMessage(passMessage, passError, 1, "Password");
      return false;
    } else {
      hideMessage(passMessage, passError);
    }
    if (getSelected == "Account Type") {
      displayMessage(
        accTypeMessage,
        accTypeError,
        1,
        "Something is not right."
      );
      console.log("lol");
      return false;
    } else {
      accTypeMessage.style = "display:none";
      accTypeError.innerText = " ";
      if (getSelected == "Student") {
        if (!user.includes("c-")) {
          displayMessage(userMessage, userError, 2, getSelected);
          return false;
        }
      } else if (getSelected == "Teacher") {
        if (!user.includes("t-")) {
          displayMessage(userMessage, userError, 2, getSelected);

          return false;
        }
      } else if (getSelected == "Facilitator") {
        if (!user.includes("f-")) {
          displayMessage(userMessage, userError, 2, getSelected);
          return false;
        }
      } else if (getSelected == "Administrator") {
        if (!user.includes("sad-")) {
          displayMessage(userMessage, userError, 2, getSelected);
          return false;
        }
        return true;
      } else {
        hideMessage(userMessage, userError);
      }
    }

    return true;
  }

  function displayMessage(alertMessage, errorMessage, errorType, accountType) {
    if (errorType == 1) {
      alertMessage.style = "display:block";
      errorMessage.innerText = accountType + ", Please fill this field.";

      return false;
    } else if (errorType == 2) {
      if (accountType == "Student") {
        alertMessage.style = "display:block";
        errorMessage.innerText = accountType + " Account Type Invalid";
      } else if (accountType == "Teacher") {
        alertMessage.style = "display:block";
        errorMessage.innerText = accountType + " Account Type Invalid";
      } else if (accountType == "Facilitator") {
        alertMessage.style = "display:block";
        errorMessage.innerText = accountType + " Account Type Invalid";
      } else if (accountType == "Administrator") {
        alertMessage.style = "display:block";
        errorMessage.innerText = accountType + " Account Type Invalid";
      }
    }
  }
  function hideMessage(alertMessage, errorMessage) {
    alertMessage.style = "display:none";
    errorMessage.innerText = " ";
  }

  $("#loginForm").submit(function (e) {
    e.preventDefault();

    var inputValues = {};
    var selectedType = $("#accType").val();
    $("input").each(function () {
      inputValues[$(this).attr("name")] = $(this).val();
    });

    inputValues = Object.values(inputValues);
    // Filter out any undefined   values

    inputValues = inputValues.filter(function (index, value) {
      return value !== undefined;
    });
    inputValues[2] = selectedType;
    var formData = {
      username: inputValues[0],
      password: inputValues[1],
      type: inputValues[2],
    };

    if (validate() == true) {
      $.ajax({
        url: "/login",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(formData),
        beforeSend: function () {
          $(".loginBtn").attr("disabled", true);
        },
        success: function (response) {
          var responseTextVal = "";
          console.log(response);
          if (response == "success") {
            responseTextVal = "Login SuccessFul!..please wait";
            console.log(formData.type);
            if (formData.type == "Student") {
              setTimeout(function () {
                window.location = "/student/dashboard";
              }, 1000);
            }
             if (formData.type == "Facilitator") {
               setTimeout(function () {
                 window.location = "/facilitator/dashboard";
               }, 1000);
             }
          } else {
            responseTextVal = response;
          }
          $("#loginMessageDiv").empty();
          var loginMessageAlert =
            '<div class="alert alert-success mt-2" role="alert">' +
            '<strong class="loginMessage">' +
            responseTextVal +
            "</strong>" +
            "</div>";
          $("#loginMessageDiv").append(loginMessageAlert);
          $(".loginBtn").attr("disabled", false);
        },
        error: function (error) {
          console.log(error);
          $("#loginMessageDiv").empty();
          var loginMessageAlert =
            '<div class="alert alert-danger mt-2" role="alert">' +
            '<strong class="loginMessage"> <i class="fa fa-warning"></i> ' +
            error.responseText +
            "</strong>" +
            "</div>";
          $("#loginMessageDiv").append(loginMessageAlert);
          $(".loginBtn").attr("disabled", false);
        },
      });
    } else {
      console.log("Login Failed");
    }
  });
});
