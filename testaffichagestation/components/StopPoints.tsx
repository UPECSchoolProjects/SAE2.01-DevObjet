import React from "react";
import { Station } from "../types/LinesAttributes";

export function StopPoint({ station, lineColor, lineWidth }: {station: Station, lineColor: string, lineWidth: string}) {
    const nR = Number(lineWidth);

    switch (station.displayType) {
        case "normal": 
            return normalStopPoint({ station, lineColor, lineWidth: nR });
        case "correspondanceSimple":
            return correspSimpleStopPoint({ station, lineColor, lineWidth: nR });
        case "correspTerminus":
            return correspTerminusStopPoint({ station, lineColor, lineWidth: nR });
        default:
            return <></>;
    }   
}

function normalStopPoint({ station, lineColor, lineWidth }: {station: Station, lineColor: string, lineWidth: number}) {
    return (<>
    <circle id={station.idName} cx={station.position.x} cy={station.position.y} r={1.5 * lineWidth} fill={lineColor} filter="url(#glow)">
        <title>{station.displayName}</title>
    </circle>
    <circle id={station.idName} cx={station.position.x} cy={station.position.y} r={1.3 * lineWidth} fill="#fff">
        <title>{station.displayName}</title>
    </circle>
    </>)
}

function correspSimpleStopPoint({ station, lineColor, lineWidth }: {station: Station, lineColor: string, lineWidth: number}) {
    return (<>
    <circle id={station.idName} cx={station.position.x} cy={station.position.y} r={1.6 * lineWidth} fill="#000" filter="url(#glow)">
        <title>{station.displayName}</title>
    </circle>
    <circle id={station.idName} cx={station.position.x} cy={station.position.y} r={1.5 * lineWidth} fill="#fff">
        <title>{station.displayName}</title>
    </circle>
    </>)
}

function correspTerminusStopPoint({ station, lineColor, lineWidth }: {station: Station, lineColor: string, lineWidth: number}) {
    return (<>
    <circle id={station.idName} cx={station.position.x} cy={station.position.y} r={1.3 * lineWidth} fill={lineColor}>
        <title>{station.displayName}</title>
    </circle>)
    </>)
}