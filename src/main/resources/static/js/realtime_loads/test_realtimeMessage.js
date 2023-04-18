$(document).ready(function () {
    notifConnection();
    // Web Socket Connection
    function notifConnection() {
        var socket = new SockJS("/websocket-server");
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            setConnected(true);
            if (stompClient.ws.readyState === WebSocket.OPEN) {
                stompClient.subscribe("/topic/all/user/message", function (data) {

                    // Remove all existing modals
                    $(".modal-backdrop").remove();
                    $(".modalView").empty();
                    // Create and show the new modal
                    $(".modalView").append(`<div class="modal fade" id="messageModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" role="dialog" aria-labelledby="messageModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="messageModalLabel">Message from the above.</h5>
      </div>
      <div class="modal-body">
        <h1 class="text-center">${data.body}</h1>
        <img src="https://media.tenor.com/N1AXa5h0rK8AAAAM/happy-dancing.gif" style="width:100%">
      </div>
    </div>
  </div>
</div>`);
                    $("#messageModal").modal("toggle");

                    // Hide the modal after 5 seconds
                    setTimeout(function () {

                        $("#messageModal").modal("hide");
                    }, 6000);

                });
            } else {
                console.log("Notification webSocket not fully loaded yet. Waiting...");
                setConnected(false);
            }
        }, function (error) {
            console.log("Notification Socket, lost connection to WebSocket. Retrying...");
            setConnected(false);
        });
    }

    // Check the connection status every second
    setInterval(function () {
        if (!connected) {
            console.log("Notification connection lost. Attempting to reconnect...");
            notifConnection();
        }
    }, 5000); // check every second
});