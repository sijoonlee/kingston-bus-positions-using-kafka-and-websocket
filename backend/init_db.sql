DROP TABLE IF EXISTS vehicles;

CREATE TABLE IF NOT EXISTS vehicles (
  id SERIAL PRIMARY KEY,
  vehicle_id VARCHAR(20),
  route_id VARCHAR(10),
  latitude FLOAT,
  longitude FLOAT,
  stop_id VARCHAR(10),
  congestion_level VARCHAR(30),
  current_status VARCHAR(30),
  signaled_at TIMESTAMP,
  updated_at TIMESTAMP
);