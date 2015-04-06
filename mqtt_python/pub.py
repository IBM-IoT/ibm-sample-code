import paho.mqtt.client as mqtt

# set up the mqtt client
mqttc = mqtt.Client("python_pub")

# the server to publish to, and corresponding port
mqttc.connect("test.mosquitto.org", 1883)

# the topic to publish to, and the message to publish
mqttc.publish("hello/world", "Hello, World!")

# establish a two-second timeout
mqttc.loop(2)