$(document).ready(function () {
  $(document).on("click", ".editDocument", function (event) {
    event.preventDefault();

    var docId = $(this).attr("href");
    fetchDocumentDataToModal(docId);

    submitEditedDocument(docId);
  });

  $(document).on("change", "#status", function (e) {
    var status = $("#status").val();
  });
  function fetchDocumentDataToModal(id) {
    $.ajax({
      type: "GET",
      url: "/fetch-document-to-modal?docId=" + id,
      success: function (result) {
        if (result.status == "success") {
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
          $("#resultEdit").attr(
            "src",
            "data:image/jpg;base64," + result.data.image
          );
        }
      },
      error: function (e) {
        console.log(e);
      },
    });
  }

  function updateCard() {
    $.ajax({
      type: "GET",
      url: "/update-document-cards",
      success: function (result) {
        if (result.status == "success") {
          $(".card-secbody").empty();
          var cards = "";
          $.each(result.data, function (count, documents) {
            cards =
              '<div class="col-lg-4 mb-2">' +
              '    <div class="card pagesCards">' +
              '    <div class="bg-image hover-overlay ripple" data-mdb-ripple-color="light">' +
              '     <a href="#!">       <div class="imageDiv"   style="background-color: rgba(251, 251, 251, 0.15);">' +
              "   <img src=" +
              "/image?id=" +
              "" +
              documents.documentId +
              '  src="images/documentImage" class="img-fluid" style="height: 270px;" />  </div>     </a>    </div>' +
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
  function submitEditedDocument(id) {
    $("#updateDocumentForm").on("submit", function (event) {
      event.preventDefault();

      $.ajax({
        url: "/update-document-info?docId=" + id,
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
            "#resultEdit",
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
            "#imageEdit",
            "#resultEdit",
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
  }
  $(".close").on("click", function () {
    $("#resultDiv").fadeOut(100);
  });
  $(document).on("click", ".addDocument", function (event) {
    event.preventDefault();

    $("#addDocumentModal").modal("toggle");
  });

  $("#addDocumentForm").on("submit", function (event) {
    event.preventDefault();
    $.ajax({
      url: "/save-document-info",
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
        url: "/delete-document-info?docId=" + docId,
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
          $("#resultMessage").html(
            "Failed to Delete Documents Failed Reason: " + m
          );
        },
      });
    });
  }
  $(document).on("click", ".deleteDocument", function (event) {
    event.preventDefault();
    var docId = $(this).attr("href");
    $("#deleteDocumentModal").modal("toggle");
    deleteDocument(docId);
  });

  function resetFields(tit, desc, img, res, btn) {
    $(tit).val("");
    $(desc).val("");
    $(img).val("");
    $(res).attr("src", " ");
    $(btn).removeAttr("disabled");
  }
});
