package com.github.sijoonlee.constant;

public class DEFAULT {
    public static String bootstrapServer = "localhost:9092";
    public static String schemaRegistryUrl = "http://localhost:8081";
    public static String postgresqlSourceConnect = "http://localhost:8083";
    public static int producerNumPartitions = 3;
    public static int producerNumReplicas = 1;
    public static String consumerGroupId = "defaultConsumerGroup";
    public static String autoOffsetResetConfig = "earliest";
    public static String dbUrl = "jdbc:postgresql://localhost:5432/kstransit";
    public static String dbUser = "transit";
    public static String dbPassword = "transit";
    public static String connectTopicPrefix = "transit-";
    public static String connectTopicTable = "vehicles";
    public static String connectTopicName = DEFAULT.connectTopicPrefix + DEFAULT.connectTopicTable;
    public static String connectDbUrl = "jdbc:postgresql://postgres:5432/kstransit";
    public static String streamTopicName = "transit-vehicles-latest";
    public static int updateInterval = 5000; // ms

}
