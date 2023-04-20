$(document).ready(function () {
  //Web Socket Connect;
  studRequestConnection();
  var id = "";
  var rid = "";
  var link = "";
  var finalValue = "";

  refreshTable();

  function refreshTable() {
    // Make AJAX request to fetch latest data
    $.ajax({
      url: "/student/my-requests/load_all",
      type: "GET",
      dataType: "json",
      success: function (data) {
        // Empty table body
        var table = $("#zero_config").DataTable({
          "ordering": false,
          "destroy": true
        });
        table.clear();
       
        var buttons = ``;

        for (let i = 0; i < data.length; i++) {
          var myrequest = data[i];

          if (myrequest.requestStatus == "Pending" || myrequest.requestStatus == "On-Going" || myrequest.requestStatus == "Approved") {
            buttons = ` <a
           
            title="View Full Details"
            href="${myrequest.requestId}"
            type="button"
            class="btn btn-success text-white toggleDetailsModal"
        >
            View Details
        </a>`;
          } else {
            buttons = `
<button
id="btnDropDown"
type="button"
class="btn btn-danger text-white"
data-bs-toggle="dropdown"

aria-expanded="false"
>
Edit|<b>Re</b>submit
</button> 
<ul class="dropdown-menu">
<li>
   <a
       data-toggle="tooltip"
       data-placement="top"
       title="View Full Details"
       href="${myrequest.requestId}"
       type="button"
       class=" dropdown-item link toggleDetailsModal"
   >
       Details
   </a>
</li>
`;

            if (myrequest.requestStatus == "Rejected") {
              buttons += `  <li>
                                               <a
                                                   data-toggle="tooltip"
                                                   data-placement="top"
                                                   title="Edit Files"
                                                  href="${myrequest.requestId}"
                                                   type="button"
                                                   class="dropdown-item link text-primary toggleEditReqModal"
                                               >
                                                   Edit Files
                                               </a>
                                           </li>`;
            }
            if (myrequest.requestStatus == "Rejected") {
              buttons += `   <li>
                                               <a
                                                   data-toggle="tooltip"
                                                   data-placement="top"
                                                   title="Edit Informations"
                                                  href="${myrequest.requestId}"
                                                   type="button"
                                                   class="dropdown-item link text-primary toggleEditInfosModal"
                                               >
                                                   Edit Informations
                                               </a>
                                           </li>`;
            }
            if (myrequest.requestStatus == "Rejected") {
              buttons += `<li>
       <a
       data-toggle="tooltip"
       data-placement="top"
       title="Re Submit"
      href="${myrequest.studentId}"
       data-value="${myrequest.requestId}"
       type="button"
       class="dropdown-item link text-success resubmitRequests">Submit</a></li>`;
            }
          }


          var statusIcon = "";
          if (myrequest.requestStatus.toLowerCase().includes("approved")) {
            statusIcon = ` <td> <strong class="btn btn-outline-success" > <i class="fas fa-check text-success" aria-hidden="true"></i> Completed</strong></td>`;
          } else if (myrequest.requestStatus.toLowerCase().includes("pending")) {
            statusIcon = ` <td> <strong class="btn btn-outline-primary" > <i class="fa fa-hourglass-start text-primary" aria-hidden="true"></i> ${myrequest.requestStatus}</strong></td>`;
          } else if (myrequest.requestStatus.toLowerCase().includes("on-going")) {
            statusIcon = ` <td> <strong class="btn btn-outline-primary" > <i class="fa fa-hourglass-half text-primary" aria-hidden="true"></i> ${myrequest.requestStatus}</strong></td>`;
          } else if (myrequest.requestStatus.toLowerCase().includes("rejected")) {
            statusIcon = ` <td> <strong class="btn btn-outline-danger" > <i class="fas fa-times text-danger" aria-hidden="true"></i> ${myrequest.requestStatus}</strong></td>`;
          }

         


          $("#zero_config")
            .DataTable()
            .row.add([
              myrequest.requestDocument,
              statusIcon,
              buttons,
            ])
            .draw();
        }
      },
      error: function (jqXHR, textStatus, errorThrown) {
        console.error("Error refreshing table: ", errorThrown);
      },
    });
  }

  $(document).on("click", ".toggleDetailsModal", function (e) {
    e.preventDefault();
    id = $(this).attr("href");
    rid = $(this).attr("href");
    link = "/student/my-requests/fetch?requestId=" + id;


    $.get(link, function (request) {
      $(".tablebody").empty();
      $(".receievedDoc").empty();
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

          if (request.data[x].status == "Pending") {
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
              "<td>" +
              request.data[x].uploaderName +
              "</td>" +
              "</tr > ";
            $(".tablebody").append(dlAnchor);
          } else if (request.data[x].status == "Approved") {
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
              "<td>" +
              request.data[x].uploaderName +
              "</td>" +
              "</tr > ";
            $(".receievedDocumentTable").show();
            $(".receievedDoc").append(dlAnchor);
          }
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
        $("#managebynpar").text(request.data[0].manageBy);
        $("#datereqpar").text(request.data[0].requestDate);
        $("#daterelpar").text(request.data[0].releaseDate);
        var status = request.data[0].requestStatus;
        var message =
          request.data[0].requestStatus + ", " + request.data[0].reply;
        var alertType = "";

        if (status.toLowerCase() == "approved") {
          alertType = "alert-success";
          faIcon = "fa fa-check-circle";
          message = request.data[0].requestStatus;
        } else if (status.toLowerCase() == "pending") {
          alertType = "alert-info";
          faIcon = "fa fa-clock-o";
          message = request.data[0].requestStatus;
        } else if (status.toLowerCase() == "on-going") {
          alertType = "alert-info";
          faIcon = "fa fa-spinner fa-spin";
          message = request.data[0].requestStatus;
        } else {
          alertType = "alert-danger";
          faIcon = "fa fa-exclamation-triangle";
        }
        var htmlDiv =
          "  <div class='alert " +
          alertType +
          " d-flex align-items-center' role='alert'>" +
          "<i class='" +
          faIcon +
          " fa-fw'></i>" +
          "Requests " +
          message +
          "</div>";
        $(".detail-alert").empty();
        $(".detail-alert").append(htmlDiv);
        $(".detail-alert").show();
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
    rid = id;

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
  $(document).on("click", ".toggleEditInfosModal", function (e) {
    e.preventDefault();
    id = $(this).attr("href");

    rid = $(this).data("value");

    $("#editReqInfos").modal("toggle");
  });
  $(".saveInfoUpdate").on("click", function (e) {
    e.preventDefault();
    $("#reqInfoUpForm").submit();
  });
  // update infos form
  $("#reqInfoUpForm").on("submit", function (e) {
    e.preventDefault();
    $.ajax({
      url: "/student/request/info/update?requestId=" + id,
      type: "POST",
      data: new FormData(this),
      enctype: "multipart/form-data",
      processData: false,
      contentType: false,
      cache: false,
      beforeSend: function () {
        $(".saveInfoUpdate").attr("disabled", true);
      },
      success: function (res) {
        $(".clearRequest").click();
        $(".saveInfoUpdate").attr("disabled", false);
        $("#editingInfoAlert").empty();
        $("#editingInfoAlert").removeClass("alert-danger");
        $("#editingInfoAlert").addClass("alert-success");
        $("#editingInfoAlert").text("Information Succesfully Updated");
        $("#editingInfoAlert").show();
        $("#reqInfoUpForm ").trigger("reset");
        $("#messageLengthLabel").text(
          "You can add message here with maximum of (1000 letters)"
        );
        setTimeout(function () {
          $("#editingInfoAlert").empty();
          $("#editingInfoAlert").hide();
          refreshTable();
        }, 2000);
      },
      error: function (err) {
        $("#editingInfoAlert").empty();
        $("#editingInfoAlert").removeClass("alert-success");
        $("#editingInfoAlert").addClass("alert-danger");
        $("#editingInfoAlert").text(err.responseText);
        $("#editingInfoAlert").show();
        $(".saveInfoUpdate").attr("disabled", false);
      },
    });
  });

  $(document).on("click", ".editItem", function (e) {
    e.preventDefault();
    var fileId = $(this).closest("a").attr("href");
    $(".editItem").addClass("btn-danger");
    $(".editItem").removeClass("disabled");
    $(this).closest("a").addClass("disabled");
    $(this).closest("a").removeClass("btn-danger");
    $(this).closest("a").addClass("btn-success");
    $("#fileId").val(fileId);
    $("#inputFile").show();
  });


  $(document).on("click", '.toggleAddNewFile', function () {
    $("#editRequirements").modal("hide");
    $("#addRequirements").modal("toggle");
  });

  $(document).on("click", ".cancelAddBtn", function () {
    $("#editRequirements").modal("toggle");
    $("#addRequirements").modal("hide");
  });

  var addFileFormData = new FormData();
  var fileCount = 0;
  $(".addFileData").on("click", function (e) {
    e.preventDefault();
    var addFileData = $("#inputAddFile")[0].files;
    if (addFileData.length != 0) {
      var fileName = addFileData[0].name;
      var extension = fileName.split('.').pop();
      var shortFileName = fileName.slice(0, 10);
      if (fileName.length > 10) {
        shortFileName += '...';
      }
      var finalFileName = shortFileName + extension;
      var fileId = "file_" + fileCount; // generate a unique id for the file
      addFileFormData.append(fileId, addFileData[0]);
      fileCount++;
      // Add the file to the table
      var $tableRow = $("<tr>");
      var $deleteButton = $("<button>")
        .text("Delete")
        .addClass("delete-file")
        .addClass("btn btn-warning")
        .data("fileId", fileId); // store the unique id with the delete button
      $tableRow.append($("<td>").append($deleteButton));
      $tableRow.append($("<td>").text(finalFileName));
      $(".add-file-table").append($tableRow);
      $("#inputAddFile").val('');
    }
  });
  $(".clearAddingData").on("click", function () {
    addFileFormData = new FormData();
    fileCount = 0;
  });
  $(document).on("click", ".delete-file", function () {
    var fileId = $(this).data("fileId");

    $(this).closest("tr").remove();

    addFileFormData.delete(fileId);

    if (Array.from(addFileFormData).length === 0) {
      fileCount = 0;
    }
  });

  $('.saveFiles').on("click", function (e) {
    e.preventDefault();
    if (Array.from(addFileFormData).length != 0) {
      $("#reqAddForm").submit();
    } else {
      alert("Please add a file!");
    }
  })

  $("#reqAddForm").on("submit", function (e) {
    e.preventDefault();
    $.ajax({
      url: "/student/request/file/add?requestId=" + rid,
      type: "POST",
      data: addFileFormData,
      enctype: "multipart/form-data",
      processData: false,
      contentType: false,
      cache: false,
      beforeSend: function () {
        $(".saveFiles").attr("disabled", true);

      },
      success: function (res) {
        $("#fileData").val('');
        $(".saveFiles").attr("disabled", false);
        $("#addingAlert").empty();
        $("#addingAlert").removeClass("alert-danger");
        $("#addingAlert").addClass("alert-success");
        $("#addingAlert").text(res);
        $("#addingAlert").show();
        setTimeout(function () {
          $(".clearAddingData").click();
          $(".cancelAddBtn").click();
          $(".add-file-table").empty();
          $("#addingAlert").hide();

          link = "/student/my-requests/fetch?requestId=" + id;
          finalValue = "";
          fetchReqFiles(id, link, finalValue);
        }, 2500);


      },
      error: function (err) {
        $("#addingAlert").empty();
        $("#addingAlert").removeClass("alert-success");
        $("#addingAlert").addClass("alert-danger");
        $("#addingAlert").text(err.responseText);
        $("#addingAlert").show();
        $(".saveFiles").attr("disabled", false);
      },
    });
  });

  $(document).on("click", ".saveChanges", function (e) {
    e.preventDefault();
    var fileData = $("#fileData")[0].files;
    if (fileData.length == 0) {
      $("#editingAlert").empty();
      $("#editingAlert").removeClass("alert-success");
      $("#editingAlert").addClass("alert-danger");
      $("#editingAlert").text(
        "Please select / upload a file in your directory!."
      );
      $("#editingAlert").show();
    } else {
      $("#reqUpForm").submit();
    }

  });
  $(document).on("click", ".cancelUpdateBtn", function (e) {
    $(".clearRequest").click();
  });
  $(document).on("click", ".clearRequest", function (e) {
    $("#inputFile").hide();
    $("#fileData").val('');
    e.preventDefault();
    $(".editItem").removeClass("disabled");
    $(this).closest("a").removeClass("btn-success");
    $(".editItem").addClass("btn-danger");
    $("#editingAlert").empty();
    $("#editingAlert").hide();
  });
  //update request file form
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

      },
      success: function (res) {
        $("#fileData").val('');
        $(".saveChanges").attr("disabled", false);
        $("#editingAlert").empty();
        $("#editingAlert").removeClass("alert-danger");
        $("#editingAlert").addClass("alert-success");
        $("#editingAlert").text("File Succesfully Updated");
        $("#editingAlert").show();
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
  $(document).on("click", ".resubmitRequests", function (e) {
    e.preventDefault();

    $("#resubmitReqModal").modal("toggle");
    id = $(this).attr("href");
    rid = $(this).data("value");

  });
  $(".confirmResubmit").on("click", function (e) {
    e.preventDefault();

    link = "/student/requests/resubmit?userId=" + id + "&requestId=" + rid;

    $.get(link, function (request) {
      if ((request = "Success")) {
        $("#resubmitReqModal").modal("hide");
        refreshTable();
      }
    });
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

  // Web Socket Connection
  function studRequestConnection() {
    var socket = new SockJS("/websocket-server");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
      setConnected(true);
      if (stompClient.ws.readyState === WebSocket.OPEN) {
        stompClient.subscribe("/topic/student_request/recieved", function (data) {
          if (data.toString().toLowerCase().includes("ok")) {
            refreshTable();
          }
          //  stompClient.send("/app/student/request/ID", {}, "I Got Send");
        });
      } else {
        console.log("Student Request Socket not fully loaded yet. Waiting...");
        setConnected(false);
      }
    }, function (error) {
      console.log("Student Request Socket Lost connection to WebSocket. Retrying...");
      setConnected(false);

    });


  }
  // Check the connection status every second
  setInterval(function () {
    if (!connected) {
      console.log("Connection lost. Attempting to reconnect...");
      studRequestConnection();
    }
  }, 5000); // check every second
});
