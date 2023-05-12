$(document).ready(function () {
  var mainHtmlModal = "";
  var htmlModal = "";
  var htmlModalVal = "";
  var htmlBtn = "";
  var dataVal = "";
  $(document).on("click", ".link", function (e) {
    e.preventDefault();

    $(this)
      .addClass("bg-light")
      .css(
        "box-shadow",
        "0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19)"
      )

      .on("mouseleave", function () {
        $(this).removeClass("bg-light");
        $(this).css("box-shadow", "");
      });
    $(".modalView").empty();
    dataVal = $(this).data("value");

    $.ajax({
      type: "GET",
      url: "/teacher/requests-view?requestId=" + dataVal,
      success: function (data) {
        if (data[0].requestStatus == "pending") {
          htmlBtn =
            `   <button type="button" class="btn btn-success text-white  response" data-value="` +
            dataVal +
            `" data-bs-dismiss="modal">Response</button>`;
          htmlModalVal = "";
        } else if (data[0].requestStatus == "recordonly") {
          htmlBtn = "";
          htmlModalVal += `
          <div class="card">
          <hr>
  <strong><i class="far fa-file"></i> Files</strong>
  <div class="card-body text-start">
   <div class="row">`;
          for (let dataIndex = 1; dataIndex < data.length; dataIndex++) {
            let fileName = data[dataIndex].name;
            let fileExtension = fileName.substring(fileName.lastIndexOf("."));
            if (fileName.length > 10 - fileExtension.length) {
              fileName =
                fileName.substring(0, 10 - fileExtension.length) +
                "..." +
                fileExtension;
            }

            htmlModalVal +=
              `
    <div  class="col m-1">
    <a class="btn btn-primary text-white viewFile" data-value="${data[dataIndex].fileId}"><i class="fas fa-eye"></i> View</a>
      <a href="/teacher/files/download?id=` +
              data[dataIndex].fileId +
              `" class="btn btn-light border border-light text-dark" title="` +
              data[dataIndex].name +
              `">
        <i class="far fa-file"></i> ` +
              fileName +
              `
      </a>
    </div>
  `;
          }
        } else if (data[0].requestStatus == "messsageonly") {
          htmlBtn = "";
          htmlModalVal =
            `<h4>-- Message -- </h4><pre>` + data[0].teacherMessage + `</pre>`;
        } else if (data[0].requestStatus == "completed") {
          htmlBtn = "";
          htmlModalVal =
            `<h4>-- Message -- </h4><pre>` + data[0].teacherMessage + `</pre>`;
          htmlModalVal += `
          <div class="card">
          <hr>
  <strong><i class="far fa-file"></i> Files</strong>
  <div class="card-body text-start">
   <div class="row">`;
          for (let dataIndex = 1; dataIndex < data.length; dataIndex++) {
            let fileName = data[dataIndex].name;
            let fileExtension = fileName.substring(fileName.lastIndexOf("."));
            if (fileName.length > 10 - fileExtension.length) {
              fileName =
                fileName.substring(0, 10 - fileExtension.length) +
                "..." +
                fileExtension;
            }

            htmlModalVal +=
              `
              <div class="col m-1">
              <div class="d-flex flex-nowrap  align-items-center">
                <a href="#" class="btn btn-primary btn-view viewFile" data-value="${data[dataIndex].fileId}">
                  <i class="fas fa-eye"></i> View
                </a>
                <a href="/teacher/files/download?id=${data[dataIndex].fileId}" class="btn btn-light btn-download" title="${data[dataIndex].name}">
                  <i class="fas fa-download"></i> ${fileName}
                </a>
              </div>
            </div>
            
  `;
          }

          htmlModalVal += `
   </div>
  </div>
</div>
          `;
        } else {
          htmlBtn = "";
          htmlModalVal =
            `<h4>-- Message -- </h4><pre>` + data[0].teacherMessage + `</pre>`;
        }

        mainHtmlModal =
          `
<div class="modal fade" id="sentReqInfo" data-bs-backdrop="static"  data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
<div class="modal-dialog modal-xl modal-dialog-scrollable">
<div class="modal-content">
  <div class="modal-header">
    <h5 class="modal-title" id="sentReqInfoLabel">` +
          data[0].requestTitle +
          `</h5>
    <button type="button" class="btn-close clearModal" data-bs-dismiss="modal" aria-label="Close"></button>
  </div>
  <div class="modal-body s-2">

      <code style="font-size:15px;">Title: ` +
          data[0].requestTitle +
          `</code>
     |
     <code style="font-size:15px;">From: ` +
          data[0].requestBy +
          `</code> 
     
      <pre style="white-space: pre-wrap; font-size:17px;"> ` +
          data[0].requestMessage +
          `</pre>` +
          htmlModalVal +
          `<code style="font-size:15px;">Date/Time: ` +
          data[0].requestDate +
          `</code>
     
  </div>
  <div class="modal-footer">
    <button type="button" class="btn btn-secondary clearModal" data-bs-dismiss="modal">Close</button>
 ` +
          htmlBtn +
          `
  </div>
</div>
</div>
</div>`;

        $(".modalView").append(mainHtmlModal);
        $("#sentReqInfo").modal("toggle");




      },
      error: function (error) {
        console.log(error.responseText);
      },
    });
  });

  $(document).on("click", ".response", function (e) {
    dataVal = $(this).data("value");
    if ($(".modalView").find("#responseModal").length > 0) {
      $("#responseModal").modal("toggle");
    } else {
      htmlModal =
        `
<div class="modal fade" id="responseModal" data-bs-backdrop="static"  data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
  <div class="modal-dialog modal-xl modal-dialog-scrollable modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="responseModalLabel">Response Form</h5>
        <button type="button" class="btn-close clearModal" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body s-2">
      <form id="reponseForm" method="post">
      <div class="alert alert-success alert-dismissible fade show" role="alert">
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>

  <h5 class="alert-heading"><i class="fas fa-info-circle"></i> Notes:</h5>
  <hr>
  <ul>
  <li>Response Message is required.</li>
    <li>You can still send a response even if you don't upload any files.</li>
   
  </li>
</div>
<div class="alert alert-danger message errorMessageAlert" style="display:none"
                                            role="alert"></div>
       <div class="row">
      <div class="col-6">
      <label  for="message">Message<span class="badge badge-secondary text-danger">*</span></label>
      <div class="mt-2 form-floating">
      <textarea id="messageVal" name="message" class="form-control floatingInput" maxlength="1000" style="height:150px" required></textarea> 
      <label id="messageLengthLabel" style="height:10px;" class="w-100" for="message">maximum of (1000 letters)</label>
      </div>
      </div>
      
      <div class="col-6">
       <div class="container">

                                            <div class="row mb-2 ">

                                                <div class="col col-lg-8">

                                                    <h5>Upload file here.
                                                       
                                                    </h5>
                                                </div>

                                                <div class="col-lg-2 mb-1 text-end">

                                                    <label for="files"  class="btn addRequirementFile btn-primary"
                                                        style="width:120px">
                                                        Add
                                                    </label>
                                                    <div class="text-left" style="display:none">

                                                        <input class="form-control" name="file[]" type="file" id="files"
                                                            multiple />
                                                    </div>
                                                </div>
                                                <div class="col-0 table-responsive ">
                                                    <table class="table table-white table-bordered mt-2">
                                                        <thead>
                                                            <tr>
                                                                <th>Action</th>
                                                                <th scope="col">File Name</th>
                                                                <th scope="col">File Size</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody id="fileInfoTable">
                                                            
                                                        </tbody>
                                                    </table>

                                                    <div class="row">
                                                        <div class="col">
                                                            <button id="finalBtn"
                                                                class="btn finalizedResponse btn-danger text-white w-100">Finalized</button>
                                                        </div>
                                                         <div class="col">
                                                            <button id="finalBtn"
                                                                class="btn editResponse btn-primary text-white w-100" disabled>Edit</button>
                                                        </div>
                                                        <div class="col">
                                                            <button id="resetBtn" type="reset"
                                                                class="btn clearResponseForm text-white btn-warning w-100">Reset</button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
      </div>
      </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary backToMainModal" data-bs-dismiss="modal">Back</button>
        <button type="button"  class="btn btn-success text-white sentConfirm"  data-value="` +
        dataVal +
        `" disabled>Sent</button>
      </div>
    </div>
  </div>
</div>`;

      $(".modalView").append(htmlModal);
      $("#responseModal").modal("toggle");
    }

  });
  $(document).on("click", ".editResponse", function (e) {
    e.preventDefault();
    $("#messageVal").prop("readonly", false);
    $(".editResponse").prop("disabled", true);
    $(".sentConfirm").prop("disabled", true);
    $(".finalizedResponse").prop("disabled", false);
    $(".addRequirementFile").removeClass("disabled");
    $(".removeItemFile").removeClass("disabled");
  });
  $(document).on("click", ".finalizedResponse", function (e) {
    e.preventDefault();
    if ($("#messageVal").val().replace(/\s+/g, "").length > 0) {
      $(".sentConfirm").prop("disabled", false);
      $("#messageVal").removeClass("border border-danger");
      $("#messageVal").prop("readonly", true);
      $(".editResponse").prop("disabled", false);
      $(this).prop("disabled", true);
      $(".addRequirementFile").addClass("disabled");
      $(".removeItemFile ").addClass("disabled");
    } else {
      $(".sentConfirm").prop("disabled", true);
      $("#messageVal").focus();

      $("#messageVal").addClass("border border-danger");
      $("#messageVal").prop("readonly", false);
      $(".editResponse").prop("disabled", true);
    }
  });

  $(document).on("click", ".clearResponseForm", function (e) {
    e.preventDefault();

    $("#reponseForm").trigger("reset");
    $(".sentConfirm").prop("disabled", true);
    $("#messageVal").prop("readonly", false);
    $("#messageVal").removeClass("border border-danger");
    $("#messageLengthLabel").text("maximum of (1000 letters)");

    $(".addRequirementFile").removeClass("disabled");

    $(".removeItemFile ").removeClass("disabled");
    $("#fileInfoTable").empty();

    formData = new FormData();

    $(".finalizedResponse").prop("disabled", false);
    $(".editResponse").prop("disabled", true);

    $(".errorMessageAlert").hide();
  });

  $(document).on("click", ".backToMainModal", function (e) {
    if ($(".modalView").find("#sentReqInfo").length > 0) {
      $("#sentReqInfo").modal("toggle");
    } else {
      $(".modalView").append(mainHtmlModal);
    }
  });
  var alertDiv = $("#alertDiv");
  var alertBox = $("#resultDiv");
  var alertMessage = $("#resultMessage");
  // Sending Request
  $(document).on("click", ".sentConfirm", function (e) {
    formData.append("message", $("#messageVal").val());
   
    $.ajax({
      type: "POST",
      url: "/teacher/re-sent-request?requestId=" + dataVal,
      data: formData,
      processData: false,
      contentType: false,
      cache: false,
      beforeSend: function () {
        $(".sentConfirm").attr("disabled", true);
      },
      success: function (result) {
        alertDiv.removeClass("alert-warning");
        alertDiv.addClass("alert-success");
        alertBox.show();
        alertMessage.text("Requests have been sent back to the registrar.");
        $(".sentConfirm").attr("disabled", false);
        $("#responseModal").modal("hide");
        $(".modalView").empty();
      },
      error: function (error) {
        $("#responseModal").modal("hide");
        if(!error.responseText.includes("<!DOCTYPE html>")){
          alertDiv.removeClass("alert-warning");
          alertDiv.addClass("alert-success");
          alertBox.show();
          alertMessage.text(`Failed to Sent: ${error.responseText}`);
          
        }
        $(".sentConfirm").attr("disabled", false);
      },
    });
  });
  $(".btn-close-alert").on("click", function () {
    alertBox.hide();
  });
  $(document).on("click", ".clearModal", function (e) {
    $(".modalView").empty();
  });

  $(document).on("keyup", "#messageVal", function (e) {
    var textMaxLength = 1000;
    var messageLength = $(this).val().length;
    if (messageLength == 0) {
      $("#messageLengthLabel").text("maximum of (1000 letters)");
      $(".sentConfirm").prop("disabled", true);
    } else {
      if (messageLength <= textMaxLength) {
        $("#messageLengthLabel").text("(" + messageLength + "/1000) ");
        messageLength = textMaxLength - messageLength;
        $("#messageVal").removeClass("border border-danger");
      } else {
        $("#messageVal").addClass("border border-danger");
        $(this).val($(this).val().substring(0, 1000));
      }
    }
  });

  // ManageFile

  $(document).change("#files", function () {
    formData = new FormData();
    $("#fileInfoTable").empty();
    var totalFiles = $("#files")[0].files.length;
    for (var x = 0; x < totalFiles; x++) {
      var fileName = $("#files")[0].files[x].name;
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
        $("#fileInfoTable").append(
          "'<tr>'" +
          "<td>" +
          "<button type = 'button' class='btn removeItemFile btn-outline-danger' data-index=" +
          x +
          ">Cancel</button>" +
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
      fileListArr = Array.from($("#files")[0].files);
      for (var i = 0; i < fileListArr.length; i++) {
        formData.append("file[]", fileListArr[i]);
      }
      $(".errorMessageAlert").hide();
      }
     
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
    var index = $(this).attr("data-index");

    formData.append("excluded" + index, fileListArr[index].name);

    $(this).closest("tr").remove();
    if ($("#fileInfoTable tr").length == 0) {
      $("#fileInfoTable").empty();
      formData = new FormData();
    }
  });


});
