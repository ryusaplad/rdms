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
  accountPassword = accountPassword.replace(/\s/g, "");
  confirmPassword = confirmPassword.replace(/\s/g, "");

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
  let url = "";
  var userId = "";
  let title = $("#title").text().replace(/\s+/g, "").replace("Accounts", "");
  var userType = title;
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
      url = "/registrar/save/user-account";
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
        username: $("#username").val(),
        password: $("#password").val(),
      };
    } else if (url.includes("update-account")) {
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
  }
  // close alert card
  $(".close").on("click", function () {
    $("#resultDiv").fadeOut(100);
  });

  function ajaxGet(userType) {
    $.ajax({
      type: "GET",
      url: "/registrar/getAllAccounts?account-type=" + userType.trim(),
      success: function (result) {
        if (result.status == "success") {
          $("#tableBody").empty();
          var htmlTable = "";
          var table = $("#zero_config").DataTable();
          table.clear();
          var action = "";
          $.each(result.data, function (count, user) {
            if (user.status.toLowerCase() == "temporary") {
              htmlTable =
                "<tr><td style='width: 300px;'>" +
                user.name +
                "</td><td style='width: 300px;'>" +
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
                  "<tr><td style='width: 300px;'>" +
                  user.name +
                  "</td><td style='width: 300px;'>" +
                  user.status +
                  "</td><td>" +
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
                  `"type="button" class="edit   dropdown-item link text-primary">Edit</a>
                        </li>
                          <li>
                              <a id="delete" href="` +
                  user.userId +
                  `" type="button"
                                  class="delete dropdown-item  text-danger">Delete</a>
                          </li>
                      </ul>
                  </div>` +
                  "</td></tr>";
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
                htmlTable =
                  " <tr><td style='width: 300px;'>" +
                  user.name +
                  "</td><td style='width: 300px;'>" +
                  user.status +
                  "</td><td style='width:5%'>" +
                  `
                  <div class="btn-group dropstart">
                      <button  type="button" class="btn btn-danger text-white" data-bs-toggle="dropdown"
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
                                href="userId=` +
                  user.userId +
                  "&to=" +
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
                  </div>` +
                  "</td></tr>";

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
                                 href="userId=` +
                  user.userId +
                  "&to=" +
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
            $("#tableBody").append(htmlTable);

            $("#zero_config")
              .DataTable()
              .row.add([user.name, user.status, actions])
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

        $("#resultDiv").fadeOut(1);
        $("#alertDiv").removeClass("alert-success").addClass("alert-warning");
        $("#resultMessage").html(e + ' <a class="link" href="#">Contact</a>');
        $("#resultDiv").fadeIn(100);
        $("#deleteTempModalConfirm").modal("hide");
      },
    });

    //updating modal
  }
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
        $("#deleteTempModalConfirm").modal("hide");
        $("#resultDiv").hide();
        $("#alertDiv").removeClass("alert-success").addClass("alert-warning");
        $("#resultMessage").html(
          error.responseText + ' <a class="link" href="#">Contact</a>'
        );
        $("#resultDiv").fadeIn(100);
      },
    });
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
        $("#deleteModalConfirm").modal("hide");
        $("#resultDiv").hide();
        $("#alertDiv").removeClass("alert-success").addClass("alert-warning");
        $("#resultMessage").html(
          error.responseText + ' <a class="link" href="#">Contact</a>'
        );
        $("#resultDiv").fadeIn(100);
      },
    });
  });

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
