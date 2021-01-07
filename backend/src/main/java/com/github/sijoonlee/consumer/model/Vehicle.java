package com.github.sijoonlee.consumer.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.ArrayList;

//    vehicle_id VARCHAR(20) PRIMARY KEY,
//    route_id VARCHAR(10),
//    latitude FLOAT,
//    longitude FLOAT,
//    stop_id VARCHAR(10),
//    congestion_level VARCHAR(30),
//    current_status VARCHAR(30),
//    signaled_at TIMESTAMP,
//    updated_at TIMESTAMP
public class Vehicle {
    private String vehicle_id;
    private String route_id;
    private float latitude;
    private float longitude;
    private String stop_id;
    private String congestion_level;
    private String current_status;
    private long signaled_at;
    private long updated_at;

    @Override
    public String toString(){
        Gson gson = new Gson();
        return gson.toJson(this);

        //return "vehicle " + vehicle_id + "|route " + route_id + "|lat " + latitude + "|long " + longitude + "|" + parseTimestamp(signaled_at);
    }

    private String parseTimestamp(long timestamp){
        return new Timestamp(timestamp).toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("+05:00")).toString();
    }

    public static void main(String[] args) {
        Gson gson = new Gson();
        Vehicle vehicle = gson.fromJson("{\"id\":953,\"vehicle_id\":\"3490101046\",\"route_id\":\"4\",\"latitude\":44.256675720214844," +
                "\"longitude\":-76.541748046875,\"stop_id\":\"00397\",\"congestion_level\":\"RUNNING_SMOOTHLY\"," +
                "\"current_status\":\"IN_TRANSIT_TO\",\"signaled_at\":1609685623000,\"updated_at\":1609685625035}", Vehicle.class);
        System.out.println(vehicle);
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(vehicle);
        vehicles.add(vehicle);
        System.out.println(vehicles);
    }
}

