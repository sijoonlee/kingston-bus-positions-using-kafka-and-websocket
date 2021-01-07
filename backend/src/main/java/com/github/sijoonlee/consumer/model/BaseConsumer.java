package com.github.sijoonlee.consumer.model;

import com.github.sijoonlee.constant.DEFAULT;
import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class BaseConsumer {

    private static Logger logger = LoggerFactory.getLogger(BaseConsumer.class);

    private Properties consumerProperties;
    private KafkaConsumer<String, String> consumer;
    private String topicName;

    public BaseConsumer(String topicName){
        this.topicName = topicName;
        this.consumerProperties = new Properties();
        this.consumerProperties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, DEFAULT.bootstrapServer);
        this.consumerProperties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        this.consumerProperties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        this.consumerProperties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, DEFAULT.consumerGroupId);
        this.consumerProperties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, DEFAULT.autoOffsetResetConfig);

    }

    public void run(){
        CountDownLatch latch = new CountDownLatch(1);
        Runnable myConsumerRunnable = new ConsumerRunnable(latch);
        Thread myThread = new Thread(myConsumerRunnable);
        myThread.start();
        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            logger.info("Caught shutdown hook");
            ((ConsumerRunnable) myConsumerRunnable).shutdown();
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("Application has exited");

        }));
        try {
            latch.await();
        } catch (InterruptedException e) {
            logger.info("Applicaton is interrupted", e);
        } finally {
            logger.info("Application is closing");
        }
    }

    public class ConsumerRunnable implements Runnable {
        Gson gson = new Gson();
        private CountDownLatch latch;
        public ConsumerRunnable(CountDownLatch latch){
            this.latch = latch;
            consumer = new KafkaConsumer<String, String>(consumerProperties);
            consumer.subscribe(Arrays.asList(topicName));
        }
        @Override
        public void run() {
            ArrayList<Vehicle> vehicles = new ArrayList<>();
            try{
                while(true){
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(DEFAULT.updateInterval));
                    System.out.println("HAPPENING!");
                    if(records.count() > 0){
                        vehicles = new ArrayList<>();
                    }
                    for (ConsumerRecord<String, String> record : records){
                        logger.info("Partition: " + record.partition() + " Offset: " + record.offset());
                        Vehicle vehicle = gson.fromJson(record.value(), Vehicle.class);
                        vehicles.add(vehicle);
                        logger.info(vehicle.toString());
                    }
                    if(vehicles.size() > 0){
                        logger.info(vehicles.toString());
                    }
                }
            } catch (Exception e) {
                logger.info("Received shutdown signal");
            } finally {
                consumer.close();
                latch.countDown();
            }

        }

        public void shutdown(){
            // wakeup method is a special method to interrupt consumer.poll
            // it will throw the exception WakeUpException
            consumer.wakeup();
        }


    }

    public static void main(String[] args) {
        BaseConsumer consumer = new BaseConsumer(DEFAULT.streamTopicName);
        consumer.run();
    }
}
