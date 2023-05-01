$(document).ready(function () {

  var modalView = $(".modalView");
  var modal = `<div class="modal fade" id="userManualModal"  data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" role="dialog" aria-labelledby="userManualModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="userManualModalLabel"></h5>
        </div>
        <div class="modal-body userManualBody">
        

      
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
                  <img src="/images/edano.jpg" alt="Ronald_Galve_Edano_Image" class="img-fluid">
                  <p class="text-center mt-2">Ronald Galve Edano</p>
                </div>
                <div class="col-md-4 mb-4">
                <img src="/images/kaye.jpeg" alt=Kaye_Torrecampo_Image" class="img-fluid">
                <p class="text-center mt-2">Kaye Torrecampo</p>
              </div>
              <div class="col-md-4 mb-4">
                <img src="/images/mhae.jpg" alt="Krystal_Mhae_Alviza__Image" class="img-fluid">
                <p class="text-center mt-2">Krystal Mhae Alviza</p>
              </div>
              <div class="col-md-4 mb-4">
                <img src="image4.jpg" alt="Erangie_Macaspac_Image" class="img-fluid">
                <p class="text-center mt-2">Erangie Macaspac</p>
              </div>
              <div class="col-md-4 mb-4">
                <img src="/images/given.jpg" alt="Given_Valenzuela_Image" class="img-fluid">
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


  var loginUserModal = `
  <div class="modal fade" id="loginUserManualModal"  data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" role="dialog" aria-labelledby="loginUserManualModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="loginUserManualModalLabel"></h5>
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
              <li>Students (S-/s-)</li>
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
  </div>
 `;

  var studentManual = ` <div style="margin: 0 auto;">
  <h2 class="text-center mb-0">"Welcome to the User Manual for Students!"</h2>
  <p>This manual is designed to guide you through the different features and functionalities of our platform. Here,
   you will learn how to make document requests, track the status of your requests, and edit and resubmit requests if needed.
   <br><br>We hope this manual will help you navigate our platform with ease and make the most out of your user experience.</p>

  <div class="accordion" id="studentUserManual">
        <div class="accordion-item">
        <h2 class="accordion-header" id="dashboardInfo-headingOne">
            <button
                class="accordion-button collapsed"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#dashboardInfo-collapseOne"
                aria-expanded="false"
                aria-controls="dashboardInfo-collapseOne"
            >
              What is Student Dashboard?
            </button>
        </h2>
        <div id="dashboardInfo-collapseOne" class="accordion-collapse collapse " aria-labelledby="dashboardInfo-headingOne">
            <div class="accordion-body">
                Your dashboard displays a summary of your activities, including Total Requests, Total Approved Requests, Total Rejected Requests, and Total Files Uploaded.
            </div>
        </div>
      </div>
      <div class="accordion-item">
          <h2 class="accordion-header" id="requestInfo-headingTwo">
              <button
                  class="accordion-button collapsed"
                  type="button"
                  data-bs-toggle="collapse"
                  data-bs-target="#requestInfo-collapseTwo"
                  aria-expanded="false"
                  aria-controls="requestInfo-collapseTwo"
              >
                  How to Requests?
              </button>
          </h2>
          <div id="requestInfo-collapseTwo" class="accordion-collapse collapse" aria-labelledby="requestInfo-headingTwo">
              <div class="accordion-body">
                  <p>As a student, you may need to request certain documents from your school, such as grades. Here is a step-by-step guide to help you make a request:</p>
                  <h4>
                      <strong>Step 1</strong>
                      :
                  </h4>
                  <p>
                      Go to the
                      <strong>Requests Document</strong>
                      <span>
                          <a href="/student/my-requests">
                              <i class="fas fa-external-link-alt ml-2"></i>
                          </a>
                      </span>
                      This will take you to a page where you can start the request process.
                  </p>
                  <h4>
                      <strong>Step 2</strong>
                      :
                  </h4>
                  <p>Fill out all the required forms and upload any necessary documents. Double-check that all the information you provide is correct and valid, as any mistakes or missing information could result in your request being rejected.</p>
                  <h4>
                      <strong>Step 3</strong>
                      :
                  </h4>
                  <p> <strong>Finalized</strong> your requests then click the submit, Once you've submitted your request, you can check on its status in the My Requests section. There are four different status options:</p>
                  <ul>
                      <li>
                          <strong>Pending</strong>
                          : This means that your request has been received, but it hasn't been reviewed yet.
                      </li>
                      <li>
                          <strong>On-going</strong>
                          : This means that your request is currently being processed and reviewed.
                      </li>
                      <li>
                          <strong>Approved</strong>
                          : This means that your request has been accepted and you can download the requested documents.
                      </li>
                      <li>
                          <strong>Rejected</strong>
                          : This means that your request has been declined, but you have the option to edit the information and files you uploaded.
                      </li>
                  </ul>
                  <strong>Notes</strong>
                 
                  <ul>
                  <li>
                  After clicking "<strong>Finalized</strong>", all fields will be disabled, and you will be able to submit your request.
                  </li>
                  <li>
                  By clicking "<strong>Finalized</strong>", you can still edit the information you inputted by clicking "Edit." This will enable all fields again.
                  </li>
                  </ul>
                  </div>
          </div>
      </div>
      <div class="accordion-item">
          <h2 class="accordion-header" id="rejectedReqInfo-headingThree">
              <button
                  class="accordion-button collapsed"
                  type="button"
                  data-bs-toggle="collapse"
                  data-bs-target="#rejectedReqInfo-collapseThree"
                  aria-expanded="false"
                  aria-controls="rejectedReqInfo-collapseThree"
              >
                  Rejected Requests?
              </button>
          </h2>
          <div id="rejectedReqInfo-collapseThree" class="accordion-collapse collapse" aria-labelledby="rejectedReqInfo-headingThree">
              <div class="accordion-body">
                  <p>If your request is rejected, follow these steps to edit and resubmit:</p>
                  <ul>
                      <li>Click the <strong>Resubmit/Edit</strong> button and choose from four different actions:</li>
                      <ul>
                          <li><strong>View Details</strong>: This allows you to view all the details of your request, including the files you uploaded. You can also download these files to check if they meet the necessary requirements.</li>
                          <li><strong>Edit Information</strong>: This option lets you update information such as your level, course, or semester.</li>
                          <li><strong>Edit Files</strong>: If you need to edit or add requirements, this is the action to select.</li>
                          <li><strong>Resubmit/Edit</strong>: Finally, this action lets you resubmit your request with the changes you've made.</li>
                      </ul>
                  </ul>
                  <p>
                      <strong>Note</strong>
                      : It's essential to make sure all information and documents are accurate and valid to avoid rejection.
                  </p>
              </div>
          </div>
      </div>

      <div class="accordion-item">
          <h2 class="accordion-header" id="documentInfo-headingFour">
              <button
                  class="accordion-button collapsed"
                  type="button"
                  data-bs-toggle="collapse"
                  data-bs-target="#documentInfo-collapseFour"
                  aria-expanded="false"
                  aria-controls="documentInfo-collapseFour"
              >
              Where can I view my uploaded documents? 
              </button>
          </h2>
          <div id="documentInfo-collapseFour" class="accordion-collapse collapse" aria-labelledby="documentInfo-headingFour">
              <div class="accordion-body">
              </h4>
              <p>
            You can find your <strong>uploaded documents</strong> by clicking on <a href="/student/my-documents" data-bs-dismiss="modal">My Documents</a>.
            <span>
                <i class="fas fa-external-link-alt ml-2"></i>
            </span>
            </br>
          <strong>Note</strong>: If you cannot find your uploaded documents or files, there is a chance that the registrar or admin personnel deleted them.
          </p> 
          </div>
      </div>
  </div>

  <div class="accordion-item">
  <h2 class="accordion-header" id="notificationInfo-headingFive">
      <button
          class="accordion-button collapsed"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#notificationInfo-collapseFive"
          aria-expanded="false"
          aria-controls="notificationInfo-collapseFive"
      >
      Where can I view my notifications? 
      </button>
  </h2>
  <div id="notificationInfo-collapseFive" class="accordion-collapse collapse" aria-labelledby="notificationInfo-headingFive">
      <div class="accordion-body">
          <a href="javascript:void()" class="notifLink" data-bs-dismiss="modal">
            Notifications
              <span>
                  <i class="fas fa-external-link-alt ml-2 "></i>
              </a>
          </span>
      </h4>
      <p>
          If you are using a desktop or a big screen, you can see the latest notifications by clicking the bell icon
          <span class="icon">
              <i class="fa fa-bell"></i>
          </span>
          in the top right corner of the screen. To see all the notifications, click the notification tab in the left side menu.
           If you are using a mobile device, you can access the notifications by clicking the notification tab in the left side menu or by
            clicking the arrow in the right side of the screen to show the right side menu.
      </p>
  </div>
</div>
</div>
<div class="accordion-item">
      <h2 class="accordion-header" id="changeProfileInfo-headingSix">
          <button
              class="accordion-button collapsed"
              type="button"
              data-bs-toggle="collapse"
              data-bs-target="#changeProfileInfo-collapseSix"
              aria-expanded="false"
              aria-controls="changeProfileInfo-collapseSix"
          >
              How To Profile Password and Picture?
          </button>
      </h2>
      <div id="changeProfileInfo-collapseSix" class="accordion-collapse collapse" aria-labelledby="changeProfileInfo-headingSix">
          <div class="accordion-body">
          <h4>Change Password</h4>
              <p>
                  To change your password, follow these steps:
              </p>
              <ol>
                  <li>
                      <strong>Step 1</strong>
                      :Enter your old password. This is to ensure that you are the owner of the account and authorized to make changes to it.
                  </li>
                  <li>
                      <strong>Step 2</strong>
                      :Enter your new password. Your new password should be a combination of uppercase and lowercase letters, numbers, and special characters to make it strong and difficult to guess.
                  </li>
                  <li>
                      <strong>Step 3</strong>
                      :Confirm your new password. This step ensures that you entered your new password correctly and matches the password you want to use.
                  </li>
              </ol>
              <p>
                  strong>Note
              </strong>: Your new password should be unique and not used for any other account or service. Please remember to keep your password secure and do not share it with anyone else. If you suspect that someone else has accessed your account, please contact support immediately.</p>
            
      <h4>Change Profile Picture</h4>
      <p>To change your profile picture, follow these steps:</p>
      <ol>
          <li>
              <strong>Step 1</strong>:Select Choose Photo.
          </li>
          <li>
              <strong>Step 2</strong>:Customize (Crop,Zoom).
          </li>
          <li>
              <strong>Step 3</strong>:Save Changes.
          </li>
      </ol>
      <strong>Note:</strong>
      Clicking the Reset button will automatically reset the image.
      <br>
      <strong>Note:</strong>
      Clicking the Clear button will clear the image and load the generated avatar.
              </div>
  </div>
</div>
<div class="accordion-item">
  <h2 class="accordion-header" id="logoutInfo-heading">
      <button
          class="accordion-button collapsed"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#logoutInfo-collapse"
          aria-expanded="false"
          aria-controls="logoutInfo-collapse"
      >
          How to Logout?
      </button>
  </h2>
  <div id="logoutInfo-collapse" class="accordion-collapse collapse" aria-labelledby="logoutInfo-heading">
      <div class="accordion-body">
          <p>To log out of the system, you can click the logout button in the left sidebar or right sidebar or profile section. This will ensure that your account is secure and that no one can access your information when you're not using it.</p>
          <p><strong>Note
          </strong>: Always remember to log out of your account, especially when using a shared computer or device, to ensure the privacy and security of your account.</p>
      
  </div>
</div>
</div>
</div>
</div>`;


  var registrarManual = `<div style="margin: 0 auto;">
  <h1 class="text-center mb-0">"Welcome to User Manual for Registrars!</h1>

  <h2>View Registrars Dashboard</h2>
  <p>The dashboard displays a summary of your activities, including Pending Requests, Approved Requests, Rejected Requests, and Files Uploaded, as well as student requests.</p>
  <h2>Managing Student Requests</h2>
  <p>As a Registrar, you have the ability to manage student requests through the Student Requests tab. To access it, simply go to the left sidebar and select the Manage Requests dropdown button, and then choose Student Requests. Once there, you will see a table of all the student requests.</p>
  <p>To manage a specific request, select the corresponding action button in the table. If the request's status is Pending, you can choose from the following actions:</p>
  <ul>
    <li><strong>Details:</strong> View all the detailed information about the request, including any attached files. You can also download the files and check whether they meet the requirements.</li>
    <li><strong>Process:</strong> Change the status of the request to On-Going.</li>
    <li><strong>Reject:</strong> Reject the request.</li>
  </ul>
  <p>If the request's status is On-Going, the Process button changes to Complete. You can then select the Complete button to upload the required documents in the finalizing modal. Once the finalizing process is complete, the request's status will be set to Completed.</p>

  <h2>Sending a Request to a Teacher</h2>
  <p>To send a request to a teacher, go to Manage Accounts and select Teachers. Click on the action button, then choose Requests. This will allow you to send a request to the teacher.</p>

  <h2>Adding, Deleting, or Editing User Accounts</h2>
  <p>As a Registrar, you also have the ability to add, edit, or delete accounts of students and teachers. To edit an account, go to Manage Accounts and select the account you want to modify. You'll then see a table of all the accounts, along with various action buttons.</p>
  <p>To edit a user profile, such as their username, password, or email, select the Edit button. To delete an account, choose the Delete button, and you'll be prompted to select either Temporary or Permanent deletion.</p>
  <p>If you choose Temporary deletion, the account will be disabled, but not permanently deleted. This is useful when you want to prevent a user from accessing their account temporarily.</p>
  <p>If you choose Permanent deletion, the account and all associated data will be permanently deleted and cannot be undone. However, you cannot delete an account if the user has any existing requests. This is to ensure data integrity.</p>
  <h2>Notifications</h2>
<p>If you are using a desktop or a big screen, you can see the latest notifications by clicking the bell icon <span class="icon"><i class="fa fa-bell"></i></span> in the top right corner of the screen. To see all the notifications, click the notification tab in the left side menu. If you are using a mobile device, you can access the notifications by clicking the notification tab in the left side menu or by clicking the arrow in the right side of the screen to show the right side menu.</p>
<p>Profile:</p>
<p>To change your profile information and picture, follow these steps:</p>
<ol>
  <li><strong>Step 1</strong>:Click the small rounded image in the top right corner of the screen.</li>
  <li><strong>Step 2</strong>:Choose the action you want, such as changing your password or profile picture.</li>
</ol>
<h2>Change Password</h2>
<p>To change your password, follow these steps:</p>
<ol>
  <li><strong>Step 1</strong>:Enter your old password. This is to ensure that you are the owner of the account and authorized to make changes to it.</li>
  <li><strong>Step 2</strong>:Enter your new password. Your new password should be a combination of uppercase and lowercase letters, numbers, and special characters to make it strong and difficult to guess.</li>
  <li><strong>Step 3</strong>:Confirm your new password. This step ensures that you entered your new password correctly and matches the password you want to use.</li>
</ol>
<p>Note: Your new password should be unique and not used for any other account or service. Please remember to keep your password secure and do not share it with anyone else. If you suspect that someone else has accessed your account, please contact support immediately.</p>
<h2>How to Logout?</h2>
<p>To log out of the system, you can click the logout button in the left sidebar or right sidebar or profile section. This will ensure that your account is secure and that no one can access your information when you're not using it.</p>
<p>Note: Always remember to log out of your account, especially when using a shared computer or device, to ensure the privacy and security of your account.</p>
  </div>`;


  var teacherManual = `
  <div style="margin: 0 auto;">
  <h1 class="text-center mb-0">"User Manual for TEACHER/PROFESSOR"</h1>

  <h2>View Teacher Dashboard</h2>
  <p>The dashboard displays a summary of your activities, including Total Requests Received, Pending Requests, Sent Requests, Files Uploaded, and registrar requests.</p>
  <p class="lead">
    As a Teacher or Professor, you play an important role in managing the registrar requests through the View Requests tab. Here's a step-by-step guide on how to respond to a request:
  </p>
  <ol>
    <li>Access the View Requests tab by clicking the corresponding option in the left sidebar. From there, choose the Student Requests option to see a table of all the registrar requests.</li>
    <li>To send a response, select the pending request that you want to respond to. A modal will pop up, and you should click the Response button to proceed.</li>
    <li>Once you've clicked the Response button, you will be taken to the Response Form. Fill in all the required information, as indicated by the notes at the top of the form. Make sure to include a message and upload any necessary files.</li>
    <li>Once you've completed the form, click the Finalize button to submit your response. Note that the Edit button will be disabled once you've submitted the response, so make sure that all information is correct before finalizing.</li>
    <li>You can check the status of your response by returning to the View Requests tab. If the request was successfully sent, the status will change from Pending to Completed.</li>
    <li>Any files that were attached to the original request will appear at the bottom of your response. You can also view the files that you uploaded into the My Documents tab.</li>
  </ol>

  <h2>Notifications</h2>
<p>If you are using a desktop or a big screen, you can see the latest notifications by clicking the bell icon <span class="icon"><i class="fa fa-bell"></i></span> in the top right corner of the screen. To see all the notifications, click the notification tab in the left side menu. If you are using a mobile device, you can access the notifications by clicking the notification tab in the left side menu or by clicking the arrow in the right side of the screen to show the right side menu.</p>
<p>Profile:</p>
<p>To change your profile information and picture, follow these steps:</p>
<ol>
  <li><strong>Step 1</strong>:Click the small rounded image in the top right corner of the screen.</li>
  <li><strong>Step 2</strong>:Choose the action you want, such as changing your password or profile picture.</li>
</ol>
<h2>Change Password</h2>
<p>To change your password, follow these steps:</p>
<ol>
  <li><strong>Step 1</strong>:Enter your old password. This is to ensure that you are the owner of the account and authorized to make changes to it.</li>
  <li><strong>Step 2</strong>:Enter your new password. Your new password should be a combination of uppercase and lowercase letters, numbers, and special characters to make it strong and difficult to guess.</li>
  <li><strong>Step 3</strong>:Confirm your new password. This step ensures that you entered your new password correctly and matches the password you want to use.</li>
</ol>
<p>Note: Your new password should be unique and not used for any other account or service. Please remember to keep your password secure and do not share it with anyone else. If you suspect that someone else has accessed your account, please contact support immediately.</p>
<h2>How to Logout?</h2>
<p>To log out of the system, you can click the logout button in the left sidebar or right sidebar or profile section. This will ensure that your account is secure and that no one can access your information when you're not using it.</p>
<p>Note: Always remember to log out of your account, especially when using a shared computer or device, to ensure the privacy and security of your account.</p>
</div>`;

  var adminManual = `
<div style="margin: 0 auto;">
<h3 class="text-center mb-0">"User manual for SCHOOL ADMIN OR /  ADMIN Personnel"</h3>
<p>As an admin, you have access to the following:</p>
<h3>Admin Dashboard</h3>
<p>The dashboard displays a summary of different users' activities, including:</p>
<ul>
  <li>Student Request Totals</li>
  <li>Total Registrar (Added and Deleted)</li>
  <li>Total Teacher (Added and Deleted)</li>
  <li>Total Students (Added and Deleted)</li>
  <li>Registrar Requests</li>
  <li>Total Files in the system</li>
</ul>
<p>You can also view requesting reports in pie chart format, which you can filter by status and date. To filter the report, choose the status and date, then click the Filter button. If no data has been found on the corresponding filter, the tab will be disabled with the text 'Not Available.'</p>
<p>Finally, you can export requests by choosing the corresponding charts that you want to export. With these features, our platform offers a simple and efficient way to manage document requests and uploads.</p>
<h3>Adding, Deleting, or Editing User Accounts</h3>
<p>As an admin, you also have the ability to add, edit, or delete accounts of students, teachers, and registrar. To edit an account, go to Active Accounts and select the account you want to modify. You'll then see a table of all the accounts, along with various action buttons.</p>
<p>To edit a user profile, such as their username, password, or email, select the Edit button. To delete an account, choose the Delete button, and you'll be prompted to select either Temporary or Permanent deletion.</p>
<p>If you choose Temporary deletion, the account will be disabled, but not permanently deleted. This is useful when you want to prevent a user from accessing their account temporarily.</p>
<p>Additionally, if you temporarily deleted the accounts, you can undo that by going to the Deleted Accounts.</p>
<p>If you choose Permanent deletion, the account and all associated data will be permanently deleted and cannot be undone. However, you cannot delete an account if the user has any existing requests. This is to ensure data integrity.</p>
<h3>View All Student and Registrar Requests</h3>
<p>This tab has two sections: Student Requests and Registrar Requests.</p>
<p>The admin can view all the student requests, but the admin cannot manage them, like rejecting, processing, or editing. The same goes for the Registrar Requests.</p>
<h3>Manage Documents</h3>
<p>In this section, you can add, edit, or delete documents. This section is the page that the student will see if they request specific documents.</p>
<p>By editing the document, you can also hide it by setting the status to hide. You can also delete the document. Note that you cannot delete any documents if they have been requested by the students.</p>
<h3>View Global Logs</h3>
<p>In this section, you can see all user logs, such as logging in, logging out, adding, deleting, and updating details.</p>
<h3>View Global Files</h3>
<p>In this section, you can see all the users' uploaded files and also delete them. Note that if you delete any files, you will see a confirmation warning with the message:</p>
<p>"Deleting this data will permanently remove it from the system. This action cannot be undone. Please note that any users who have uploaded or have access to this data will no longer be able to view or download it."</p>
<h3>Settings</h3
<p>On this section you will see the Database Backup and Restore.</p>
<ul>
  <li>You can download the latest backup of the database and restore it.</li>
  <li>You can restore the database by adding .sql database file into the textbox.</li>
</ul>
<p>Note: The database will automatically backup every midnight, and the folder location is in the C:\rdms_db_backup_sql.</p>`
    ;

  $(".triggerAbout").on("click", function () {
    modalView.empty();
    modalView.append(welcomeModal);
    $("#welcomeModal").modal("toggle");
  });

  $(".triggerUserManual").on("click", function () {
    modalView.empty();

    modalView.append(loginUserModal);

    $("#loginUserManualModal").modal("toggle");
  });

  $(document).on("click", ".clearAbout", function () {
    modalView.empty();
  });

  $(".userManual").on("click", function () {
    var userType = window.location.href.toLowerCase();
    modalView.empty();
    modalView.append(modal);
    if (userType.includes("student")) {
      $("#userManualModalLabel").text("Student Manual");
      $(".userManualBody").append(studentManual);
    } else if (userType.includes("registrar")) {
      $("#userManualModalLabel").text("Registrar Manual");
      $(".userManualBody").append(registrarManual);
    } else if (userType.includes("teacher")) {
      $("#userManualModalLabel").text("Teacher Manual");
      $(".userManualBody").append(teacherManual);
    } else if (userType.includes("admin")) {
      $("#userManualModalLabel").text("School Admin Personel Manual");
      $(".userManualBody").append(adminManual);
    }
    $("#userManualModal").modal("toggle");
  });

});