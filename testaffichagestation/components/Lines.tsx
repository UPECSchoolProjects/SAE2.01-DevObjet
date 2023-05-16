import React from 'react';
import type { Station, StopPointData, pathAttr, pathDataType, point } from '../types/LinesAttributes';
import { getPathEndpoints } from '../utils/SvgLineUtils';
import { Transition } from 'react-transition-group';
import { v4 as uuidv4 } from 'uuid';

async function getPathData(line: string) {
    let res = await fetch(`${process.env.NEXT_PUBLIC_HOST}/svgLines/${line}.json`);
    let data = await res.json();
    return data;
}

function LinePath({ id, d, strokeColor, strokeWidth, delay, nbItem, animationDuration }: pathAttr & { strokeColor: string, strokeWidth: string, delay: number, nbItem: number, animationDuration: number }) {
    const pathRef = React.useRef<SVGPathElement>(null);

    React.useEffect(() => {
        if (pathRef.current) {
            const length = pathRef.current.getTotalLength();
            pathRef.current.style.strokeDasharray = `${length} ${length}`;
            pathRef.current.style.strokeDashoffset = `${length}`;
        }
    }, []);

    return <React.Fragment>
        <Transition
            in={true}
            timeout={{ enter: 0, exit: delay + nbItem * animationDuration }}
            mountOnEnter
            unmountOnExit
        >
            {(state) => (
                <path
                    id={id + "-neon"}
                    d={d}
                    fill='none'
                    stroke={strokeColor}
                    strokeWidth={strokeWidth}
                    filter="url(#glow)"
                    ref={pathRef as React.Ref<SVGPathElement>}
                    opacity={state === 'entered' ? 0.8 : 0}
                    style={{
                        animation: state === 'entered' ? `dash-animation ${animationDuration}ms ${delay}ms linear infinite` : 'none',
                        transition: 'opacity 800ms',
                    }}
                />
            )}
        </Transition>
        <path
            id={id + "-plain"}
            d={d}
            fill='none'
            stroke={strokeColor}
            strokeWidth={strokeWidth}
        />
        <style>
            {`
        @keyframes dash-animation {
          0% {
            opacity: 0.6
          }
          85% {
            opacity: 0.6
          }
            to {
            stroke-dashoffset: 0;
            opacity: 0;
          }
        }
      `}
        </style>
    </React.Fragment>
}

function StopPoint({ id, cx, cy, r, fill, displayName }: { id: string, cx: number, cy: number, r: string, fill: string, displayName: string }) {
    const nR = Number(r);
    return <React.Fragment>

        <circle id={id} cx={cx} cy={cy} r={1.5 * nR} fill={fill} filter="url(#glow)">
            <title>{displayName}</title>
        </circle>
        <circle id={id} cx={cx} cy={cy} r={1.3 * nR} fill="#fff">
            <title>{displayName}</title>
        </circle>
    </React.Fragment>
}

function getDistance(point1: point, point2: point) {
    return Math.sqrt(Math.pow(point1.x - point2.x, 2) + Math.pow(point1.y - point2.y, 2));
}

export default function SvgLineComponent({ line }: { line: string }) {
    const [points, setPoints] = React.useState<StopPointData[]>([]);
    const [pathData, setPathData] = React.useState<pathDataType | null>(null);

    React.useEffect(() => {
        getPathData(line).then((data) => {
            setPathData(data);
        });

        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    React.useEffect(() => {
        if (pathData) {
            // reset points
            setPoints((prevPoints) => []);

            const newPoints: StopPointData[] = [];
            pathData.troncons.forEach((p) => {
                const res: {
                    start: point | null;
                    end: point | null;
                } | null = getPathEndpoints(p.d);

                for (let point of newPoints) {
                    // if distance between points is less than 1px, remove it
                    if (res && res.start && getDistance(res.start, point.point) < 1) {
                        res.start = null;
                    }
                    if (res && res.end && getDistance(res.end, point.point) < 1) {
                        res.end = null;
                    }
                }

                console.log(pathData.stations[p.beginStation])



                if (res) {
                    if (res.start) {
                        let station = pathData.stations[p.beginStation];

                        if (!station) {
                            console.warn('station not found', p.beginStation);
                        }

                        newPoints.push({ point: res.start, station: station });
                    }

                    if (res.end) {
                        let station = pathData.stations[p.endStation];

                        if (!station) {
                            console.warn('station not found', p.endStation);
                        }
                        newPoints.push({ point: res.end, station: station });
                    }
                }

                console.log('newPoints', newPoints);
            });

            setPoints((prevPoints) => [...prevPoints, ...newPoints]);
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [pathData]);

    return (
        <>
            {pathData && pathData.troncons.map((s: pathAttr, index: number) => (<LinePath key={crypto.randomUUID()} {...s} strokeColor={pathData.linedata.strokeColor} strokeWidth={pathData.linedata.strokeWidth} delay={1500 * index} nbItem={pathData.troncons.length} animationDuration={1500} />))}
            {pathData && points && points.map((p) => (<StopPoint key={p.station.id} id={p.station.id} cx={p.point.x} cy={p.point.y} r={pathData.linedata.strokeWidth} displayName={p.station.displayName} fill="#76d5e3" />))}
        </>);
}