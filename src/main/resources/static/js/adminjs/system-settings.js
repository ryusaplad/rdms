$(document).ready(function () {
    var db_restore = $("#database_back_res");
    var file = $("#file");
    var message = $(".message");
    $(".restoreBtn").on("click", function (e) {
        e.preventDefault();
        var fileExtension = file.val().split('.').pop().toLowerCase();
        if (file.val() == "") {
            file.addClass("is-invalid");
            message.show();
            message.text("Please select a file.")
        } else if ($.inArray(fileExtension, ['sql']) == -1) {
            file.addClass("is-invalid");
            message.show();
            message.text("Invalid file format. Only .sql files are allowed.");

        } else {
            file.removeClass("is-invalid");
            message.show();
            message.text("")
            db_restore.submit();
        }


    });

    $(db_restore).on("submit", function (e) {
        e.preventDefault();
        var formData = new FormData(this);
        $.ajax({
            url: "/database/backup/restore",
            type: "POST",
            data: formData,
            enctype: "multipart/form-data",
            processData: false,
            contentType: false,
            beforeSend: function () {
                $(".restoreBtn").attr("disabled", true);
            },
            success: function (response) {
                $(".restoreBtn").attr("disabled", false);
                file.addClass("is-valid");
                message.show();
                message.removeClass("invalid-feedback");
                message.addClass("valid-feedback");
                message.text("Database Restored")
                setTimeout(() => {
                    $(db_restore).trigger("reset");
                    message.hide();
                    message.removeClass("valid-feedback");
                    message.addClass("invalid-feedback");
                    file.removeClass("is-valid");

                }, 1500);
            },
            error: function (xhr, status, error) {
                $(".restoreBtn").attr("disabled", false);
                file.addClass("is-invalid");
                message.show();
                message.text(error);
            }
        });


    });
    $(".downloadBtn").on("click", function (e) {
        e.preventDefault();
        $(".downloadBtn").addClass("disabled");
        $.ajax({
            type: "GET",
            url: "/database/backup/download",
            success: function (data, textStatus, xhr) {
                const filename = getFilenameFromResponse(xhr);
                const blob = new Blob([data], { type: xhr.getResponseHeader('Content-Type') });
                downloadFile(blob, filename);
                $(".downloadBtn").removeClass("disabled");
            },
            error: function (xhr, textStatus, errorThrown) {
                $(".downloadBtn").removeClass("disabled");
                alert("Failed to download backup file, please try again.");
            }
        });
    });

    function getFilenameFromResponse(xhr) {
        const disposition = xhr.getResponseHeader('Content-Disposition');
        const matches = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/.exec(disposition);
        if (matches != null && matches[1]) {
            return matches[1].replace(/['"]/g, '');
        }
        return '';
    }

    function downloadFile(blob, filename) {
        const URL = window.URL || window.webkitURL;
        const downloadUrl = URL.createObjectURL(blob);
        const a = document.createElement('a');
        if (typeof a.download === 'undefined') {
            window.location.href = downloadUrl;
        } else {
            a.href = downloadUrl;
            a.download = filename;
            document.body.appendChild(a);
            a.click();
        }
        setTimeout(() => URL.revokeObjectURL(downloadUrl), 100);
    }
});