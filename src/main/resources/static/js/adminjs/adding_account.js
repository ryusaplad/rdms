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

  if (accountName.value.trim() === "") {
    accountName.classList.add("is-invalid");
    nameAlert.style.display = "block";
    nameAlert.innerText = "Name cannot be empty!";
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
  } else if (title.includes("Student") && !accountUserName.value.trim().toLowerCase().includes("c-")) {
    accountUserName.classList.add("is-invalid");
    usernameAlert.style.display = "block";
    usernameAlert.innerText = "Student Username Invalid!, must be start with (C-/c-)";
    return false;
  } else if (title.includes("Registrar") && !accountUserName.value.trim().toLowerCase().includes("r-")) {
    accountUserName.classList.add("is-invalid");
    usernameAlert.style.display = "block";
    usernameAlert.innerText = "Registrar Username Invalid!, must be start with (R-/r-)";
    return false;
  } else if (title.includes("Teacher") && !accountUserName.value.trim().toLowerCase().includes("t-")) {
    accountUserName.classList.add("is-invalid");
    usernameAlert.style.display = "block";
    usernameAlert.innerText = "Teacher Username Invalid!, must be start with (T-/t-)";
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
  var title = $("#main-title").text();
  var stat = title;
  var userType = "";
  var userId = "";
  if (title != null) {
    if (title.toLowerCase().includes("deleted")) {
      stat = "Temporary";
      title = title
        .replace("Accounts", "")
        .replace("Deleted", "")
        .replace(" ", "");
      ajaxGet(title, stat);
    } else {
      stat = "Active";
      title = title.replace("Accounts", "").replace(" ", "");
      ajaxGet(title, stat);
      userType = title;
    }
  }

  let url = "";

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
      url = "/svfc-admin/saveUserAcc";
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
      url = "/svfc-admin/updateUserAcc";
      ajaxPost(url);
    }
  });

  function ajaxPost(url) {
    // PREPARE FORM DATA
    let formData = {};
    if (url.includes("saveUserAcc")) {
      formData = {
        name: $("#name").val(),
        email: $("#email").val(),
        username: $("#username").val(),
        password: $("#password").val(),
      };
    } else if (url.includes("updateUserAcc")) {
      formData = {
        userId: $("#userIdEdit").val(),
        name: $("#nameEdit").val(),
        email: $("#emailEdit").val(),
        username: $("#usernameEdit").val(),
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
          if (url.includes("save")) {
            $("#resultMessage").html(
              "The account has been successfully inserted."
            );
            $("#registerModal").modal("hide");
          } else if (url.includes("update")) {
            $("#resultMessage").html(
              "The account has been successfully updated."
            );
            $("#updateModal").modal("hide");
          }
          ajaxGet(result.data.type, result.data.status);
        } else {
          $("#resultDiv").fadeOut(1);
          $("#resultDiv").fadeIn(100);

          $("#alertDiv").removeClass("alert-success").addClass("alert-warning");
          $("#resultMessage").html("Insertion/Updating Failed!");
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
        $("#resultDiv").fadeIn(100);

        $("#alertDiv").removeClass("alert-success").addClass("alert-warning");
        $("#resultMessage").html("Insertion/Updating Failed Reason: " + m);

        $("#updateModal").modal("hide");
        $("#registerModal").modal("hide");
      },
    });
  }
  // close alert card
  $(".close").on("click", function () {
    $("#resultDiv").fadeOut(100);
  });

  function ajaxGet(type, stat) {
    $.ajax({
      type: "GET",
      url: "/svfc-admin/getAllUser?account-type=" + type.trim() + "&status=" + stat,
      success: function (result) {
        if (result.status == "success") {
          var table = $("#zero_config").DataTable();
          table.clear();
          $("#tableBody").empty();
          var htmlTable = "";
          $.each(result.data, function (count, user) {
            if (user.status.toLowerCase() == "temporary") {
              htmlTable =
                " <tr><td>" +
                user.name +
                "</td><td>" +
                user.email +
                "</td><td>" +
                user.username +
                "</td><td>" +
                user.status +
                "</td><td>" +
                "<div class='row'>" +
                "<div class='col-sm'>" +
                "<a href='" +
                user.userId +
                "' type='button' class='undo  btn btn-danger w-100'>Undo</a>" +
                "</div> </div>" +
                "</td></tr>";
              actions =
                "<div class='row'>" +
                "<div class='col-sm'>" +
                "<a href='" +
                user.userId +
                "' type='button' class='undo btn btn-danger text-white w-100'>Undo</a>" +
                "</div> </div>";
            } else {
              if (user.type != "Teacher") {
                htmlTable =
                  " <tr><td>" +
                  user.name +
                  "</td><td>" +
                  user.email +
                  "</td><td>" +
                  user.username +
                  "</td><td>" +
                  user.status +
                  "</td><td>" +
                  "<div class='row'>" +
                  "<div class='col-sm'>" +
                  "<a href='/svfc-admin/user/update/?userId=" +
                  user.userId +
                  "' type='button' class='edit btn btn-primary w-100'>Edit</a>" +
                  "</div>" +
                  "<div class='col-sm'>" +
                  "<a href='" +
                  user.userId +
                  "'type='button'class='delete btn btn-danger w-100'>Delete</a>" +
                  "</div></div>" +
                  "</td></tr>";
                actions =
                  "<div class='row'>" +
                  "<div class='col-sm'>" +
                  "<a href='/svfc-admin/user/update/?userId=" +
                  user.userId +
                  "' type='button' class='edit btn btn-primary w-100'>Edit</a>" +
                  "</div>" +
                  "<div class='col-sm'>" +
                  "<a href='" +
                  user.userId +
                  "'type='button'class='delete btn btn-danger w-100'>Delete</a>" +
                  "</div></div>";
              } else {
                htmlTable =
                  " <tr><td>" +
                  user.name +
                  "</td><td>" +
                  user.email +
                  "</td><td>" +
                  user.username +
                  "</td><td>" +
                  user.status +
                  "</td><td>" +
                  "<div class='row'>" +
                  "<div class='col-sm'>" +
                  "<a href='/svfc-admin/user/update/?userId=" +
                  user.userId +
                  "' type='button' class='edit btn btn-primary w-100'>Edit</a>" +
                  "</div>" +
                  "<div class='col-sm'>" +
                  "<a href='" +
                  user.userId +
                  "'type='button'class='delete btn btn-danger w-100'>Delete</a>" +
                  "</div>" +
                  "</td></tr>";
                actions =
                  "<div class='row'>" +
                  "<div class='col-sm'>" +
                  "<a href='/svfc-admin/user/update/?userId=" +
                  user.userId +
                  "' type='button' class='edit btn btn-primary w-100'>Edit</a>" +
                  "</div>" +
                  "<div class='col-sm'>" +
                  "<a href='" +
                  user.userId +
                  "'type='button'class='delete btn btn-danger w-100'>Delete</a>" +
                  "</div> </div>";
              }
            }
            $("#tableBody").append(htmlTable);
            $("#zero_config")
              .DataTable()
              .row.add([
                user.name,
                user.email,
                user.username,
                user.status,
                actions,
              ])
              .draw();
          });
        } else {
          $("#resultDiv").fadeOut(1);
          $("#resultDiv").fadeIn(100);

          $("#alertDiv").removeClass("alert-success").addClass("alert-warning");
          $("#resultMessage").html(type + " Deletion Failed :(");
          $("#deleteModal").modal("hide");
        }
      },
      error: function (e) {
        if (e.responseText != null) {
          e = e.responseText;
        } else {
          e = e.responseJSON.message;
        }

        $("#resultDiv").fadeOut(1);
        $("#resultDiv").fadeIn(100);

        $("#alertDiv").removeClass("alert-success").addClass("alert-warning");
        $("#resultMessage").html("Deletion Failed Reason: " + e);
        $("#deleteModal").modal("hide");
      },
    });
  }
  $(document).on("click", ".undo", function (event) {
    event.preventDefault();
    userId = $(this).attr("href");

    $("#undoModal").modal("toggle");
    $(".undo").attr("data-value", userId);
  });

  $(".undoAccount").on("click", function (event) {
    userId = $(".undo").data("value");
    var undoStatus = "/svfc-admin/user/active/?userId=" + userId;
    $.ajax({
      url: undoStatus,
      type: "GET",
      success: function (status) {
        ajaxGet(userType, stat);
        $("#resultDiv").hide();

        $("#alertDiv").removeClass("alert-warning").addClass("alert-success");
        $("#resultMessage").html("The account has been successfully restored.");
        $("#resultDiv").fadeIn(100);
        $("#undoModal").modal("hide");
      },
      error: function (xhr, ajaxOptions, thrownError) {
        console.log(thrownError);
        $("#resultDiv").hide();
        $("#alertDiv").removeClass("alert-success").addClass("alert-warning");

        $("#resultMessage").html(
          "An error occurred while restoring the account."
        );
        $("#resultDiv").fadeIn(100);
      },
    });
  });
  //updating modal
  $(document).on("click", ".edit", function (event) {
    event.preventDefault();

    var href = $(this).attr("href");
    $.get(href, function (user) {
      $("#userIdEdit").val(user[0].userId);
      $("#nameEdit").val(user[0].name);
      $("#emailEdit").val(user[0].email);
      $("#usernameEdit").val(user[0].username);
      $("#passwordEdit").val(user[0].password);
      $("#confirmPassEdit").val(user[0].password);

      $("#updateModal").modal("toggle");
    });
  });

  //deleting modal
  $(document).on("click", ".delete", function (event) {
    event.preventDefault();
    userId = $(this).attr("href");

    $("#deleteModal").modal("toggle");
  });

  $(".deleteTemporarily").on("click", function (event) {
    var tempStatus = "/svfc-admin/user/temporary/?userId=" + userId;

    $.ajax({
      url: tempStatus,
      type: "GET",
      success: function (status) {
        $("#deleteModal").modal("hide");
        ajaxGet(userType, stat);
        $("#resultDiv").hide();
        $("#resultDiv").fadeIn(100);
        $("#alertDiv").removeClass("alert-warning").addClass("alert-success");
        $("#resultMessage").html("The account has been temporarily removed.");
      },
      error: function (xhr, ajaxOptions, thrownError) {
        console.log(thrownError);
        $("#deleteModal").modal("hide");
        $("#resultDiv").hide();
        $("#alertDiv").removeClass("alert-success").addClass("alert-warning");
        $("#resultMessage").html(
          "An error occurred while temporarily removing the account."
        );
        $("#resultDiv").fadeIn(100);
      },
    });
  });

  $(document).on("click", ".deletePermanently", function (event) {
    var permStatus = "/svfc-admin/user/delete/?userId=" + userId;

    $.ajax({
      url: permStatus,
      type: "GET",
      success: function (status) {
        $("#deleteModal").modal("hide");
        ajaxGet(userType, stat);
        $("#resultDiv").hide();
        $("#alertDiv").removeClass("alert-warning").addClass("alert-success");
        $("#resultMessage").html("The account has been deleted permanently.");
        $("#resultDiv").fadeIn(100);
      },
      error: function (error) {
        $("#deleteModal").modal("hide");
        $("#resultDiv").hide();
        $("#alertDiv").removeClass("alert-success").addClass("alert-warning");
        $("#resultMessage").html(error.responseText);
        $("#resultDiv").fadeIn(100);
      },
    });
  });
  $(".btn-close-alert").on("click", function () {
    $("#resultDiv").hide();
  });
});
