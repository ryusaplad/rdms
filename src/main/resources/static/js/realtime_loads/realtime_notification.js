$(document).ready(function () {

    notifConnection();


    var mainNotificationCard = $(".ntcardMain");
    var mainTotalItems = 30;
    var secondaryTotalItems = 3;
    var maximumSecondaryitem = 10;
    var mainPageCount = 0;
    var secondaryPageCount = 0;

    var totalItems = 5;
    var totalPageCount = 0;
    var maxItems = 7;


    // handle "load more" button click
    $(document).on("click", ".loadMoreBtn", function () {
        mainTotalItems += 2; // increment page number
        getCurrentLoggedIn(function (userType) {

            fetchNotificationData(userType, "");
        });
        $(mainNotificationCard).animate({
            scrollTop: $(mainNotificationCard).offset().top

        }, 50);

    });
    // handle "load more" button click
    $(document).on("click", ".load-more-btn", function () {
        secondaryTotalItems += 2; // increment page number
        fetchMainNotification();
        fetchNotificationCount();
        $(mainNotificationCard).animate({
            scrollTop: $(mainNotificationCard).offset().top

        }, 50);
    });


    function fetchNotificationCount() {

        getCurrentLoggedIn(function (userType) {
            $.ajax({
                url: `/${userType}/notification/false/0/` + secondaryTotalItems,
                type: "GET",
                dataType: "json",

                success: function (data) {

                    var datalength = data.body.length;

                    if (datalength == 0 || datalength < -1) {
                        $(".blicon").css("margin-right", "-10px");
                        $(".notifCount").empty();
                        $(".notifCount").hide();
                    } else {
                        $(".blicon").css("margin-right", "-25px");
                        $(".notifCount").show();
                        for (var i = 0; i < data.body.length; i++) {
                            $(".notifCount").text(datalength);
                        }
                    }


                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log("Error fetching count: " + textStatus + " - " + errorThrown);
                },
                complete: function () {
                    $(".load-more-btn").prop("disabled", false);
                }
            });
        });


    }

    function fetchMainNotification() {

        getCurrentLoggedIn(function (userType) {
            var loaderReplacement = `<div class="notificationLoader" style="display: block;">
          <div class="spinner-border" role="status">
              <span class="visually-hidden">Loading...</span>
          </div>
        </div>`;
            mainNotificationCard.empty().append(loaderReplacement);

            $.ajax({
                url: `/${userType}/notification/false/0/` + secondaryTotalItems,
                type: "GET",
                dataType: "json",
                beforeSend: function () {
                    $(".load-more-btn").prop("disabled", true);
                },
                success: function (data) {
                    mainNotificationCard.empty();
                    var datalength = data.body.length;

                    if (datalength == 0 || datalength < -1) {
                        mainNotificationCard.append(`<div style="width: 200px;
                        width: 100%;
                        height: auto;
                        margin: auto;
                        text-align: center;
              position: relative;">No Latest Notifications Available.</div>`);
                    } else {

                        for (var i = 0; i < data.body.length; i++) {
                            var notification = data.body[i];
                            var btnColor = "";

                            if (notification.message.toLowerCase().includes("ongoing")) {
                                btnColor = "btn-primary";
                            } else if (notification.message.toLowerCase().includes("rejected")) {
                                btnColor = "btn-danger";
                            } else if (notification.message.toLowerCase().includes("approved")) {
                                btnColor = "btn-success";
                            } else {
                                btnColor = "btn-secondary";
                            }

                            var notificationHtml = `
                <a href="javascript:void(0)"  data-location="dashNotif" id="notif" data-value="${notification.notifId}" title="${notification.title}:${notification.message}" class=" text-dark border-top notifLink ">
                  <div class="d-flex no-block align-items-center p-10">
                    <span class="btn ${btnColor} btn-circle left-icon">
                      <i class="ti-info-alt" style="font-size: 15px;"></i>
                    </span>
                    <div class="ms-2">
                      <h5 title="${notification.title}" class="mb-0 title notifTitle">${notification.title}</h5>
                      <p title="${notification.message}" style="margin-bottom:-5px;" class="mail-desc notifdescription">${notification.message}</p>
                    </div>
                  </div>
                </a>
                <hr>
              `;

                            mainNotificationCard.append(notificationHtml);
                            secondaryPageCount = notification.pageCount;
                        }

                        if (maximumSecondaryitem == secondaryTotalItems) {
                            $(".load-more-btn").remove();
                        } else {
                            if (secondaryTotalItems < secondaryPageCount) {
                                var loadMore = ` <div id="loadm"  class="load-more-btn mx-auto d-block btn btn-outline-success">LOAD MORE</div>`;
                                mainNotificationCard.append(loadMore);
                            } else {
                                $(".load-more-btn").remove();
                            }
                        }

                    }

                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log("Error fetching data: " + textStatus + " - " + errorThrown);
                },
                complete: function () {
                    $(".load-more-btn").prop("disabled", false);
                }
            });
        });
    }


    // fetch data on page load
    fetchMainNotification();
    fetchNotificationCount()




    $(document).on("click", ".notifLink", function () {
        var modalView = $(".modalView");
        var reqId = $(this).data("value");
        var htmlModal = `
    

      <div class="modal fade" id="notificationModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="notificationModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-fullscreen">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="notificationModalLabel">Notifications</h5>
        <button type="button" class="btn-close clearModal" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body mainBodyCardContainer">
        <div class="container-sm mainCardContainer">
        
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary clearModal" data-bs-dismiss="modal">Close</button>
       
      </div>
    </div>
  </div>
</div>
      `;
        modalView.append(htmlModal);
        // fetch data on page load

        $("#notificationModal").modal("toggle");



        getCurrentLoggedIn(function (userType) {

            fetchNotificationData(userType, reqId);
        });

    });



    $(document).on("click", "#notif", function () {
        
        var notifId = $(this).data('value');
        getCurrentLoggedIn(function (userType) {

            $.ajax({
                url: `/${userType}/notification-status/true/` + notifId,
                type: "GET",
                success: function (data) {
                    fetchRightSideNotificationData();
                    fetchMainNotification();
                    fetchNotificationCount();
                },error : function(error){
                    console.log(error);
                }
            });
        });

    });

    //auto scroll to the selected latest notification

    $(document).on('shown.bs.modal', "#notificationModal", function () {
        var element = $(".highlight");
        if (element.length > 0) {
            var scrollTopPos = element.offset().top - $(".mainBodyCardContainer").offset().top + $(".mainBodyCardContainer").scrollTop();
            $(".mainBodyCardContainer").animate({
                scrollTop: scrollTopPos
            }, 500);
            element.addClass("border border-danger");
            $(".highlight-item button").css({ 'color': '#6856d6', 'background-color': '#eaefff' });
        }

    });


    $(document).on("click", ".highlight", function () {
        $(".highlight").removeClass("border border-danger");
    });


    //Manage Notification in a right side bar

    function fetchNotificationData(userType, reqId) {
        var mainNotificationCard = $(".mainCardContainer");
        // show loader animation
        var loaderReplacement = `
        <div class="notificationLoader" style="display: block;">
          <div class="spinner-border" role="status">
            <span class="visually-hidden">Loading...</span>
          </div>
        </div>
      `;
        mainNotificationCard.empty().append(loaderReplacement);

        $.ajax({
            url: `/${userType}/notification/0/${mainTotalItems}`,
            type: "GET",
            dataType: "json",
            beforeSend: function () {
                $(".loadMoreBtn").prop("disabled", true); // disable the load more button
            },
            success: function (data) {
                mainNotificationCard.empty();
                var datalength = data.body.length;
                if (datalength == 0 || datalength < -1) {
                    $(mainNotificationCard).append(`<div style="width: 250px;
        height: auto;
        margin: 0 auto;
        padding: 10px;
        font-size:15px;
        position: relative;">No Latest Notifications Available.</div>`);
                } else {
                    for (var i = 0; i < datalength; i++) {
                        var notification = data.body[i];
                        var btnColor = "";
                        if (notification.message.toLowerCase().includes("ongoing")) {
                            btnColor = "btn-primary";
                        } else if (notification.message.toLowerCase().includes("rejected")) {
                            btnColor = "btn-danger";
                        } else if (notification.message.toLowerCase().includes("approved")) {
                            btnColor = "btn-success";
                        }
    
                        var notificationHtml = `
                  <div class=" accordion-item ${notification.notifId === reqId ? 'highlight highlight-item' : ''}">
                    <h2 class="accordion-header">
                      <button class=" accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#notification-${i}" aria-expanded="false" aria-controls="notification-${i}">
                        <span class="btn ${btnColor} btn-circle left-icon">
                          <i class="ti-info-alt" style="font-size: 15px;"></i>
                        </span>
                        <div class="ms-2">
                          <h5 class="text-start">${notification.title}</h5>
                          <p style="margin-bottom:-5px;" class="mail-desc ">${notification.message}</p>
                        </div>
                      </button>
                    </h2>
                    <div id="notification-${i}" class="accordion-collapse collapse ${notification.notifId === reqId ? 'show' : ''}" aria-labelledby="notification-${i}" data-bs-parent="#notification-list">
                      <div class="accordion-body">
                        <p><strong>Message:</strong> ${notification.message}</p>
                        <p><strong>Date and Time:</strong> ${notification.dateAndTime}</p>
                        <p><strong>From:</strong> System</p>
                        <p><strong>Receiver:</strong> ${notification.to}</p>
                      </div>
                    </div>
                  </div>
                `;
    
                        mainNotificationCard.append(notificationHtml);
    
                        mainPageCount = notification.pageCount;
                    }
    
                    if (mainTotalItems < mainPageCount) {
                        var loadMore = `<div id="" class="loadMoreBtn mx-auto d-block btn btn-outline-success">LOAD MORE</div>`;
                        mainNotificationCard.append(loadMore);
                    } else {
                        $(".loadMoreBtn").remove();
                    }
    
                }
             

            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("Error fetching data: " + textStatus + " - " + errorThrown);
            },
            complete: function () {

                $(".loadMoreBtn").prop("disabled", false);
            }
        });
    }




    $(document).on('click', '.accordion-item button', function () {
        var clickedCollapse = $(this).closest('.accordion-item').find('.accordion-collapse');
        var otherCollapses = $('.accordion-item').not($(this).closest('.accordion-item')).find('.accordion-collapse');

        // Close all other accordion items except the clicked one
        otherCollapses.removeClass('show');
        $('.accordion-item button').not($(this)).addClass('collapsed');

        // Remove background color from other accordion items
        $('.accordion-item button').not($(this)).css({ 'background-color': '' });
        $('.accordion-item button').not($(this)).css({ 'color': '' });
        // Toggle the clicked item
        clickedCollapse.collapse('toggle');
        $(this).toggleClass('collapsed');

        // Set color and background color based on accordion state
        if ($(this).hasClass('collapsed')) {
            $(this).css({ 'color': '#6856d6', 'background-color': '#eaefff' });
        } else {
            $(this).css({ 'color': '', 'background-color': '' });
        }
    });

    function getCurrentLoggedIn(callback) {
        $.ajax({
            type: "GET",
            url: "/session/accountType",
            success: function (data) {
                callback(data);
            },
            error: function (error) {
                callback(null);
            },
        });




    }


    // Right Side Bar Notification

    //Right Side Bar btn
    $(document).on("click", ".rightSidebarToggle", function (e) {
        if (
            document.getElementById("btnIcon").className ==
            "fas fa-angle-left text-white"
        ) {
            document.getElementById("btnIcon").className =
                "fas fa-angle-right text-white";
            fetchRightSideNotificationData();
            $(".sidebar").animate(
                {
                    right: "0",
                },
                200,
                function () { }
            );
        } else {
            document.getElementById("btnIcon").className =
                "fas fa-angle-left text-white";
            $(".sidebar").animate(
                {
                    right: "-300px",
                },
                200,
                function () { }
            );
        }
    });


    //Notifications - Right
    function fetchRightSideNotificationData() {

        getCurrentLoggedIn(function (userType) {
            var loaderReplacement = `<div class="notificationLoader" style="display: block;">
        <div class="spinner-border" role="status">
            <span class="visually-hidden">Loading...</span>
        </div>
      </div>`;
            $('.notificationCard').empty().append(loaderReplacement);

            $.ajax({
                url: `/${userType}/notification/false/0/` + totalItems,
                type: "GET",
                dataType: "json",
                beforeSend: function () {
                    $(".loadRightSideNotifBtn").prop("disabled", true);
                },
                success: function (data) {
                    $('.notificationCard').empty();
                    var datalength = data.body.length;

                    if (datalength == 0 || datalength < -1) {
                        $('.notificationCard').append(`<div style="width: 200px;
            height: auto;
            margin: 0 auto;
            padding: 10px;
            position: relative;">No Latest Notifications Available.</div>`);
                    } else {

                        for (var i = 0; i < data.body.length; i++) {
                            var notification = data.body[i];
                            var btnColor = "";

                            if (notification.message.toLowerCase().includes("ongoing")) {
                                btnColor = "btn-primary";
                            } else if (notification.message.toLowerCase().includes("rejected")) {
                                btnColor = "btn-danger";
                            } else if (notification.message.toLowerCase().includes("approved")) {
                                btnColor = "btn-success";
                            } else {
                                btnColor = "btn-secondary";
                            }

                            var notificationHtml = `
              <a href="javascript:void(0)" data-location="right" id="notif" data-value="${notification.notifId}" title="${notification.title}:${notification.message}" class=" text-dark border-top notifLink ">
                <div class="d-flex no-block align-items-center p-10">
                  <span class="btn ${btnColor} btn-circle left-icon">
                    <i class="ti-info-alt" style="font-size: 15px;"></i>
                  </span>
                  <div class="ms-2">
                    <h5 title="${notification.title}" class="mb-0 title notifTitle">${notification.title}</h5>
                    <p title="${notification.message}" style="margin-bottom:-5px;" class="mail-desc notifdescription">${notification.message}</p>
                  </div>
                </div>
              </a>
              <hr>
            `;

                            $('.notificationCard').append(notificationHtml);
                            totalPageCount = notification.pageCount;
                        }
                        if (maxItems == totalItems) {
                            $(".loadRightSideNotifBtn").remove();
                        } else {
                            if (totalItems < totalPageCount) {
                                var loadMore = ` <div id="loadRight" class="loadRightSideNotifBtn mx-auto d-block btn btn-outline-success">LOAD MORE</div>`;
                                $('.notificationCard').append(loadMore);
                            } else {
                                $(".loadRightSideNotifBtn").remove();
                            }
                        }

                    }

                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log("Error fetching data: " + textStatus + " - " + errorThrown);
                },
                complete: function () {
                    $(".load-more-btn").prop("disabled", false);
                }
            });
        });

    }
    $(document).on("click", ".loadRightSideNotifBtn", function () {
        totalItems += 2; // increment page number
        fetchRightSideNotificationData();
        $('.notificationCard').animate({
            scrollTop: $('.notificationCard').offset().top

        }, 50);
    });
    function getCurrentLoggedIn(callback) {
        $.ajax({
            type: "GET",
            url: "/session/accountType",
            success: function (data) {
                callback(data);
            },
            error: function (error) {
                callback(null);
            },
        });

    }

    // Web Socket Connection
    function notifConnection() {
        var socket = new SockJS("/websocket-server");
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            setConnected(true);
            if (stompClient.ws.readyState === WebSocket.OPEN) {
                stompClient.subscribe("/topic/notifications/", function (data) {
                    if (data.toString().toLowerCase().includes("ok")) {
                        fetchRightSideNotificationData();
                        fetchMainNotification();
                        fetchNotificationCount();
                    }
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