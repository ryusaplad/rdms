$(document).ready(function () {
  var htmlTable = "";
  var htmlModal = "";
  var formData = new FormData();
  var fileListArr;
  var finalValue = "";
  var usId = "";
  var rId = "";
  $(document).on("click", ".toggleRequestDetail", function (e) {
    e.preventDefault();
    $(".modalView").empty();
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
                    <button type="button" class="btn-close clearModal" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <h4 class="card-title">Requests Informations</h4>
                    <div class="container-sm"></div>
                    <table class="table table-primary table-responsive">
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
                    <hr>
                    <h4 class="card-title ">Message</h4>
                    <pre class="messHeader" style="display:none;overflow-y: scroll; font-size:14px; white-space: pre-wrap;">

                    </pre>
                    <hr>
                    <h4 class="card-title">Request Status</h4>
                    <table class="table table-primary table-responsive">
                        <thead>
                            <tr>
                                <th>Date Request</th>
                                <th>Request Status</th>
                                <th>Manage by</th>
                            </tr>

                        </thead>
                        <tbody>
                            <tr>
                                <td class="font-weight-bold" id="datereqpar">N/A</td>
                                <td class="font-weight-bold" id="reqstatuspar">N/A</td>
                                <td class="font-weight-bold" id="manageby"></td>
                            </tr>
                        </tbody>
                    </table>
                    <hr>
                    <h4 class="card-title">Receieved Requirements</h4>
                    <table class="table table-white table-responsive receiveRequirementsTable"
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
                    <h4 class="card-title">Sent Documents</h4>
                    <table class="table table-white table-responsive sentDocumentTable"
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
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary cancelFinalizing clearModal"
                        data-bs-dismiss="modal">Close</button>

                </div>
            </div>
        </div>
    </div>
      `;
    $(".modalView").append(htmlModal);

    $.ajax({
      url: link,
      type: "GET",
      success: function (result) {
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
              dlAnchor =
                "<tr>" +
                "<td>" +
                "<a href = '/registrar/files/download?id=" +
                result.data[x].fileId +
                "'class='btn btn-danger text-white'>Download</a>" +
                "</td>" +
                "<td>" +
                finalValue +
                "</td>" +
                "<td>" +
                result.data[x].uploaderName +
                "</td>" +
                "</tr > ";
              $(".tablebody").append(dlAnchor);
            } else if (result.data[x].status == "Approved") {
              dlAnchor =
                "<tr>" +
                "<td>" +
                "<a href = '/registrar/files/download?id=" +
                result.data[x].fileId +
                "'class='btn btn-danger text-white'>Download</a>" +
                "</td>" +
                "<td>" +
                finalValue +
                "</td>" +
                "<td>" +
                result.data[x].uploaderName +
                "</td>" +
                "</tr > ";
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
        $("#reqDetailModal").modal("toggle");
      },
      error: function (error) { },
    });
  });
  $(document).on("click", ".processBtn", function (e) {
    e.preventDefault();
    var userId = $(this).attr("href");
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
                    <p>Are you sure you want to process this requests.?</p>
                    <b> Note:</b>
                    <p>This action cannot be undo, after submission.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary clearModal" data-bs-dismiss="modal">Cancel</button>
                    <a id="processHref" href=` +
      usId +
      ` type="button" class="btn btn-danger confirmProcess clearModal">Confirm</a>
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

    rId = $(".completeBtn").data("value");
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
						  <button type="button" class="btn btn-success okCompleted clearModal text-white" data-bs-dismiss="modal">Ok</button>
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
        }, 5000);
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

  $(document).on("click", ".okCompleted", function (e) {
    window.location.reload();
  });

  $(document).on("click", ".rejectBtn", function (e) {
    e.preventDefault();
    var userId = $(this).attr("href");
    rId = $(".rejectBtn").data("value");
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
      "/registrar/studentreq/change/" +
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
    var textMaxLength = 1000;
    var messageLength = $(this).val().length;
    if (messageLength <= textMaxLength) {
      $("#messageLengthLabel").text("(" + messageLength + "/1000) ");
      messageLength = textMaxLength - messageLength;
    } else {
      $(this).val($(this).val().substring(0, 1000));
    }
  });
});