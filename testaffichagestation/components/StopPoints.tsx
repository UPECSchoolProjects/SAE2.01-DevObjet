import React from "react";
import { Station } from "../types/LinesAttributes";

export function StopPoint({ station, lineColor, lineWidth, activated }: { station: Station, lineColor: string, lineWidth: string, activated: boolean }) {
    const nR = Number(lineWidth);

    if (!activated) {
        switch (station.displayType) {
            case "normal":
                return deactiveStopPoint({ station, lineColor, lineWidth: nR });
            case "correspondanceSimple":
                return deactiveStopPoint({ station, lineColor, lineWidth: nR });
            case "correspTerminus":
                return correspTerminusStopPoint({ station, lineColor: '#666', lineWidth: nR });
            case "correspHover": // permet de mettre sa souris pour voir le nom de la station mais ne l'affiche pas
                return correspHoverStopPoint({ station, lineWidth: nR });
            default:
                return <></>;
        }
    }

    switch (station.displayType) {
        case "normal":
            return normalStopPoint({ station, lineColor, lineWidth: nR });
        case "correspondanceSimple":
            return correspSimpleStopPoint({ station, lineColor, lineWidth: nR });
        case "correspTerminus":
            return correspTerminusStopPoint({ station, lineColor, lineWidth: nR });
        case "correspHover":
            return correspHoverStopPoint({ station, lineWidth: nR });
        default:
            return <></>;
    }
}

function correspHoverStopPoint({ station, lineWidth }: { station: Station, lineWidth: number }) {
    return (<>
        <circle id={station.idName} cx={station.position.x} cy={station.position.y} r={2.5 * lineWidth} fill="#fff">
            <title>{station.displayName} ({station.id})</title>
        </circle>
    </>)
}

function deactiveStopPoint({ station, lineColor, lineWidth }: { station: Station, lineColor: string, lineWidth: number }) {
    return (<>
        <circle id={station.idName} cx={station.position.x} cy={station.position.y} r={1.5 * lineWidth} fill="#666" filter="url(#glow)">
            <title>{station.displayName} ({station.id})</title>
        </circle>
        <circle id={station.idName} cx={station.position.x} cy={station.position.y} r={1.3 * lineWidth} fill="#fff">
            <title>{station.displayName} ({station.id})</title>
        </circle>
    </>)
}

function normalStopPoint({ station, lineColor, lineWidth }: { station: Station, lineColor: string, lineWidth: number }) {
    return (<>
        <circle id={station.idName} cx={station.position.x} cy={station.position.y} r={1.5 * lineWidth} fill={lineColor} filter="url(#glow)">
            <title>{station.displayName} ({station.id})</title>
        </circle>
        <circle id={station.idName} cx={station.position.x} cy={station.position.y} r={1.3 * lineWidth} fill="#fff">
            <title>{station.displayName} ({station.id})</title>
        </circle>
    </>)
}

function correspSimpleStopPoint({ station, lineColor, lineWidth }: { station: Station, lineColor: string, lineWidth: number }) {
    return (<>
        <circle id={station.idName} cx={station.position.x} cy={station.position.y} r={1.6 * lineWidth} fill="#000" filter="url(#glow)">
            <title>{station.displayName} ({station.id})</title>
        </circle>
        <circle id={station.idName} cx={station.position.x} cy={station.position.y} r={1.5 * lineWidth} fill="#fff">
            <title>{station.displayName} ({station.id})</title>
        </circle>
    </>)
}

function correspTerminusStopPoint({ station, lineColor, lineWidth }: { station: Station, lineColor: string, lineWidth: number }) {
    return (<>
        <circle id={station.idName} cx={station.position.x} cy={station.position.y} r={1.3 * lineWidth} fill={lineColor}>
            <title>{station.displayName} ({station.id})</title>
        </circle>)
    </>)
}