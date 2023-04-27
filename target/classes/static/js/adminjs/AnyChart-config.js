

function isSelected(e) {
  if (!$(e).is(':disabled')) {
    $(".anychart-credits").remove();
    $("#select").hide();
    if (e !== "exporting") {
      if (e === "#all-tab") {
        $("#exporting").addClass("exportingOption");
        $("#exporting").show();
      } else {
        $("#exporting").removeClass("exportingOption");
        $("#exporting").hide();
      }
    }
  }
}


var preschoolChartData = [];
var shsChartData = [];
var highschoolChartData = [];
var collegeChartData = [];

var preschoolChart = anychart.pie();
var shsChart = anychart.pie();
var highschoolChart = anychart.pie();
var collegeChart = anychart.pie();

var all_preschoolChart = anychart.pie();
var all_shsChart = anychart.pie();
var all_highschoolChart = anychart.pie();
var all_collegeChart = anychart.pie();

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
  loadChart("Approved", modifyDate(currentDate), 0);

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
        console.log(date + " = " + modifyDate(date));
        loadChart(status, modifyDate(date), 1);
      }
    }
  });

});

function modifyDate(date) {

  const weekdays = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
  const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
  const dayOfMonth = date.getDate().toString().padStart(2, '0');
  const formattedDate = `${weekdays[date.getDay()]}, ${months[date.getMonth()]} ${dayOfMonth} ${date.getFullYear()}`;
  return formattedDate;
}

function loadDefaultPage() {



  $("#select").show();
  $("#psChart").hide();
  $("#hsChart").hide();
  $("#shsChart").hide();
  $("#collegeChart").hide();

  $("#all_psChart").hide();
  $("#all_hsChart").hide();
  $("#all_shsChart").hide();
  $("#all_collegeChart").hide();

  $("#all-tab").addClass("disabled");
  $("#ps-tab").addClass("disabled");
  $("#hs-tab").addClass("disabled");
  $("#shs-tab").addClass("disabled");
  $("#college-tab").addClass("disabled");


  $("#ps-tab").removeClass("active");
  $("#hs-tab").removeClass("active");
  $("#shs-tab").removeClass("active");
  $("#college-tab").removeClass("active");



}

function loadChart(filterValue, currentDate, action) {
  preschoolChartData = [];
  shsChartData = [];
  highschoolChartData = [];
  collegeChartData = [];

  $.ajax({
    url: "/svfc-admin/chart/data-filter?s=" + filterValue + "&d=" + currentDate, // replace with the URL to your server-side script that retrieves the data
    method: "GET",
    dataType: "json",
    success: function (data) {

      loadDefaultPage();
      var psChartToolTip = preschoolChart.tooltip();
      var collegeChartToolTip = collegeChart.tooltip();
      var shsChartToolTip = shsChart.tooltip();
      var hsChartToolTip = highschoolChart.tooltip();

      var all_psChartToolTip = all_preschoolChart.tooltip();
      var all_collegeChartToolTip = all_collegeChart.tooltip();
      var all_shsChartToolTip = all_shsChart.tooltip();
      var all_hsChartToolTip = all_highschoolChart.tooltip();

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
      preschoolChart.labels().position("outside")
      preschoolChart.title("Preschool Students");
      preschoolChart.data(preschoolChartData);
      preschoolChart.container("psChart");
      preschoolChart.legend().position("bottom");
      preschoolChart.legend().itemsLayout("vertical");

      psChartToolTip.format(
        "Value: {%value}\nStatus:{%status}\nPercentage:{%percentValue}%"
      );

      //ALl

      all_preschoolChart.labels().position("outside")
      all_preschoolChart.title("Preschool Students");
      all_preschoolChart.data(preschoolChartData);
      all_preschoolChart.container("all_psChart");
      all_preschoolChart.legend().position("bottom");
      all_preschoolChart.legend().itemsLayout("vertical");

      all_psChartToolTip.format(
        "Value: {%value}\nStatus:{%status}\nPercentage:{%percentValue}%"
      );
      ///

      highschoolChart.labels().position("outside")
      highschoolChart.title("High School Students");
      highschoolChart.data(highschoolChartData);
      highschoolChart.container("hsChart");
      highschoolChart.legend().position("bottom");
      highschoolChart.legend().itemsLayout("vertical");

      hsChartToolTip.format(
        "Value: {%value}\nStatus:{%status}\nPercentage:{%percentValue}%"
      );

      //All
      all_highschoolChart.labels().position("outside")
      all_highschoolChart.title("High School Students");
      all_highschoolChart.data(highschoolChartData);
      all_highschoolChart.container("all_hsChart");
      all_highschoolChart.legend().position("bottom");
      all_highschoolChart.legend().itemsLayout("vertical");

      all_hsChartToolTip.format(
        "Value: {%value}\nStatus:{%status}\nPercentage:{%percentValue}%"
      );
      //

      shsChart.labels().position("outside")
      shsChart.title("Senior High School Students");
      shsChart.data(shsChartData);
      shsChart.container("shsChart");
      shsChart.legend().position("bottom");
      shsChart.legend().itemsLayout("vertical");

      shsChartToolTip.format(
        "Value: {%value}\nStatus:{%status}\nPercentage:{%percentValue}%"
      );


      //All
      all_shsChart.labels().position("outside")
      all_shsChart.title("Senior High School Students");
      all_shsChart.data(shsChartData);
      all_shsChart.container("all_shsChart");
      all_shsChart.legend().position("bottom");
      all_shsChart.legend().itemsLayout("vertical");

      all_shsChartToolTip.format(
        "Value: {%value}\nStatus:{%status}\nPercentage:{%percentValue}%"
      );
      //

      collegeChart.labels().position("outside")
      collegeChart.title("College Students");
      collegeChart.data(collegeChartData);

      collegeChart.container("collegeChart");
      collegeChart.legend().position("bottom");
      collegeChart.legend().itemsLayout("vertical");

      collegeChartToolTip.format(
        "Value: {%value}\nStatus:{%status}\nPercentage:{%percentValue}%"
      );

      all_collegeChart.labels().position("outside")
      all_collegeChart.title("College Students");
      all_collegeChart.data(collegeChartData);
      all_collegeChart.container("all_collegeChart");
      all_collegeChart.legend().position("bottom");
      all_collegeChart.legend().itemsLayout("vertical");

      all_collegeChartToolTip.format(
        "Value: {%value}\nStatus:{%status}\nPercentage:{%percentValue}%"
      );


      $('.modalView').empty();
      $('.modalView').html(success_ModalContent);

      if (preschoolChart.data().getRowsCount() > 0) {
        $("#all_psChart").hide();
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
        $("#all_hsChart").hide();
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
        $("#all_shsChart").hide();
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
        $("#all_collegeChart").hide();
        $("#collegeChart").show();
        $("#college-tab").removeClass("disabled");
        collegeChart.draw();

        $(".withdata").append("<li>College</li>");
      } else {
        $(".nodata").append("<li>College</li>");
        $("#college-tab").addClass("disabled");
        $("#college-tab").removeClass("active");

      }
      //all

      if (all_preschoolChart.data().getRowsCount() != 0 ||
        all_highschoolChart.data().getRowsCount() != 0 ||
        all_shsChart.data().getRowsCount() != 0 ||
        all_collegeChart.data().getRowsCount() != 0) {

        $("#ps").removeClass("active show");
        $("#hs").removeClass("active show");
        $("#shs").removeClass("active show");
        $("#college").removeClass("active show");
        $("#all-tab").click();
        $("#all-tab").addClass("active");
        $("#all").addClass("active show");
        $("#select").hide();
        console.log("i got called");
        $("#all_hsChart").show();
        $("#all_psChart").show();
        $("#all_shsChart").show();
        $("#all_collegeChart").show();
        all_preschoolChart.draw();
        all_highschoolChart.draw();
        all_shsChart.draw();
        all_collegeChart.draw();
        $("#all-tab").removeClass("disabled");
        $("#all-tab").text($("#all-tab").text().replace(" (Not Available)", ""));
      } else {
        $("#all-tab").addClass("disabled");
        if ($("#all-tab").text().indexOf("(Not Available)") == -1) {
          $("#all-tab").text($("#all-tab").text() + " (Not Available)");
        }
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

$(document).on("click", ".exportingOption", function () {
  var htmlModal = `
<!-- Export Modal -->
<div class="modal fade" id="exportModal" tabindex="-1" aria-labelledby="exportModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exportModalLabel">Export Options</h5>
      </div>
      <div class="modal-body">
      <div class="form-group form-check">
      <input type="radio" class="form-check-input form-check-input-radio" id="allChartCheckbox" name="studentType">
      <label class="form-check-label form-check-label-radio as" for="allChartCheckbox">All Students</label>
    </div>
      <div class="form-group form-check">
  <input type="radio" class="form-check-input form-check-input-radio" id="psChartCheckbox" name="studentType">
  <label class="form-check-label form-check-label-radio ps" for="psChartCheckbox">Preschool Students</label>
</div>
<div class="form-group form-check">
  <input type="radio" class="form-check-input form-check-input-radio" id="hsChartCheckbox" name="studentType">
  <label class="form-check-label form-check-label-radio hs" for="hsChartCheckbox">High School Students</label>
</div>
<div class="form-group form-check">
  <input type="radio" class="form-check-input form-check-input-radio" id="shsChartCheckbox" name="studentType">
  <label class="form-check-label form-check-label-radio shs" for="shsChartCheckbox">Senior High School Students</label>
</div>
<div class="form-group form-check">
  <input type="radio" class="form-check-input form-check-input-radio" id="collegeChartCheckbox" name="studentType">
  <label class="form-check-label form-check-label-radio cs" for="collegeChartCheckbox">College Students</label>
</div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
        <button type="button" class="btn btn-primary exportBtn" >Export</button>
      </div>
    </div>
  </div>
</div>

`;




  $(".modalView").empty();
  $(".modalView").html(htmlModal);
  if (preschoolChartData.length == 0 || shsChartData.length == 0 || highschoolChartData.length == 0 || collegeChartData.length == 0) {
    $(".as").text($(".as").text() + " (Not Available)");
    $("#allChartCheckbox").attr("disabled",true);
  }else{
    $(".as").text($(".as").text().replace(" (Not Available)", ""));
    $("#allChartCheckbox").attr("disabled",false);
  }
  if (preschoolChartData.length == 0) {
    $(".ps").text($(".ps").text() + " (Not Available)");
    $("#psChartCheckbox").attr("disabled",true);
  } else {
    $(".ps").text($(".ps").text().replace(" (Not Available)", ""));
    $("#psChartCheckbox").attr("disabled",false);
  }
  if (highschoolChartData.length == 0) {
    $(".hs").text($(".hs").text() + " (Not Available)");
    $("#hsChartCheckbox").attr("disabled",true);
  } else {
    $(".hs").text($(".hs").text().replace(" (Not Available)", ""));
    $("#hsChartCheckbox").attr("disabled",false);
  }
  if (shsChartData.length == 0) {
    $(".shs").text($(".shs").text() + " (Not Available)");
    $("#shsChartCheckbox").attr("disabled",true);
  } else {
    $(".shs").text($(".shs").text().replace(" (Not Available)", ""));
    $("#shsChartCheckbox").attr("disabled",false);
  }

  if (collegeChartData.length == 0) {
    $(".cs").text($(".cs").text() + " (Not Available)");
    $("#collegeChartCheckbox").attr("disabled",true);
  } else {
    $(".cs").text($(".cs").text().replace(" (Not Available)", ""));
    $("#collegeChartCheckbox").attr("disabled",false);
  }
  $("#exportModal").modal("show");
});



$(document).on("click", ".exportBtn", function () {
  window.jsPDF = window.jspdf.jsPDF;
  // var exportType = $("input[name='exportType']:checked").val(); // get selected export type
  var exportType = "pdf";
  var exportStatus = [];
  var allTrue = false;
  var doc = new jsPDF();
  // check which checkboxes are checked and add corresponding charts to exportCharts array
  if ($("#allChartCheckbox").is(":checked")) {
    allTrue = true;
  }
  if ($("#psChartCheckbox").is(":checked")) {
    if (preschoolChart.data().getRowsCount() != 0) {
      if (exportType === "pdf") {
        exportStatus.push(true);
      }
    }
  } else {
    exportStatus.push(false);
  }
  if ($("#hsChartCheckbox").is(":checked")) {
    if (highschoolChart.data().getRowsCount() != 0) {
      if (exportType === "pdf") {
        exportStatus.push(true);
      }
    }
  } else {
    exportStatus.push(false);
  }
  if ($("#shsChartCheckbox").is(":checked")) {
    if (shsChart.data().getRowsCount() != 0) {
      if (exportType == "pdf") {
        exportStatus.push(true);
      }
    }

  } else {
    exportStatus.push(false);
  }
  if ($("#collegeChartCheckbox").is(":checked")) {
    if (collegeChart.data().getRowsCount() != 0) {
      if (exportType == "pdf") {
        exportStatus.push(true);
      }
    }
  } else {
    exportStatus.push(false);
  }

  var atLeastOneSelected = false;
  for (var i = 0; i < exportStatus.length; i++) {
    if (exportStatus[i] === true) {
      atLeastOneSelected = true;
      break;
    }
  }

  if (!atLeastOneSelected && !allTrue) {
    alert("Please select at least one option to export.");
    return;
  }

  if (preschoolChart.data().getRowsCount() == 0 &&
    highschoolChart.data().getRowsCount() == 0 &&
    shsChart.data().getRowsCount() == 0 &&
    collegeChart.data().getRowsCount() == 0) {
    alert("Exporting Failed, no data available.");
  } else {
    const allPromises = [];
    const singlePromises = [];





    if (allTrue && exportType === "pdf") {

      allPromises.push(
        html2canvas($("#all")[0]).then(canvas => {
          var imgData = canvas.toDataURL("image/jpeg", 1);
          console.log(canvas);
          var pageWidth = doc.internal.pageSize.getWidth();
          var pageHeight = doc.internal.pageSize.getHeight();
          var imageWidth = canvas.width;
          var imageHeight = canvas.height;

          var ratio = imageWidth / imageHeight >= pageWidth / pageHeight ? pageWidth / imageWidth : pageHeight / imageHeight;

          doc.addImage(imgData, 'JPEG', 20, 1, imageWidth * ratio, imageHeight * ratio, null, 'FAST', 0);
        })
      );

      Promise.all(allPromises).then(() => {
        doc.save("studentRequest.pdf");
      });

    } else {
      var txt = "";

      if (exportStatus[0] == true) {
        singlePromises.push(
          html2canvas($("#all_psChart")[0]).then(canvas => {
            var imgData = canvas.toDataURL("image/jpeg", 1);
            console.log(canvas);
            var pageWidth = doc.internal.pageSize.getWidth();
            var pageHeight = doc.internal.pageSize.getHeight();
            var imageWidth = canvas.width;
            var imageHeight = canvas.height;
            var ratio = imageWidth / imageHeight >= pageWidth / pageHeight ? pageWidth / imageWidth : pageHeight / imageHeight;


            doc.addImage(imgData, 'JPEG', 0, 1, imageWidth * ratio, imageHeight * ratio, null, 'FAST', 0);
            txt = "PreSchool";
          })
        );
      } if (exportStatus[1] == true) {
        singlePromises.push(
          html2canvas($("#all_hsChart")[0]).then(canvas => {
            var imgData = canvas.toDataURL("image/jpeg", 1);
            console.log(canvas);
            var pageWidth = doc.internal.pageSize.getWidth();
            var pageHeight = doc.internal.pageSize.getHeight();
            var imageWidth = canvas.width;
            var imageHeight = canvas.height;
            var ratio = imageWidth / imageHeight >= pageWidth / pageHeight ? pageWidth / imageWidth : pageHeight / imageHeight;


            doc.addImage(imgData, 'JPEG', 0, 1, imageWidth * ratio, imageHeight * ratio, null, 'FAST', 0);
            txt += "HighSchool";
          })
        );
      } if (exportStatus[2] == true) {
        singlePromises.push(
          html2canvas($("#all_shsChart")[0]).then(canvas => {
            var imgData = canvas.toDataURL("image/jpeg", 1);
            console.log(canvas);
            var pageWidth = doc.internal.pageSize.getWidth();
            var pageHeight = doc.internal.pageSize.getHeight();
            var imageWidth = canvas.width;
            var imageHeight = canvas.height;
            var ratio = imageWidth / imageHeight >= pageWidth / pageHeight ? pageWidth / imageWidth : pageHeight / imageHeight;


            doc.addPage();
            doc.addImage(imgData, 'JPEG', 0, 1, imageWidth * ratio, imageHeight * ratio, null, 'FAST', 0);
            txt += "SeniorSchool";
          })
        );
      } if (exportStatus[3] == true) {
        singlePromises.push(
          html2canvas($("#all_collegeChart")[0]).then(canvas => {
            var imgData = canvas.toDataURL("image/jpeg", 1);
            console.log(canvas);

            var pageWidth = doc.internal.pageSize.getWidth();
            var pageHeight = doc.internal.pageSize.getHeight();
            var imageWidth = canvas.width;
            var imageHeight = canvas.height;
            var ratio = imageWidth / imageHeight >= pageWidth / pageHeight ? pageWidth / imageWidth : pageHeight / imageHeight;

            doc.addImage(imgData, 'JPEG', 0, 1, imageWidth * ratio, imageHeight * ratio, null, 'FAST', 0);

            txt += "College";
          })
        );

      }
      Promise.all(singlePromises).then(() => {
        doc.save(txt + "_chart.pdf");
      });

    }

  }


});


function changeTabText() {
  if (preschoolChart.data().getRowsCount() == 0) {

    if ($("#ps-tab").text().indexOf("(Not Available)") == -1) {
      $("#ps-tab").text($("#ps-tab").text() + " (Not Available)");
    }
  } else {
    $("#ps-tab").text($("#ps-tab").text().replace(" (Not Available)", ""));
  }

  if (highschoolChart.data().getRowsCount() == 0) {
    if ($("#hs-tab").text().indexOf("(Not Available)") == -1) {
      $("#hs-tab").text($("#hs-tab").text() + " (Not Available)");
    }
  } else {
    $("#hs-tab").text($("#hs-tab").text().replace(" (Not Available)", ""));
  }

  if (shsChart.data().getRowsCount() == 0) {
    if ($("#shs-tab").text().indexOf("(Not Available)") == -1) {
      $("#shs-tab").text($("#shs-tab").text() + " (Not Available)");
    }
  } else {
    $("#shs-tab").text($("#shs-tab").text().replace(" (Not Available)", ""));
  }


  if (collegeChart.data().getRowsCount() == 0) {
    if ($("#college-tab").text().indexOf("(Not Available)") == -1) {
      $("#college-tab").text($("#college-tab").text() + " (Not Available)");
    }
  } else {
    $("#college-tab").text($("#college-tab").text().replace(" (Not Available)", ""));
  }
}
