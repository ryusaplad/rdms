$(document).ready(function () {
  var modalView = $(".modalView");
  var htmlModal = "";

  $(".toggleLogDetail").on("click", function (e) {
    e.preventDefault();
    var logId = $(this).data("value");

    // AJAX request to fetch data
    $.ajax({
      url: "/admin/globallogs/fetch?logId=" + logId,
      method: "GET",
      success: function (data) {
        // Clear modalView
        modalView.empty();
        htmlModal = `<div class="modal fade" id="logDetailModal" tabindex="-1" role="dialog" aria-labelledby="logDetailModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="logDetailModalLabel">Log Details</h5>
      
      </div>
      <div class="modal-body">
        <p id="logMessage"></p>
        <p id="logMessageType"></p>
        <p id="logDateAndTime"></p>
        <p id="logThreatLevel"></p>
        <p id="logPerformedBy"></p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary clearModal" data-bs-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>
`;



        modalView.append(htmlModal);
        var logDetailModal = $("#logDetailModal");
        var logMessage = $("#logMessage");
        var logMessageType = $("#logMessageType");
        var logDateAndTime = $("#logDateAndTime");
        var logThreatLevel = $("#logThreatLevel");
        var logPerformedBy = $("#logPerformedBy");
        // Loop through data and add to modalView
        $("#logDetailModalLabel").text($("#logDetailModalLabel").text() + " - " + logId);
        logMessage.html("<strong>Message:</strong> " + data.message);
        logMessageType.html("<strong>Type:</strong> " + data.messageType);
        logDateAndTime.html("<strong>Date &amp; Time:</strong> " + data.dateAndTime);
        logThreatLevel.html("<strong>Threat Level:</strong> " + data.threatLevel);
        logPerformedBy.html("<strong>Performed By:</strong> " + data.performedBy);
        // Show the modal
        logDetailModal.modal("show");
      },
      error: function (error) {
        console.log(error);
      }
    });
  });
});