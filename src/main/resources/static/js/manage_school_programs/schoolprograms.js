$(document).ready(function () {
  var loc = window.location.href;
  var id = "";
  var type = "";
  var url = "";
  var message = "";
  if (loc.includes("/registrar/")) {
    type = "registrar";
  } else if (loc.includes("/svfc-admin")) {
    type = "svfc-admin";
  }
  refreshTable();

  $(document).on('click', ".updateSchoolProgram", function (event) {
    url = `/${type}/update/school-program-information/${id}`
    message = "School Program Updated";
    $('#manage-school-form').submit();
  });
  $(document).on('click', ".submitSchoolProgram", function (event) {
    url = `/${type}/save/school-program-information`
    message = "School Program Inserted";
    $('#manage-school-form').submit();
  });
  $('#manage-school-form').on('submit', function (event) {
    event.preventDefault();
    event.stopPropagation();

    var form = $(this);
    if (form[0].checkValidity() === false) {
      form.addClass('was-validated');
    } else {

      var formData = form.serialize();

      // Send an AJAX request
      $.ajax({
        type: 'POST',
        url: url,
        data: formData,
        success: function (response) {

          // Show the success alert
          $('#form-alert').addClass('alert-success');
          $('#form-alert').find('p').text(message).end().fadeIn();
          setTimeout(() => {
            $('#form-alert').fadeOut();
          }, 2000);
          refreshTable();
        },
        error: function (xhr, textStatus, errorThrown) {
          // Handle the error response
          console.log(xhr.responseText);
        }
      });
    }
  });

  function refreshTable() {
    $.ajax({
      type: 'GET',
      url: `/${type}/load/school-program-informations`,
      success: function (data) {
        var table = $("#zero_config").DataTable();
        table.clear();
        var status = "";
        var hideBtnText = "";
        var hideBtnClass = "";
        var hideBtnIcon = "";
        for (var i = 0; i < data.length; i++) {
          var row = data[i];


          if (row.status.toLowerCase() === ("available")) {
            status = ` <td> <strong class="btn btn-outline-success" > <i class="fas fa-check-circle text-success" aria-hidden="true"></i> Available</strong></td>`;
            hideBtnText = "Hide";
            hideBtnClass = "hide-button";
            hideBtnIcon = "fa fa-eye-slash";
          } else if (row.status.toLowerCase() === ("not available")) {
            status = ` <td> <strong class="btn btn-outline-danger" > <i class="fas fa-times-circle text-danger" aria-hidden="true"></i> Not Available</strong></td>`;
            hideBtnText = "Un-Hide"
            hideBtnClass = "show-button";

            hideBtnIcon = "fa fa-eye";
          }
          var buttons = `<td>
          <!-- Action buttons for delete and hide -->
          <button class="btn btn-primary btn-sm edit-button" data-value="${row.id}" title="Edit">
          <i class="fas fa-edit"></i>
          Edit
        </button>
          <button class="btn btn-danger btn-sm delete-button" data-value="${row.id}" title="Delete">
            <i class="fas fa-trash"></i>
            Delete
          </button>
          <button class="btn btn-secondary btn-sm ${hideBtnClass}" data-value="${row.id}" title="Hide">
            <i class="${hideBtnIcon}"></i>
           ${hideBtnText}
          </button>
        </td>`;
          table.row.add([row.level.toUpperCase(), row.code, row.name, status, buttons]);
        }


        table.draw();

        $(".clearBtn").click();

      },
      error: function (xhr, textStatus, errorThrown) {

        console.log(xhr.responseText);
      }
    });
  }

  $(document).on('click', '.edit-button', function () {
    id = $(this).data('value')
    $.ajax({
      type: 'GET',
      url: `/${type}/load/school-program-informations/${id}`,
      success: function (data) {
        $("#courseCode").val(data.code);
        $("#course").val(data.name);
        $('#educationLevel').val(data.level.toLowerCase()).change();
        $(".submitSchoolProgram").addClass("updateSchoolProgram");
        $(".updateSchoolProgram").removeClass("submitSchoolProgram");
        $(".updateSchoolProgram").text("Update");
        $(".clearBtn").text("Cancel");

        var toastHtml = `
        <div class="position-fixed top-0 end-0 p-3" style="z-index: 50">
          <div id="liveToast" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="toast-header"> <i class="fas fa-check-circle text-success" aria-hidden="true"></i>
              <strong class="me-auto">Successful Program Information Retrieval</strong>
              <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
            <div class="toast-body">
            School program successfully retrieved.
            </div>
          </div>
        </div>`;
        $(".modalView").empty();
        $(".modalView").append(toastHtml);
        var toast = new bootstrap.Toast($('#liveToast'));
        toast.show();


      },
      error: function (xhr, textStatus, errorThrown) {
        if (xhr.includes("<!DOCTYPE html>")) {
          xhr.responseText = "Failed to retrieve school program informations, Please try again.";

        }
        var toastHtml = `
        <div class="position-fixed top-0 end-0 p-3" style="z-index: 50">
          <div id="liveToast" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="toast-header"> <i class="fas fa-check-circle text-success" aria-hidden="true"></i>
              <strong class="me-auto">Failed Program Information Retrieval</strong>
              <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
            <div class="toast-body">
            ${xhr.responseText}
            </div>
          </div>
        </div>`;
        $(".modalView").empty();
        $(".modalView").append(toastHtml);
        var toast = new bootstrap.Toast($('#liveToast'));
        toast.show();
      }
    });

  });


  $(document).on('click', '.delete-button', function () {
    var id = $(this).data('value');

    // Clear the modal view
    $(".modalView").empty();

    // Confirmation modal HTML
    var htmlModal = `
      <div class="modal fade" id="confirmModal" tabindex="-1" aria-labelledby="confirmModalLabel" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="confirmModalLabel">Confirmation</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
              Are you sure you want to delete this school program?
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
              <button type="button" class="btn btn-danger" id="confirmDeleteBtn">Delete</button>
            </div>
          </div>
        </div>
      </div>`;

    // Append the confirmation modal to the modal view
    $(".modalView").append(htmlModal);

    // Show the confirmation modal
    $("#confirmModal").modal('show');

    // Delete button click event inside the confirmation modal
    $("#confirmDeleteBtn").click(function () {
      // Send an AJAX request to delete the school program
      $.ajax({
        type: 'POST',
        url: `/${type}/delete/school-program-information/${id}`,
        success: function (response) {
          // Show the success toast
          var toastHtml = `
            <div class="position-fixed top-0 end-0 p-3" style="z-index: 50">
              <div id="liveToast" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
                <div class="toast-header">
                <i class="fas fa-trash"></i> <strong class="me-auto">Program Status</strong>
                  <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
                </div>
                <div class="toast-body">
                  School program deleted
                </div>
              </div>
            </div>`;

          // Clear the modal view
          $(".modalView").empty();

          // Append the toast HTML to the modal view
          $(".modalView").append(toastHtml);

          // Show the toast
          var toast = new bootstrap.Toast($('#liveToast'));
          toast.show();

          // Refresh the table
          refreshTable();
        },
        error: function (xhr, textStatus, errorThrown) {
          // Handle the error response
          console.log(xhr.responseText);
        }
      });

      // Hide the confirmation modal
      $("#confirmModal").modal('hide');
    });
  });


  $(document).on('click', '.hide-button', function () {
    var id = $(this).data('value');

    // Send an AJAX request to update the program status
    $.ajax({
      type: 'POST',
      url: `/${type}/update/school-program-status/${id}/not%20available`, // Assuming "not available" is the desired status
      success: function (response) {
        // Show the success alert or perform any other actions
        var toastHtml = `
        <div class="position-fixed top-0 end-0 p-3" style="z-index: 50">
          <div id="liveToast" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="toast-header"> <i class="fas fa-eye-slash"></i>
              <strong class="me-auto">Program Status</strong>
              <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
            <div class="toast-body">
              School program hidden
            </div>
          </div>
        </div>`;
        $(".modalView").empty();
        $(".modalView").append(toastHtml);
        var toast = new bootstrap.Toast($('#liveToast'));
        toast.show();

        refreshTable(); // Refresh the table to reflect the updated status
      },
      error: function (xhr, textStatus, errorThrown) {
        // Handle the error response
        console.log(xhr.responseText);
      }
    });
  });

  $(document).on('click', '.show-button', function () {
    var id = $(this).data('value');

    // Send an AJAX request to update the program status
    $.ajax({
      type: 'POST',
      url: `/${type}/update/school-program-status/${id}/available`, // Assuming "not available" is the desired status
      success: function (response) {
        // Show the success alert or perform any other actions
        var toastHtml = `<div class="position-fixed top-0 end-0 p-3" style="z-index: 50">
        <div id="liveToast" class="toast hide" role="alert" aria-live="assertive" aria-atomic="true">
          <div class="toast-header">
          <i class="fas fa-eye"></i> <strong class="me-auto">Program Status</strong>
            <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
          </div>
          <div class="toast-body">
          School program showed
          </div>
        </div>
      </div>`;
        $(".modalView").append(toastHtml);
        var toast = new bootstrap.Toast($('#liveToast'));
        toast.show();

        refreshTable(); // Refresh the table to reflect the updated status
      },
      error: function (xhr, textStatus, errorThrown) {
        // Handle the error response
        console.log(xhr.responseText);
      }
    });
  });


  $(document).on('click', '.clearBtn', function () {
    $(".updateSchoolProgram").addClass("submitSchoolProgram");
    $(".submitSchoolProgram").removeClass("updateSchoolProgram");
    $(".submitSchoolProgram").text("Submit");
    $(".clearBtn").text("Reset");
    var courseCode = $('#courseCode');
    var course = $('#course');
    var educationLevelInput = $('#educationLevel');
    courseCode.removeClass("is-invalid");
    course.removeClass("is-invalid");
    educationLevelInput.removeClass("is-invalid");
    courseCode.next('.invalid-feedback').hide();
    course.next('.invalid-feedback').hide();
    educationLevelInput.next('.invalid-feedback').hide();
    var form = $("#manage-school-form");
    form.removeClass('was-validated');
  });

  $('#educationLevel').on('change', function () {
    var educationLevelInput = $('#educationLevel');
    if (educationLevelInput.val() === null) {
      educationLevelInput.removeClass('is-valid').addClass('is-invalid');
      educationLevelInput.next('.invalid-feedback').show();
    } else {
      educationLevelInput.next('.invalid-feedback').hide();
    }
  });
  $('#courseCode, #course').on('input', function () {
    var maxLength = $(this).attr('maxlength');
    if ($(this).val().length > maxLength) {
      $(this).val($(this).val().slice(0, maxLength));
    }
  });
});
