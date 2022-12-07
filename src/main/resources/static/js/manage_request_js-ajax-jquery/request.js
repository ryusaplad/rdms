$(document).ready(function () {
  $("#files").change(function () {
    $("#fileInfoTable").empty();
    var totalFiles = $("#files")[0].files.length;
    for (var x = 0; x < totalFiles; x++) {
      var fileName = $("#files")[0].files[x].name;
      var split = fileName.split(".");
      fileName = split[0];
      var extension = split[1];
      if (fileName.length > 10) {
        fileName = fileName.substring(0, 10);
      }
      var name = fileName + "." + extension;
      $("#fileInfoTable").append(
        "'<tr>'" +
          "<td>" +
          "<button type = 'button' class='btn removeItemFile btn-outline-danger'>Delete</button>" +
          "</td>" +
          "<td>" +
          name +
          "</td >" +
          "<td>" +
          formatFileSize($("#files")[0].files[x].size) +
          "</td >" +
          "</tr >"
      );
    }

    function formatFileSize(bytes) {
      var decimalPoint = 0;
      if (bytes == 0) return "0 Bytes";
      var k = 1000,
        dm = decimalPoint || 2,
        sizes = ["Bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"],
        i = Math.floor(Math.log(bytes) / Math.log(k));
      return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + " " + sizes[i];
    }
    $("#fileForm").submit();
  });

  $(document).on("click", ".removeItemFile", function () {
    const fileListArr = Array.from($("#files")[0].files);
    const selectedIndex = $(this).closest("tr").index();
    console.log("You Removed");

    $(this).closest("tr").remove();
  });
  $("#fileForm").submit(function (event) {
    event.preventDefault();

    $.ajax({
      url: "/save_file",
      type: "POST",
      data: new FormData(this),
      enctype: "multipart/form-data",
      processData: false,
      contentType: false,
      cache: false,
      beforeSend: function () {
        $(".saveFile").attr("disabled", "disabled");
        console.log("Please Wait");
      },
      success: function (res) {
        console.log(res);
      },
      error: function (err) {
        console.error(err);
      },
    });
  });
});
