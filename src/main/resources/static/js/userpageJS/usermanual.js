$(document).ready(function () {

    var modalView = $(".modalView");
    var loginModal = `<div class="modal fade" id="userManualModal"  data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" role="dialog" aria-labelledby="userManualModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="userManualModalLabel">Login Manual</h5>
        </div>
        <div class="modal-body">
        <div style="max-width: 600px; margin: 0 auto;">
 
  <h3 class="text-center mb-0">"How to Login?"</h3>
  <p>If you don't have a username and password yet, please <strong>note</strong> that we don't have a registration portal. Instead, the school registrar or selected admin personnel will add the user accounts manually.</p>
  <p>Once you've obtained your login credentials, simply log in with your corresponding username.</p>
  <p>Different user types have specific account types and prefix formats, which are as follows:</p>
  <ul>
    <li>
      <strong>Students</strong>
      <ul>
        <li>Pre-School to High School (B-/b-)</li>
        <li>Senior High School (SHS-/shs-)</li>
        <li>College (C-/c-)</li>
      </ul>
    </li>
    <li>
    <strong>Teachers and Registrars</strong>
    <ul>
        <li>Teacher (T-/t-)</li>
        <li>Registrar (R-/r-)</li>
      </ul>
  </ul>
  </li>
 
</div>

      
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary text-white clearAbout" data-bs-dismiss="modal">CLOSE</button>
        </div>
      </div>
    </div>
  </div>`;

    var welcomeModal = `<div class="modal fade" id="welcomeModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" role="dialog" aria-labelledby="welcomeModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl" role="document">
      <div class="modal-content">
        <div class="modal-body">
          <div class="row">
            <div class="col-md-6">
              <div style="max-width: 600px; margin: 0 auto;">
                <p class="text-center mb-0">Welcome to our Capstone Project System</p>
                <h3 class="text-center mb-0">"Requests and Document Management System"</h3>
                <p class="text-center mb-3">For St. Vincent de Ferrer College of Camarin Inc.</p>
                <p>Our goal is to make document requests and uploads easy for students while also providing registrars with simple ways to manage the requests.</p>
              </div>
            </div>
            <div class="col-md-6">
              <h4 class="text-center mb-4">About Us</h4>
              <div class="row mt-5">
                <div class="col-md-4 mb-4">
                  <img src="image1.jpg" alt="Image 1" class="img-fluid">
                  <p class="text-center mt-2">Ronald Galve Edano</p>
                </div>
                <div class="col-md-4 mb-4">
                <img src="image2.jpg" alt="Image 2" class="img-fluid">
                <p class="text-center mt-2">Kaye Torrecampo</p>
              </div>
              <div class="col-md-4 mb-4">
                <img src="image3.jpg" alt="Image 3" class="img-fluid">
                <p class="text-center mt-2">Krystal Alviza Mhae</p>
              </div>
              <div class="col-md-4 mb-4">
                <img src="image4.jpg" alt="Image 4" class="img-fluid">
                <p class="text-center mt-2">Erangie Macaspac</p>
              </div>
              <div class="col-md-4 mb-4">
                <img src="image5.jpg" alt="Image 5" class="img-fluid">
                <p class="text-center mt-2">Given Valenzuela</p>
              </div>
              <div class="col-md-4 mb-4">
                <img src="/images/ryu.jpg" alt="ryu.jpg" class="img-fluid">
                <p class="text-center mt-2">Ryu Saplad</p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary text-white clearAbout" data-bs-dismiss="modal">CLOSE</button>
      </div>
    </div>
  </div>
</div>
  `;



    $(".triggerAbout").on("click", function () {
        modalView.empty();
        modalView.append(welcomeModal);
        $("#welcomeModal").modal("toggle");
    });

    $(".triggerUserManual").on("click", function () {
        modalView.empty();
        modalView.append(loginModal);
        $("#userManualModal").modal("toggle");
    });

    $(document).on("click",".clearAbout", function () {
        modalView.empty();
    });

});