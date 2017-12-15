package main;


import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Start {
    static int counter = 1;

    public static void main(String[] args) {
    try{
        String host = "wss://broker.hivemq.com";
        String port = "8000";
        String clientId = "client";
        String topic = "top/secret";

        final String hostName = InetAddress.getLocalHost().getHostName();


        String presentation_host = System.getenv("HOST");
        if(presentation_host!= null ){
            host = presentation_host;
        }

        String presentation_port = System.getenv("PORT");
        if(presentation_port!= null ){
            port = presentation_port;
        }

        String presentation_topic = System.getenv("TOPIC");
        if(presentation_topic!= null ){
            topic = presentation_topic;
        }

        clientId = clientId + new Random().nextInt(100);

        String complete = host + ":" + port;
        MqttClient client = new MqttClient(complete, clientId, new MemoryPersistence());
        client.setCallback(new MqttCallback() {
            public void connectionLost(Throwable cause) {
                System.out.println("Connection lost");
            }

            public void messageArrived(String topic, MqttMessage message) throws Exception {
                System.out.println("################# Message arrived #################\n Topic: " + topic + "\n" + "payload:" + new String(message.getPayload() )+ "\n");
            }

            public void deliveryComplete(IMqttDeliveryToken token) {
            }
        });

        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(clientId);
        client.connect(options);

        System.out.println("sending subscribe");
        client.subscribe(topic);



        while(true){
            client.publish("danielpresentation/daniel", new MqttMessage(("Hello World" + counter + " from " + hostName).getBytes()));
            TimeUnit.MILLISECONDS.sleep(3000);
            counter++;
        }


    } catch (MqttException e) {
        e.printStackTrace();
    } catch (UnknownHostException e) {
        e.printStackTrace();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }


    }


}
