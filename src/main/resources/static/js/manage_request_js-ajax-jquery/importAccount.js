$(document).ready(function () {
    var accType = "";
    $(".importAccountToggle").on("click", function () {
        // Get the value inside the span element
        accType = $(this).find("span").text().trim();
        if (accType.toLowerCase().includes("student")) {
            accType = "Student";
        } else if (accType.toLowerCase().includes("teacher")) {
            accType = "Teacher";
        } else if (accType.toLowerCase().includes("registrar")) {
            accType = "Registrar"
        }
        $(".modalView").empty();
        // Create the modal with the file input
        var modal = `<div class="modal fade" id="importModal" tabindex="-1" aria-labelledby="importModalLabel" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
         <div class="modal-dialog ">
             <div class="modal-content">
                 <div class="modal-header">
                     <h5 class="modal-title" id="importModalLabel">Import ${accType}s</h5>
                     <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                 </div>
                 <div class="modal-body">
                 <div class="importAlertMessage alert alert-warning alert-dismissible fade show d-none" role="alert">
                 <pre class="mb-0 importErrorMessage"></pre>
                 <button type="button" class="btn-close btn-close-alert"  aria-label="Close"></button>
                 <a class="link-primary ms-2" onclick="location.reload();" style="cursor: pointer;">
                     Refresh
                 </a>
                 <span class="ms-2">to see the changes.</span>
             </div>
             
                   <div class="form-group">
                 
                    <label for="importAccountFileInput" class="form-label">Select Excel File</label>
                    <input type="file" class="form-control importAccountFileInput" accept=".xlsx, .xls">
                   
                     <div class="invalid-feedback validateExcelFormat d-none">
                       Please select a valid Excel file.
                     </div>
                   </div>
                   <div class="importingLoading" style="display: none;">
                   <div class="spinner-border" role="status">
                       <span class="visually-hidden">Loading...</span>
                   </div>
                 </div>
                   <div class="table-responsive d-none">
                     <table class="table table-bordered text-center" id="zero_config">
                       <thead>
                      
                       </thead>
                       <tbody>
                       </tbody>
                     </table>
                   </div>
                 </div>
                 <div class="modal-footer">
                     <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                     <button type="button" class="btn btn-primary importAccountBtn">Import</button>
                 </div>
             </div>
         </div>
     </div>`;

        // Add the modal to the page
        $(".modalView").append(modal);

        // Show the modal
        $("#importModal").modal("toggle");

    });

    $(document).on("click", ".btn-close-alert", function () {
        $(".importAlertMessage").fadeOut("");
    });
    // Import the Excel data into the table
    $(document).on("click", ".importAccountBtn", function () {

        var type = window.location.href;

        if (type.includes("/svfc-admin/")) {
            type = "svfc-admin";
        } else if (type.includes("/registrar/")) {
            type = "registrar";
        }
        // Disable the import button while processing
        $(this).prop("disabled", true);

        // Get the table data
        var tableData = [];
        $("#importModal table tr").each(function () {
            var rowData = [];
            $(this).find("td").each(function () {
                rowData.push($(this).text());
            });
            tableData.push(rowData);
        });

        // Make an AJAX call to send the data to the server
        $.ajax({
            url: `/${type}/import-account/information?accountType=${accType}`,
            type: "POST",
            data: JSON.stringify(tableData),
            contentType: "application/json",
            beforeSend: function () {
                $(".importingLoading").show();
                $(".importAlertMessage").addClass("d-none");
                $(".importAlertMessage").fadeOut("");
                // Show a loading indicator
            },
            success: function (response) {
                $(".importAlertMessage").removeClass("d-none");
                $(".importAlertMessage").fadeIn("");
                $(".importErrorMessage").text(response);
                $('.importAlertMessage').get(0).scrollIntoView({ behavior: 'smooth' });

            },
            error: function (jqXHR, textStatus, errorThrown) {
                // Handle any errors that occur
                console.error(textStatus, errorThrown);
            },
            complete: function () {
                // Remove the loading indicator and re-enable the import button
                $(".importingLoading").hide();
                $(".importAccountBtn").prop("disabled", false);

            }
        });
    });




    // Check if the selected file is a valid Excel file
    $(document).on("change", ".importAccountFileInput", function () {

        var fileName = $(this).val();
        var fileExtension = fileName.split(".").pop().toLowerCase();
        if ($.inArray(fileExtension, ["xls", "xlsx"]) == -1) {
            $(".importAccountFileInput").addClass("is-invalid");
            $("#importModal .table-responsive").removeClass("d-none");
            $(".importAccountBtn").prop("disabled", true);
            $(".validateExcelFormat").removeClass("d-none");
            $(".validateExcelFormat").text("Invalid File Format. Please upload an Excel file with .xls or .xlsx format.");
        } else {
            $(".validateExcelFormat").addClass("d-none");
            $(".validateExcelFormat").text("");
            $("#importModal .modal-dialog").removeClass("modal-xl");

            $(this).removeClass("is-invalid");
            $("#importModal .table-responsive").addClass("d-none");
            $(".importAccountBtn").prop("disabled", false);

            // Load the Excel file
            $('.importingLoading').show();
            var file = $(this)[0].files[0];
            var reader = new FileReader();
            reader.onload = function (e) {
                var data = e.target.result;
                var workbook = XLSX.read(data, { type: "binary" });
                var sheetName = workbook.SheetNames[0];
                var sheet = workbook.Sheets[sheetName];
                var rows = XLSX.utils.sheet_to_json(sheet, { header: 1 });

                // Check if the number of columns is 4
                if (rows[0].length !== 5) {
                    $(this).addClass("is-invalid");
                    $("#importModal .table-responsive").removeClass("d-none");
                    $(".importAccountBtn").prop("disabled", true);
                    $(".validateExcelFormat").removeClass("d-none");
                    $(".validateExcelFormat").text("The number of columns must be 5.");
                    return;
                }

                // Check if the columns are in the correct order
                var columnNames = ["Name", "Email", "Username", "Password", "Account_Type"];
                for (var i = 0; i < 5; i++) {
                    if (rows[0][i] !== columnNames[i]) {
                        $(this).addClass("is-invalid");
                        $("#importModal .table-responsive").removeClass("d-none");
                        $(".importAccountBtn").prop("disabled", true);
                        $(".validateExcelFormat").removeClass("d-none");
                        $(".validateExcelFormat").text("The column header must be: Name, Email, Username, Password.");
                        return;
                    }
                }

                // Check if the number of columns exceeds 5
                for (var i = 0; i < rows.length; i++) {
                    if (rows[i].length > 5) {
                        $(this).addClass("is-invalid");
                        $("#importModal .table-responsive").removeClass("d-none");
                        $(".importAccountBtn").prop("disabled", true);
                        $(".validateExcelFormat").removeClass("d-none");
                        $(".validateExcelFormat").text("The number of columns must not exceed 5.");
                        return;
                    }
                }

                // Clear the existing table
                var table = $("#importModal table");
                table.find("thead").empty();
                table.find("tbody").empty();


                // Add the header row to the table
                var header = rows[0];
                var headerHtml = '<tr>';
                for (var i = 0; i < header.length; i++) {
                    headerHtml += '<th>' + header[i] + '</th>';
                }
                headerHtml += '<th>Exclude</th></tr>'; // Adding the Exclude column header
                table.find("thead").append(headerHtml);
                // Add the data rows to the table
                for (var i = 1; i < rows.length; i++) {
                    var row = rows[i];
                    var accountType = row[4]; // Assuming Account Type is the third column
                    if (accountType === accType) { // Filter by Account Type
                        var rowHtml = '<tr>';
                        for (var j = 0; j < row.length; j++) {
                            rowHtml += '<td>' + row[j] + '</td>';
                        }
                        rowHtml += '<td><div class="actionDiv"><button class="btn btn-danger text-white btn-sm exclude-row-btn"><i class="fas fa-minus-circle"></i> Exclude</button></div></td>';
                        rowHtml += '</tr>';
                        table.find("tbody").append(rowHtml);
                    }
                }


                // Initialize the DataTable plugin
                table.DataTable({
                    "ordering": true,
                    "destroy": true
                });


                $("#importModal .modal-dialog").addClass("modal-xl");
                table.addClass("table  text-center table-bordered ");
                $("#importModal .table-responsive").removeClass("d-none");

                $('.importingLoading').hide();
                //  $('.importingLoading').remove();
            };

            reader.readAsBinaryString(file);
        }

    });

    // Check if the selected file is a valid Excel file
    $(document).on("click", ".exclude-row-btn", function () {
        var excludeButton = $(this).closest('.actionDiv');

        // Create new buttons for confirmation
        var yesButton = $('<button class="btn btn-danger text-white btn-sm exclude-confirmed"><i class="fas fa-check"></i> Yes</button>');
        var noButton = $('<button class="btn btn-secondary ml-1 text-white btn-sm exclude-cancel"><i class="fas fa-times"></i> No</button>');

        // Add the new buttons to the action div
        var actionDiv = excludeButton.closest('.actionDiv');
        actionDiv.empty();
        actionDiv.append(`<i class="fas fa-trash"></i> Are you sure?`);
        actionDiv.append(yesButton);
        actionDiv.append(noButton);
    });

    $(document).on("click", ".exclude-cancel", function () {
        var actionDiv = $(this).closest('.actionDiv');

        actionDiv.empty();
        actionDiv.append(`<button class="btn btn-danger text-white btn-sm exclude-row-btn"><i class="fas fa-minus-circle"></i> Exclude</button>`);
    });

    $(document).on("click", ".exclude-confirmed", function () {
        var row = $(this).closest('tr');

        // Remove the row from the table
        var table = row.closest('table').DataTable();
        table.row(row).remove().draw();

        // Remove the row from the data source
        var dataSource = table.data();
        dataSource.splice(row.index(), 1);

        // Update the row indexes
        table.rows().invalidate('data').draw();

        // Remove the confirmation buttons
        var actionDiv = row.find('.actionDiv');
        actionDiv.find('.exclude-cancel').remove();
        actionDiv.find('.exclude-confirmed').remove();
    });

});

