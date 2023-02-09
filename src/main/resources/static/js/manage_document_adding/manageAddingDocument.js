$(document).ready(function () {
  var htmlModal = "";
  var modalView = $(".modalView");
  var cards = "";
  var id = "";
  $(document).on("click", ".editDocument", function (event) {
    event.preventDefault();

    var docId = $(this).attr("href");
    id = docId;
    $.ajax({
      type: "GET",
      url: "/admin/fetch-document-to-modal?docId=" + id,
      success: function (result) {
        if (result.status == "success") {
          modalView.empty();
          htmlModal = `
            <div class="modal fade " id="editDocumentModal" tabindex="-1" role="dialog" aria-labelledby="editDocumentModalLabel"
        aria-hidden="true">
        <div class="modal-dialog modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editDocumentModalLabel">Document Information</h5>
                    <button type="button" class="close" data-bs-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="card">
                        <form id="updateDocumentForm" class="form-horizontal">
                            <div class=" card-body">

                                <p><b class="text-danger">(*) all fields are required</b></p>

                                <label for="title">Title</label>
                                <input id="titleEdit" name="title" type="text" class="required form-control">
                                <label for="description">Description</label>
                                <textarea contenteditable="true" id="descriptionEdit" name="description"
                                    class="required form-control" maxlength="1000" rows="3" style="height:300px"></textarea>
                                <label for="course">Image</label>

                                <div class="row">
                                    <div class="col-sm">
                                        <label for="image">Upload Image
                                            *</label>
                                        <div class="input-group mb-3 px-2 py-2 rounded-pill bg-white shadow-sm">

                                            <input id="image" type="file" name="image"
                                                
                                                class="required form-control border-0   " accept="image/png,image/jpeg">
                                        </div>
                                    </div>
                                    <div class="col-sm">
                                        <p class="font-italic text-dark text-center">Preview</p>
                                        <div class="image-area">
                                            <img id="result" style="height: 270px; width: 100%;" src="#" alt=""
                                                class="img-fluid rounded shadow-sm mx-auto d-block border border-dark">
                                        </div>
                                    </div>

                                </div>
                                <label for="status">Status(Show/Hide)</label>

                                <select id="status" name="status" class="form-select form-select-lg"
                                    aria-label=".form-select-lg example">
                                    <option id="valueSelected">Select Status</option>
                                    <option value="1">Show</option>
                                    <option value="0">Hide</option>
                                </select>
                                <p>(*) Mandatory</p>
                            </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="reset" class="btn btn-secondary clearModal" data-bs-dismiss="modal">Close</button>
                    <button type="submit" class="btn btn-primary saveEditDocument clearModal"  data-bs-dismiss="modal">Save changes</button>
                </div>
                </form>
            </div>
        </div>
    </div>
          `;
          modalView.append(htmlModal);
          $("#editDocumentModal").modal("toggle");
          $("#titleEdit").val(result.data.title);
          $("#descriptionEdit").val(result.data.description);

          if (result.data.status == true) {
            $("#status").val(1).change();
            $("#valueSelected").text("Show");
          } else {
            $("#status").val(0).change();
            $("#valueSelected").text("Hide");
          }
          $("#image").val("");
          $("#result").attr(
            "src",
            "data:image/jpg;base64," + result.data.image
          );
        }
      },
      error: function (e) {
        console.log(e);
      },
    });
  });

  $(document).on("change", "#status", function (e) {
    var status = $("#status").val();
  });

  function updateCard() {
    cards = "";
    $.ajax({
      type: "GET",
      url: "/update-document-cards",
      success: function (result) {
        if (result.status == "success") {
          $(".card-secbody").empty();

          $.each(result.data, function (count, documents) {
            console.log(documents);
            var cards =
              '<div class="col-lg-4 mb-2">' +
              '    <div class="card pagesCards">' +
              '    <div class="bg-image hover-overlay ripple" data-mdb-ripple-color="light">' +
              '     <a href="#!">       <div class="imageDiv"   style="background-color: rgba(251, 251, 251, 0.15);">' +
              '   <img src="data:image/jpg;base64,' +
              documents.image +
              '" class="img-fluid" style="height: 270px;" />  </div>     </a>    </div>' +
              '       <div class="card-body">' +
              ' <h5 class="card-title text-center">' +
              documents.title +
              "</h5>" +
              '     <div class="row">' +
              ' <div class="col-sm mt-2">' +
              "     <a href=" +
              documents.documentId +
              ' class="w-100 btn btn-primary editDocument">Edit</a>' +
              "    </div>" +
              '     <div class="col-sm mt-2">' +
              "          <a href=" +
              documents.documentId +
              ' class="w-100 btn btn-danger deleteDocument">Delete</a>' +
              "     </div></div></div></div></div>";
            $(".card-secbody").append(cards);
          });
        }
      },
      error: function (e) {
        console.log("error: " + e);
      },
    });
  }
  $(document).on("click", ".saveEditDocument", function (e) {
    e.preventDefault();
    $("#updateDocumentForm").submit();
  });

  $(document).on("submit", "#updateDocumentForm", function (event) {
    event.preventDefault();

    $.ajax({
      url: "/admin/update-document-info?docId=" + id,
      type: "POST",
      data: new FormData(this),
      enctype: "multipart/form-data",
      processData: false,
      contentType: false,
      cache: false,
      beforeSend: function () {
        $(".saveEditDocument").attr("disabled", "disabled");
      },
      success: function (res) {
        resetFields(
          "#title",
          "#description",
          "#image",
          "#result",
          ".saveEditDocument"
        );
        updateCard();
        $("#editDocumentModal").modal("hide");

        $("#resultDiv").fadeOut(100);
        $("#resultDiv").fadeIn(100);
        $("#buttonColor").removeClass("bg-warning").addClass("bg-success");
        $("#alertDiv").removeClass("alert-warning").addClass("alert-success");

        $("#resultMessage").html("A Document has been successfully updated.");
      },
      error: function (err) {
        var m = "";
        if (err.responseText != null) {
          m = err.responseText;
        } else {
          m = err.responseJSON.message;
        }
        console.error(err.responseText);
        resetFields(
          "#titleEdit",
          "#descriptionEdit",
          "#image",
          "#result",
          ".saveEditDocument"
        );
        updateCard();
        $("#editDocumentModal").modal("hide");

        $("#resultDiv").fadeOut(1);
        $("#resultDiv").fadeIn(100);
        $("#buttonColor").removeClass("bg-success").addClass("bg-warning");
        $("#alertDiv").removeClass("alert-success").addClass("alert-warning");
        $("#resultMessage").html(
          "Insertion/Updating Documents Failed Reason: " + m
        );
      },
    });
  });

  $(".close").on("click", function () {
    $("#resultDiv").fadeOut(100);
  });
  $(document).on("click", ".addDocument", function (event) {
    event.preventDefault();
    modalView.empty();
    htmlModal = `
       <div class="modal fade " id="addDocumentModal" tabindex="-1" role="dialog" aria-labelledby="addDocumentModalLabel"
                                            aria-hidden="true">
                                            <div class="modal-dialog modal-dialog modal-lg" role="document">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title" id="addDocumentModalLabel">Document Information</h5>
                                                        <button type="button" class="close" data-bs-dismiss="modal" aria-label="Close">
                                                            <span aria-hidden="true">&times;</span>
                                                        </button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <div class="card">
                                                            <form id="addDocumentForm" class="form-horizontal">
                                                                <div class=" card-body">
                                        
                                                                    <p><b class="text-danger">(*) all fields are required</b></p>
                                                                    <label for="title">Title</label>
                                                                    <input id="title" name="title" type="text" class="required form-control">
                                                                    <label for="description">Description</label>
                                                                    <textarea contenteditable="true" id="description" name="description"
                                                                        class="required form-control " rows="3" style="height:300px"></textarea>
                                                                    <label for="course">Image</label>
                                        
                                                                    <div class="row">
                                                                        <div class="col-sm">
                                                                            <label for="image">Upload Image
                                                                                *</label>
                                                                            <div class="input-group mb-3 px-2 py-2 rounded-pill bg-white shadow-sm">
                                        
                                                                                <input id="image" type="file" name="image"
                                                                                    class="required form-control border-0" accept="image/png,image/jpeg">
                                                                            </div>
                                                                        </div>
                                                                        <div class="col-sm">
                                                                            <p class="font-italic text-dark text-center">Preview</p>
                                                                            <div class="image-area">
                                                                                <img id="result" style="height: 270px; width: 100%;" src="#" alt=""
                                                                                    class="img-fluid rounded shadow-sm mx-auto d-block border border-dark">
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <p>(*) Mandatory</p>
                                                                </div>
                                                        </div>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="reset" class="btn btn-secondary clearModal" data-bs-dismiss="modal">Close</button>
                                                        <button type="submit" class="btn btn-primary saveDocument clearModal"  data-bs-dismiss="modal" >Save</button>
                                                    </div>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
    `;
    modalView.append(htmlModal);
    $("#addDocumentModal").modal("toggle");
  });
  $(document).on("click", ".saveDocument", function (e) {
    e.preventDefault();
    $("#addDocumentForm").submit();
  });

  $(document).on("submit", "#addDocumentForm", function (event) {
    event.preventDefault();
    $.ajax({
      url: "/admin/save-document-info",
      type: "POST",
      data: new FormData(this),
      enctype: "multipart/form-data",
      processData: false,
      contentType: false,
      cache: false,
      beforeSend: function () {
        $(".saveDocument").attr("disabled", "disabled");
      },
      success: function (res) {
        console.log(res);
        resetFields(
          "#title",
          "#description",
          "#image",
          "#result",
          ".saveDocument"
        );
        updateCard();
        $("#addDocumentModal").modal("hide");
        $("#resultDiv").fadeOut(100);
        $("#resultDiv").fadeIn(100);
        $("#buttonColor").removeClass("bg-warning").addClass("bg-success");
        $("#alertDiv").removeClass("alert-warning").addClass("alert-success");
        $("#resultMessage").html("A Document has been successfully inserted.");
      },
      error: function (err) {
        var m = "";
        if (err.responseText != null) {
          m = err.responseText;
        } else {
          m = err.responseJSON.message;
        }
        console.error(err.responseText);
        resetFields(
          "#title",
          "#description",
          "#image",
          "#result",
          ".saveDocument"
        );
        updateCard();
        $("#addDocumentModal").modal("hide");

        $("#resultDiv").fadeOut(1);
        $("#resultDiv").fadeIn(100);
        $("#buttonColor").removeClass("bg-success").addClass("bg-warning");
        $("#alertDiv").removeClass("alert-success").addClass("alert-warning");
        $("#resultMessage").html(
          "Insertion/Updating Documents Failed Reason: " + m
        );
      },
    });
  });

  function deleteDocument(docId) {
    $(document).on("click", ".confirmDeleteDocument", function (event) {
      event.preventDefault();

      $.ajax({
        url: "/admin/delete-document-info?docId=" + docId,
        type: "DELETE",
        success: function (res) {
          updateCard();
          $("#deleteDocumentModal").modal("hide");

          $("#resultDiv").fadeOut(100);
          $("#resultDiv").fadeIn(100);
          $("#buttonColor").removeClass("bg-warning").addClass("bg-success");
          $("#alertDiv").removeClass("alert-warning").addClass("alert-success");
          $("#resultMessage").html("A Document has been successfully deleted.");
        },
        error: function (err) {
          var m = "";
          if (err.responseText != null) {
            m = err.responseText;
          } else {
            m = err.responseJSON.message;
          }
          console.log(err.responseText);
          updateCard();

          $("#deleteDocumentModal").modal("hide");

          $("#resultDiv").fadeOut(1);
          $("#resultDiv").fadeIn(100);
          $("#buttonColor").removeClass("bg-success").addClass("bg-warning");
          $("#alertDiv").removeClass("alert-success").addClass("alert-warning");
          $("#obal").html("Failed to Delete Documents Failed Reason: " + m);
        },
      });
    });
  }
  $(document).on("click", ".deleteDocument", function (event) {
    event.preventDefault();
    var docId = $(this).attr("href");
    modalView.empty();
    htmlModal = `
      <div class="modal fade" id="deleteDocumentModal" tabindex="-1" role="dialog" aria-labelledby="deleteDocumentModalLabel"
        aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="deleteDocumentModalLabel">Select Deleting Type</h5>
                    <button type="button" class="close" data-bs-dismiss="modal"
                        style="border: 1px solid white; background-color:red; color:white;" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
    
                    <ul>
                    </ul> </b>Are you sure?</b>, This document will be <u>Deleted Permanently</u>.
                    </ul>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary clearModal" data-bs-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-warning confirmDeleteDocument clearModal" data-bs-dismiss="modal">Confirm</button>
    
                </div>
            </div>
        </div>
    </div>
    `;
    modalView.append(htmlModal);
    $("#deleteDocumentModal").modal("toggle");
    deleteDocument(docId);
  });
  $(document).on("click", ".clearModal", function (e) {
    e.preventDefault();
    modalView.empty();
    htmlModal = "";
    $(this).modal("hide");
  });
  function resetFields(tit, desc, img, res, btn) {
    $(tit).val("");
    $(desc).val("");
    $(img).val("");
    $(res).attr("src", " ");
    $(btn).removeAttr("disabled");
  }

  $(document).on("change", "#image", function () {
    if ($("#image")[0].files && $("#image")[0].files[0]) {
      var reader = new FileReader();
      reader.onload = function (e) {
        $("#result").attr("src", e.target.result);
      };
      reader.readAsDataURL($("#image")[0].files[0]);
    }
  });
});
