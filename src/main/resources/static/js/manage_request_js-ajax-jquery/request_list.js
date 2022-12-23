$(document).ready(function () {
  var id = "";
  var link = "";
  var finalValue = "";
  $(document).on("click", ".toggleDetailsModal", function (e) {
    e.preventDefault();
    id = $(this).attr("href");

    link = "/student/my-requests/fetch?requestId=" + id;

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
        $("#managebynpar").text(request.data[0].manageBy);
        $("#datereqpar").text(request.data[0].requestDate);
        $("#daterelpar").text(request.data[0].releaseDate);
      }
    });
    $("#detailModal").modal("toggle");
  });
  $(document).on("click", ".toggleEditReqModal", function (e) {
    e.preventDefault();
    id = $(this).attr("href");
    link = "/student/my-requests/fetch?requestId=" + id;
    finalValue = "";
    fetchReqFiles(id, link, finalValue);

    $("#editRequirements").modal("toggle");
    // $.get(link, function (request) {});
  });

  function fetchReqFiles(id, link, finalValue) {
    $.get(link, function (request) {
      $(".edit-file-table").empty();
      var editAnchor = "";
      if (request.status == "success") {
        for (var x = 1; x < request.data.length; x++) {
          var findDot = request.data[x].fname.lastIndexOf(".");
          var newValue = request.data[x].fname.substring(0, findDot);
          var secondValue = request.data[x].fname.substring(findDot);

          if (newValue.length > 15) {
            finalValue = newValue.substring(0, 15) + ".." + secondValue;
          } else {
            finalValue = newValue + secondValue;
          }

          editAnchor =
            "<tr>" +
            "<td>" +
            "<a href = '" +
            request.data[x].fileId +
            "'class='btn btn-danger text-white editItem'>Edit</a>" +
            "</td>" +
            "<td id='fileN'>" +
            finalValue +
            "</td>" +
            "</tr > ";

          $(".edit-file-table").append(editAnchor);
        }
      }
    });
  }

  $(document).on("click", ".editItem", function (e) {
    e.preventDefault();
    var row = $(this).closest("tr");

    var fileId = $(this).closest("a").attr("href");
    var filename = row.find("#fileN").text();
    $(".editItem").addClass("btn-danger");
    $(".editItem").removeClass("disabled");
    $(this).closest("a").addClass("disabled");
    $(this).closest("a").removeClass("btn-danger");
    $(this).closest("a").addClass("btn-success");
    $("#fileId").val(fileId);
    $("#fileName").val(filename);
    $("#inputFile").show();
  });
  $(document).on("click", ".saveChanges", function (e) {
    e.preventDefault();
    var fileData = $("#fileData")[0].files;
    console.log(fileData.length);
    if (fileData.length == 0) {
      $("#editingAlert").empty();

      $("#editingAlert").text(
        "Please select / upload a file in your directory!."
      );
      $("#editingAlert").show();
    } else {
    }
    $("#reqUpForm").submit();
  });
  $(document).on("click", ".cancelUpdateBtn", function (e) {
    $(".clearRequest").click();
  });
  $(document).on("click", ".clearRequest", function (e) {
    $("#inputFile").hide();
    e.preventDefault();
    $(".editItem").removeClass("disabled");
    $(this).closest("a").removeClass("btn-success");
    $(".editItem").addClass("btn-danger");
    $("#editingAlert").empty();
    $("#editingAlert").hide();
  });
  $("#reqUpForm").on("submit", function (e) {
    e.preventDefault();
    $.ajax({
      url: "/student/request/file/update",
      type: "POST",
      data: new FormData(this),
      enctype: "multipart/form-data",
      processData: false,
      contentType: false,
      cache: false,
      beforeSend: function () {
        $(".saveChanges").attr("disabled", true);
        $("#editingAlert").empty();
        $("#editingAlert").removeClass("alert-danger");
        $("#editingAlert").addClass("alert-success");
        $("#editingAlert").text("File Succesfully Updated");
        $("#editingAlert").show();
      },
      success: function (res) {
        $(".clearRequest").click();
        $(".saveChanges").attr("disabled", false);
        $("#editingAlert").empty();
        $("#editingAlert").hide();
        console.log(id);
        console.log(link);
        console.log(finalValue);
        fetchReqFiles(id, link, finalValue);
      },
      error: function (err) {
        $("#editingAlert").empty();
        $("#editingAlert").removeClass("alert-success");
        $("#editingAlert").addClass("alert-danger");
        $("#editingAlert").text(err.responseText);
        $("#editingAlert").show();
        $(".saveChanges").attr("disabled", false);
      },
    });
  });
  $(".resubmitRequests").on("click", function (e) {
    e.preventDefault();
    id = $(this).attr("href");
    // var row = $(this).closest("tr");
    // $(this).closest("tr").remove();
    link = "/student/requests/resubmit?userId=" + id;

    $.get(link, function (request) {
      if ((request = "Success")) {
        alert("oh yeah!");
      }
    });
  });
});
