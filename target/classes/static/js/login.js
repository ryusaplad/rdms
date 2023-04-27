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
  } else if (getSelected == "Registrar") {
    document.getElementById("username").placeholder = "Registrar Id";
  } else if (getSelected == "Teacher") {
    document.getElementById("username").placeholder = "Teacher Id";
  } else if (getSelected == "School_Admin") {
    document.getElementById("username").placeholder = "Admin Id";
  }
}
var checkboxValue = false;
$("#rememberMe").change(function () {
  checkboxValue = $(this).is(":checked");
});

$(document).ready(function () {
  $("#username").focus();

  $("html,body").animate(
    {
      scrollTop: $("#username").offset().top,
    },
    1
  );

  function validate() {
    let accountType = document.getElementById("accType");
    let user = document.getElementById("username").value.toLowerCase();
    let pass = document.getElementById("password").value.toLowerCase();
    let getSelected = accountType.options[accountType.selectedIndex].value;
    if (user.length <= 0) {
      displayMessage(userMessage, userError, 1, "Username");
      $(".userInput").addClass("is-invalid");
      return false;
    } else {
      hideMessage(userMessage, userError);
    }
    if (pass.length <= 0) {
      $(".userInput").removeClass("is-invalid");
      $(".passInput").addClass("is-invalid");
      displayMessage(passMessage, passError, 1, "Password");
      return false;
    } else {
      hideMessage(passMessage, passError);
    }
    if (getSelected == "login_as") {
      $(".passInput").removeClass("is-invalid");
      $(".typeInput").addClass("is-invalid");
      displayMessage(accTypeMessage, accTypeError, 1, "Account Type");

      return false;
    } else {
      $(".userInput").removeClass("is-invalid");
      $(".passInput").removeClass("is-invalid");
      $(".typeInput").removeClass("is-invalid");

      $(".typeInput").addClass("is-invalid");
      accTypeMessage.style = "display:none";
      accTypeError.innerText = " ";
      if (getSelected == "Student") {
        if (!user.includes("s-")) {
          displayMessage(userMessage, userError, 2, getSelected);
          return false;
        }
      } else if (getSelected == "Registrar") {
        if (!user.includes("r-")) {
          displayMessage(userMessage, userError, 2, getSelected);

          return false;
        }
      } else if (getSelected == "Teacher") {
        if (!user.includes("t-")) {
          displayMessage(userMessage, userError, 2, getSelected);

          return false;
        }
      } else if (getSelected == "School Admin") {
        if (!user.includes("sad-")) {
          displayMessage(userMessage, userError, 2, getSelected);
          return false;
        }
        return true;
      } else {
        hideMessage(userMessage, userError);
      }
    }
    $(".userInput").removeClass("is-invalid");
    $(".typeInput").removeClass("is-invalid");

    return true;
  }

  function displayMessage(alertMessage, errorMessage, errorType, accountType) {
    if (errorType == 1) {
      alertMessage.style = "display:block";
      errorMessage.innerText = accountType + ", Please fill this field.";

      return false;
    } else if (errorType == 2) {
      if (accountType == "Student") {
        $(".userInput").addClass("is-invalid");
        alertMessage.style = "display:block";
        errorMessage.innerText = `Account Type Invalid (${accountType})`;
      } else if (accountType == "Registrar") {
        $(".userInput").addClass("is-invalid");
        alertMessage.style = "display:block";
        errorMessage.innerText = `Account Type Invalid (${accountType})`;
      } else if (accountType == "Teacher") {
        $(".userInput").addClass("is-invalid");
        alertMessage.style = "display:block";
        errorMessage.innerText = `Account Type Invalid (${accountType})`;
      } else if (accountType == "Administrator") {
        $(".userInput").addClass("is-invalid");
        alertMessage.style = "display:block";
        errorMessage.innerText = `Account Type Invalid (${accountType})`;
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
    var loginMessageAlert = "";
    if (validate() == true) {
      if ($("#rememberMe").is(":checked")) {
        checkboxValue = true;
      } else {
        checkboxValue = false;
      }
      $.ajax({
        url: "/login?rememberMe=" + checkboxValue,
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(formData),
        beforeSend: function () {
          $(".loginBtn").attr("disabled", true);
          $("#loginMessageDiv").empty();
          loginMessageAlert = `
        
  <div class="alert alert-success mt-2" role="alert">
                <strong class="loginMessage"> <i class="fa fa-warning"></i>
                  <div class="spinner-border spinner-border-sm" role="status">
  <span class="visually-hidden">Loading...</span>
          
</div>

                </strong>
                Please Wait...
                </div>
          `;
          $("#loginMessageDiv").append(loginMessageAlert);
        },
        success: function (response) {
          var responseTextVal = "";

          if (response == "success") {
            responseTextVal = "Login SuccessFul!, Redirecting..";
            if (formData.type == "Student") {
              setTimeout(function () {
                window.location = "/student/dashboard";
              }, 1000);
            }

            if (formData.type == "Registrar") {
              setTimeout(function () {
                window.location = "/registrar/dashboard";
              }, 1000);
            }

            if (formData.type == "Teacher") {
              setTimeout(function () {
                window.location = "/teacher/dashboard";
              }, 1000);
            }
            if (formData.type == "School_Admin") {
              setTimeout(function () {
                window.location = "/svfc-admin/dashboard";
              }, 1000);
            }
          } else {
            responseTextVal = response;
          }
          $("#loginMessageDiv").empty();
          loginMessageAlert =
            `
            <div class="alert alert-success text-center mt-2" role="alert">
          <i class="fas fa-check-circle" aria-hidden="true"></i>
                <strong class="loginMessage"> <i class="fa fa-warning"></i>
                ` +
            responseTextVal +
            ` 
                </strong>
                </div>
          `;
          $("#loginMessageDiv").append(loginMessageAlert);
          $(".loginBtn").attr("disabled", false);
        },
        error: function (error) {
          $("#loginMessageDiv").empty();
          loginMessageAlert =
            `

            <div class="alert alert-danger text-center mt-2" role="alert">
      

                <strong class="loginMessage">     
                <i class="fas fa-exclamation-circle" aria-hidden="true"></i>
                ` +
            error.responseText +
            ` 
                </strong>
                </div>
          `;

          $("#loginMessageDiv").append(loginMessageAlert);
          $(".loginBtn").attr("disabled", false);
        },
      });
    } else {
      console.log("Login Failed");
    }
  });
});

