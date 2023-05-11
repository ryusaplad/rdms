$(document).ready(function () {
  var mainHtmlModal = "";
  var htmlModal = "";

  $(document).on("click", ".link", function (e) {
    e.preventDefault();

    $(this)
      .addClass("bg-light")
      .css(
        "box-shadow",
        "0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19)"
      )

      .on("mouseleave", function () {
        $(this).removeClass("bg-light");
        $(this).css("box-shadow", "");
      });
    $(".modalView").empty();
    var dataVal = $(this).data("value");

    $.ajax({
      type: "GET",
      url: "/svfc-admin/view/request-data?requestId=" + dataVal,
      success: function (data) {
        if (data[0].requestStatus == "pending") {
          htmlModal = "";
        } else if (data[0].requestStatus == "recordonly") {
          htmlModal += `
          <div class="card">
          <hr>
  <strong>Files</strong>
  <div class="card-body text-start">
   <div class="row">`;
          for (let dataIndex = 1; dataIndex < data.length; dataIndex++) {
            let fileName = data[dataIndex].name;
            let fileExtension = fileName.substring(fileName.lastIndexOf("."));
            if (fileName.length > 10 - fileExtension.length) {
              fileName =
                fileName.substring(0, 10 - fileExtension.length) +
                "..." +
                fileExtension;
            }

            htmlModal +=
              `
    <div  class="col m-1">
    <a data-value="${data[dataIndex].fileId}" class="btn btn-primary text-white viewFile"><i class="fas fa-eye"></i> View</a>
      <a href="/registrar/files/download?id=` +
              data[dataIndex].fileId +
              `" class="btn btn-light border border-light text-dark" title="` +
              data[dataIndex].name +
              `">
        <i class="far fa-file"></i> ` +
              fileName +
              `
      </a>
    </div>
  `;
          }
        } else if (data[0].requestStatus == "messsageonly") {
          htmlModal =
            `<h4>-- Message -- </h4><pre>` + data[0].teacherMessage + `</pre>`;
        } else if (data[0].requestStatus == "completed") {
          htmlModal =
            `<h4>-- Message -- </h4><pre>` + data[0].teacherMessage + `</pre>`;
          htmlModal += `
          <div class="card">
          <hr>
  <strong>Files</strong>
  <div class="card-body text-start">
   <div class="row">`;
          for (let dataIndex = 1; dataIndex < data.length; dataIndex++) {
            let fileName = data[dataIndex].name;
            let fileExtension = fileName.substring(fileName.lastIndexOf("."));
            if (fileName.length > 10 - fileExtension.length) {
              fileName =
                fileName.substring(0, 10 - fileExtension.length) +
                "..." +
                fileExtension;
            }

            
            htmlModal +=
              `
    <div  class="col m-1">
    <a data-value="${data[dataIndex].fileId}" class="btn btn-primary text-white viewFile"><i class="fas fa-eye"></i> View</a>
      <a href="/svfc-admin/files/download?id=` +
              data[dataIndex].fileId +
              `" class="btn btn-light border border-light text-dark" title="` +
              data[dataIndex].name +
              `">
        <i class="far fa-file"></i> ` +
              fileName +
              `
      </a>
    </div>
  `;
          }

          htmlModal += `
   </div>
  </div>
</div>
          `;
        } else {
          htmlModal =
            `<h4>-- Message -- </h4><pre>` + data[0].teacherMessage + `</pre>`;
        }

        mainHtmlModal =
          `
<div class="modal fade" id="sentReqInfo" data-bs-backdrop="static"  data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
  <div class="modal-dialog modal-xl modal-dialog-scrollable">
    <div class="modal-retent">
      <div class="modal-header">
        <h5 class="modal-title" id="sentReqInfoLabel">` +
          data[0].requestTitle +
          `</h5>
        <button type="button" class="btn-close clearModal" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body s-2">
    
          <code style="font-size:15px;">Title: ` +
          data[0].requestTitle +
          `</code>
         |
         <code style="font-size:15px;">To: ` +
          data[0].requestTo +
          `</code> 
         
          <pre style="white-space: pre-wrap; font-size:17px;"> ` +
          data[0].requestMessage +
          `</pre>` +
          htmlModal +
          `<code style="font-size:15px;">Date/Time: ` +
          data[0].requestDate +
          `</code>
         
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary clearModal" data-bs-dismiss="modal">Close</button>
     
      </div>
    </div>
  </div>
</div>`;

        $(".modalView").append(mainHtmlModal);
        $("#sentReqInfo").modal("toggle");
      },
      error: function (error) {
        console.log(error.responseText);
      },
    });
  });

  $(document).on("click", ".clearModal", function (e) {
    $(".modalView").empty();
  });


  
});
