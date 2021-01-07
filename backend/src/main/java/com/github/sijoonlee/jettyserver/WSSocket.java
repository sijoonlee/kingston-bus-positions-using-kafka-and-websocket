package com.github.sijoonlee.jettyserver;

import com.github.sijoonlee.constant.DEFAULT;
import com.github.sijoonlee.consumer.model.Vehicle;
import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ClientEndpoint
@ServerEndpoint(value = "/events/")
public class WSSocket
{
    private static Logger logger = LoggerFactory.getLogger(WSSocket.class);

    public Session session;
    private CountDownLatch closureLatch = new CountDownLatch(1);



    @OnOpen
    public void onWebSocketConnect(Session session) throws IOException {
        this.session = session;
        System.out.println("Socket Connected: " + session);
        Gson gson = new Gson();

        Properties consumerProperties = new Properties();
        consumerProperties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, DEFAULT.bootstrapServer);
        consumerProperties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProperties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProperties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, DEFAULT.consumerGroupId);
        consumerProperties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, DEFAULT.autoOffsetResetConfig);

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProperties);
        consumer.subscribe(Arrays.asList(DEFAULT.streamTopicName));
        ArrayList<Vehicle> vehicles;
        try{
            while(true){
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(DEFAULT.updateInterval));

                vehicles = new ArrayList<>();
                for (ConsumerRecord<String, String> record : records){
                    logger.info("Partition: " + record.partition() + " Offset: " + record.offset());
                    Vehicle vehicle = gson.fromJson(record.value(), Vehicle.class);
                    vehicles.add(vehicle);
                    //logger.info(vehicle.toString());
                }
                logger.info("Consumer - # of vehicles updated : " + vehicles.size());
                if(vehicles.size() > 0 ){
                    session.getBasicRemote().sendText(vehicles.toString());
                }
            }
        } catch(Exception ex){
            logger.info(ex.getMessage());
        } finally {
            consumer.close();
        }



    }

    @OnMessage
    public void onWebSocketText(Session sess, String message) throws IOException
    {
        System.out.println("Received TEXT message: " + message);

        if (message.toLowerCase(Locale.US).contains("bye"))
        {
            sess.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Thanks"));
        }
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason)
    {
        System.out.println("Socket Closed: " + reason);
        closureLatch.countDown();
    }

    @OnError
    public void onWebSocketError(Throwable cause)
    {
        cause.printStackTrace(System.err);
    }

    public void awaitClosure() throws InterruptedException
    {
        System.out.println("Awaiting closure from remote");
        closureLatch.await();
    }

}

