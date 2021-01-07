package com.github.sijoonlee.connector;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sijoonlee.constant.DEFAULT;

public class ConnectAPI {
    private static Logger logger = LoggerFactory.getLogger(ConnectAPI.class);

    private String baseUriString;

    public ConnectAPI(){
        baseUriString = DEFAULT.postgresqlSourceConnect;
    }

    public void post(URI uri, String data) {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .build();

        try {
            logger.info("using POST");
            HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("RESP STATUS CODE: " + String.valueOf(response.statusCode()));
            logger.info("RESP STATUS BODY: " + response.body().toString());
        } catch (Exception ex) {
            logger.info(ex.getMessage());
        }


    }


    public void get(URI uri){
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        try {
            logger.info("Using GET");
            HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("RESP STATUS CODE: " + String.valueOf(response.statusCode()));
            logger.info("RESP STATUS BODY: " + response.body().toString());
        } catch (Exception ex) {
            logger.info(ex.getMessage());
        }
    }

    public void delete(URI uri){
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        try {
            logger.info("Using DELETE");
            HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("RESP STATUS CODE: " + String.valueOf(response.statusCode()));
            logger.info("RESP STATUS BODY: " + response.body().toString());
        } catch (Exception ex) {
            logger.info(ex.getMessage());
        }
    }

    public void getVersion(){
        URI uri = URI.create(this.baseUriString);
        this.get(uri);
    }

    public void getConnectors(){
        URI uri = URI.create(this.baseUriString + "/connectors");
        this.get(uri);
    }

    public void getAvailablePlugins(){
        URI uri = URI.create(this.baseUriString + "/connector-plugins");
        this.get(uri);
    }


    public void createConnector(String name){
        // https://docs.confluent.io/platform/current/connect/transforms/extractfield.html
        URI uri = URI.create(this.baseUriString + "/connectors");
        String body = String.format("{\"name\": \"%s\",", name)
            + "\"config\": {"
            + "\"connector.class\": \"io.confluent.connect.jdbc.JdbcSourceConnector\","
            + "\"transforms\": \"createKey,extractStr\","
            + "\"transforms.createKey.type\": \"org.apache.kafka.connect.transforms.ValueToKey\","
            + "\"transforms.createKey.fields\": \"vehicle_id\","
            + "\"transforms.extractStr.type\": \"org.apache.kafka.connect.transforms.ExtractField$Key\","
            + "\"transforms.extractStr.field\": \"vehicle_id\","
            + "\"key.converter\": \"org.apache.kafka.connect.storage.StringConverter\","
            + "\"key.converter.schemas.enable\": \"false\","
            + "\"value.converter\": \"org.apache.kafka.connect.json.JsonConverter\","
            + "\"value.converter.schemas.enable\": \"false\","
            + "\"batch.max.rows\": \"100\","
            + String.format("\"connection.url\": \"%s\",", DEFAULT.connectDbUrl)
            + String.format("\"connection.user\": \"%s\",", DEFAULT.dbUser)
            + String.format("\"connection.password\": \"%s\",", DEFAULT.dbPassword)
            + "\"mode\": \"incrementing\","
            + "\"incrementing.column.name\": \"id\","
            + String.format("\"topic.prefix\": \"%s\",", DEFAULT.connectTopicPrefix)
            + String.format("\"table.whitelist\": \"%s\",", DEFAULT.connectTopicTable)
            + "\"poll.interval.ms\": \"1000\" }}";
        this.post(uri, body);
    }

    public void deleteConnector(String name){
        URI uri = URI.create(this.baseUriString + "/connectors/" + name );
        this.delete(uri);
    }

    public static void main(String[] args) {
        ConnectAPI psc = new ConnectAPI();
        psc.deleteConnector("vehicles");
        psc.createConnector("vehicles");
        psc.getConnectors();
        //psc.getAvailablePlugins();
    }
}

