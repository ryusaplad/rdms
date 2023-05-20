$(document).ready(function () {
  //Web Socket Connect;
  regRequestListsConnection();
  var htmlTable = "";
  var htmlModal = "";
  var formData = new FormData();
  var totalFiles = 0;
  var fileListArr = [];
  var finalValue = "";
  var usId = "";
  var rId = "";

  refreshTable();

  function refreshTable() {

    // Make AJAX request to fetch latest data
    $.ajax({
      url: "/registrar/fetch/student-requests",
      type: "GET",
      dataType: "json",
      success: function (data) {
        // Empty table body
        var table = $("#zero_config").DataTable({
          "ordering": true,
          "destroy": true,
          columnDefs: [
            { targets: [3], orderable: false }]
        });
        table.clear();

        var actions = "";
        for (var i = 0; i < data.length; i++) {
          var studRequest = data[i];
          var statusIcon = "";
          if (studRequest.requestStatus.toLowerCase().includes("approved")) {
            statusIcon = ` <td> <strong class="btn btn-outline-success" > <i class="fas fa-check text-success" aria-hidden="true"></i> Completed</strong></td>`;
          } else if (studRequest.requestStatus.toLowerCase().includes("pending")) {
            statusIcon = ` <td> <strong class="btn btn-outline-primary" > <i class="fa fa-hourglass-start text-primary" aria-hidden="true"></i> ${studRequest.requestStatus}</strong></td>`;
          } else if (studRequest.requestStatus.toLowerCase().includes("on-going")) {
            statusIcon = ` <td> <strong class="btn btn-outline-orange" > <i class="fa fa-hourglass-half" aria-hidden="true"></i> ${studRequest.requestStatus}</strong></td>`;
          } else if (studRequest.requestStatus.toLowerCase().includes("rejected")) {
            statusIcon = ` <td> <strong class="btn btn-outline-danger" > <i class="fas fa-times text-danger" aria-hidden="true"></i> ${studRequest.requestStatus}</strong></td>`;
          }


          actions = `<div class="row">
          <div class="col-sm">
              <a href="?s=${studRequest.studentId}&req=${studRequest.requestId}" type="button" class="btn btn-primary w-100 toggleRequestDetail">Details</a>
          </div>
          <div class="col-sm" ${studRequest.requestStatus == 'Pending' ? '' : 'style="display:none;"'}>
              <a href="${studRequest.studentId}" data-value="${studRequest.requestId}" type="button" class="btn btn-success w-100 text-white processBtn">Process</a>
          </div>
          <div class="col-sm" ${studRequest.requestStatus == 'On-Going' ? '' : 'style="display:none;"'}>
              <a href="${studRequest.studentId}" data-value="${studRequest.requestId}" type="button" class="btn btn-success w-100 text-white completeBtn">Complete</a>
          </div>
          <div class="col-sm" ${studRequest.requestStatus != 'Approved' && studRequest.requestStatus != 'Rejected' ? '' : 'style="display:none;"'}>
              <a href="${studRequest.studentId}" data-value="${studRequest.requestId}" type="button" class="btn btn-danger w-100 text-white rejectBtn">Reject</a>
          </div>
      </div>`;
          // Append new data to table body

          $("#zero_config")
            .DataTable()
            .row.add([
              studRequest.requestBy,
              studRequest.requestDate,
              statusIcon,
              actions,
            ])
            .draw();
        }

      },
      error: function (jqXHR, textStatus, errorThrown) {
        console.error("Error refreshing table: ", errorThrown);
      }
    });

  }



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
      "/registrar/studentrequest/fetch?s=" +
      paramMap.s +
      "&req=" +
      paramMap.req;

    htmlModal = `
        <div class="modal fade" id="reqDetailModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
        role="dialog" aria-labelledby="reqDetailModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="reqDetailModal">
                        <span id="titleVal">More Details</span> - <span class="btn" id="statusInSup" style="cursor: text;"></span>
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                <div class="reqDetailedLoaderDiv">
                <div id="loaderDiv">
                    <div class="cardLoader">
                        <div class="loader-wheel"></div>
                        <div class="loader-text"></div>
                    </div>
                </div>
            </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary cancelFinalizing clearModal"
                        data-bs-dismiss="modal">Close</button>

                </div>
            </div>
        </div>
    </div>
      `;

    $(".reqDetailedLoaderDiv").show();
    $(".modalView").append(htmlModal);
    $("#reqDetailModal").modal("toggle");
    $.ajax({
      url: link,
      type: "GET",
      success: function (result) {
        $(".reqDetailedLoaderDiv").replaceWith(`<h4 class="card-title">Requests Informations</h4>
        <div class="container-sm table-responsive">
        <table class="table table-primary ">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>C/Y/SEM</th>
                    <th>Document</th>
    
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td id="requestbyupar">
                    </td>
                    <td class="font-weight-bold" id="requestbynpar">
                    </td>
                    <td class="font-weight-bold" id="cysem">
                    </td>
                    <td class="font-weight-bold" id="docreqpar"></td>
    
                </tr>
            </tbody>
        </table>
        </div>
        <hr>
        <h4 class="card-title ">Message</h4>
        <pre class="messHeader" style="display:none;overflow-y: scroll; font-size:14px; white-space: pre-wrap;">
    
        </pre>
        <hr>
        <h4 class="card-title">Request Status</h4>
        <div class="container-sm table-responsive">
        <table class="table table-primary ">
            <thead>
                <tr>
                    <th>Date Of Request</th>
                    <th>Request Status</th>
                    <th>Target Date / Due</th>
                    <th>Manage by</th>
                </tr>
    
            </thead>
            <tbody>
                <tr>
                    <td class="font-weight-bold" id="datereqpar">N/A</td>
                    <td class="font-weight-bold" id="reqstatuspar">N/A</td>
                    <td class="font-weight-bold" id="targetdate">N/A</td>
                    <td class="font-weight-bold" id="manageby"></td>
                </tr>
            </tbody>
        </table>
        </div>
        <hr>
        <h4 class="card-title">Receieved Requirements</h4>
        <div class="container-sm table-responsive">
        <table class="table table-white receiveRequirementsTable"
            style="overflow-y: scroll; height: 100px;">
            <thead>
                <tr>
    
                    <th>Download</th>
                    <th>File Name</th>
                    <th>Uploaded By</th>
                </tr>
    
            </thead>
            <tbody class="tablebody">
                <tr style="width:2px; white-space: pre-wrap; word-spacing: 1px; ">
                    <td>
    
                    </td>
                </tr>
            </tbody>
        </table>
        </div>
        <h4 class="card-title">Sent Documents</h4>
        <div class="container-sm table-responsive">
        <table class="table table-white sentDocumentTable"
            style="overflow-y: scroll; height: 100px; display:none">
            <thead>
                <tr>
    
                    <th>Download</th>
                    <th>File Name</th>
                    <th>Uploaded By</th>
                </tr>
    
            </thead>
            <tbody class="sentDocsBody">
                <tr style="width:2px; white-space: pre-wrap; word-spacing: 1px; ">
                    <td>
                    </td>
                </tr>
            </tbody>
        </table> </div>`);
        $(".tablebody").empty();
        $(".sentDocsBody").empty();
        var dlAnchor = "";
        if (result.status == "success") {
          for (var x = 1; x < result.data.length; x++) {
            var findDot = result.data[x].fname.indexOf(".");
            var newValue = result.data[x].fname.substring(0, findDot);
            var secondValue = result.data[x].fname.substring(
              findDot,
              result.data[x].fname.length
            );

            if (newValue.length > 15) {
              finalValue = newValue.substring(0, 15) + ".." + secondValue;
            } else {
              finalValue = newValue + secondValue;
            }
            if (result.data[x].status == "Pending") {
              dlAnchor = `<tr><td>
              <a class="btn btn-primary text-white viewFile" data-value="${result.data[x].fileId}"><i class="fas fa-eye"></i> View</a>
              <a href = "/registrar/files/download?id=${result.data[x].fileId}"
               class="btn btn-secondary text-white"><i class="fas fa-download"></i> Download</a>
               </td>
            
              <td>${finalValue}</td>
              <td>${result.data[x].uploaderName}</td></tr>`;

              $(".tablebody").append(dlAnchor);
            } else if (result.data[x].status == "Approved") {

              dlAnchor = `<tr><td>
              <a class="btn btn-primary text-white viewFile" data-value="${result.data[x].fileId}"><i class="fas fa-eye"></i> View</a>
              <a href = "/registrar/files/download?id=${result.data[x].fileId}"
               class="btn btn-secondary text-white"><i class="fas fa-download"></i> Download</a>
               </td>
            
              <td>${finalValue}</td>
              <td>${result.data[x].uploaderName}</td></tr>`;


              $(".sentDocumentTable").show();
              $(".sentDocsBody").append(dlAnchor);
            }
          }

          $("#requestbyupar").text(result.data[0].requestBy);
          $("#requestbynpar").text(result.data[0].name);
          $("#cysem").text(
            result.data[0].course +
            "," +
            result.data[0].year +
            "," +
            result.data[0].semester.substring(0, 3)
          );
          var requestStatusValue = result.data[0].requestStatus.toLowerCase();

          $("#docreqpar").text(result.data[0].requestDocument);
          if (
            requestStatusValue == "approved" ||
            requestStatusValue == "on-going"
          ) {
            $("#statusInSup").addClass("bg-success");
            $("#statusInSup").addClass("text-white");
          } else if (requestStatusValue == "rejected") {
            $("#statusInSup").addClass("bg-danger");
            $("#statusInSup").addClass("text-white");
          } else if (requestStatusValue == "pending") {
            $("#statusInSup").addClass("bg-primary");
            $("#statusInSup").addClass("text-white");
          } else {
            $("#statusInSup").addClass("bg-dark");
            $("#statusInSup").addClass("text-white");
          }
          $("#statusInSup").text(result.data[0].requestStatus);
          $("#reqstatuspar").text(result.data[0].requestStatus);
          $("#datereqpar").text(result.data[0].requestDate);

          // Get the target date as a string
          const targetDateStr = result.data[0].targetDate;

          // Check if targetDateStr is empty or not a valid date
          if (targetDateStr === "" || isNaN(Date.parse(targetDateStr))) {
            $("#targetdate").text(`(${result.data[0].targetDate}):${ result.data[0].releaseDate}`);
          } else {
       
            //parse the value to date
            const targetDate = new Date(targetDateStr);
            const currentDate = new Date();

            // Calculate the remaining time in milliseconds
            const remainingTime = targetDate.getTime() - currentDate.getTime();
            // Calculate the remaining days
            const remainingDays = Math.ceil(remainingTime / (1000 * 3600 * 24));
            // Format the date to remove the time zone text
            const formattedDate = targetDate.toLocaleDateString(undefined, { day: 'numeric', month: 'short', year: 'numeric' });
            // Create the text to display
            const displayText = formattedDate + " (" + remainingDays + " days remaining)";
            // Set the text of the element with id "targetdate"
            $("#targetdate").text(displayText);
          }



          $("#manageby").text(result.data[0].manageBy);
          $(".messHeader").empty();
          $(".messHeader").show();
          $("#mess").text();

          if (result.data[0].reply != null) {
            if (result.data[0].reply.length != 0) {
              if (
                result.data[0].reply.length > 0 &&
                result.data[0].manageBy.length > 0
              ) {
                var htmlP =
                  " <p id='mess'>From:<mark>" +
                  result.data[0].name +
                  "</mark>:(" +
                  result.data[0].message +
                  ")</p>" +
                  "<p id='reply'>From:<mark>" +
                  result.data[0].manageBy +
                  "</mark>:(" +
                  result.data[0].reply +
                  ")</p>";
                $(".messHeader").append(htmlP);
              }
            } else {
              var htmlP =
                " <p id='mess'>From:<mark> " +
                result.data[0].name +
                "</mark>:(" +
                result.data[0].message +
                ")</p>";
              $(".messHeader").append(htmlP);
            }
          } else {
            var htmlP =
              " <p id='mess'>From:<mark> " +
              result.data[0].name +
              "</mark>:(" +
              result.data[0].message +
              ")</p>";
            $(".messHeader").append(htmlP);
          }
        }
        $(".reqDetailedLoaderDiv").hide();
        $(".reqDetailedLoaderDiv").remove();
      },
      error: function (error) { },
    });
  });
  $(document).on("click", ".processBtn", function (e) {
    e.preventDefault();
    var userId = $(this).attr("href");
    rId = $(this).data("value");
    usId = userId;
    htmlModal =
      `
        <div class="modal fade" id="confirmModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
          role="dialog" aria-labelledby="confirmModalModalLabel" aria-hidden="true">
          <div class="modal-dialog" role="document">
              <div class="modal-content">
                  <div class="modal-header">
                      <h5 class="modal-title" id="confirmModalModalLabel">Process Confirmation</h5>
                      <button type="button" class="btn-close clearModal" data-bs-dismiss="modal" aria-label="Close"></button>
                  </div>
                  <div class="modal-body mBody">
                    <p>Are you sure you want to process this request?</p>
                    <b>Notes:</b>
                    <ul>
                      <li>This action can be undone and changed to <strong>'REJECTED'</strong> if necessary.</li>
                      <li>Selecting a target date is required and will set the deadline for completing this request.</li>
                      <li>The target date must not be less than or greater than to the current date.</li>
                    </ul>
                    <label for="datepicker">Select target date:</label>
                    <input type="date" id="datepicker" name="datepicker" class="form-control" required>
                    <div class="invalid-feedback d-none invalidMessage"></div>
                  
                  </div>
                  <div class="modal-footer">
                      <button type="button" class="btn btn-secondary clearModal" data-bs-dismiss="modal">Cancel</button>
                      <a id="processHref" href=` +
      usId +
      ` type="button" class="btn btn-danger confirmProcess">Confirm</a>
                  </div>
              </div>
          </div>
      </div>
      `;
    $(".modalView").append(htmlModal);
    $("#confirmModal").modal("toggle");
  });
  $(document).on("click", ".completeBtn", function (e) {
    e.preventDefault();
    var userId = $(this).attr("href");

    rId = $(this).data("value");
    usId = userId;
    htmlModal =
      `
     <div class="modal fade" id="confirmCompleteModal" aria-hidden="true" data-bs-backdrop="static"
        data-bs-keyboard="false" aria-labelledby="confirmCompleteLabel" tabindex="-1">
        <div class="modal-dialog  modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="confirmCompleteLabel">Completition Confirmation</h5>
                    <button type="button" class="btn-close cancelFinalizing clearModal" data-bs-dismiss="modal"
                        aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <p>Are you sure you want to complete this requests.?</p>
                    <b> Note:</b>
                    <p>This action cannot be undo, after submission.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary clearModal" data-bs-dismiss="modal">Cancel</button>
                    <button class="btn btn-warning confirmCompleteNext" data-value=` +
      usId +
      ` data-bs-dismiss="modal" >Yes</button>
                </div>
            </div>
        </div>
    </div>

    `;
    $(".modalView").append(htmlModal);
    $("#confirmCompleteModal").modal("toggle");
  });

  $(document).on("click", ".confirmCompleteNext", function (e) {
    e.preventDefault();

    htmlModal =
      `
    <div class="modal fade" id="completeModal" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false"
        aria-labelledby="completeModalToggleLabel2" tabindex="-1">
        <div class="modal-dialog modal-lg modal-dialog-centered  modal-dialog-scrollable">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="completeModalToggleLabel2">Finalizing Requests</h5>
                    <button type="button" class="btn-close cancelFinalizing clearModal" data-bs-dismiss="modal"
                        aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div style="display:none" class="f-alert">
                        <div class="alert alert-danger d-flex align-items-center " role="alert">
                            <svg class="bi flex-shrink-0 me-2" width="24" height="24" role="img" aria-label="Danger:">
                                <use xlink:href="#exclamation-triangle-fill" />
                            </svg>
                            <div id="f-alertM">

                            </div>
                        </div>
                    </div>

                    <div class="file-upload container-sm">
                        <button class="file-upload-btn" type="button"
                            onclick="$('.file-upload-input').trigger('click')">Add
                            Files</button>

                        <div class="file-upload-wrap">
                            <form id="f-reqs-upload">
                                <input class="file-upload-input" type='file' id="rfile" name="rfile[]" multiple />
                            </form>

                            <div class="drag-text">
                                <h3>Drag and drop a file or click add Files</h3>
                            </div>
                        </div>

                        <table class="table table-white w-100">
                            <thead>
                                <tr>

                                    <th>Action</th>
                                    <th>File Name</th>
                                    <th>Size</th>
                                </tr>

                            </thead>
                            <tbody class="r-file-table">

                            </tbody>

                        </table>
                    </div>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary cancelFinalizing clearModal"
                        data-bs-dismiss="modal">Cancel</button>
                    <button class="btn btn-warning confirmCompleteFinal " data-value=` +
      usId +
      `>Finalized Request</button>
                </div>
            </div>
        </div>
    </div>
    `;
    $(".modalView").empty();
    $(".modalView").append(htmlModal);
    $("#completeModal").modal("toggle");
  });

  $(document).on("click", ".confirmCompleteFinal", function (e) {
    e.preventDefault();
    $("#f-reqs-upload").submit();

  });

  // Finalized Requests with uploaded files

  $(document).change("#rfile", function () {
    formData = new FormData();
    $(".r-file-table").empty();


    if ($("#rfile")[0] && $("#rfile")[0].files) {
      totalFiles = $("#rfile")[0].files.length;
      fileListArr = Array.from($("#rfile")[0].files);
    }

    for (var x = 0; x < totalFiles; x++) {
      var fileName = $("#rfile")[0].files[x].name;
      var split = fileName.split(".");
      fileName = split[0];
      var extension = split[1];
      if (!["jpg", "jpeg", "png", "pdf"].includes(extension)) {
        var message = "";
        message = "Please select a valid file type (JPG, PNG, or PDF).";
        $(".message").text(message);
        $(".errorMessageAlert").show();
        formData = new FormData();
        $("#fileInfoTable").empty();
        return false;
      } else {
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
        $(".errorMessageAlert").hide();
      }

    }
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
      $("#rfile").val("");
      formData = new FormData();
    }
  });

  $(".cancelFinalizing").on("click", function (e) {
    formData = new FormData();
    $("#f-reqs-upload").trigger("reset");
    $(".r-file-table").empty();
    $("#rfile").val("");
    $(".f-alert").hide();
    $("#f-alertM").text("");
  });

  $(document).on("click", ".clearModal", function (e) {
    $(".modalView").empty();
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

  $(document).bind("dragover", ".file-upload-wrap", function () {
    $(".file-upload-wrap").addClass("file-dropping");
  });
  $(document).bind("dragleave", ".file-upload-wrap", function () {
    $(".file-upload-wrap").removeClass("file-dropping");
  });
  $(document).on("submit", "#f-reqs-upload", function (e) {
    e.preventDefault();
    console.log("i got called");
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
        htmlModal = `
         <div class="modal fade" id="finalizedCompletedModal" aria-hidden="true" data-bs-backdrop="static"
        data-bs-keyboard="false" aria-labelledby="confirmCompleteLabel" tabindex="-1">
        <div class="modal-dialog  modal-dialog-centered">
            <div class="modal-content">
               
                <div class="modal-body">

                    <div class="card text-center">
						<div class="card-img">
						<img src="../images/checkMark.png"></img>
							</div>
							<div class="h3">
							<p>Request has been finalized.</p>
							</div>
						</div>
                </div>
              
            </div>
        </div>
    </div>
        `;
        $(".modalView").append(htmlModal);
        $("#finalizedCompletedModal").modal("toggle");
        setTimeout(function () {
          $("#finalizedCompletedModal").modal("hide");
          $(".okCompleted").trigger("click");
        }, 2000);
        $(".f-alert").hide();
        $("#f-alertM").text("");
        refreshTable();
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

  $(document).on("click", ".rejectBtn", function (e) {
    e.preventDefault();
    var userId = $(this).attr("href");
    rId = $(this).data("value");
    htmlModal =
      `
     <div class="modal fade" id="rejectModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
        role="dialog" aria-labelledby="rejectModalModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="rejectModalModalLabel">Rejecting Confirmation</h5>
                    <button type="button" class="btn-close clearModal" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <p>Are you sure you want to reject this requests.?</p>
                    <b> Note:</b>
                    <p>This action cannot be undo, after submission.</p>
                    <label>Specify Reason Here</label>
                    <div class="mt-2 form-floating">
                        <textarea id="reason" name="reason" class="form-control floatingInput" maxlength="1000"
                            style="height:150px"></textarea>
                        <label id="messageLengthLabel" for="reason">You can add message here with maximum of (1000
                            letters)
                        </label>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary clearModal" data-bs-dismiss="modal">Cancel</button>
                    <a id="rejectHref" href=` +
      userId +
      ` type="button" class="btn btn-danger text-white confirmReject">Confirm</a>
                </div>
            </div>
        </div>
    </div>
    `;
    $(".modalView").append(htmlModal);
    $("#rejectModal").modal("toggle");
  });





  $(document).on("click", ".confirmProcess", function (e) {
    e.preventDefault();
    var userId = $(this).attr("href");
    var targetDate = new Date($("#datepicker").val());
    var currentDate = new Date();
    var maxDate = new Date();
    maxDate.setDate(maxDate.getDate() + 100);

    if (isNaN(targetDate) || targetDate < currentDate) {
      $("#datepicker").addClass("is-invalid");
      $(".invalidMessage").removeClass("d-none");
      $(".invalidMessage").text("Please select a valid target and date that is not in the past.");
      return;
    } else if (targetDate > maxDate) {
      $(".invalidMessage").removeClass("d-none");
      $(".invalidMessage").text("Please select a target date within the next 100 days.");
      return;
    } else {
      $("#datepicker").removeClass("is-invalid");
      $(".invalidMessage").addClass("d-none");
    }

    updateStudentRequests(userId, targetDate, "", "On-Going");
  });



  $(document).on("click", ".confirmReject", function (e) {
    e.preventDefault();
    var userId = $(this).attr("href");

    var message = $("#reason").val();
    if (message.length < -1) {
      updateStudentRequests(userId, "", "N/A", "Rejected");
    }
    updateStudentRequests(userId, "", message, "Rejected");
  });
  function updateStudentRequests(userId, targetDate, reason, status) {
    var status =
      "/registrar/studentreq/change/" +
      status +
      "/?userId=" +
      userId +
      "&targetDate=" +
      targetDate +
      "&requestId=" +
      rId +
      "&reason=" +
      reason;
    $.get(status, function (status) {
      if (status == "Success") {
        $("#rejectModal").modal("hide");
        $("#confirmModal").modal("hide");
        $(".modalView").empty();

        refreshTable();
      }
    });
  }

  $(document).on("keyup", "#reason", function (e) {
    var textMaxLength = 1000;
    var messageLength = $(this).val().length;
    if (messageLength <= textMaxLength) {
      $("#messageLengthLabel").text("(" + messageLength + "/1000) ");
      messageLength = textMaxLength - messageLength;
    } else {
      $(this).val($(this).val().substring(0, 1000));
    }
  });

  function regRequestListsConnection() {
    var socket = new SockJS('/websocket-server');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
      setConnected(true);
      if (stompClient.ws.readyState === WebSocket.OPEN) {
        stompClient.subscribe('/topic/student/requests', function (data) {

          if (data.toString().toLowerCase().includes("ok")) {
            refreshTable();
          }
          //  stompClient.send("/app/student/request/ID", {}, "I Got Send");
        });
      } else {
        console.log("Registrar Student Request View Socket not fully loaded yet. Waiting...");
        setConnected(false);
      }
    }, function (error) {
      console.log("Registrar Student Request View Socket, Lost connection to WebSocket. Retrying...");
      setConnected(false);

    });


  }
  // Check the connection status every second
  setInterval(function () {
    if (!connected) {
      console.log("Registrar Student Request View Socket, connection lost. Attempting to reconnect...");
      regRequestListsConnection();
    }
  }, 5000); // check every second
});
