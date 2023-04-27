var connected = false;
var stompClient;

function setConnected(value) {
  connected = value;
}


window.onbeforeunload = function () {
  if (stompClient && stompClient.connected) {
    stompClient.disconnect();
  }
};