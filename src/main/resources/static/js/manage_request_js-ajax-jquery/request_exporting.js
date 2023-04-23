$(document).ready(function () {
    var modalView = $(".modalView");


    $(".export-link").click(function () {

        var htmlModal = `<div class="modal fade" id="exportedModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="exportedModalLabel" aria-hidden="true">
          <div class="modal-dialog">
            <div class="modal-content">
              <div class="modal-body exportedBody">
                <div class="spinner-export"></div>
                <p style="margin: auto;">Please wait while your file is being generated...</p>
              </div>
            </div>
          </div>
      </div>`;



        modalView.append(htmlModal);
        // Show the loading modal
        $("#exportedModal").modal("toggle");

        getCurrentLoggedIn(function (userType) {



            $.ajax({
                type: "GET",
                url: `/${userType}/student-request-export/all`,
                success: function (response) {
                    // Hide the loading modal


                    if (response === "success") {

                        $(".exportedBody").empty();
                        $(".exportedBody").append(`
                          <div class="alert alert-success d-flex align-items-center" role="alert">
                              <div style="margin: auto; text-align: center;">
                                  <img src="/images/checkIcon.png" alt="checkmark-icon.png" style="max-width: 50%; height: auto;">
                                  <h4 style="margin-top: 1rem;">Data Exported</h4>
                              </div>
                          </div>
                      `);
                        window.location.href = `/${userType}/student-request-export/confirm`;
                        setTimeout(function () {
                            $("#exportedModal").modal("hide");
                            modalView.empty();

                        }, 1000);
                    } else {

                        $(".exportedBody").empty();
                        $(".exportedBody").append(`
                  <div class="alert alert-primary d-flex align-items-center" role="alert">
                      <div style="margin: auto; text-align: center;">
                          <img src="/images/infoIcon.png" alt="checkmark-icon.png" style="max-width: 50%; height: auto;">
                          <h4 style="margin-top: 1rem;"> No Chart Data has been found.</h4>
                      </div>
                  </div>
              `);

                        setTimeout(function () {
                            $("#exportedModal").modal("hide");
                            modalView.empty();
                        }, 1000);
                    }
                },
                error: function (xhr, status, error) {
                    // Hide the loading modal
                    $(".exportedBody").empty();
                    // Error message
                    $(".exportedBody").append(`
                  <div class="alert alert-danger d-flex align-items-center" role="alert">
                      <div style="margin: auto; text-align: center;">
                          <img  src="/images/failIcon.png" alt="fail-icon.png" style="max-width: 40%; height: auto;border 1px solid red;">
                          <h4 style="margin-top: 1rem;">Data Exporting Failed!, Please try again.</h4>
                      </div>
                  </div>
              `);
                    setTimeout(function () {
                        $("#exportedModal").modal("hide");
                        modalView.empty();
                    }, 1000);
                }
            });
        });

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
});