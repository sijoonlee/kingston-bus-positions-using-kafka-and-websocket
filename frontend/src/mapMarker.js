import React from "react";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faBus } from '@fortawesome/free-solid-svg-icons'

/** @param {props}
{lat: 44.21, lng: -76.54, name: "Test Marker 2", $hover: false, $getDimensions: ƒ, …}
$dimensionKey: "2"
$geoService: e {hasSize_: true, hasView_: true, transform_: e, maps_: {…}, mapCanvasProjection_: _.Dn}
$getDimensions: ƒ (e)
$hover: false
$onMouseAllow: ƒ (e)
$prerender: false
lat: 44.21
lng: -76.54
name: "Test Marker 2"
*/
const MapMarker = (props) => {

    const { routeNumber, congestion } = props;
    
    let routeClassName = "routeNumber";
    if(routeNumber > 500) routeClassName += " express";

    let busIconClassName = "busIcon";
    
    if(congestion === "RUNNING_SMOOTHLY"){
        busIconClassName += " level1"
    } else if(congestion === "STOP_AND_GO"){
        busIconClassName += " level2"
    } else if(congestion === "CONGESTION"){
        busIconClassName += " level3"
    } else if(congestion === "SEVERE_CONGESTION"){
        busIconClassName += " level4"
    } else { // UNKNOWN_CONGESTION_LEVEL
        busIconClassName += " level0"
    }
    
    return <div className="bus">
        <div className={routeClassName}>{routeNumber}</div>
        <FontAwesomeIcon className={busIconClassName} icon={faBus}/>
    </div>
  
}

export {MapMarker};