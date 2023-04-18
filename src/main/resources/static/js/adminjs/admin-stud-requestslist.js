$(document).ready(function () {
  var htmlModal = "";

  $(document).on("click", ".clearModal", function (e) {
    $(".modalView").empty();
  });

  $(document).on("click", ".toggleRequestDetail", function (e) {
    e.preventDefault();
    $(".modalView").empty();
    var href = $(this).attr("href");

    var queryString = href.split("?")[1];
    var params = queryString.split("&");
    var paramMap = {};

    for (var i = 0; i < params.length; i++) {
      var param = params[i].split("=");
      var key = param[0];
      var value = param[1];
      paramMap[key] = value;
    }

    var link =
      "/svfc-admin/dashboard/studentrequest/fetch?s=" +
      paramMap.s +
      "&req=" +
      paramMap.req;

    htmlModal = `
        <div class="modal fade" id="reqDetailModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
        role="dialog" aria-labelledby="reqDetailModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="reqDetailModal">
                        <span id="titleVal">More Details</span> - <span class="btn" id="statusInSup" style="cursor: text;"></span>
                    </h5>
                    <button type="button" class="btn-close clearModal" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                <div class="reqDetailedLoaderDiv">
                <div id="loaderDiv">
                    <div class="cardLoader">
                        <div class="loader-wheel"></div>
                        <div class="loader-text"></div>
                    </div>
                </div>
            </div>
                   
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary cancelFinalizing clearModal"
                        data-bs-dismiss="modal">Close</button>

                </div>
            </div>
        </div>
    </div>
      `;
    $
    
    $(".reqDetailedLoaderDiv").show();
    $(".modalView").append(htmlModal);
    $("#reqDetailModal").modal("toggle");
    $.ajax({
      url: link,
      type: "GET",
      success: function (result) {
        $(".reqDetailedLoaderDiv").replaceWith(` <h4 class="card-title">Requests Informations</h4>
    <div class="container-sm"></div>
    <table class="table table-primary table-responsive">
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>C/Y/SEM</th>
                <th>Document</th>

            </tr>
        </thead>
        <tbody>
            <tr>
                <td id="requestbyupar">
                </td>
                <td class="font-weight-bold" id="requestbynpar">
                </td>
                <td class="font-weight-bold" id="cysem">
                </td>
                <td class="font-weight-bold" id="docreqpar"></td>

            </tr>
        </tbody>
    </table>
    <hr>
    <h4 class="card-title ">Message</h4>
    <pre class="messHeader" style="display:none;overflow-y: scroll; font-size:14px; white-space: pre-wrap;">

    </pre>
    <hr>
    <h4 class="card-title">Request Status</h4>
    <table class="table table-primary table-responsive">
        <thead>
            <tr>
                <th>Date Request</th>
                <th>Request Status</th>
                <th>Manage by</th>
            </tr>

        </thead>
        <tbody>
            <tr>
                <td class="font-weight-bold" id="datereqpar">N/A</td>
                <td class="font-weight-bold" id="reqstatuspar">N/A</td>
                <td class="font-weight-bold" id="manageby"></td>
            </tr>
        </tbody>
    </table>
    <hr>
    <h4 class="card-title">Receieved Requirements</h4>
    <table class="table table-white table-responsive receiveRequirementsTable"
        style="overflow-y: scroll; height: 100px;">
        <thead>
            <tr>

                <th>Download</th>
                <th>File Name</th>
                <th>Uploaded By</th>
            </tr>

        </thead>
        <tbody class="tablebody">
            <tr style="width:2px; white-space: pre-wrap; word-spacing: 1px; ">
                <td>

                </td>
            </tr>
        </tbody>
    </table>
    <h4 class="card-title">Sent Documents</h4>
    <table class="table table-white table-responsive sentDocumentTable"
        style="overflow-y: scroll; height: 100px; display:none">
        <thead></thead>
            <tr>

                <th>Download</th>
                <th>File Name</th>
                <th>Uploaded By</th>
            </tr>

        </thead>
        <tbody class="sentDocsBody">
            <tr style="width:2px; white-space: pre-wrap; word-spacing: 1px; ">
                <td>
                </td>
            </tr>
        </tbody>
    </table>`);
       
        $(".tablebody").empty();
        $(".sentDocsBody").empty();
        var dlAnchor = "";
        if (result.status == "success") {
          for (var x = 1; x < result.data.length; x++) {
            var findDot = result.data[x].fname.indexOf(".");
            var newValue = result.data[x].fname.substring(0, findDot);
            var secondValue = result.data[x].fname.substring(
              findDot,
              result.data[x].fname.length
            );

            if (newValue.length > 15) {
              finalValue = newValue.substring(0, 15) + ".." + secondValue;
            } else {
              finalValue = newValue + secondValue;
            }
            if (result.data[x].status == "Pending") {
              dlAnchor =
                "<tr>" +
                "<td>" +
                "<a href = '/svfc-admin/files/download?id=" +
                result.data[x].fileId +
                "'class='btn btn-danger text-white'>Download</a>" +
                "</td>" +
                "<td>" +
                finalValue +
                "</td>" +
                "<td>" +
                result.data[x].uploaderName +
                "</td>" +
                "</tr > ";
              $(".tablebody").append(dlAnchor);
            } else if (result.data[x].status == "Approved") {
              dlAnchor =
                "<tr>" +
                "<td>" +
                "<a href = '/svfc-admin/files/download?id=" +
                result.data[x].fileId +
                "'class='btn btn-danger text-white'>Download</a>" +
                "</td>" +
                "<td>" +
                finalValue +
                "</td>" +
                "<td>" +
                result.data[x].uploaderName +
                "</td>" +
                "</tr > ";
              $(".sentDocumentTable").show();
              $(".sentDocsBody").append(dlAnchor);
            }
          }

          $("#requestbyupar").text(result.data[0].requestBy);
          $("#requestbynpar").text(result.data[0].name);
          $("#cysem").text(
            result.data[0].course +
            "," +
            result.data[0].year +
            "," +
            result.data[0].semester.substring(0, 3)
          );
          var requestStatusValue = result.data[0].requestStatus.toLowerCase();

          $("#docreqpar").text(result.data[0].requestDocument);
          if (
            requestStatusValue == "approved" ||
            requestStatusValue == "on-going"
          ) {
            $("#statusInSup").addClass("bg-success");
            $("#statusInSup").addClass("text-white");
          } else if (requestStatusValue == "rejected") {
            $("#statusInSup").addClass("bg-danger");
            $("#statusInSup").addClass("text-white");
          } else if (requestStatusValue == "pending") {
            $("#statusInSup").addClass("bg-primary");
            $("#statusInSup").addClass("text-white");
          } else {
            $("#statusInSup").addClass("bg-dark");
            $("#statusInSup").addClass("text-white");
          }
          $("#statusInSup").text(result.data[0].requestStatus);
          $("#reqstatuspar").text(result.data[0].requestStatus);
          $("#datereqpar").text(result.data[0].requestDate);
          $("#manageby").text(result.data[0].manageBy);
          $(".messHeader").empty();
          $(".messHeader").show();
          $("#mess").text();

          if (result.data[0].reply != null) {
            if (result.data[0].reply.length != 0) {
              if (
                result.data[0].reply.length > 0 &&
                result.data[0].manageBy.length > 0
              ) {
                var htmlP =
                  " <p id='mess'>From:<mark>" +
                  result.data[0].name +
                  "</mark>:(" +
                  result.data[0].message +
                  ")</p>" +
                  "<p id='reply'>From:<mark>" +
                  result.data[0].manageBy +
                  "</mark>:(" +
                  result.data[0].reply +
                  ")</p>";
                $(".messHeader").append(htmlP);
              }
            } else {
              var htmlP =
                " <p id='mess'>From:<mark> " +
                result.data[0].name +
                "</mark>:(" +
                result.data[0].message +
                ")</p>";
              $(".messHeader").append(htmlP);
            }
          } else {
            var htmlP =
              " <p id='mess'>From:<mark> " +
              result.data[0].name +
              "</mark>:(" +
              result.data[0].message +
              ")</p>";
            $(".messHeader").append(htmlP);
          }
        }
        $(".reqDetailedLoaderDiv").hide();
        $(".reqDetailedLoaderDiv").remove();
        // $("#reqDetailModal").modal("toggle");
      },
      error: function (error) { },
    });
  });
});
