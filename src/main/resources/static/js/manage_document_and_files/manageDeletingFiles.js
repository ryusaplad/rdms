$(document).ready(function () {
  var modalView = $(".modalView");
  var loc = window.location.href;
  var accountType = "";
  if (loc.includes("admin")) {
    accountType = "svfc-admin";
  }
  $(document).on("click", ".deleteFile", function (event) {
    event.preventDefault();




    var fileId = $(this).data("value");
    modalView.empty();
    modalView.append(`<div class="modal fade" id="deletionModal" tabindex="-1" aria-labelledby="deletionModalLabel" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="deletionModalLabel">Modal title</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
              Are you sure you want to delete this data?
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary clearDeleteModal" data-bs-dismiss="modal">Cancel</button>
              <button type="button" class="btn btn-primary deleteConfirm">Confirm</button>
            </div>
          </div>
        </div>
      </div>`);
    $('.deleteConfirm').attr('data-value', fileId);
    $("#deletionModal").modal("toggle");
  });

  $(document).on("click", ".clearDeleteModal", function () {
    $("#deletionModal").modal("hide");
    modalView.empty();
  });

  $(document).on("click", ".deleteConfirm", function () {
    var fileId = $(this).data("value");
    $.ajax({
      url: '/' + accountType + '/delete/file?id=' + fileId,
      type: 'GET',
      success: function (response) {
        console.log('File deleted');
      },
      error: function (error) {
        console.error(error);
      }
    });
  });

  loadUserFiles();
  function loadUserFiles() {

    
    $.ajax({
      url: `/${accountType}/load/userfiles`,
      type: 'GET',
      success: function (response) {
        console.log(response);
      },
      error: function (error) {
        console.error(error);
      }
    });
  }
});