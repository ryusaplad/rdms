$(document).ready(function () {
    var modalView = $(".modalView");
  

    $(document).on("click", ".deleteFile", function (event) {
        var loc = window.location.href;
        var accountType = "";
        if(loc.includes("admin")){
            accountType = "svfc-admin";
        }
        event.preventDefault();
        var fileId = $(this).data("value");
        $.ajax({
            url: '/' + accountType + '/delete/file?id=' + fileId,
            type: 'GET',
            success: function(response) {
              console.log('File deleted');
            },
            error: function(error) {
              console.error(error);
            }
          });
    });
});