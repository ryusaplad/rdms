function isSelected() {
  $(this).click((event) => {
    var id = event.target.id;
    $(".anychart-credits").remove();
    $("#select").hide();
  });
}


var preschoolChartData = [];
var shsChartData = [];
var highschoolChartData = [];
var collegeChartData = [];
var preschoolChart = anychart.pie();
var shsChart = anychart.pie();
var highschoolChart = anychart.pie();
var collegeChart = anychart.pie();

var error_modalContent = `
<div class="modal fade" id="errorModal" tabindex="-1" role="dialog" aria-labelledby="errorModalLabel" aria-hidden="true"  data-bs-backdrop="static"
data-bs-keyboard="false">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="errorModalLabel"><i class="fa fa-exclamation-triangle text-danger mr-2"></i>Error</h5>
        <button
                                type="button"
                                class="btn-close"
                                data-bs-dismiss="modal"
                                aria-label="Close"
                            ></button>
      </div>
      <div class="modal-body">
        Please select a valid date and status.<br>
        Please enter a valid date in the format YYYY-MM-DD.
      </div>
    </div>
  </div>
</div>
`;

var success_ModalContent = `<div class="modal fade" id="successModal" tabindex="-1" role="dialog" aria-labelledby="successModalLabel" aria-hidden="true"  data-bs-backdrop="static" data-bs-keyboard="false">
<div class="modal-dialog" role="document">
  <div class="modal-content">
    <div class="modal-header">
      <h5 class="modal-title" id="successModalLabel"><i class="fa fa-check-circle text-success mr-2"></i>Filtered Results</h5>
      <button
      type="button"
      class="btn-close"
      data-bs-dismiss="modal"
      aria-label="Close"
  ></button>
    </div>
    <div class="modal-body">
    <p>Data has been found in the following:</p>
    <ul class="withdata">
   
  </ul>
  <p>No Data have been found in the following:</p>
  <ul class="nodata">
</ul>
<div class="alert alert-info" role="alert">
  <p class="mb-0">Note: You can find data in other date or status.</p>
</div>
    </div>
  </div>
</div>
</div>
`;
$(document).ready(function () {

  var currentDate = new Date();
  const year = currentDate.getFullYear();
  let month = currentDate.getMonth() + 1;
  if (month < 10) {
    month = '0' + month;
  }
  let day = currentDate.getDate();
  if (day < 10) {
    day = '0' + day;
  }
  const formattedDate = `${year}-${month}-${day}`;
  $(".date").val(formattedDate);
  $(".status").val("Approved")
  currentDate = new Date($(".date").val());
  loadChart("Approved", modifyDate(currentDate),0);

  $(".filterButton").on("click", function () {
    const status = $(".status").val();
    const dateValue = $(".date").val();

    if (dateValue.trim() === '' || status === "Status") {
      $('.modalView').html(error_modalContent);
      $('#errorModal').modal('show');
      setTimeout(function () {
        $('#errorModal').modal('hide');
      }, 5000);
    } else {
      const date = new Date(dateValue);
      if (isNaN(date.getTime())) {
        $('.modalView').html(error_modalContent);
        $('#errorModal').modal('show');
        $('.modalView').empty();
        setTimeout(function () {
          $('#errorModal').modal('hide');
          $('.modalView').empty();
        }, 5000);
      } else {
        loadChart(status, modifyDate(date),1);
      }
    }
  });

});

function modifyDate(date) {

  const weekdays = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
  const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
  const formattedDate = `${weekdays[date.getDay()]}, ${months[date.getMonth()]} ${date.getDate()} ${date.getFullYear()}`;
  return formattedDate;
}

function loadDefaultPage() {
  $("#select").show();
  $("#psChart").hide();
  $("#hsChart").hide();
  $("#shsChart").hide();
  $("#collegeChart").hide();

  $("#ps-tab").addClass("disabled");
  $("#hs-tab").addClass("disabled");
  $("#shs-tab").addClass("disabled");
  $("#college-tab").addClass("disabled");
}

function loadChart(filterValue, currentDate, action) {
  preschoolChartData = [];
  shsChartData = [];
  highschoolChartData = [];
  collegeChartData = [];
  var text = "";
  preschoolChart = anychart.pie();
  shsChart = anychart.pie();
  highschoolChart = anychart.pie();
  collegeChart = anychart.pie();
  $.ajax({
    url: "/admin/chart/data-filter?s=" + filterValue + "&d=" + currentDate, // replace with the URL to your server-side script that retrieves the data
    method: "GET",
    dataType: "json",
    success: function (data) {

      loadDefaultPage();
      var psChartToolTip = preschoolChart.tooltip();
      var collegeChartToolTip = collegeChart.tooltip();
      var shsChartToolTip = shsChart.tooltip();
      var hsChartToolTip = highschoolChart.tooltip();

      // create the charts
      $.each(data, function (n, value) {
        if (value[4] == "Preschool") {
          preschoolChartData.push({
            x: value[1],
            value: value[3],
            status: value[2],
          });
        }
        if (value[4] == "Highschool") {
          highschoolChartData.push({
            x: value[1],
            value: value[3],
            status: value[2],
          });
        }

        if (value[4] == "SHS") {
          shsChartData.push({ x: value[1], value: value[3], status: value[2] });
        }
        if (value[4] == "College") {
          collegeChartData.push({
            x: value[5],
            value: value[3],
            status: value[2],
          });
        }
      });
      preschoolChart.title("Preschool Students");
      preschoolChart.data(preschoolChartData);
      preschoolChart.container("psChart");
      preschoolChart.legend().position("right");
      preschoolChart.legend().itemsLayout("vertical");

      psChartToolTip.format(
        "Value: {%value}\nStatus:{%status}\nPercentage:{%percentValue}%"
      );

      highschoolChart.title("High School Students");
      highschoolChart.data(highschoolChartData);
      highschoolChart.container("hsChart");
      highschoolChart.legend().position("right");
      highschoolChart.legend().itemsLayout("vertical");

      hsChartToolTip.format(
        "Value: {%value}\nStatus:{%status}\nPercentage:{%percentValue}%"
      );

      shsChart.title("Senior High School Students");
      shsChart.data(shsChartData);

      shsChart.container("shsChart");
      shsChart.legend().position("right");
      shsChart.legend().itemsLayout("vertical");

      shsChartToolTip.format(
        "Value: {%value}\nStatus:{%status}\nPercentage:{%percentValue}%"
      );

      collegeChart.title("College Students");
      collegeChart.data(collegeChartData);

      collegeChart.container("collegeChart");
      collegeChart.legend().position("right");
      collegeChart.legend().itemsLayout("vertical");

      collegeChartToolTip.format(
        "Value: {%value}\nStatus:{%status}\nPercentage:{%percentValue}%"
      );


      $('.modalView').empty();
      $('.modalView').html(success_ModalContent);

      if (preschoolChart.data().getRowsCount() > 0) {
        $("#psChart").show();
        $("#ps-tab").removeClass("disabled");
        preschoolChart.draw();
        $(".withdata").append("<li>Pre-School</li>");
      } else {
        $(".nodata").append("<li>Pre-School</li>");
        $("#ps-tab").addClass("disabled");
        $("#ps-tab").removeClass("active");
      }

      if (highschoolChart.data().getRowsCount() > 0) {
        $("#hsChart").show();
        $("#hs-tab").removeClass("disabled");
        highschoolChart.draw();
        $(".withdata").append("<li>High School</li>");
      } else {
        $(".nodata").append("<li>High School</li>");
        $("#hs-tab").addClass("disabled");
        $("#hs-tab").removeClass("active");
      }
      if (shsChart.data().getRowsCount() > 0) {
        $("#shsChart").show();
        $("#shs-tab").removeClass("disabled");
        shsChart.draw();
        $(".withdata").append("<li>Senior High School</li>");
      } else {
        $(".nodata").append("<li>Senior High School</li>");
        $("#shs-tab").addClass("disabled");
        $("#shs-tab").removeClass("active");
      }
      if (collegeChart.data().getRowsCount() > 0) {
        $("#collegeChart").show();
        $("#college-tab").removeClass("disabled");
        collegeChart.draw();

        $(".withdata").append("<li>College</li>");
      } else {
        $(".nodata").append("<li>College</li>");
        $("#college-tab").addClass("disabled");
        $("#college-tab").removeClass("active");

      }
      changeTabText();

      if (action == 1) {
        $('#successModal').modal('show');
      }

      $(".anychart-credits").remove();
    },
    error: function (xhr, status, error) {
      loadDefaultPage();
      changeTabText();
      if (action == 1) {
        $('.modalView').empty();
        $('.modalView').html(success_ModalContent);

        $(".nodata").append("<li>Pre-School</li>");
        $(".nodata").append("<li>High School</li>");
        $(".nodata").append("<li>Senior High School</li>");
        $(".nodata").append("<li>College</li>");

        $('#successModal').modal('show');
      }
      $(".anychart-credits").remove();

    },
  });
}

function changeTabText() {
  if (preschoolChart.data().getRowsCount() == 0) {

    $("#ps-tab").text($("#ps-tab").text().replace(" (Empty)", "") + " (Empty)");
  } else {
    $("#ps-tab").text($("#ps-tab").text().replace(" (Empty)", ""));
  }

  if (highschoolChart.data().getRowsCount() == 0) {
    $("#hs-tab").text($("#hs-tab").text().replace(" (Empty)", "") + " (Empty)");
  } else {
    $("#hs-tab").text($("#hs-tab").text().replace(" (Empty)", ""));
  }

  if (shsChart.data().getRowsCount() == 0) {
    $("#shs-tab").text(
      $("#shs-tab").text().replace(" (Empty)", "") + " (Empty)"
    );
  } else {
    $("#shs-tab").text($("#shs-tab").text().replace(" (Empty)", ""));
  }

 
  if (collegeChart.data().getRowsCount() == 0) {
    $("#college-tab").text(
      $("#college-tab").text().replace(" (Empty)", "") + " (Empty)"
    );
  } else {
    $("#college-tab").text($("#college-tab").text().replace(" (Empty)", ""));
  }
}
