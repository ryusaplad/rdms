$(document).ready(function () {

    connect();

    function refreshTable() {
        var url = "";
        var location = window.location.href;

        if (location.includes("/registrar/sent-requests")) {
            url = "/registrar/registrar/requests/load";
        } else if (location.includes("/teacher/registrar-requests")) {
            url = "/teacher/registrar/requests/load";
        } else if (location.includes("/admin/registrar_requests")) {
            url = "/school_admin/registrar/requests/load";
        } else {
            url = "/invalid/";
        }

        // Make AJAX request to fetch latest data
        $.ajax({
            url: url,
            type: "GET",
            dataType: "json",
            success: function (data) {
                // Empty table body
                var table = $("#zero_config").DataTable({
                    "ordering": false
                });
                table.clear();

                for (let i = 0; i < data.length; i++) {
                    var requests = data[i];
                  
                    var tableItem = `
                    <strong style="opacity:.75;">${requests.requestTitle}</strong>
                    <strong id="message" style="opacity:.50;" >${requests.requestMessage}</strong>`;
                    var tableRowNode = table.row.add([tableItem]).draw();
                    $(tableRowNode.node()).find("td").addClass("link");
                    $(tableRowNode.node()).find("td").attr('id', 'tdMessage')
                    $(tableRowNode.node()).find("td").attr('style', 'cursor:pointer');
                    $(tableRowNode.node()).find("td").attr('data-value', requests.requestId);


                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error("Error refreshing table: ", errorThrown);
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
                refreshTable();
                
                stompClient.subscribe("/topic/registrar/requests", function (data) {
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