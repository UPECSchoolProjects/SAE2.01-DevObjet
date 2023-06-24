/* eslint-disable react-hooks/exhaustive-deps */
"use client";

import { v4 as uuidv4 } from 'uuid';
import React from 'react';
import { GraphicCorrespondance, Station, Troncons, linedata, point } from '../types/LinesAttributes';
import { StopPoint } from './StopPoints';
import { LinePath } from './Lines';
import svgPanZoom from 'svg-pan-zoom';
import StationPopup from './StationPopup';


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

function GraphicCorrespondance({ correspondance, activated }: { correspondance: GraphicCorrespondance, activated: boolean }) {
    return activated ? <>
        <path d={correspondance.d} fill='#FFF' stroke={correspondance.strokeColor} strokeWidth={correspondance.strokeWidth}>
            <title>{correspondance.displayName}</title>
        </path>
    </> :
        <>
            <path d={correspondance.d} fill='#444' stroke="#444" strokeWidth={correspondance.strokeWidth} strokeDasharray="5,5">
                <title>{correspondance.displayName}</title>
            </path>
        </>
}

function renderTroncons(tronconsController: TroconController[], linesData: Map<String, linedata>, StopAnimation: boolean): JSX.Element[] {
    const itemsPerLine = new Map<String, number>();
    const countedPerLine = new Map<String, number>();


    // count how many troncons per line
    tronconsController.filter(s => s.activated).forEach((tronconsControl) => {
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

        return <LinePath key={troncon.line + '-' + troncon.id} id={troncon.line + '-' + troncon.id} activated={tronconsControl.activated} stopAnimation={StopAnimation} d={troncon.d} strokeColor={lineData?.strokeColor || "#FFF"} strokeWidth={lineData?.strokeWidth || "1"} delay={3000 * count} nbItem={itemsPerLine.get(troncon.line || "3") || 5} animationDuration={3000} />

    })
}

type StationController = {
    station: Station,
    activated: boolean,
    toDisplay: boolean
}

type TroconController = {
    troncon: Troncons,
    activated: boolean
}

type CorrespondanceController = {
    correspondance: GraphicCorrespondance,
    activated: boolean
}


function stationToDisplay(stations: StationController[]): StationController[] {
    // check that stations doesn't overlap and if so set Todisplay to False for the one that overlap (priority to the one that are activated)
    // if both are activated or both are not activated,  set Todisplay to False for the one that is the most on the left

    const strictOverlapProcessed = [] as StationController[];

    const stationsToDisplay = stations.map((station) => {
        // search stations that are distant from the current station of less than 4px
        const stationsToCheck: { station: StationController, distance: number }[] = stations.map((stationToCheck) => {
            const distance = Math.abs(stationToCheck.station.position.x - station.station.position.x);
            return { station: stationToCheck, distance: distance };
        }).filter((stationToCheck) => {
            return stationToCheck.distance < 4;
        });

        // if there is no station to check, return the station
        if (stationsToCheck.length === 0) {
            return { ...station, toDisplay: true };
        }

        // if there is a distance of 0, check if the station is activated
        const strictEqualStations = stationsToCheck.filter((stationToCheck) => {
            return stationToCheck.distance === 0;
        });

        if (strictEqualStations.length > 0) {
            const isStationActivated = station.activated;
            const isOtherStationActivated = strictEqualStations[0].station.activated;

            if (isStationActivated && !isOtherStationActivated) {
                return { ...station, toDisplay: true };
            } else if (!isStationActivated && isOtherStationActivated) {
                return { ...station, toDisplay: false };
            } else {
                // if both are activated or both are not activated, check strictOverlapProcessed to see if the station other station has already been processed
                // if not add the current station to strictOverlapProcessed and return the current station
                const isOtherStationAlreadyProcessed = strictOverlapProcessed.filter((stationProcessed) => {
                    return stationProcessed.station.id === strictEqualStations[0].station.station.id;
                }).length > 0;

                if (!isOtherStationAlreadyProcessed) {
                    strictOverlapProcessed.push(station);
                    return { ...station, toDisplay: true };
                } else {
                    return { ...station, toDisplay: false };
                }
            }
        } else {
            // if there is a station to check but the distance is not 0, check if the station is activated
            const isStationActivated = station.activated;
            const isOtherStationActivated = stationsToCheck[0].station.activated;

            if (isStationActivated && !isOtherStationActivated) {
                return { ...station, toDisplay: true };
            } else if (!isStationActivated && isOtherStationActivated) {
                return { ...station, toDisplay: false };
            } else {
                // if both are activated or both are not activated, the station that is the most on the left is the one that is displayed
                const isStationOnTheLeft = station.station.position.x < stationsToCheck[0].station.station.position.x;

                if (isStationOnTheLeft) {
                    return { ...station, toDisplay: true };
                } else {
                    return { ...station, toDisplay: false };
                }
            }
        }
    }) as StationController[];

    return stationsToDisplay;
}

const hashCode = function (s: string) {
    return s.split("").reduce(function (a, b) {
        a = ((a << 5) - a) + b.charCodeAt(0);
        return a & a;
    }, 0);
}

function SvgComponent({ stations, path, pathRel, animate, lastRequestedPath }: { stations: Station[], path: number[], pathRel: RelationFromBackend[], animate: boolean, lastRequestedPath: string[] }) {
    const [stationsController, setStationsController] = React.useState<StationController[]>([]);
    const [correspondances, setCorrespondances] = React.useState<CorrespondanceController[]>([]);
    const [troncons, setTroncons] = React.useState<TroconController[]>([]);
    const [linesData, setLinesData] = React.useState<Map<String, linedata>>(new Map());

    const [lastRequestedName, setLastRequestedName] = React.useState<string[]>([]);

    const [mapLoaded, setMapLoaded] = React.useState(false);

    const svgRef = React.useRef<SVGSVGElement | null>(null);

    const lines = ["M1", "M2", "M3", "M3bis", "M4", "M5", "M6", "M7", "M7bis", "M8", "M9", "M10", "M11", "M12", "M13", "M14"]

    const fetchData = async () => {
        let data: { stations: Station[], correspondances: GraphicCorrespondance[] } = await getStations()

        const correspondances = data.correspondances.map((correspondance) => {
            return { correspondance, activated: true };
        });

        setCorrespondances(correspondances);

        await Promise.all(
            lines.map(async (line) => {
                const { linedata, troncons }: { linedata: linedata, troncons: Troncons[] } = await getTroncons(line);


                const tronconsWithLine = troncons.map((troncon) => ({
                    troncon: { ...troncon, line },
                    activated: true,
                }));

                setTroncons((prevTroncons) => [...prevTroncons, ...tronconsWithLine]);

                setLinesData((prevLinesData) => new Map(prevLinesData).set(line, linedata));
            })
        );

        const panZoom = svgPanZoom(svgRef.current!, {
            zoomEnabled: true,
            controlIconsEnabled: true,
            fit: true,
            center: true,
            minZoom: 0.8,
        });

        panZoom.zoom(1.5);

        setMapLoaded(true);
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
        console.log(stationsController);
    }, [stationsController]);

    React.useEffect(() => {
        let names = lastRequestedPath.map((path) => {
            let station = getStationById(path.replace('Q', ''), stations);

            console.log(station);

            if (station === null) {
                return '';
            }
            return station.idName;
        }
        );

        setLastRequestedName(names);
    }, [lastRequestedPath]);

    React.useEffect(() => {

        if (path.length > 0 || pathRel.length > 0) {
            return;
        }

        let stationsControl: StationController[] = stations.map((station) => {
            return { station, activated: true, toDisplay: true };
        });

        setStationsController(stationToDisplay(stationsControl));

        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [stations]);

    React.useEffect(() => {
        console.log('Activating');

        if (!mapLoaded || pathRel.length > 0) {
            return;
        }

        const activate = async () => {
            setStationsController(() =>
                stations.map((prevStation: Station) => ({
                    station: prevStation,
                    activated: path.includes(parseInt(prevStation.id)),
                    toDisplay: path.includes(parseInt(prevStation.id)),
                }))
            );

            setTroncons((prevTroncons) =>
                prevTroncons.map((prevTroncon) => ({
                    ...prevTroncon,
                    activated:
                        path.includes(parseInt(prevTroncon.troncon.beginStation)) &&
                        path.includes(parseInt(prevTroncon.troncon.endStation)),
                }))
            );

            setCorrespondances((prevCorrespondances) =>
                prevCorrespondances.map((prevCorrespondance) => ({
                    ...prevCorrespondance,
                    // corresp à une liste de stations associé
                    // si au moins une station de la correspondance est dans le path, on active la correspondance
                    activated: prevCorrespondance.correspondance.stations.some((station) =>
                        path.includes(station.id)
                    ),
                }))
            );
        };

        activate();
    }, [mapLoaded, path, stations]);

    React.useEffect(() => {
        console.log('Activating pathrel');
        console.log("PathRel: ", pathRel);

        if (!mapLoaded) {
            return;
        }

        const activate = async () => {
            // on fait une liste de toutes les stations presentes (set)
            let stationsControl: Set<String> = new Set();

            pathRel.forEach((rel) => {
                stationsControl.add(rel.st1.id);
                stationsControl.add(rel.st2.id);
            });

            console.log("nombre de stations dans le path rel : ", stationsControl);

            console.log(stations[0].id);

            // on active les stations qui sont dans le path
            setStationsController((prevStationsController) =>
                stations.map((prevStation: Station) => ({
                    station: prevStation,
                    activated: stationsControl.has("Q" + prevStation.id),
                    toDisplay: stationsControl.has("Q" + prevStation.id),
                }))
            );

            // on active les troncons qui sont dans le path
            setTroncons((prevTroncons) =>
                prevTroncons.map((prevTroncon) => ({
                    ...prevTroncon,
                    activated:
                        pathRel.some(
                            (rel) =>
                                (rel.st1.id.replace("Q", "") == prevTroncon.troncon.beginStation &&
                                    rel.st2.id.replace("Q", "") == prevTroncon.troncon.endStation) ||
                                (rel.st1.id.replace("Q", "") == prevTroncon.troncon.endStation &&
                                    rel.st2.id.replace("Q", "") == prevTroncon.troncon.beginStation)
                        ),

                })));

            // on active les correspondances qui sont dans le path
            setCorrespondances((prevCorrespondances) =>
                prevCorrespondances.map((prevCorrespondance) => ({
                    ...prevCorrespondance,
                    activated: prevCorrespondance.correspondance.stations.some((station) =>
                        stationsControl.has("Q" + station.id.toString())
                    ),
                }))
            );
        };

        activate();

    }, [mapLoaded, pathRel]);


    return (
        <svg
            id="metro-map-svg"
            xmlns="http://www.w3.org/2000/svg"
            xmlnsXlink="http://www.w3.org/1999/xlink"
            width="100%"
            height="100%"
            viewBox="0 0 4536 4536"
            shapeRendering="geometricPrecision"
            textRendering="geometricPrecision"
            ref={svgRef}
            // add cursor grab
            onMouseDown={(e) => { e.currentTarget.style.cursor = 'grab'; }}
            onMouseUp={(e) => { e.currentTarget.style.cursor = 'default'; }}
            style={{ backgroundColor: '#222' }}
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
                {stationsController && troncons && renderTroncons(troncons, linesData, !animate)}

            </g>
            <g id="correspondances">
                {correspondances && correspondances.map((correspondance) => (
                    <GraphicCorrespondance key={correspondance.correspondance.id} correspondance={correspondance.correspondance} activated={correspondance.activated} />
                ))}
            </g>
            <g id="stations">
                {stationsController && stationsController.map((stationControl) => {
                    if (!stationControl?.toDisplay) {
                        return null;
                    }

                    const station = stationControl.station;

                    const lineData = linesData.get(station.line || "3")

                    // hash de la position
                    const hashProp = hashCode(station.position.x + "" + station.position.y);

                    if (lastRequestedPath.length > 0 && (lastRequestedPath.includes("Q" + station.id) || lastRequestedName.includes(station.idName))) {
                        return <>
                            <StationPopup station={station} />
                            <StopPoint key={station.line + "-" + station.id + hashProp} activated={stationControl.activated} station={station} lineColor={linesData.get(station.line || "3")?.strokeColor || "#FFF"} lineWidth={lineData?.strokeWidth || "1"} />
                        </>
                    } else {
                        return <StopPoint key={station.line + "-" + station.id + hashProp} activated={stationControl.activated} station={station} lineColor={linesData.get(station.line || "3")?.strokeColor || "#FFF"} lineWidth={lineData?.strokeWidth || "1"} />
                    }
                })}
            </g>
        </svg>
    );
}

export default SvgComponent;

function getStationById(id: string, stations: Station[]): Station | null {
    id = id.replace("Q", "");
    for (const station of stations) {
        if (station.id == id) {
            return station;
        }
    }
    return null;
}

function getStationByName(name: string, stations: Station[]): Station | null {
    for (const station of stations) {
        if (station.idName == name) {
            return station;
        }
    }
    return null;
}