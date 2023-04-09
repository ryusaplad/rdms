$(document).ready(function () {
  var modalView = $(".modalView");
  var htmlModal = "";

  $(document).on("click", ".toggleLogDetail", function (e) {
    e.preventDefault();
    var logId = $(this).data("value");

    // AJAX request to fetch data
    $.ajax({
      url: "/admin/globallog/fetch?logId=" + logId,
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
        <p id="logClientIp"></p>
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
        var logClientIp = $("#logClientIp");
        // Loop through data and add to modalView
        $("#logDetailModalLabel").text($("#logDetailModalLabel").text() + " - " + logId);
        logMessage.html("<strong>Message:</strong> " + data.message);
        logMessageType.html("<strong>Type:</strong> " + data.messageType);
        logDateAndTime.html("<strong>Date &amp; Time:</strong> " + data.dateAndTime);



        var threatLevel = data.threatLevel;
        var iconClass, badgeClass;
        switch (threatLevel) {
          case "low":
            iconClass = "fas fa-shield-alt text-success";
            badgeClass = "badge bg-success";
            break;
          case "medium":
            iconClass = "fas fa-shield-alt text-warning";
            badgeClass = "badge bg-warning";
            break;
          case "high":
            iconClass = "fas fa-shield-alt text-danger";
            badgeClass = "badge bg-danger";
            break;
          case "critical":
            iconClass = "fas fa-shield-alt text-danger";
            badgeClass = "badge bg-danger";
            break;
          default:
            iconClass = "fas fa-shield-alt text-muted";
            badgeClass = "badge bg-secondary";
            threatLevel = "Unknown";
            break;
        }

        var html = '<strong>Threat Level:</strong> ' +
          '<i class="' + iconClass + '"></i>' +
          '<span class="' + badgeClass + '">' + threatLevel + '</span>';
        logThreatLevel.html(html);

        logPerformedBy.html("<strong>Performed By:</strong> " + data.performedBy);
        logClientIp.html("<strong>Client Ip:</strong> " + data.clientIpAddress);
        // Show the modal
        logDetailModal.modal("show");
      },
      error: function (error) {
        console.log(error);
      }
    });
  });
  refreshTable();
  connect();
  // Initialize the DataTable
  var table = $('#zero_config').DataTable({
    ordering: false
  });

  function refreshTable() {
    // Make AJAX request to fetch latest data
    $.ajax({
      url: "/fetch/admin/global_logs",
      type: "GET",
      dataType: "json",
      success: function (data) {
        // Clear table data
        table.clear();

        for (var i = 0; i < data.length; i++) {
          var log = data[i];

          var tableBodyItems = `
          <tr>
            <td>${log.dateAndTime}</td>
            <td>${log.message}</td>
            <td>
              <span>
                ${(() => {
              switch (log.threatLevel) {
                case "low":
                  return `
                        <i class="fas fa-shield-alt text-success"></i>
                        <span class="badge bg-success">Low</span>
                      `;
                case "medium":
                  return `
                        <i class="fas fa-shield-alt text-warning"></i>
                        <span class="badge bg-warning">Medium</span>
                      `;
                case "high":
                  return `
                        <i class="fas fa-shield-alt text-danger"></i>
                        <span class="badge bg-danger">High</span>
                      `;
                case "critical":
                  return `
                        <i class="fas fa-shield-alt text-danger"></i>
                        <span class="badge bg-danger">Critical</span>
                      `;
                default:
                  return `
                        <i class="fas fa-shield-alt text-muted"></i>
                        <span class="badge bg-secondary">Unknown</span>
                      `;
              }
            })()}
              </span>
            </td>
            <td>
              <a type="button" class="btn btn-outline-success text-black toggleLogDetail" data-value="${log.logsId}">
                Details
              </a>
            </td>
          </tr>
        `;

          // Add new data to table
          table.rows.add($(tableBodyItems));
        }

        // Get a reference to the first cell of the first row
        var cell = table.cell(':eq(0)', 3);

        // Get the existing text in the cell
        var existingText = cell.data();

        // Replace the first occurrence of "Details" with "Details - Latest"
        var newText = existingText.replace("Details", "Details - <span class='text-danger'>Latest</span>");

        // Set the updated text back to the cell
        cell.data(newText);

        // Redraw the table to update the content
        table.draw();
      },
      error: function (jqXHR, textStatus, errorThrown) {
        console.error("Error refreshing table: ", errorThrown);
      }
    });
  }


  // Web Socket Connection
  function connect() {
    var socket = new SockJS("/websocket-server");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
      setConnected(true);
      if (stompClient.ws.readyState === WebSocket.OPEN) {
        stompClient.subscribe("/topic/totals", function (data) {
          if (data.toString().toLowerCase().includes("ok")) {
            refreshTable();
          }
        });
      } else {
        console.log("WebSocket not fully loaded yet. Waiting...");
        setTimeout(function () {
          connect();
        }, 1000); // retry after 1 second
      }
    });
  }

});