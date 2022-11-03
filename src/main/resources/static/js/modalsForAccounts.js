// Manage modals - For Adding / Updating / Deleting Modal

$(document).ready(function () {
  $("table .edit").on("click", function (event) {
    event.preventDefault();

    var href = $(this).attr("href");

    $.get(href, function (user) {
      $("#userIdEdit").val(user[0].userId);
      $("#nameEdit").val(user[0].name);
      $("#usernameEdit").val(user[0].username);
      $("#passwordEdit").val(user[0].password);
      $("#confirmPassEdit").val(user[0].password);
    });
    $("#updateModal").modal("toggle");
  });

  $(".save").on("click", function (event) {
    var form = document.getElementById("updateFormModal");
    form.submit();
  });

  // Deleting Accounts
  $("table .delete").on("click", function (event) {
    event.preventDefault();
    var userId = $(this).attr("href");
    var link = window.location.href;

    $("#deleteModal").modal("toggle");

    $(".deleteTemporarily").on("click", function (event) {
      var tempStatus = "/user/temporary/?userId=" + userId;
      $.get(tempStatus, function (status) {
        window.location.replace(link);
      });
    });

    $(".deletePermanently").on("click", function (event) {
      var permStatus = "/user/delete/?userId=" + userId;
      $.get(permStatus, function (status) {
        window.location.replace(link);
      });
    });
  });

  // Undoing Accounts
  $("table .undo").on("click", function (event) {
    event.preventDefault();
    var userId = $(this).attr("href");
    var link = window.location.href;

    $("#undoModal").modal("toggle");

    $(".undoAccount").on("click", function (event) {
      var undoStatus = "/user/active/?userId=" + userId;
      $.get(undoStatus, function (status) {
        window.location.replace(link);
      });
    });
  });
});
