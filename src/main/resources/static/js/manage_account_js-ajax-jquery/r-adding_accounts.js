function validateAddingForm(
  title,
  nameAlert,
  usernameAlert,
  emailAlert,
  passwordAlert,
  confirmPasswordAlert,
  accountName,
  accountEmail,
  accountUserName,
  accountPassword,
  confirmPassword
) {


  const isValidEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(accountEmail.value.trim());
  var usernameMaxlength = 10;
  var nameMaxLength = 50;
  var passwordMaxLength = 26;
  var maxEmailLength = 50;

  if (accountName.value.trim() === "") {
    accountName.classList.add("is-invalid");
    nameAlert.style.display = "block";
    nameAlert.innerText = "Name cannot be empty!";
    return false;
  } else if (accountName.value.trim().length > nameMaxLength) {
    accountName.classList.add("is-invalid");
    nameAlert.style.display = "block";
    nameAlert.innerText = 'Name should not exceed ' + usernameMaxlength + ' characters.';
    return false;
  } else {
    accountName.classList.remove("is-invalid");
    nameAlert.style.display = "none";
    nameAlert.innerText = "";
  }

  if (!isValidEmail) {
    accountEmail.classList.add("is-invalid");
    emailAlert.style.display = "block";
    emailAlert.innerText = "Invalid email address!";
    return false;
  } else if (accountEmail.value.trim().length > maxEmailLength) {
    accountEmail.classList.add("is-invalid");
    emailAlert.style.display = "block";
    emailAlert.innerText = 'Email should not exceed ' + usernameMaxlength + ' characters.';
    return false;
  } else {
    accountEmail.classList.remove("is-invalid");
    emailAlert.style.display = "none";
    emailAlert.innerText = "";
  }


  if (accountUserName.value.trim() === "") {
    accountUserName.classList.add("is-invalid");
    usernameAlert.style.display = "block";
    usernameAlert.innerText = "Username cannot be empty!";
    return false;
  } else if (accountUserName.value.trim().length > usernameMaxlength) {
    accountUserName.classList.add("is-invalid");
    usernameAlert.style.display = "block";
    usernameAlert.innerText = 'Username should not exceed ' + usernameMaxlength + ' characters.';
    return false;
  } else {
    accountUserName.classList.remove("is-invalid");
    usernameAlert.style.display = "none";
    usernameAlert.innerText = "";
  }



  if (accountPassword.value.trim() === "") {
    accountPassword.classList.add("is-invalid");
    passwordAlert.style.display = "block";
    passwordAlert.innerText = "Password cannot be empty!";
    return false;
  } else if (accountPassword.value.trim().length < 8) {
    accountPassword.classList.add("is-invalid");
    passwordAlert.style.display = "block";
    passwordAlert.innerText = "Password must be at least 8 characters long";
    return false;
  } else if (

    !/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{8,})/.test(
      accountPassword.value
    )
  ) {
    accountPassword.classList.add("is-invalid");
    passwordAlert.style.display = "block";
    passwordAlert.innerText =
      "Password must contain at least 1 lowercase letter, 1 uppercase letter, 1 special character, and 1 number";
    return false;
  } else if (accountPassword.value.trim().length > passwordMaxLength) {
    accountPassword.classList.add("is-invalid");
    passwordAlert.style.display = "block";
    passwordAlert.innerText = 'Name should not exceed ' + usernameMaxlength + ' characters.';
    return false;
  } else {
    accountPassword.classList.remove("is-invalid");
    passwordAlert.style.display = "none";
    passwordAlert.innerText = "";
  }

  if (confirmPassword.value.trim() === "") {
    confirmPassword.classList.add("is-invalid");
    confirmPasswordAlert.style.display = "block";
    confirmPasswordAlert.innerText = "Password cannot be empty!";
    return false;
  } else if (accountPassword.value.trim() !== confirmPassword.value.trim()) {
    confirmPassword.classList.add("is-invalid");
    confirmPasswordAlert.style.display = "block";
    confirmPasswordAlert.innerText = "Passwords must match!";
    return false;
  } else {
    confirmPassword.classList.remove("is-invalid");
    confirmPasswordAlert.style.display = "none";
    confirmPasswordAlert.innerText = "";
  }

  return true;
}



$(document).ready(function () {
  let url = "";
  var userId = "";
  let title = $("#title").text().replace(/\s+/g, "").replace("Accounts", "");
  var userType = title;

  var prefixInitial = "";
  if (title.includes("Student")) {

    prefixInitial = "S-";
  } else if (title.includes("Registrar")) {
    prefixInitial = "R-";
  } else if (title.includes("Teacher")) {
    prefixInitial = "T-";
  }


  ajaxGet(userType);
  $('#username,#usernameEdit').on('keypress', function (event) {
    var keyCode = event.which;
    /* Numbers have keyCodes between 48 and 57*/
    var isValid = (keyCode >= 48 && keyCode <= 57) || (keyCode == 8);
    return isValid;
  });
  $('#name,#nameEdit').on('keypress', function (event) {
    /* Numbers have keyCodes between 48 and 57
     And Letters have keyCodes between 65-90 (A-Z) and 97-122 (a-z), whitespace has keyCode 32*/
    if ((event.keyCode >= 48 && event.keyCode <= 57) ||
      !(event.keyCode >= 65 && event.keyCode <= 90) && !(event.keyCode >= 97 && event.keyCode <= 122) && event.keyCode !== 32) {
      event.preventDefault();
    }
  });


  $("#form-insert").submit(function (event) {
    // Prevent the form from submitting via the browser.
    event.preventDefault();

    let alert1 = document.getElementById("alertMessage1");
    let alert2 = document.getElementById("alertMessage2");
    let alertEmail = document.getElementById("alertMessageEmail");
    let alert3 = document.getElementById("alertMessage3");
    let alert4 = document.getElementById("alertMessage4");
    let accountName = document.getElementById("name");
    let email = document.getElementById("email");
    let accountUserName = document
      .getElementById("username");
    let accountPassword = document.getElementById("password");
    let confirmPassword = document.getElementById("confirmPass");

    if (
      validateAddingForm(
        title,
        alert1,
        alert2,
        alertEmail,
        alert3,
        alert4,
        accountName,
        email,
        accountUserName,
        accountPassword,
        confirmPassword
      )
    ) {
      url = "/registrar/save/user-account";
      ajaxPost(url);
    }
  });

  $("#form-update").submit(function (event) {
    // Prevent the form from submitting via the browser.
    event.preventDefault();
    let alert1 = document.getElementById("alertMessage5");
    let alert2 = document.getElementById("alertMessage6");
    let alertEmail = document.getElementById("alertMessageEmailEdit");
    let alert3 = document.getElementById("alertMessage7");
    let alert4 = document.getElementById("alertMessage8");
    let accountName = document.getElementById("nameEdit");
    let email = document.getElementById("emailEdit");
    let accountUserName = document
      .getElementById("usernameEdit");
    let accountPassword = document.getElementById("passwordEdit");
    let confirmPassword = document.getElementById("confirmPassEdit");

    if (
      validateAddingForm(
        title,
        alert1,
        alert2,
        alertEmail,
        alert3,
        alert4,
        accountName,
        email,
        accountUserName,
        accountPassword,
        confirmPassword
      )
    ) {
      url = "/registrar/save/update-account";
      ajaxPost(url);
    }
  });

  function ajaxPost(url) {
    // PREPARE FORM DATA
    let formData = {};
    if (url.includes("user-account")) {
      formData = {
        name: $("#name").val(),
        email: $("#email").val(),
        username: prefixInitial + $("#username").val().replace(/\D/g, ''),
        password: $("#password").val(),
      };
    } else if (url.includes("update-account")) {
      formData = {
        userId: $("#userIdEdit").val(),
        name: $("#nameEdit").val(),
        email: $("#emailEdit").val(),
        username: prefixInitial + $("#usernameEdit").val().replace(/\D/g, ''),
        password: $("#passwordEdit").val(),
      };
    }

    // DO POST
    $.ajax({
      type: "POST",
      contentType: "application/json",
      url: url,
      data: JSON.stringify(formData),
      dataType: "json",
      success: function (result) {
        if (result.status == "success") {
          $("#resultDiv").fadeOut(100);
          $("#resultDiv").fadeIn(100);

          $("#alertDiv").removeClass("alert-warning").addClass("alert-success");
          if (url.includes("user-account")) {
            $("#resultMessage").html(
              "The account has been successfully inserted."
            );
            $("#registerModal").modal("hide");
          } else if (url.includes("update-account")) {
            $("#resultMessage").html(
              "The account has been successfully updated."
            );
            $("#updateModal").modal("hide");
          }
          ajaxGet(result.data.type);
        } else {
          $("#resultDiv").fadeOut(1);

          $("#alertDiv").removeClass("alert-success").addClass("alert-warning");
          $("#resultMessage").html("Insertion/Updating Failed!");
          $("#resultDiv").fadeIn(100);
        }
      },
      error: function (e) {
        var m = "";
        if (e.responseText != null) {
          m = e.responseText;
        } else {
          m = e.responseJSON.message;
        }
        $("#resultDiv").fadeOut(1);

        $("#alertDiv").removeClass("alert-success").addClass("alert-warning");
        $("#resultMessage").html("Insertion/Updating Failed Reason: " + m);
        $("#resultDiv").fadeIn(100);
        $("#updateModal").modal("hide");
        $("#registerModal").modal("hide");
      },

    });
    $('#resultDiv').get(0).scrollIntoView({ behavior: 'smooth' });
  }
  // close alert card
  $(".close").on("click", function () {
    $("#resultDiv").fadeOut(100);
  });


  $(document).on("click", ".delete", function (event) {
    event.preventDefault();
    userId = $(this).attr("href");

    $("#deleteModal").modal("toggle");
    $(".delete").attr("data-value", userId);
  });
  $(".btn-close-alert").on("click", function () {
    $("#resultDiv").hide();
  });
  $(".deleteTemporarily").on("click", function (event) {
    userId = $(".delete").data("value");
    var tempStatus = "/registrar/user/change/temporary?userId=" + userId;
    $.ajax({
      url: tempStatus,
      type: "GET",
      success: function (status) {
        $("#deleteTempModalConfirm").modal("hide");
        ajaxGet(userType);
        $("#resultDiv").hide();
        $("#resultDiv").fadeIn(100);
        $("#alertDiv").removeClass("alert-warning").addClass("alert-success");
        $("#resultMessage").html("The account has been temporarily removed.");
      },
      error: function (error) {
        if (!error.includes("<!DOCTYPE html>")) {
          $("#deleteTempModalConfirm").modal("hide");
          $("#resultDiv").hide();
          $("#alertDiv").removeClass("alert-success").addClass("alert-warning");
          $("#resultMessage").html(
            error.responseText + ' <a class="link" href="#">Contact</a>'
          );
          $("#resultDiv").fadeIn(100);
        }

      },
    });
    $('#resultDiv').get(0).scrollIntoView({ behavior: 'smooth' });
  });
  $(".deletePermanently").on("click", function (event) {
    userId = $(".delete").data("value");
    var permStatus = "/registrar/user/change/permanently?userId=" + userId;

    $.ajax({
      url: permStatus,
      type: "GET",
      success: function (status) {
        $("#deleteModalConfirm").modal("hide");
        ajaxGet(userType);
        $("#resultDiv").hide();
        $("#alertDiv").removeClass("alert-warning").addClass("alert-success");
        $("#resultMessage").html("The account has been deleted permanently.");
        $("#resultDiv").fadeIn(100);

      },
      error: function (error) {
        console.log(error);
        if (!error.includes("<!DOCTYPE html>")) {
          $("#deleteModalConfirm").modal("hide");
          $("#resultDiv").hide();
          $("#alertDiv").removeClass("alert-success").addClass("alert-warning");
          $("#resultMessage").html(
            error.responseText + ' <a class="link" href="#">Contact</a>'
          );
          $("#resultDiv").fadeIn(100);

        }

      },

    });
    $('#resultDiv').get(0).scrollIntoView({ behavior: 'smooth' });
  });

  $(document).on("click", ".edit", function (event) {
    event.preventDefault();

    var href = $(this).attr("href");
    $.get(href, function (user) {
      $("#userIdEdit").val(user[0].userId);
      $("#nameEdit").val(user[0].name);
      $("#emailEdit").val(user[0].email);
      $("#usernameEdit").val(user[0].username.replace(/\D/g, ''));
      $("#passwordEdit").val(user[0].password);
      $("#confirmPassEdit").val(user[0].password);

      $("#updateModal").modal("toggle");
    });
  });
  $(document).on("click", ".undo", function (event) {
    event.preventDefault();
    var userId = $(this).attr("href");
    $(".undoAccount").attr("data-value", userId);
    $("#undoModal").modal("toggle");
  });
  $(".undoAccount").on("click", function (event) {
    userId = $(".undoAccount").data("value");
    var undoStatus = "/registrar/user/change/active?userId=" + userId;
    $.ajax({
      url: undoStatus,
      type: "GET",
      success: function (status) {
        ajaxGet(userType);
        $("#resultDiv").hide();

        $("#alertDiv").removeClass("alert-warning").addClass("alert-success");
        $("#resultMessage").html("The account has been successfully restored.");
        $("#resultDiv").fadeIn(100);
        $("#undoModalConfirm").modal("hide");
      },
      error: function (error) {
        $("#resultDiv").hide();
        $("#alertDiv").removeClass("alert-success").addClass("alert-warning");

        $("#resultMessage").html(
          error.responseText + ' <a class="link" href="#">Contact</a>'
        );
        $("#resultDiv").fadeIn(100);
      },
    });
  });
});
function ajaxGet(userType) {

  $.ajax({
    type: "GET",
    url: "/registrar/getAllAccounts?account-type=" + userType.trim(),
    success: function (result) {
      if (result.status == "success") {
        var table = $("#zero_config").DataTable({
          "ordering": false,
          "destroy": true,
          columnDefs: [
            { targets: [2], orderable: false }]
        });
        table.clear();
        // Save the current page index and page length
        var currentPageIndex = table.page();
        var currentPageLength = table.page.len();
        console.log(currentPageIndex + "And " + currentPageLength);
        var actions = "";
        $.each(result.data, function (count, user) {
          if (user.status.toLowerCase() == "temporary") {

            actions =
              "<div class='row'>" +
              "<div class='col-sm'>" +
              "<a href='" +
              user.userId +
              "' type='button' class='undo btn btn-warning  text-white w-100'>Undo</a>" +
              "</div> </div>";
          } else {
            if (user.type != "Teacher") {

              actions =
                `
                <div class="btn-group dropstart">
                    <button  type="button" class="btn btn-danger text-white " data-bs-toggle="dropdown"
                        aria-expanded="false">
                        Action
                    </button>
                    <ul class="dropdown-menu">
                      <li>
                            <a href="/registrar/user/update/?userId=` +
                user.userId +
                `
                                "type="button" class="edit dropdown-item link text-primary">Edit</a>
                      </li>
                        <li>
                            <a id="delete" href="` +
                user.userId +
                `" type="button"
                                class="delete dropdown-item  text-danger">Delete</a>
                        </li>
                    </ul>
                </div>`;
            } else {


              actions =
                `
                <div class="btn-group dropstart">
                    <button  type="button" class="btn btn-danger text-white " data-bs-toggle="dropdown"
                        aria-expanded="false">
                        Action
                    </button>
                    <ul class="dropdown-menu">
                      <li>
                            <a href="/registrar/user/update/?userId=` +
                user.userId +
                `
                                "type="button" class="edit  dropdown-item link text-primary">Edit</a>
                      </li>
                       
                         <li ><a id="send_requests"
                               href="/?userId=` +
                user.userId +
                "&sendTo=" +
                user.name +
                `" type="button"
                                class="sendRequests  dropdown-item link text-success">
                                Requests</a></li>
                                 <li>
                            <a id="delete" href="` +
                user.userId +
                `" type="button"
                                class="delete dropdown-item  text-danger">Delete</a>
                        </li>
                    </ul>
                </div>`;
            }
          }

          table.row.add([user.name, user.status, actions])
            .draw();

        });

      } else {
        $("#resultDiv").fadeOut(1);

        $("#alertDiv").removeClass("alert-success").addClass("alert-warning");
        $("#resultMessage").html(userType + " Deletion Failed :(");
        $("#resultDiv").fadeIn(100);
        $("#deleteTempModalConfirm").modal("hide");
      }

    },
    error: function (e) {
      if (e.responseText != null) {
        e = e.responseText;
      } else {
        e = e.responseJSON.message;
      }
      if (!e.includes("<!DOCTYPE html>")) {
        $("#resultDiv").fadeOut(1);
        $("#alertDiv").removeClass("alert-success").addClass("alert-warning");
        $("#resultMessage").html(e + ' <a class="link" href="#">Contact</a>');
        $("#resultDiv").fadeIn(100);
        $("#deleteTempModalConfirm").modal("hide");
      }

    },
  });

  //updating modal
}