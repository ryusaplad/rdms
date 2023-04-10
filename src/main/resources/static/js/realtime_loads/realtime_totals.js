$(document).ready(function () {

    var location = window.location.href;
    connect();
    if (location.includes("student")) {
        var studentDashboardTotals = ["totalRequests", "totalApprovedRequests", "totalRejectedRequests", "totalUploaded"];
        loadTotals("/student/dashboard/totals", studentDashboardTotals);
    } else if (location.includes("registrar")) {
        var registrarashboardTotals = ["totalStudentRequests", "totalTeacherRequests", "totalPendingRequests", "totalApprovedRequests", "totalRejectedRequests", "totalUploadedFiles"];
        loadTotals("/registrar/dashboard/totals", registrarashboardTotals);
    } else if (location.includes("teacher")) {
        var teacherDashboardTotals = ["totalRequests","totalPendingRequests", "totalSentRequests", "totalUploadedFiles"];
        loadTotals("/teacher/dashboard/totals", teacherDashboardTotals);
    } else if (location.includes("admin")) {


        var adminDashboardTotals = ["totalStudentRequests", "totalRegistrarRequests", "totalPendingRequests",
            "totalApprovedRequests",
            "totalRejectedRequests",
            "totalRegistrarAcc",
            "totalTeacherAcc",
            "totalStudentsAcc",
            "totalDelRegistarAcc",
            "totalDelTeacherAcc",
            "totalDelStudentsAcc",
            "totalGlobalFiles"];

        loadTotals("/admin/dashboard/totals", adminDashboardTotals);
    }

    function loadTotals(url, elements) {

        console.log(elements.length);
        $.ajax({
            url: url,
            type: "GET",
            dataType: "json",
            beforeSend: function () {
                for (var i = 0; i <= elements.length; i++) {
                    $('.' + elements[i]).text("");
                }
            },
            success: function (data) {
                console.log()
                for (var x = 0; x <= elements.length; x++) {

                    $('.' + elements[x]).text(data[x]);
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("Error fetching data: " + textStatus + " - " + errorThrown);
            },
            complete: function () {

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
                        if (location.includes("student")) {
                            loadTotals("/student/dashboard/totals", studentDashboardTotals);
                        } else if (location.includes("registrar")) {
                            loadTotals("/registrar/dashboard/totals", registrarashboardTotals);
                        } else if (location.includes("teacher")) {
                            loadTotals("/teacher/dashboard/totals", teacherDashboardTotals);
                        } else if (location.includes("admin")) {
                            loadTotals("/admin/dashboard/totals", adminDashboardTotals);
                        }
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