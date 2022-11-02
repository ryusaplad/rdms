// Updating Modal

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
});
