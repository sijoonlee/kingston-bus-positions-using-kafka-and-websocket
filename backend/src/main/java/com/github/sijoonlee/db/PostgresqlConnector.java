package com.github.sijoonlee.db;

import java.sql.*;

import com.github.sijoonlee.constant.DEFAULT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostgresqlConnector {
    private static Logger logger = LoggerFactory.getLogger(PostgresqlConnector.class);

    private String url;
    private String user;
    private String password;
    private Connection con;

    public PostgresqlConnector(){
        this.url = DEFAULT.dbUrl;
        this.user = DEFAULT.dbUser;
        this.password = DEFAULT.dbPassword;
        this.connect();
    }
    public PostgresqlConnector(String url, String user, String password){
        this.url = url;
        this.user = user;
        this.password = password;
        this.connect();
    }

    public void connect(){
        try {
            this.con = DriverManager.getConnection(this.url, this.user, this.password);
        } catch (SQLException ex) {
            logger.info(ex.getMessage());
        }
    }
//    vehicle_id VARCHAR(20) PRIMARY KEY,
//    route_id VARCHAR(10),
//    latitude NUMERIC(10,6),
//    longitude NUMERIC(10,6),
//    stop_id VARCHAR(10),
//    congestion_level VARCHAR(30),
//    current_status VARCHAR(30),
//    signaled_at TIMESTAMP,
//    updated_at TIMESTAMP
    public void insertVehicle(String vehicleId, String routeId, Float latitude, Float longitude,
                       String stopId, String congestionLevel, String currentStatus,
                       Timestamp updatedAt){
        try{
            String sql = "INSERT INTO vehicles VALUES(DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement pst = this.con.prepareStatement(sql);
            pst.setString(1, vehicleId);
            pst.setString(2, routeId);
            pst.setFloat(3, latitude);
            pst.setFloat(4, longitude);
            pst.setString(5, stopId);
            pst.setString(6, congestionLevel);
            pst.setString(7, currentStatus);
            pst.setTimestamp(8, updatedAt);
            pst.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
            pst.executeUpdate();
        } catch (SQLException ex) {
            logger.info(ex.getMessage());
        }
    }


    public void close(){
        try{
            this.con.close();
        } catch (SQLException ex){
            logger.info(ex.getMessage());
        }

    }


    public static void main(String[] args) {
        PostgresqlConnector connector = new PostgresqlConnector();
        connector.insertVehicle("1", "1", 50.000f, 50.0000f, "1", "cong", "curr", new Timestamp(System.currentTimeMillis()));
        connector.insertVehicle("2", "2", 50.000f, 50.0000f, "2", "cong", "curr", new Timestamp(System.currentTimeMillis()));
        connector.close();

//        String url = "jdbc:postgresql://localhost:5432/kstransit";
//        String user = "transit";
//        String password = "transit";
//
//        try (Connection con = DriverManager.getConnection(url, user, password);
//             Statement st = con.createStatement();
//             ResultSet rs = st.executeQuery("SELECT VERSION()")) {
//
//            if (rs.next()) {
//                System.out.println(rs.getString(1));
//            }
//
//        } catch (SQLException ex) {
//
//            logger.info(ex.getMessage());
//        }
    }
}
