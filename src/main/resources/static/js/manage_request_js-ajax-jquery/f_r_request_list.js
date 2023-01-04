$(document).ready(function () {
  var htmlTable = "";
  var formData = new FormData();
  var fileListArr;
  var finalValue = "";
  var usId = "";
  var rId = "";
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
        $("#manageby").text(request.data[0].manageBy);
        $("#mess").text(request.data[0].message);
      }
    });
    $("#reqDetailModal").modal("toggle");
  });
  $(document).on("click", ".processBtn", function (e) {
    e.preventDefault();
    var userId = $(this).attr("href");
    $("#processHref").attr("href", userId);
    usId = userId;

    $("#confirmModal").modal("toggle");
  });
  $(document).on("click", ".completeBtn", function (e) {
    e.preventDefault();
    var userId = $(this).attr("href");
    $(".confirmCompleteFinal").attr("data-value", userId);
    rId = $(".completeBtn").data("value");
    $("#confirmCompleteModal").modal("toggle");
  });

  $(document).on("click", ".confirmCompleteFinal", function (e) {
    e.preventDefault();

    $("#f-reqs-upload").submit();
  });

  // Finalized Requests with uploaded files

  $("#rfile").change(function () {
    formData = new FormData();
    $(".r-file-table").empty();
    var totalFiles = $("#rfile")[0].files.length;
    for (var x = 0; x < totalFiles; x++) {
      var fileName = $("#rfile")[0].files[x].name;
      var split = fileName.split(".");
      fileName = split[0];
      var extension = split[1];
      if (fileName.length > 10) {
        fileName = fileName.substring(0, 10);
      }
      var name = fileName + "." + extension;
      htmlTable =
        " <tr style='width: 2px; white-space:pre-wrap; word-spacing: 1px;'>" +
        "<td> <a href='#' class='btn btn-danger f-removeItem'  data-index=" +
        x +
        ">Cancel</td>" +
        "<td title='" +
        $("#rfile")[0].files[x].name +
        "'>" +
        name +
        "</td>" +
        "<td>" +
        formatFileSize($("#rfile")[0].files[x].size) +
        "</td>" +
        "</tr>";
      $(".r-file-table").append(htmlTable);
    }
    fileListArr = Array.from($("#rfile")[0].files);
    for (var i = 0; i < fileListArr.length; i++) {
      formData.append("rfile[]", fileListArr[i]);
    }
  });
  $(document).on("click", ".f-removeItem", function (e) {
    var index = $(this).attr("data-index");

    formData.append("excluded" + index, fileListArr[index].name);

    $(this).closest("tr").remove();
    if ($(".r-file-table tr").length == 0) {
      $(".r-file-table").empty();
      formData = new FormData();
    }
  });

  $(".cancelFinalizing").on("click", function (e) {
    formData = new FormData();
    $("#f-reqs-upload").trigger("reset");
    $(".r-file-table").empty();

    $(".f-alert").hide();
    $("#f-alertM").text("");
  });
  function formatFileSize(bytes) {
    var decimalPoint = 0;
    if (bytes == 0) return "0 Bytes";
    var k = 1000,
      dm = decimalPoint || 2,
      sizes = ["Bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"],
      i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + " " + sizes[i];
  }

  $(".file-upload-wrap").bind("dragover", function () {
    $(".file-upload-wrap").addClass("file-dropping");
  });
  $(".file-upload-wrap").bind("dragleave", function () {
    $(".file-upload-wrap").removeClass("file-dropping");
  });
  $("#f-reqs-upload").on("submit", function (e) {
    e.preventDefault();
    for (let [key, val] of formData.entries()) {
      console.log(key, val);
    }
    usId = $(".confirmCompleteFinal").data("value");
    $.ajax({
      url:
        "/registrar/studentreq/finalized/?userId=" + usId + "&requestId=" + rId,
      type: "POST",
      data: formData,
      enctype: "multipart/form-data",
      processData: false,
      contentType: false,
      cache: false,
      beforeSend: function () {
        $(".confirmCompleteFinal").attr("disabled", true);
      },
      success: function (res) {
        formData = new FormData();
        $("#f-reqs-upload").trigger("reset");
        $(".r-file-table").empty();
        $(".confirmCompleteFinal").attr("disabled", false);
        $("#completeModal").modal("hide");
        $("#finalizedCompletedModal").modal("toggle");
        setTimeout(function () {
          $("#finalizedCompletedModal").hide();
        }, 10000);
        $(".f-alert").hide();
        $("#f-alertM").text("");
      },
      error: function (err) {
        $(".f-alert").show();
        $("#f-alertM").text(err.responseText);
        setTimeout(function () {
          $(".f-alert").hide();
          $("#f-alertM").text("");
        }, 10000);
        console.log(err);
        $(".confirmCompleteFinal").attr("disabled", false);
      },
    });
  });

  $(".okCompleted").on("click", function (e) {
    window.location.reload();
  });

  $(document).on("click", ".rejectBtn", function (e) {
    e.preventDefault();
    var userId = $(this).attr("href");
    rId = $(".rejectBtn").data("value");
    $("#rejectHref").attr("href", userId);
    $("#rejectModal").modal("toggle");
  });

  $(document).on("click", ".confirmProcess", function (e) {
    e.preventDefault();
    var userId = $(this).attr("href");
    rId = $(".processBtn").data("value");
    updateStudentRequests(userId, "N/A", "On-Going");
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
      "&requestId=" +
      rId +
      "&reason=" +
      reason;
    $.get(status, function (status) {
      if (status == "Success") {
        $("#rejectModal").modal("hide");
        window.location.reload();
      }
    });
  }

  $(document).on("keyup", "#reason", function (e) {
    var textMaxLength = 250;
    var messageLength = $(this).val().length;
    if (messageLength <= textMaxLength) {
      $("#messageLengthLabel").text("(" + messageLength + "/250) ");
      messageLength = textMaxLength - messageLength;
    } else {
      $(this).val($(this).val().substring(0, 250));
    }
  });
});
