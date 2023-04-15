$(document).ready(function () {
  var formData = new FormData();
  var fileListArr;
  $("#files").change(function () {
    formData = new FormData();
    $("#fileInfoTable").empty();
    var totalFiles = $("#files")[0].files.length;
    var maxSize = 10 * 1024 * 1024; // 10MB in bytes
    for (var x = 0; x < totalFiles; x++) {
      var fileSize = $("#files")[0].files[x].size;
      if (fileSize > maxSize) {
        var message = "";
        message = "Please select a valid file size that does not exceed the maximum limit of 10MB.";
        $(".message").text(message);
        $(".errorMessageAlert").show();
        formData = new FormData();
        $("#fileInfoTable").empty();
        return false;
      } else {
        var fileName = $("#files")[0].files[x].name;
        var split = fileName.split(".");
        fileName = split[0];
        var extension = split[1];
        if (fileName.length > 10) {
          fileName = fileName.substring(0, 10);
        }
        var name = fileName + "." + extension;
        $("#fileInfoTable").append(
          "'<tr>'" +
          "<td>" +
          "<button type = 'button' class='btn removeItemFile btn-outline-danger' data-index=" +
          x +
          ">Delete</button>" +
          "</td>" +
          "<td>" +
          name +
          "</td >" +
          "<td>" +
          formatFileSize($("#files")[0].files[x].size) +
          "</td >" +
          "</tr >"
        );
      }

    }


    fileListArr = Array.from($("#files")[0].files);
    for (var i = 0; i < fileListArr.length; i++) {
      formData.append("file[]", fileListArr[i]);
    }
    $(".errorMessageAlert").hide();
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


  $(document).on("click", ".removeItemFile", function () {
    // Get the other input values

    // Create a new FormData object

    // Add the input values and files to the form data

    var index = $(this).attr("data-index");

    formData.append("excluded" + index, fileListArr[index].name);

    $(this).closest("tr").remove();
    if ($("#fileInfoTable tr").length == 0) {
      $("#fileInfoTable").empty();
      formData = new FormData();
    }
  });

  $("#message").on("keyup", function (e) {
    var textMaxLength = 1000;
    var messageLength = $(this).val().length;
    if (messageLength <= textMaxLength) {
      $("#messageLengthLabel").text("(" + messageLength + "/1000) ");
      messageLength = textMaxLength - messageLength;
    } else {
      $(this).val($(this).val().substring(0, 1000));
    }
  });

  $(".finalizedBtn").on("click", function (event) {
    event.preventDefault();
    var file = $("#files").val();
    var userMessage = $("#message").val();
    if (file === "") {
      message = "Please Select File";
      $(".message").text(message);
      $(".errorMessageAlert").show();
    } else if (userMessage.length > 1000) {
      message = "Invalid Message length, must be 1000 letters below.";
      $(".servererrorMessageAlert").text(message);
      $(".servererrorMessageAlert").show();
    } else {
      $(".servererrorMessageAlert").hide();
      $(".servererrorMessageAlert").empty();
      if ($(".finalizedBtn").text() == "Edit") {
        $("#savebtn").removeClass("saveRequest");
        $("#savebtn").attr("disabled", true);
        $(".finalizedBtn").text("Finalized");
        $("#year").attr("disabled", false);
        $("#course").attr("disabled", false);
        $("#semester").attr("disabled", false);
        $("#message").attr("disabled", false);
        $(".addRequirementFile").removeClass("disabled");
        $(".finalizedBtn").removeClass("btn-primary");
        $(".finalizedBtn").addClass("btn-danger");
        $(".clearRequest").attr("disabled", false);
        $(".removeItemFile").attr("disabled", false);
      } else {
        $("#savebtn").addClass("saveRequest");
        $("#savebtn").attr("disabled", false);
        $(".addRequirementFile").addClass("disabled");
        $(".finalizedBtn").text("Edit");
        $(".finalizedBtn").removeClass("btn-danger");
        $(".finalizedBtn").addClass("btn-primary");
        $("#regForm :input").prop("disabled", true);
        $("#year").attr("disabled", true);
        $("#course").attr("disabled", true);
        $("#semester").attr("disabled", true);
        $("#message").attr("disabled", true);
        $(".clearRequest").attr("disabled", true);
        $(".removeItemFile").attr("disabled", true);
      }
    }
  });
  $(".clearRequest").on("click", function () {
    $("#fileInfoTable").empty();

    formData = new FormData();
    $(".message").text("");
    $(".errorMessageAlert").hide();

    $("#year").attr("disabled", false);
    $("#course").attr("disabled", false);
    $("#semester").attr("disabled", false);
  });


  $(document).on("click", ".saveRequest", function (event) {
    event.preventDefault();
    var message = "";
    var id = $("#documentTypeTitle").text();
    var studId = $("#studentId").val();
    var studName = $("#studName").val();

    var file = $("#files").val();

    if (id === "") {
      message = "invalid request";
      console.log(message);
      window.location = "/student/request/documents";
    } else if (studId === "" || studName === "") {
      message = "Invalid Student Informatation(id,name)";
      window.location = "/";
      console.log(message);
    } else if (file === "") {
      message = "Please Select File";
      $(".message").text(message);
      $(".errorMessageAlert").show();
    } else {
      $("#confirmModal").modal("toggle");
      $("#confirmModal").modal({
        keyboard: false,
      });
    }
  });

  $(".confirmRequestModal").on("click", function () {


    $("#reqForm").submit();
  });

  $("#reqForm").submit(function (event) {
    event.preventDefault();

    if ($("#files").val() === "") {
      alert("please select file");
    } else if ($("#files")[0].files.size) {

    } else {
      var id = $("#documentTypeTitle").text();
      var studId = $("#studentId").val();
      var studName = $("#studName").val();
      var studYear = $("#year").val();
      var studCourse = $("#course").val();
      var studSemester = $("#semester").val();
      var userMessage = $("#message").val();
      if (studCourse == undefined) {
        studCourse = "N/A";
      }
      if (studSemester == undefined) {
        studSemester = "N/A";
      }

      formData.append("studentId", studId);
      formData.append("studName", studName);
      formData.append("year", studYear);
      formData.append("course", studCourse);
      formData.append("semester", studSemester);
      formData.append("message", userMessage);

     

    }
    $.ajax({
      url: "/student/request/" + id + "/sent",
      type: "POST",
      data: formData,
      enctype: "multipart/form-data",
      processData: false,
      contentType: false,
      cache: false,
      beforeSend: function () {
        $(".confirmRequestModal").attr("disabled", true);
        console.log("Please Wait");
      },
      success: function (res) {
        // Send the data via WebSocket after
        console.log("Request successfully submitted");
        $("#confirmModal").modal("hide");
        $("#successModal").modal("toggle");
        window.location = "/student/my-requests";
      },
      error: function (err) {
        $(".confirmRequestModal").attr("disabled", false);
        $("#confirmModal").modal("hide");
        $(".message").text(err.responseText);
        $(".errorMessageAlert").show();
      },
      complete: function () {
        $(".confirmRequestModal").attr("disabled", false);
        console.log("Request complete");
      },
    });
  });


});
