$(document).ready(function() {
    $('#manage-school-form').on('submit', function(event) {
      event.preventDefault();
      event.stopPropagation();

      var form = $(this);
      if (form[0].checkValidity() === false) {
        form.addClass('was-validated');
      } else {
        console.log('Form submission triggered!');
        // form.submit();
      }
    });

    // Add input maxlength validation
    $('#school-level').on('input', function() {
      var maxLength = $(this).attr('maxlength');
      if ($(this).val().length > maxLength) {
        $(this).val($(this).val().slice(0, maxLength));
      }
    });

    $('#year-level').on('input', function() {
      var maxLength = $(this).attr('maxlength');
      if ($(this).val().length > maxLength) {
        $(this).val($(this).val().slice(0, maxLength));
      }
    });

    $('#course').on('input', function() {
      var maxLength = $(this).attr('maxlength');
      if ($(this).val().length > maxLength) {
        $(this).val($(this).val().slice(0, maxLength));
      }
    });
  });