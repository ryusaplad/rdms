$(document).ready(function () {
  $(document).on("click", ".link", function (e) {
    e.preventDefault();

    var htmlModal = "";
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
      url: "/registrar/sent-requests-view?requestId=" + dataVal,
      success: function (data) {
        htmlModal =
          `
<div class="modal fade" id="sentReqInfo" data-bs-backdrop="static"  data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
  <div class="modal-dialog modal-xl modal-dialog-scrollable">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="sentReqInfoLabel">` +
          data.requestTitle +
          `</h5>
        <button type="button" class="btn-close clearModal" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body s-2">
    
          <code>Title: ` +
          data.requestTitle +
          `</code>
         |
         <code>From: ` +
          data.requestBy +
          `</code> 
         
          |<code>To: ` +
          data.requestTo +
          `</code>
          <pre style="white-space: pre-wrap;"> ` +
          data.requestMessage +
          `</pre>

          <div class="card">
          <hr>
  <strong>Files</strong>
  <div class="card-body text-start">
   <div class="row">
   <div class="col">
  
   <a class="btn btn-light border border-light text-dark"href="#">
    <i class="far fa-file"></i> Lol.pdf
   </a>
   </div>
   </div>
  </div>
</div>
           <code>Date/Time: ` +
          data.requestDate +
          `</code>
         
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary clearModal" data-bs-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>`;

        $(".modalView").append(htmlModal);
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
