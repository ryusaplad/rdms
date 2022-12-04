window.addEventListener('DOMContentLoaded', event => {
 

    const datatablesSimple = document.getElementById('datatablesSimple');
    if (datatablesSimple) {
        new simpleDatatables.DataTable(datatablesSimple);
    }
});

function showPasswordBtn(id) {
  document.getElementById(id).style.color = "black";
}
function hidePasswordBtn(id) {
  document.getElementById(id).style.color = "white";
}

