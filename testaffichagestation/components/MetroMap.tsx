"use client";

import { v4 as uuidv4 } from 'uuid';
import React from 'react';
import { GraphicCorrespondance, Station, Troncons, linedata } from '../types/LinesAttributes';
import { StopPoint } from './StopPoints';
import { LinePath } from './Lines';
import { cp } from 'fs';
import svgPanZoom from 'svg-pan-zoom';


function addIntensity(num: number) {
    const arr = [];
    while (num) {
        arr.push(<feMergeNode key={uuidv4()} in="blurred" />);
        num--;
    }

    return arr;
}

async function getStations(): Promise<{ stations: Station[], correspondances: GraphicCorrespondance[] }> {
    let res = await fetch(`${process.env.NEXT_PUBLIC_HOST}/svgLines/stations.json`, { cache: "no-cache" });
    let data = await res.json();
    return data;
}

async function getTroncons(line: string) {
    let res = await fetch(`${process.env.NEXT_PUBLIC_HOST}/svgLines/${line}.json`, { cache: "no-cache" });
    let data = await res.json();
    return data;
}

function GraphicCorrespondance({ correspondance }: { correspondance: GraphicCorrespondance }) {
    return <>
        <path d={correspondance.d} fill='#FFF' stroke={correspondance.strokeColor} strokeWidth={correspondance.strokeWidth}>
            <title>{correspondance.displayName}</title>
        </path>
    </>
}

function renderTroncons(tronconsController: TroconController[], linesData: Map<String, linedata>): JSX.Element[] {
    const itemsPerLine = new Map<String, number>();
    const countedPerLine = new Map<String, number>();


    // count how many troncons per line
    tronconsController.forEach((tronconsControl) => {
        const troncon = tronconsControl.troncon;


        const line = troncon.line || "3";
        if (itemsPerLine.has(line)) {
            itemsPerLine.set(line, itemsPerLine.get(line) || 0 + 1);
        } else {
            itemsPerLine.set(line, 1);
        }
    });

    return tronconsController.map((tronconsControl) => {
        const troncon = tronconsControl.troncon;

        const lineData = linesData.get(troncon.line || "3");

        if (countedPerLine.has(troncon.line || "3")) {
            countedPerLine.set(troncon.line || "3", 0);
        }

        const count = countedPerLine.get(troncon.line || "3") || 0;

        if (tronconsControl.activated) {
            countedPerLine.set(troncon.line || "3", count + 1);
        }

        return <LinePath key={troncon.line + '-' + troncon.id} id={troncon.line + '-' + troncon.id} activated={tronconsControl.activated} d={troncon.d} strokeColor={lineData?.strokeColor || "#FFF"} strokeWidth={lineData?.strokeWidth || "1"} delay={3000 * count} nbItem={itemsPerLine.get(troncon.line || "3") || 5} animationDuration={3000} />

    })
}

type StationController = {
    station: Station,
    activated: boolean
}

type TroconController = {
    troncon: Troncons,
    activated: boolean
}

type CorrespondanceController = {
    correspondance: GraphicCorrespondance,
    activated: boolean
}

function SvgComponent() {
    const [stations, setStations] = React.useState<StationController[]>([]);
    const [correspondances, setCorrespondances] = React.useState<CorrespondanceController[]>([]);
    const [troncons, setTroncons] = React.useState<TroconController[]>([]);
    const [linesData, setLinesData] = React.useState<Map<String, linedata>>(new Map());

    const [currentPath, setCurrentPath] = React.useState<number[]>([]);

    const [mapLoaded, setMapLoaded] = React.useState(false);

    const svgRef = React.useRef<SVGSVGElement | null>(null);

    const lines = ["M1", "M2", "M3", "M3bis", "M4", "M5", "M6", "M7", "M7bis", "M8", "M9", "M10", "M11", "M13", "M14"]

    const fetchData = async () => {
        let data: { stations: Station[], correspondances: GraphicCorrespondance[] } = await getStations()

        const stations = data.stations.map((station) => {
            return { station, activated: false };
        });
        setStations(stations);

        const correspondances = data.correspondances.map((correspondance) => {
            return { correspondance, activated: false };
        });

        setCorrespondances(correspondances);

        await Promise.all(
            lines.map(async (line) => {
                const { linedata, troncons }: { linedata: linedata, troncons: Troncons[] } = await getTroncons(line);


                const tronconsWithLine = troncons.map((troncon) => ({
                    troncon: { ...troncon, line },
                    activated: false,
                }));

                setTroncons((prevTroncons) => [...prevTroncons, ...tronconsWithLine]);

                setLinesData((prevLinesData) => new Map(prevLinesData).set(line, linedata));
            })
        );

        const panZoom = svgPanZoom(svgRef.current!, {
            zoomEnabled: true,
            controlIconsEnabled: true,
            fit: true
        });

        setMapLoaded(true);
        setCurrentPath([275, 212, 295, 119, 16, 331, 135, 67, 173, 227, 228, 282, 224]);
    };

    React.useEffect(() => {
        fetchData();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    React.useEffect(() => {
        console.log(troncons);
    }, [troncons]);

    React.useEffect(() => {
        console.log(linesData);
    }, [linesData]);

    React.useEffect(() => {
        console.log(stations);
    }, [stations]);

    React.useEffect(() => {
        console.log('Activating');

        if (!mapLoaded) {
            return;
        }

        const activate = async () => {
            setStations((prevStations) =>
                prevStations.map((prevStation) => ({
                    ...prevStation,
                    activated: currentPath.includes(parseInt(prevStation.station.id)),
                }))
            );

            setTroncons((prevTroncons) =>
                prevTroncons.map((prevTroncon) => ({
                    ...prevTroncon,
                    activated:
                        currentPath.includes(parseInt(prevTroncon.troncon.beginStation)) &&
                        currentPath.includes(parseInt(prevTroncon.troncon.endStation)),
                }))
            );

            setCorrespondances((prevCorrespondances) =>
                prevCorrespondances.map((prevCorrespondance) => ({
                    ...prevCorrespondance,
                    activated: true,
                }))
            );
        };

        activate();
    }, [mapLoaded, currentPath]);

    return (
        <svg
            id="test"
            xmlns="http://www.w3.org/2000/svg"
            xmlnsXlink="http://www.w3.org/1999/xlink"
            width="1000"
            height="800"
            viewBox="0 0 4536 4536"
            shapeRendering="geometricPrecision"
            textRendering="geometricPrecision"
            ref={svgRef}
            style= {{cursor: 'grab'}}
        >
            <defs>
                <filter id="glow" x="-50%" y="-50%" width="200%" height="200%">
                    <feGaussianBlur in="SourceGraphic" stdDeviation="2" ></feGaussianBlur>
                    <feMerge>
                        {addIntensity(2)}
                    </feMerge>
                </filter>
            </defs>
            {/*            {lines.map((line) => 
            <SvgLineComponent key={line} line={line} />
           )} */}
            <g id="troncons">
                {stations && troncons && renderTroncons(troncons, linesData)}

            </g>
            <g id="correspondances">
                {correspondances && correspondances.map((correspondance) => (
                    <GraphicCorrespondance key={correspondance.correspondance.id} correspondance={correspondance.correspondance} />
                ))}
            </g>
            <g id="stations">
                {stations && stations.map((stationControl) => {
                    const station = stationControl.station;

                    const lineData = linesData.get(station.line || "3")
                    return <StopPoint key={station.line + "-" + station.id} activated={stationControl.activated} station={station} lineColor={linesData.get(station.line || "3")?.strokeColor || "#FFF"} lineWidth={lineData?.strokeWidth || "1"} />
                })}
            </g>
        </svg>
    );
}

export default SvgComponent;
