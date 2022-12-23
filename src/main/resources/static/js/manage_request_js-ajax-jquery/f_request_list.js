$(document).ready(function () {
  $(document).on("click", ".toggleRequestDetail", function (e) {
    e.preventDefault();
    var href = $(this).attr("href");

    var queryString = href.split("?")[1];
    var params = queryString.split("&");
    var paramMap = {};

    for (var i = 0; i < params.length; i++) {
      var param = params[i].split("=");
      var key = param[0];
      var value = param[1];
      paramMap[key] = value;
    }

    var link =
      "/facilitator/studentrequest/fetch?s=" +
      paramMap.s +
      "&req=" +
      paramMap.req;
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
            "<a href = '/student/files/download?id=" +
            request.data[x].fileId +
            "'class='btn btn-danger text-white'>Download</a>" +
            "</td>" +
            "<td>" +
            finalValue +
            "</td>" +
            "</tr > ";

          $(".tablebody").append(dlAnchor);
        }

        $("#requestbyupar").text(request.data[0].requestBy);
        $("#requestbynpar").text(request.data[0].name);
        $("#cysem").text(
          request.data[0].course +
            "," +
            request.data[0].year +
            "," +
            request.data[0].semester.substring(0, 3)
        );

        $("#docreqpar").text(request.data[0].requestDocument);
        $("#reqstatuspar").text(request.data[0].requestStatus);
        $("#datereqpar").text(request.data[0].requestDate);
        $("#mess").text(request.data[0].message);
      }
    });
    $("#reqDetailModal").modal("toggle");
  });
  $(document).on("click", ".processBtn", function (e) {
    e.preventDefault();
    var userId = $(this).attr("href");
    $("#processHref").attr("href", userId);
    $("#confirmModal").modal("toggle");
  });

  $(document).on("click", ".rejectBtn", function (e) {
    e.preventDefault();
    var userId = $(this).attr("href");
    $("#rejectHref").attr("href", userId);
    $("#rejectModal").modal("toggle");
  });

  $(document).on("click", ".confirmProcess", function (e) {
    e.preventDefault();
    var userId = $(this).attr("href");
    updateStudentRequests(userId, "N/A", "Processing");
  });
  $(document).on("click", ".confirmReject", function (e) {
    e.preventDefault();
    var userId = $(this).attr("href");
    var message = $("#reason").val();
    if (message.length < -1) {
      updateStudentRequests(userId, "N/A", "Rejected");
    }
    updateStudentRequests(userId, message, "Rejected");
  });
  function updateStudentRequests(userId, reason, status) {
    var status =
      "/facilitator/studentreq/change/" +
      status +
      "/?userId=" +
      userId +
      "&reason=" +
      reason;
    $.get(status, function (status) {
      console.log(status);
    });
  }

  $(document).on("keyup", "#reason", function (e) {
    var textMaxLength = 250;
    var messageLength = $(this).val().length;
    if (messageLength <= textMaxLength) {
      $("#messageLengthLabel").text("(" + messageLength + "/250) letter Left");
      messageLength = textMaxLength - messageLength;
    } else {
      $(this).val($(this).val().substring(0, 250));
    }
  });
});
