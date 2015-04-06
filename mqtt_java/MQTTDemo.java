import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTDemo {

  MqttClient client;
  
  public MQTTDemo() {}

  public static void main(String[] args) {
    new MQTTDemo().doDemo();
  }

  public void doDemo() {
    try {
      client = new MqttClient("tcp://test.mosquitto.org", "pahomqttpublish1");
      client.connect();
      MqttMessage message = new MqttMessage();
      message.setPayload("A single message".getBytes());
      client.publish("hello/world", message);
      client.disconnect();
    } catch (MqttException e) {
      e.printStackTrace();
    }
  }
}