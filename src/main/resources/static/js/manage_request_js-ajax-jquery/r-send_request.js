$(document).ready(function () {
  var userId, sendTo, from;
  $(document).on("click", ".sendRequests", function (event) {
    event.preventDefault();
    var href = $(this).attr("href");

    var search = href.split("?")[1];
    var params = search.split("&");

    $.ajax({
      type: "GET",
      url: "/session/name",
      success: function (data) {
        from = data;
        params.forEach(function (param) {
          var keyValue = param.split("=");
          switch (keyValue[0]) {
            case "userId":
              userId = keyValue[1];
              break;
            case "sendTo":
              sendTo = keyValue[1].replace("%20", " ");
              break;
          }
        });
        $("#requestsModal").empty();

        $("#requestsModal").append(
          `<div  class="modal-dialog mdialogLoad" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="requestsModal">
                    Send Requests To</h5><span class="toName"></span>

                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="form-requests-insert"  class="r-requestsForm">
                     <label for="from">Sender</label>
                    <div class="form-floating">
                        
                        <input type="text" class="form-control " id="from" placeholder="From:" name="from" required readonly value="` +
            from +
            `">
             <br>
            <label id="fromLabel" for="from">From:</label>
                    </div>
                  
                    <label for="to">Receiver</label>
                    <div class="form-floating">
                               <input type="text"  class="form-control" id="to"placeholder="To:" name="to" required readonly value="` +
            sendTo +
            `">
                        <label id="toLabel" for="to">To:</label>
                    
                
                   </div>
                <br>
                <label for="titleValue">Title</label>
                   <div class="form-floating">
                               <input type="text"   class="form-control" id="titleValue" placeholder="Title" name="title" required maxlength="50">
                        <label id="titleLabel" for="titleValue">Add the title here (max 50 letters)</label>
                    
                
                   </div>
                <br>
                    <label for="requestsMessage">Message</label>
                    <div class="form-floating">
                        <textarea id="requestsMessage"  name="message" class="form-control floatingInput" maxlength="10000"
                            style="height:150px" ></textarea>
                        <label id="messageLengthLabel" for="reason">You can add message here (max 10000 letters) 
                        </label>
                    </div>
                   
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-warning" data-bs-dismiss="modal">Close</button>
                <button type="submit" class="btn btn-success text-white submitRegReq">Sent</button>
            </div>
            </form>
        </div>
    </div>`
        );

        $("#requestsModal").modal("toggle");
      },
    });
  });
  $("#requestsModal").on("shown.bs.modal", function () {
    $("#titleValue").focus();
  });
  // Form
  $(document).on("click", ".submitRegReq", function (event) {
    event.preventDefault();
    $("#form-requests-insert").submit();
  });

  $(document).on("submit", "#form-requests-insert", function (e) {
    e.preventDefault();
    $.ajax({
      type: "POST",
      url: "/registrar/send/requests/?userId=" + userId,
      data: new FormData(this),
      processData: false,
      contentType: false,
      cache: false,
      beforeSend: function () {
        $(".submitRegReq").attr("disabled", true);
      },
      success: function (data) {
        $(".submitRegReq").attr("disabled", false);
        $("#requestsModal").modal("hide");
        $("#resultDiv").hide();
        $("#alertDiv").removeClass("alert-warning").addClass("alert-success");
        $("#resultMessage").html("Requests has been sent!.");
        $("#resultDiv").fadeIn(100);
        $("#textboxid").removeClass("border border-danger");
      },
      error: function (error) {
        $(".submitRegReq").attr("disabled", false);

        if (
          error.responseText.includes("sender") ||
          error.responseText.includes("reciever")
        ) {
          $("#requestsModal").modal("hide");
          $("#resultDiv").hide();
          $("#alertDiv").removeClass("alert-success").addClass("alert-warning");
          $("#resultMessage").html(error.responseText);
          $("#resultDiv").fadeIn(100);
        } else if (
          error.responseText.includes("Message cannot be empty") ||
          error.responseText.includes("Message Length Invalid")
        ) {
          $("#messageLengthLabel").text(error.responseText);
          $("#requestsMessage").focus();
          $("#requestsMessage").addClass("border border-danger");
        } else if (
          error.responseText.includes("Title cannot be empty") ||
          error.responseText.includes("Title Length Invalid")
        ) {
          $("#titleLabel").text(error.responseText);
          $("#titleValue").focus();
          $("#titleValue").addClass("border border-danger");
        } else {
          $("#requestsModal").modal("hide");
          $("#resultDiv").hide();
          $("#alertDiv").removeClass("alert-success").addClass("alert-warning");
          $("#resultMessage").html(error.responseText);
          $("#resultDiv").fadeIn(100);
        }
      },
    });
  });
  $(document).on("keyup", "#requestsMessage", function (e) {
    var defaultPlaceHolder = "You can add message here (max 10000 letters)";
    var textMaxLength = 10000;
    var messageLength = $(this).val().length;
    if (messageLength == 0) {
      $("#messageLengthLabel").text(defaultPlaceHolder);
    } else {
      $("#requestsMessage").removeClass("border border-danger");
      if (messageLength <= textMaxLength) {
        $("#messageLengthLabel").text("(" + messageLength + "/10000) ");
        messageLength = textMaxLength - messageLength;
        if (messageLength == textMaxLength) {
          $("#requestsMessage").addClass("border border-danger");
        }
      } else {
        $(this).val($(this).val().substring(0, 10000));
      }
    }
  });
  $(document).on("keyup", "#titleValue", function (e) {
    var defaultPlaceHolder = "Add the title here (max 50 letters)";
    var textMaxLength = 50;
    var messageLength = $(this).val().length;
    if (messageLength == 0) {
      $("#titleLabel").text(defaultPlaceHolder);
    } else {
      $("#titleValue").removeClass("border border-danger");
      if (messageLength <= textMaxLength) {
        $("#titleLabel").text("(" + messageLength + "/50) ");
        messageLength = textMaxLength - messageLength;
        if (messageLength == textMaxLength) {
          $("#titleValue").addClass("border border-danger");
        }
      } else {
        $(this).val($(this).val().substring(0, 50));
      }
    }
  });
});
