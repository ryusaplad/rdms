$(document).ready(function () {
  $(document).on("click", ".toggleDetailsModal", function (e) {
    e.preventDefault();
    var id = $(this).attr("href");

    var link = "/student/my-requests/fetch?requestId=" + id;
    var finalValue = "";
    $.get(link, function (request) {
      $(".tablebody").empty();
      var dlAnchor = "";
      if (request.status == "success") {
        for (var x = 1; x < request.data.length; x++) {
          var findDot = request.data[x].fname.indexOf(".");
          var newValue = request.data[x].fname.substring(0, findDot);
          var secondValue = request.data[x].fname.substring(
            findDot,
            request.data[x].fname.length
          );
          if (newValue.length > 15) {
            finalValue = newValue.substring(0, 15) + ".." + secondValue;
          } else {
            finalValue = newValue + secondValue;
          }

          dlAnchor =
            "<tr>" +
            "<td>" +
            "<a href = '/admin/my-request/download?id=" +
            request.data[x].fileId +
            "'class='btn btn-danger text-white'>Download</a>" +
            "</td>" +
            "<td>" +
            finalValue +
            "</td>" +
            "</tr > ";

          $(".tablebody").append(dlAnchor);
        }

        $("#requestidpar").text("Request Id: " + request.data[0].requestId);
        $("#requestbyupar").text("Student ID: " + request.data[0].requestBy);
        $("#requestbynpar").text("Name: " + request.data[0].name);
        $("#yearpar").text("Year: " + request.data[0].year);
        $("#coursepar").text("Course: " + request.data[0].course);
        $("#sempar").text("Semester: " + request.data[0].semester);
        $("#docreqpar").text(
          "Requested Document: " + request.data[0].requestDocument
        );
        $("#reqstatuspar").text(
          "Request Status: " + request.data[0].requestStatus
        );
        $("#managebynpar").text("Manage By: " + request.data[0].manageBy);
        $("#datereqpar").text("Request Date: " + request.data[0].requestDate);
        $("#daterelpar").text("Release Date: " + request.data[0].releaseDate);
      }
    });
    $("#detailModal").modal("toggle");
  });
});
