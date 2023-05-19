$(document).ready(function () {
  var loc = window.location.href;
  var type = "";
  if (loc.includes("/registrar/")) {
    type = "registrar";
  } else if (loc.includes("/svfc-admin")) {
    type = "svfc-admin";
  }
  refreshTable();
  $('#manage-school-form').on('submit', function (event) {
    event.preventDefault();
    event.stopPropagation();

    var form = $(this);
    if (form[0].checkValidity() === false) {
      form.addClass('was-validated');
    } else {

      var formData = form.serialize(); // Serialize the form data

      // Send an AJAX request
      $.ajax({
        type: 'POST',
        url: `/${type}/save/school-program-information`, // Replace with the actual URL of your Java REST controller
        data: formData,
        success: function (response) {

          // Show the success alert
          $('#form-alert').addClass('alert-success');
          $('#form-alert').find('p').text("School program data inserted").end().fadeIn();
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
      url: `/${type}/load/school-program-informations`, // Replace with the actual URL of your API endpoint to fetch the table data
      success: function (data) {
        console.log(data);
        // Clear the existing table data
        var table = $("#zero_config").DataTable();
        table.clear();

        // Loop through the data and add rows to the table
        for (var i = 0; i < data.length; i++) {
          var row = data[i];
          var buttons = `<td>
            <!-- Action buttons for delete and hide -->
            <button class="btn btn-danger btn-sm" data-value="${row.id}" title="Delete">
              <i class="fas fa-trash"></i>
              Delete
            </button>
            <button class="btn btn-secondary btn-sm" data-value="${row.id}" title="Hide">
              <i class="fa fa-eye"></i>
              Hide
            </button>
          </td>`;

          table.row.add([row.schoolLevel, row.level, row.courseOrcategory, buttons]);
        }

        // Draw the updated table
        table.draw();
      },
      error: function (xhr, textStatus, errorThrown) {
        // Handle the error response
        console.log(xhr.responseText);
      }
    });
  }

  // Add input maxlength validation
  $('#school-level').on('change', function () {
    var selectedValue = $(this).val();
    if (selectedValue === '0') {
      $(this).addClass('is-invalid');
      $(this).removeClass('is-valid');
    } else {
      $(this).addClass('is-valid');
      $(this).removeClass('is-invalid');
    }

    if (selectedValue === 'kinder' || selectedValue === 'elementary' || selectedValue === 'junior-high') {
      $('#course').prop('readonly', true);
      $('#course').val('N/A');
    } else {
      $('#course').prop('readonly', false);
      $('#course').val('');
    }
  });

  $('#year-level, #course').on('input', function () {
    var maxLength = $(this).attr('maxlength');
    if ($(this).val().length > maxLength) {
      $(this).val($(this).val().slice(0, maxLength));
    }
  });
});
