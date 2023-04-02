
function isSelected() {
  $(this).click(function () {
    $("#select").remove();
    $(".anychart-credits").remove();
  });

}

function filterChart(e) {
  $(document).on("click", "." + e, function () {
    var status = $(this).text();
    $("#psChart").empty();
    $("#hsChart").empty();
    $("#shsChart").empty();
    $("#collegeChart").empty();
    loadChart(status);
   
  });
}


$(document).ready(function () {
 
  loadChart("Approved");
});
 function loadChart(filterValue) {
  var preschoolChartData = [];
var shsChartData = [];
var highschoolChartData = [];
var collegeChartData = [];
    $.ajax({
      url: '/admin/chart/data-filter?s=' + filterValue, // replace with the URL to your server-side script that retrieves the data
      method: 'GET',
      dataType: 'json',
      success: function (data) {
        var preschoolChart = anychart.pie();
        var shsChart = anychart.pie();
        var highschoolChart = anychart.pie();
        var collegeChart = anychart.pie();

        var psChartToolTip = preschoolChart.tooltip();
        var collegeChartToolTip = collegeChart.tooltip();
        var shsChartToolTip = shsChart.tooltip();
        var hsChartToolTip = highschoolChart.tooltip();

        // create the charts
        $.each(data, function (n, value) {

          if (value[4] == "Preschool") {

            preschoolChartData.push({ x: value[1], value: value[3], status: value[2] });

          }
          if (value[4] == "Highschool") {
            highschoolChartData.push({ x: value[1], value: value[3], status: value[2] });

          }

          if (value[4] == "SHS") {

            shsChartData.push({ x: value[1], value: value[3], status: value[2] });
          }
          if (value[4] == "College") {
            collegeChartData.push({ x: value[5], value: value[3], status: value[2] });
          }
        });
        preschoolChart.title("Preschool Students");
        preschoolChart.data(preschoolChartData);
        preschoolChart.container('psChart');
        preschoolChart.legend().position("right");
        preschoolChart.legend().itemsLayout("vertical");


        psChartToolTip.format("Value: {%value}\nStatus:{%status}\nPercentage:{%percentValue}%");

        preschoolChart.draw();

        highschoolChart.title("High School Students");
        highschoolChart.data(highschoolChartData);
        highschoolChart.container('hsChart');
        highschoolChart.legend().position("right");
        highschoolChart.legend().itemsLayout("vertical");


        hsChartToolTip.format("Value: {%value}\nStatus:{%status}\nPercentage:{%percentValue}%");

        highschoolChart.draw();


        shsChart.title("Senior High School Students");
        shsChart.data(shsChartData);


        shsChart.container('shsChart');
        shsChart.legend().position("right");
        shsChart.legend().itemsLayout("vertical");


        shsChartToolTip.format("Value: {%value}\nStatus:{%status}\nPercentage:{%percentValue}%");

        shsChart.draw();

        collegeChart.title("College Students");
        collegeChart.data(collegeChartData);


        collegeChart.container('collegeChart');
        collegeChart.legend().position("right");
        collegeChart.legend().itemsLayout("vertical");


        collegeChartToolTip.format("Value: {%value}\nStatus:{%status}\nPercentage:{%percentValue}%");

        collegeChart.draw();
        $(".anychart-credits").remove();
      },
      error: function (xhr, status, error) {
        console.log(error);
      }


    });
  }