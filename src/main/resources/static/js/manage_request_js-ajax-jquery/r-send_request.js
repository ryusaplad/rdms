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
                    <div class="form-group">
                        <label for="from">From</label>
                        <input type="text" class="form-control " id="from" placeholder="From:" name="from" required readonly value="` +
            from +
            `">
                    </div>
                    <div class="alert alert-danger mt-2 font-weight-bold" role="alert" id="alertMessage1"
                        style="display:none"></div>
                    <div class="form-group">
                        <label for="to">To</label>
                        <input type="text"  class="form-control" id="to"placeholder="To:" name="to" required readonly value="` +
            sendTo +
            `">
                    </div>
                    <label for="requestsMessage">Message</label>
                    <div class="mt-2 form-floating">
                        <textarea id="requestsMessage" name="message" class="form-control floatingInput" maxlength="250"
                            style="height:150px"></textarea>
                        <label id="messageLengthLabel" for="reason">You can add message here with maximum of (250
                            letters)
                        </label>
                    </div>
                   
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-warning" data-bs-dismiss="modal">Close</button>
                <button type="submit" class="btn btn-success submitRegReq">Sent Requests</button>
            </div>
            </form>
        </div>
    </div>`
        );
        $("#requestsModal").modal("toggle");
      },
    });
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
      },
      error: function (error) {
        $(".submitRegReq").attr("disabled", false);
        console.log(error);
      },
    });
  });
  $(document).on("keyup", "#requestsMessage", function (e) {
    var defaultPlaceHolder =
      "You can add message here with maximum of (250 letters)";
    var textMaxLength = 250;
    var messageLength = $(this).val().length;
    if (messageLength == 0) {
      $("#messageLengthLabel").text(defaultPlaceHolder);
    } else {
      if (messageLength <= textMaxLength) {
        $("#messageLengthLabel").text("(" + messageLength + "/250) ");
        messageLength = textMaxLength - messageLength;
      } else {
        $(this).val($(this).val().substring(0, 250));
      }
    }
  });
});
