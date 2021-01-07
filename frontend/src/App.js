import './App.css';
import {MapContainer} from './mapContainer';
import React, {useEffect, useState} from 'react'

var W3CWebSocket = require('websocket').w3cwebsocket;



// https://www.npmjs.com/package/websocket
// // https://blog.logrocket.com/websockets-tutorial-how-to-go-real-time-with-node-and-react-8e4693fbf843/

function App() {

  const [vehicles, updateVehicles] = useState([]); // string

  useEffect(() => {
    var client = new W3CWebSocket('ws://localhost:8080/events/', 'echo-protocol');

    client.onerror = function() {
        console.log('Connection Error');
    };
    
    client.onopen = function() {
        console.log('WebSocket Client Connected');
    
        function sendNumber() {
            if (client.readyState === client.OPEN) {
                var number = Math.round(Math.random() * 0xFFFFFF);
                client.send(number.toString());
                setTimeout(sendNumber, 1000);
            }
        }
        sendNumber();
    };
    
    client.onclose = function() {
        console.log('echo-protocol Client Closed');
    };
    
    client.onmessage = function(e) {
        if (typeof e.data === 'string') {
          const jsonData = JSON.parse(e.data)
          console.log(jsonData);
          updateVehicles(jsonData);
        }
    };

  }, []);


  return (
    <div className="App">
      <MapContainer
        markers={vehicles}
        center={{lat:44.228892, lng:-76.489821}}
        zoom={10}
      />
    </div>
  );
}

export default App;
