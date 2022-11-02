function validateAddingForm() {
  let title = document.getElementById("titleVal").innerText;
  let alert1 = document.getElementById("alertMessage1");
  let alert2 = document.getElementById("alertMessage2");
  let alert3 = document.getElementById("alertMessage3");
  let alert4 = document.getElementById("alertMessage4");
  let accountName = document.getElementById("name").value;
  let accountUserName = document.getElementById("username").value.toLowerCase();
  let accountPassword = document.getElementById("password").value;
  let confirmPassword = document.getElementById("confirmPass").value;

  if (accountName == "" || accountName.length < 1) {
    alert1.style.display = "block";
    alert1.innerText = "Name cannot be empty!";
    return false;
  } else if (accountUserName == "" || accountName.length < 1) {
    alert1.style.display = "none";
    alert1.innerText = "";

    alert2.style.display = "block";
    alert2.innerText = "Username cannot be empty!";
    return false;
  } else if (title.includes("Students")) {
    if (!accountUserName.includes("c-")) {
      alert2.style.display = "block";
      alert2.innerText = "Student Username Invalid!";
      return false;
    }
  } else if (title.includes("Facilitator")) {
    if (!accountUserName.includes("f-")) {
      alert2.style.display = "block";
      alert2.innerText = "Facilitator Username Invalid!";
      return false;
    }
  } else if (title.includes("Registrar")) {
    if (!accountUserName.includes("r-")) {
      alert2.style.display = "block";
      alert2.innerText = "Registrar Username Invalid!";
      return false;
    }
  } else if (title.includes("Teacher")) {
    if (!accountUserName.includes("t-")) {
      alert2.style.display = "block";
      alert2.innerText = "Teacher Username Invalid!";
      return false;
    }
  }
  if (accountPassword == "" || accountPassword.length < 1) {
    alert2.style.display = "none";
    alert2.innerText = "";

    alert3.style.display = "block";
    alert3.innerText = "Password cannot be empty!";

    return false;
  }
  if (confirmPassword == "" || confirmPassword.length < 1) {
    alert2.style.display = "none";
    alert2.innerText = "";

    alert3.style.display = "none";
    alert3.innerText = "";

    alert4.style.display = "block";
    alert4.innerText = "Password cannot be empty!";
    return false;
  }
  if (accountPassword != confirmPassword) {
    alert2.style.display = "none";
    alert2.innerText = "";

    alert3.style.display = "block";
    alert3.innerText = "Password must be match";

    alert4.style.display = "block";
    alert4.innerText = "Password must be match!";
    return false;
  }
  if (accountPassword.length < 8 || confirmPassword.length < 8) {
    alert2.style.display = "none";
    alert2.innerText = "";

    alert3.style.display = "block";
    alert3.innerText = "Password must 8 characters";

    alert4.style.display = "block";
    alert4.innerText = "Password must 8 characters";
    return false;
  } else {
    alert1.style.display = "none";
    alert1.innerText = "";

    alert2.style.display = "none";
    alert2.innerText = "";

    alert3.style.display = "none";
    alert3.innerText = "";

    alert4.style.display = "none";
    alert4.innerText = "";
    return true;
  }
}
