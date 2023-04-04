function isSelected() {
  $(this).click((event) => {
    var id = event.target.id;
    $(".anychart-credits").remove();

    if (id == "ps-tab") {
    }
  });
}

$(document).on("change", ".filterChart", function () {
  var status = $(".filterChart").val();
  $("#psChart").empty();
  $("#hsChart").empty();
  $("#shsChart").empty();
  $("#collegeChart").empty();

  $("#ps-tab").removeClass("active");
  $("#hs-tab").removeClass("active");
  $("#shs-tab").removeClass("active");
  $("#college-tab").removeClass("active");
  loadChart(status);
});

var preschoolChart = anychart.pie();
var shsChart = anychart.pie();
var highschoolChart = anychart.pie();
var collegeChart = anychart.pie();
$(document).ready(function () {
  loadChart("Approved");
});

function loadDefaultPage(pageCount) {
  $("#psChart").hide();
  $("#hsChart").hide();
  $("#shsChart").hide();
  $("#collegeChart").hide();

  $("#ps-tab").addClass("disabled");
  $("#hs-tab").addClass("disabled");
  $("#shs-tab").addClass("disabled");
  $("#college-tab").addClass("disabled");
}

function loadChart(filterValue) {
  var preschoolChartData = [];
  var shsChartData = [];
  var highschoolChartData = [];
  var collegeChartData = [];

  preschoolChart = anychart.pie();
  shsChart = anychart.pie();
  highschoolChart = anychart.pie();
  collegeChart = anychart.pie();
  $.ajax({
    url: "/admin/chart/data-filter?s=" + filterValue, // replace with the URL to your server-side script that retrieves the data
    method: "GET",
    dataType: "json",
    success: function (data) {
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

      if (preschoolChart.data().getRowsCount() > 0) {
        $("#psChart").show();
        $("#ps-tab").removeClass("disabled");

        preschoolChart.draw();
      } else {
        $("#ps-tab").addClass("disabled");
        $("#ps-tab").removeClass("active");
      }

      if (highschoolChart.data().getRowsCount() > 0) {
        $("#hsChart").show();
        $("#hs-tab").removeClass("disabled");
        highschoolChart.draw();
      } else {
        $("#hs-tab").addClass("disabled");
        $("#hs-tab").removeClass("active");
      }
      if (shsChart.data().getRowsCount() > 0) {
        $("#shsChart").show();
        $("#shs-tab").removeClass("disabled");
        shsChart.draw();
      } else {
        $("#shs-tab").addClass("disabled");
        $("#shs-tab").removeClass("active");
      }
      if (collegeChart.data().getRowsCount() > 0) {
        $("#collegeChart").show();
        $("#college-tab").removeClass("disabled");
        collegeChart.draw();
      } else {
        $("#college-tab").addClass("disabled");
        $("#college-tab").removeClass("active");
      }
      changeTabText();
      $("#select").hide();
      $(".anychart-credits").remove();
    },
    error: function (xhr, status, error) {
      loadDefaultPage();
      changeTabText();
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

  console.log(collegeChart.data().getRowsCount());
  if (collegeChart.data().getRowsCount() == 0) {
    $("#college-tab").text(
      $("#college-tab").text().replace(" (Empty)", "") + " (Empty)"
    );
  } else {
    $("#college-tab").text($("#college-tab").text().replace(" (Empty)", ""));
  }
}
