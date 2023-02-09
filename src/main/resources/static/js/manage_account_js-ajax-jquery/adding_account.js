function validateAddingForm(
  title,
  alert1,
  alert2,
  alert3,
  alert4,
  accountName,
  accountUserName,
  accountPassword,
  confirmPassword
) {
  if (accountName == "" || accountName.length < 1) {
    alert1.style.display = "block";
    alert1.innerText = "Name cannot be empty!";
    return false;
  } else if (accountUserName == "" || accountName.length < 1) {
    alert1.style.display = "none";
    alert1.innerText = "";

    alert2.style.display = "block";
    alert2.innerText = "Username cannot be empty!";
    return false;
  } else if (title.includes("Student")) {
    if (!accountUserName.includes("c-")) {
      alert2.style.display = "block";
      alert2.innerText = "Student Username Invalid!";
      return false;
    }
  } else if (title.includes("Registrar")) {
    if (!accountUserName.includes("r-")) {
      alert2.style.display = "block";
      alert2.innerText = "Registrar Username Invalid!";
      return false;
    }
  } else if (title.includes("Teacher")) {
    if (!accountUserName.includes("t-")) {
      alert2.style.display = "block";
      alert2.innerText = "Teacher Username Invalid!";
      return false;
    }
  }
  if (accountPassword == "" || accountPassword.length < 1) {
    alert2.style.display = "none";
    alert2.innerText = "";

    alert3.style.display = "block";
    alert3.innerText = "Password cannot be empty!";

    return false;
  }
  if (confirmPassword == "" || confirmPassword.length < 1) {
    alert2.style.display = "none";
    alert2.innerText = "";

    alert3.style.display = "none";
    alert3.innerText = "";

    alert4.style.display = "block";
    alert4.innerText = "Password cannot be empty!";
    return false;
  }
  if (accountPassword != confirmPassword) {
    alert2.style.display = "none";
    alert2.innerText = "";

    alert3.style.display = "block";
    alert3.innerText = "Password must be match";

    alert4.style.display = "block";
    alert4.innerText = "Password must be match!";
    return false;
  }
  if (accountPassword.length < 8 || confirmPassword.length < 8) {
    alert2.style.display = "none";
    alert2.innerText = "";

    alert3.style.display = "block";
    alert3.innerText = "Password must 8 characters";

    alert4.style.display = "block";
    alert4.innerText = "Password must 8 characters";
    return false;
  } else {
    alert1.style.display = "none";
    alert1.innerText = "";

    alert2.style.display = "none";
    alert2.innerText = "";

    alert3.style.display = "none";
    alert3.innerText = "";

    alert4.style.display = "none";
    alert4.innerText = "";
    return true;
  }
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
    let alert3 = document.getElementById("alertMessage3");
    let alert4 = document.getElementById("alertMessage4");
    let accountName = document.getElementById("name").value;
    let accountUserName = document
      .getElementById("username")
      .value.toLowerCase();
    let accountPassword = document.getElementById("password").value;
    let confirmPassword = document.getElementById("confirmPass").value;

    if (
      validateAddingForm(
        title,
        alert1,
        alert2,
        alert3,
        alert4,
        accountName,
        accountUserName,
        accountPassword,
        confirmPassword
      )
    ) {
      url = "/admin/saveUserAcc";
      ajaxPost(url);
    }
  });

  $("#form-update").submit(function (event) {
    // Prevent the form from submitting via the browser.
    event.preventDefault();
    let alert1 = document.getElementById("alertMessage5");
    let alert2 = document.getElementById("alertMessage6");
    let alert3 = document.getElementById("alertMessage7");
    let alert4 = document.getElementById("alertMessage8");
    let accountName = document.getElementById("nameEdit").value;
    let accountUserName = document
      .getElementById("usernameEdit")
      .value.toLowerCase();
    let accountPassword = document.getElementById("passwordEdit").value;
    let confirmPassword = document.getElementById("confirmPassEdit").value;
    if (
      validateAddingForm(
        title,
        alert1,
        alert2,
        alert3,
        alert4,
        accountName,
        accountUserName,
        accountPassword,
        confirmPassword
      )
    ) {
      url = "/admin/updateUserAcc";
      ajaxPost(url);
    }
  });

  function ajaxPost(url) {
    // PREPARE FORM DATA
    let formData = {};
    if (url.includes("saveUserAcc")) {
      formData = {
        name: $("#name").val(),
        username: $("#username").val(),
        password: $("#password").val(),
      };
    } else if (url.includes("updateUserAcc")) {
      formData = {
        userId: $("#userIdEdit").val(),
        name: $("#nameEdit").val(),
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
      url: "/admin/getAllUser?account-type=" + type.trim() + "&status=" + stat,
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
                user.userId +
                "</td><td>" +
                user.name +
                "</td><td>" +
                user.username +
                "</td><td>" +
                "********" +
                "</td><td>" +
                user.type +
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
                  user.username +
                  "</td><td>" +
                  "********" +
                  "</td><td>" +
                  user.type +
                  "</td><td>" +
                  user.status +
                  "</td><td>" +
                  "<div class='row'>" +
                  "<div class='col-sm'>" +
                  "<a href='/admin/user/update/?userId=" +
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
                  "<a href='/admin/user/update/?userId=" +
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
                  user.username +
                  "</td><td>" +
                  "********" +
                  "</td><td>" +
                  user.type +
                  "</td><td>" +
                  user.status +
                  "</td><td>" +
                  "<div class='row'>" +
                  "<div class='col-sm'>" +
                  "<a href='/admin/user/update/?userId=" +
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
                  "<a href='/admin/user/update/?userId=" +
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
                user.userId,
                user.name,
                user.username,
                "********",
                user.type,
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
    var undoStatus = "/admin/user/active/?userId=" + userId;
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
    var tempStatus = "/admin/user/temporary/?userId=" + userId;

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
    var permStatus = "/admin/user/delete/?userId=" + userId;

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
