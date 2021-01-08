import GoogleMap from 'google-map-react';
import React from "react";
import { MapMarker } from './mapMarker'
import { mapStyles } from './mapStyle';
import { API_KEY } from './config'

const MapContainer = (props) => {

    const { markers, zoom, center } = props;

    const createMapOptions = (maps) => {
        return {
            styles: mapStyles
        }
    }

    const Markers = markers && 
        markers.map((marker, index)=>{

            return (<MapMarker
                    key={index}
                    lat={marker.latitude}
                    lng={marker.longitude}
                    routeNumber = {marker.route_id}
                    congestion = {marker.congestion_level}
                />)
        })

    return (
        <div className="h600px">
            <GoogleMap
                key = "googlemap"
                bootstrapURLKeys={{key: API_KEY}}
                center={center}
                zoom={zoom}
                options={createMapOptions}
            >
                {Markers}
            </GoogleMap>
        </div>
    );
}

export { MapContainer };
