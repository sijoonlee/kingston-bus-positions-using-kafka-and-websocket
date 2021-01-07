package com.github.sijoonlee.producer.model;

import com.github.sijoonlee.constant.DEFAULT;

import org.apache.kafka.clients.admin.*;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutionException;


class BaseProducer {
    private static Logger logger = LoggerFactory.getLogger(BaseProducer.class);

    private String topicName;
    private int numPartitions;
    private int numReplicas;
    private KafkaProducer<String,String> producer;

    private Properties producerProperties;
    private Properties adminProperties;

    public BaseProducer(String topicName){
        this.topicName = topicName;
//        this.schemaRegistryUrl = CONSTANT.schemaRegistryUrl;
        this.numPartitions = DEFAULT.producerNumPartitions;
        this.numReplicas = DEFAULT.producerNumReplicas;

        this.producerProperties = new Properties();
        this.producerProperties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, DEFAULT.bootstrapServer);
        this.producerProperties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        this.producerProperties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        this.adminProperties = new Properties();
        this.adminProperties.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, DEFAULT.bootstrapServer);
    }

    public boolean createTopic() {

        boolean created = true;

        AdminClient adminClient = AdminClient.create(this.adminProperties);
        NewTopic newTopic = new NewTopic(this.topicName, this.numPartitions, (short) this.numReplicas);
        List<NewTopic> newTopics = new ArrayList<NewTopic>();
        newTopics.add(newTopic);

        try {
            logger.info("Checking the topic is already existing");
            ListTopicsResult existingTopics = adminClient.listTopics();
            KafkaFuture<Set<String>> topicNamesFuture = existingTopics.names();
            Set<String> topicNames = topicNamesFuture.get();
            if(!topicNames.contains(this.topicName)){
                logger.info("Creating the topic");
                CreateTopicsResult result = adminClient.createTopics(newTopics);
                Map<String, KafkaFuture<Void>> values = result.values();
                values.get(this.topicName).get();
            }
        } catch (ExecutionException | InterruptedException ex) {
            logger.info(ex.getMessage());
            created = false;
        }


        adminClient.close();

        return created;

    }

    public void run(List<String> messages) {
        if(this.createTopic()){
            this.producer = new KafkaProducer<String, String>(producerProperties);
            for(String message : messages){
                ProducerRecord<String ,String> record = new ProducerRecord<String, String>(this.topicName, message);
                producer.send(record);
            }
        }
    }

    public void close() {
        if(this.producer != null) {
            this.producer.close();
        }
    }


    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }


    public void setNumPartitions(int numPartitions) {
        this.numPartitions = numPartitions;
    }

    public void setNumReplicas(int numReplicas) {
        this.numReplicas = numReplicas;
    }

    public static void main(String[] args) {
        BaseProducer baseProducer = new BaseProducer("testTopic");
        List<String> messages = new ArrayList<>();
        messages.add("hello 1");
        messages.add("hello 2");
        messages.add("hello 3");
        baseProducer.run(messages);
        baseProducer.close();
    }


    // docker-compose exec broker kafka-topics -list --zookeeper zookeeper:2181
    // docker-compose exec broker kafka-console-consumer --bootstrap-server localhost:29092 --topic testTopic --from-beginning
}
