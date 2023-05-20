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
    var message = "";
    if (accountType == "svfc-admin") {
      message = `Deleting this data will permanently remove it from the system. This action cannot be undone.
       Please note that any users who have uploaded or have access to this data will no longer be able to view or download it.`;
    }
    modalView.append(`<div class="modal fade" id="deletionModal" tabindex="-1" aria-labelledby="deletionModalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="deletionModalLabel"><i class="fa fa-exclamation-triangle text-warning" aria-hidden="true"></i>Delition Warning</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
        <p><strong class="bg-warning">Warning:</strong>${message}</p>
        <p><em>Are you absolutely sure you want to proceed with the deletion?</em></p>
          
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
    var successModal = `
    <div class="modal fade" id="successModal" tabindex="-1" aria-labelledby="successModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="successModalLabel">File Deleted</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body text-center">
            <i class="fas fa-check-circle fa-4x text-success successCheck"></i>
            <p class="mt-3">The file has been successfully deleted.</p>
          </div>
        </div>
      </div>
    </div>`;
    var fileId = $(this).data("value");
    $.ajax({
      url: '/' + accountType + '/delete/file?id=' + fileId,
      type: 'GET',
      success: function (response) {
        modalView.append(successModal);
        $('#deletionModal').modal('hide');

        $('#successModal').modal('show');
        // Animate checkmark icon
        setTimeout(function () {
          $('.successCheck').addClass('animate__animated animate__heartBeat');
          $('#successModal').modal('hide');
          modalView.empty();

        }, 1500);
        // Refresh File table
        loadUserFiles();
      },
      error: function (error) {
        console.error(error);
      }
    });
  });

  // Refresh File table
  loadUserFiles();
  function loadUserFiles() {


    $.ajax({
      url: `/${accountType}/load/userfiles`,
      type: 'GET',
      success: function (data) {
        var table = $("#zero_config").DataTable({
          "ordering": true,
          "destroy": true,
          "order": [[1, "asc"]],
          columnDefs: [
            { targets: [0], orderable: false }]
        });
        var action = "";
        table.clear();
        console.log(data.length)
        for (let i = 0; i < data.length; i++) {
          var file = data[i];


          action = `<div class="btn-group row">
          <div class="col btn-group">
              <a class="btn btn-primary text-white viewFile" data-value="${file.fileId}"><i class="fas fa-eye"></i> View</a>
          </div>
          <div class="col btn-group">
              <a href="/school_admin/files/download?id=${file.fileId}" type="button" class="btn btn-secondary">
              <i class="fas fa-download"></i> Download
              </a>
          </div>
          <div class="col btn-group">
              <a type="button" data-value="${file.fileId}" class="btn btn-danger mt-1 text-white"><i class="fas fa-trash"></i> Delete</a>
          </div>
      </div>`;

          $("#zero_config")
            .DataTable()
            .row.add([
              action,
              file.name,
              file.uploadedBy,
              file.size
            ])
            .draw();

        }

      },
      error: function (error) {
        console.error(error);
      }
    });
  }
});