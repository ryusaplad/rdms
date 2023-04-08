var stompClient = null;

  function setConnected(connected) {

    if (connected) {
      console.log("Web Server : Online");
    }
    else {
      console.log("Web Server : Offline");
    }

  }
 

  function disconnect() {
    if (stompClient !== null) {
      stompClient.disconnect();
    }
    setConnected(false);
  }