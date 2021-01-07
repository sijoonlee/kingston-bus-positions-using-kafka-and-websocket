import React from "react";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faMapMarker } from '@fortawesome/free-solid-svg-icons'

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

    return <FontAwesomeIcon icon={faMapMarker}/>
  
}

export {MapMarker};