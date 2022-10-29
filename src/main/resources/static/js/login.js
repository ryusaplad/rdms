let userMessage = document.querySelector(".userMessage");
let passMessage = document.querySelector(".passMessage");
let accTypeMessage = document.querySelector(".accTypeMessage");
let userError = document.querySelector(".userError");
let passError = document.querySelector(".passError");
let accTypeError = document.querySelector(".accTypeError");

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
    displayMessage(accTypeMessage, accTypeError, 1, "Something is not right.");
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
      if (!user.includes("prof-")) {
        displayMessage(userMessage, userError, 2, getSelected);

        return false;
      }
    } else if (getSelected == "Facilitator") {
      if (!user.includes("faci-")) {
        displayMessage(userMessage, userError, 2, getSelected);
        return false;
      }
    } else if (getSelected == "Administrator") {
      if (!user.includes("admin-")) {
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
function usernameChangerBySelection() {
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
