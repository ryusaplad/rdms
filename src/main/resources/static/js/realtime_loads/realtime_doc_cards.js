$(document).ready(function () {
    updateCards();
    docCardsonnection();




    function updateCards() {
        $.ajax({
            type: "GET",
            url: "/student/document/cards/load",
            beforeSend: function () {
                $(".mainLoaderDiv").show();
            },
            success: function (data) {
                $(".mainLoaderDiv").hide();
                $(".cardRow").empty();
                console.log(data.length);
                if (data.length != 0) {
                    $.each(data, function (count, documents) {

                        var cards = `<div class="col-lg-4 mb-2">
                  <div class="card border border-dark shadow card-hover">
                    <div class="bg-image hover-overlay ripple" data-mdb-ripple-color="light">
                      <a href="#!">
                        <div class="imageDiv mt-2 text-center" style="background-color: rgba(251, 251, 251, 0.15);">
                          <img
                            src="data:image/png;base64,${documents.image}"
                            class="img-fluid"
                            style="height: 250px; width: 250px;"
                          >
                        </div>
                      </a>
                    </div>
                    <div class="card-body">
                      <h3 class="card-title text-center">${documents.title}</h3>
                      <div class="row">
                        <div class="col-sm mt-2">
                          <a href="/student/request/${documents.title}" class="w-100 btn btn-primary">
                            Request This Document.
                          </a>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>`;
                        $(".cardRow").append(cards);
                    });
                } else {
                    $(".cardRow").append(`<div style="text-align:center;margin:auto;font-size:16px;">No Document Available</div>`);
                }
            },
            error: function (e) {
                $("#resultDiv").fadeOut(1);
                $("#resultDiv").fadeIn(100);
                $("#buttonColor").removeClass("bg-success").addClass("bg-warning");
                $("#alertDiv").removeClass("alert-success").addClass("alert-warning");
                $("#resultMessage").html(
                    "Document Loading Failed, Please try again later!."
                );
            },
        });
    }


    // Web Socket Connection
    function docCardsonnection() {
        var socket = new SockJS("/websocket-server");
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            setConnected(true);
            if (stompClient.ws.readyState === WebSocket.OPEN) {
                stompClient.subscribe("/topic/request/cards", function (data) {
                    if (data.toString().toLowerCase().includes("ok")) {
                        updateCards();
                    }
                });
            } else {
                console.log("Documents WebSocket not fully loaded yet. Waiting...");
                setConnected(false);
            }
        }, function (error) {
            console.log("Documents Socket, lost connection to WebSocket. Retrying...");
            setConnected(false);

        });
    }


    // Check the connection status every second
    setInterval(function () {
        if (!connected) {
            console.log("Documents connection lost. Attempting to reconnect...");
            docCardsonnection();
        }
    }, 5000); // check every second

});