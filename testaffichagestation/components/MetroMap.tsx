"use client";

import { v4 as uuidv4 } from 'uuid';
import React from 'react';
import SvgLineComponent, { LinePath } from './Lines';
import { GraphicCorrespondance, Station, Troncons, linedata } from '../types/LinesAttributes';
import { StopPoint } from './StopPoints';


function addIntensity(num: number) {
    const arr = [];
    while (num) {
        arr.push(<feMergeNode key={uuidv4()} in="blurred" />);
        num--;
    }

    return arr;
}

async function getStations() {
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

function renderTroncons(troncons: Troncons[], linesData: Map<String, linedata>): JSX.Element[] {
    const itemsPerLine = new Map<String, number>();
    const countedPerLine = new Map<String, number>();
    // count how many troncons per line
    troncons.forEach((troncon) => {
        const line = troncon.line || "3";
        if (itemsPerLine.has(line)) {
            itemsPerLine.set(line, itemsPerLine.get(line) || 0 + 1);
        } else {
            itemsPerLine.set(line, 1);
        }
    });

    return troncons.map((troncon) => {
        const lineData = linesData.get(troncon.line || "3");

        if (countedPerLine.has(troncon.line || "3")) {
            countedPerLine.set(troncon.line || "3", 0);
        }

        const count = countedPerLine.get(troncon.line || "3") || 0;

        countedPerLine.set(troncon.line || "3", count + 1);

        return <LinePath key={troncon.id} {...troncon} strokeColor={lineData?.strokeColor || "#FFF" } strokeWidth={lineData?.strokeWidth || "1"} delay={1500*count} nbItem={itemsPerLine.get(troncon.line || "3") || 5} animationDuration={1500} />
    
    })
}



function SvgComponent() {
    const [stations, setStations] = React.useState<Station[]>([]);
    const [correspondances, setCorrespondances] = React.useState<GraphicCorrespondance[]>([]);
    const [troncons, setTroncons] = React.useState<Troncons[]>([]);
    const [linesData, setLinesData] = React.useState<Map<String, linedata>>(new Map());

    const lines = ["1","2","3", "3bis", "4"]

    React.useEffect(() => {
        getStations().then((data: {stations: Station[], correspondances: GraphicCorrespondance[]}) => {
            setStations(data.stations);
            setCorrespondances(data.correspondances);

            lines.forEach((line) => {
                getTroncons(line).then((data: {linedata: linedata, troncons: Troncons[]}) => {
                    // add all troncons to troncons array
                    // add line attr to each troncons
                    data.troncons.forEach((troncon) => {
                        troncon.line = line;
                    });

                    setTroncons((prevTroncons) => prevTroncons.concat(data.troncons));

                    //copy new map and add new line
                    setLinesData((prevLinesData) => {
                        const newLinesData = new Map(prevLinesData);
                        newLinesData.set(line, data.linedata);
                        return newLinesData;
                    });
            }
            )
        });
        });
    
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    React.useEffect(() => {
        console.log(troncons);
    }, [troncons]);

    React.useEffect(() => {
        console.log(linesData);
    }, [linesData]);

    return (
        <svg
            id="test"
            xmlns="http://www.w3.org/2000/svg"
            xmlnsXlink="http://www.w3.org/1999/xlink"
            width="500"
            height="500"
            viewBox="0 0 4536 4536"
            shapeRendering="geometricPrecision"
            textRendering="geometricPrecision"
        >
            <defs>
                <filter id="glow" x="-50%" y="-50%" width="200%" height="200%">
                    <feGaussianBlur in="SourceGraphic" stdDeviation="2" result="blurred"></feGaussianBlur>
                    <feMerge>
                        {addIntensity(3)}
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
                    <GraphicCorrespondance key={correspondance.id} correspondance={correspondance} />
                ))}
            </g>
            <g id="stations">
                {stations && stations.map((station) => {
                    const lineData = linesData.get(station.line || "3")
                    return <StopPoint key={station.id} station={station} lineColor={linesData.get(station.line || "3")?.strokeColor || "#FFF"} lineWidth={lineData?.strokeWidth || "1"} />
                })}
            </g>
        </svg>
    );
}

export default SvgComponent;
