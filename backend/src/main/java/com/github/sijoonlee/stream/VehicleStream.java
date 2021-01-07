package com.github.sijoonlee.stream;

import java.util.Properties;

import com.github.sijoonlee.constant.DEFAULT;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

// https://stackoverflow.com/questions/50524867/parsing-json-data-using-apache-kafka-streaming
// https://docs.confluent.io/platform/current/streams/developer-guide/datatypes.html#json
// https://github.com/apache/kafka/blob/2.6/streams/examples/src/main/java/org/apache/kafka/streams/examples/pageview/PageViewTypedDemo.java#L83
public class VehicleStream {
    public Topology createTopology(){
        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> vehicleInfo = builder.stream(DEFAULT.connectTopicName);
        KTable<String, String> latestVehicleInfo = vehicleInfo.toTable();

        latestVehicleInfo.toStream().to(DEFAULT.streamTopicName, Produced.with(Serdes.String(), Serdes.String()));

        return builder.build();
    }

    public void run(){
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "transit-vehicle-table");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, DEFAULT.bootstrapServer);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        KafkaStreams streams = new KafkaStreams(this.createTopology(), config);
        streams.start();

        // shutdown hook to correctly close the streams application
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

        // Update:
        // print the topology every 10 seconds for learning purposes
        while(true){
            streams.localThreadsMetadata().forEach(data -> System.out.println(data));
            try {
                Thread.sleep(DEFAULT.updateInterval);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public static void main(String[] args) {
        VehicleStream vehicleStream = new VehicleStream();
        vehicleStream.run();
    }
}
