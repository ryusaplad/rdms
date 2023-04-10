$(document).ready(function () {
    updateCards();
    connect();




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
                                src="/student/documents/image?id=${documents.documentId}"
                                src="images/documentImage"
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
                                Request
                        This Document.
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
    function connect() {
        var socket = new SockJS("/websocket-server");
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            setConnected(true);
            if (stompClient.ws.readyState === WebSocket.OPEN) {
                stompClient.subscribe("/topic/totals", function (data) {
                    if (data.toString().toLowerCase().includes("ok")) {
                        updateCards();
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