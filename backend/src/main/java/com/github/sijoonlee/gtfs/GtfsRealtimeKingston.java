package com.github.sijoonlee.gtfs;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.github.sijoonlee.constant.DEFAULT;
import com.google.transit.realtime.GtfsRealtime;
import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import com.google.transit.realtime.GtfsRealtime.FeedMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sijoonlee.db.PostgresqlConnector;

public class GtfsRealtimeKingston {

    private static Logger logger = LoggerFactory.getLogger(GtfsRealtimeKingston.class);

    public void run(URL url){
        PostgresqlConnector connector = new PostgresqlConnector();

        try{
            FeedMessage feed = FeedMessage.parseFrom(url.openStream());
            logger.info("Updating Vehicle info");
            for (FeedEntity entity : feed.getEntityList()) {
                if(entity.hasVehicle()){
                    GtfsRealtime.VehiclePosition vp = entity.getVehicle();
                    if(vp.hasTrip()){
                        String vehicleId = vp.getVehicle().getId();
                        String routeId = vp.getTrip().getRouteId();
                        Float latitude = vp.getPosition().getLatitude();
                        Float longitude = vp.getPosition().getLongitude();
                        String stopId = vp.getStopId();
                        String congestionLevel = vp.getCongestionLevel().toString();
                        String currentStatus = vp.getCurrentStatus().toString();
                        Timestamp signaledAt = new Timestamp(vp.getTimestamp() * 1000); // + 5hours to Kingston local time(EST)
//                        LocalDateTime localDateTime = signaledAt.toLocalDateTime();
//                        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("UTC"));
//                        //https://stackoverflow.com/questions/49853999/convert-zoneddatetime-to-localdatetime-at-time-zone/49854116
//                        zonedDateTime.withZoneSameInstant(ZoneId.of("+05:00")).toLocalDateTime();
                        connector.insertVehicle(vehicleId, routeId, latitude, longitude, stopId, congestionLevel, currentStatus, signaledAt);
                        logger.info(vehicleId);
                    }
                } else if (entity.hasTripUpdate()) {
                    GtfsRealtime.TripUpdate tripUpdate = entity.getTripUpdate();
                    System.out.println(tripUpdate);
                } else if (entity.hasAlert()) {
                    GtfsRealtime.Alert alert = entity.getAlert();
                    System.out.println(alert);
                }
            }
        } catch (IOException ex){
            logger.info(ex.getMessage());
        }
        connector.close();
    }

    public void loopRun(URL url, int interval){
        while(true){
            run(url);
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        GtfsRealtimeKingston gtfs = new GtfsRealtimeKingston();
        gtfs.loopRun(new URL("https://api.cityofkingston.ca/gtfs-realtime/vehicleupdates.pb"), DEFAULT.updateInterval);
//        gtfs.run(new URL("https://api.cityofkingston.ca/gtfs-realtime/tripupdates.pb"));
//        gtfs.run(new URL("https://api.cityofkingston.ca/gtfs-realtime/alerts.pb"));

    }
}
