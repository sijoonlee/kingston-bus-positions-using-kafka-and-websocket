## Introduction

This is to show Kingston Transit Buses' position on Google map in real-time

## Demo

<div>
    <video height="300" width="400">
        <source src="busdemo.mp4" type="video/mp4">
    </video>
</div>

## Running Backend
1. Go To Directory 'backend'

1. Run Kafka, PostgresSQL
```
docker-compose up -d
```

1. Run Java applications
    - Need few minutes before running below applications since docker containers need time to boot up. You should be able to see messages like below if successful. If not, try to run again few minutes later
    ```
        [main] INFO com.github.sijoonlee.connector.ConnectAPI - Using GET
        [main] INFO com.github.sijoonlee.connector.ConnectAPI - RESP STATUS CODE: 200
        [main] INFO com.github.sijoonlee.connector.ConnectAPI - RESP STATUS BODY: ["vehicles"]
    ```
    - Setup Kafka Connect : Run main method in ConnectAPI.java
    - Get GTFS real time data: Run main method in GtfsRealtimeKingston.java
    - Setup Kafka Stream : Run main method in VehicleStream.java
    - Server up Websocket : Run main method in WSServer.java

## Running Frontend
1. Run React Server
```
npm run start
```
    - Bus icons will be shown up on the map after few seconds

## Clean up
1. Shutdown docker containers
```
docker-compose down --volumes
```

## Data flow
![img](./data-flow.png)



    