import paho.mqtt.client as mqtt

# called when we connect to the mqtt server
def on_connect(client, userdata, rc):
	print("Connected with result code "+str(rc))
	# subscribe to our topic after connection, to ensure
	# that we're always subscribed when we're connected.
	client.subscribe("hello/world")

# called when the server receives a message
def on_message(client, userdata, msg):
	# print the message
	print "Topic: ", msg.topic+'\nMessage: '+str(msg.payload)

# set up the mqtt client
client = mqtt.Client()
client.on_connect = on_connect
client.on_message = on_message

# connect to the message server
client.connect("test.mosquitto.org", 1883, 60)

# blocks while waiting for messages to be published
client.loop_forever()