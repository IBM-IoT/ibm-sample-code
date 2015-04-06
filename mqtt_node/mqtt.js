// before running this, install the mqtt module as seen below:
// npm mqtt
var mqtt    = require('mqtt');

// connect to the message server
var client  = mqtt.connect('mqtt://test.mosquitto.org');

// subscribe to the topic 'presence'
client.subscribe('presence');

// publish 'Hello mqtt' to 'presence'
client.publish('presence', 'Hello mqtt');

// callback to execute when our client receives a message
client.on('message', function (topic, message) {
  
  // print out our message (message is a Buffer)
  console.log(message.toString());
  
});

// terminate the client
client.end();