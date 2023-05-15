"use client";

import { v4 as uuidv4 } from 'uuid';
import path from 'path';
import React, { useEffect, useRef } from 'react';
import * as SPD from "svg-path-d";

interface pathAttr { id: string, d: string, transform: string, fill: string, strokeColor: string, strokeWidth: string };
type point = { x: number, y: number };

function getPathEndpoints(d: string) {
    // get the start and end points of a path
    const res = SPD.fromString(d);
    const start: point = { x: 0, y: 0 };

    console.log(res);

    if (res.length === 0) {
        return null;
    }

    const first = res[0];

    if (first.name === 'M') {
        start.x = first.x;
        start.y = first.y;
    }

    const end: point = { x: 0, y: 0 };

    const lastPoint = res[res.length - 1];

    console.log('lastPoint', lastPoint);

    switch (lastPoint.name) {
        case 'L':
            end.x = lastPoint.x;
            end.y = lastPoint.y;
            break;
        case 'Q':
            end.x = lastPoint.x1
            end.y = lastPoint.y1
            break;
        case 'C':
            end.x = lastPoint.x
            end.y = lastPoint.y;
            break;
        default:
            return null;
    }

    return { start: start, end: end };
}

async function getPathData() {
    let res = await fetch(`${process.env.NEXT_PUBLIC_HOST}/svgLines/3b.json`);
    let data = await res.json();
    return data;
}

function generateKeyFrameAnimation(nbItem: number, delay: number, duration: number) {
    //  @keyframes dash-animation {
    //    to {
    //        stroke-dashoffset: 0;
    //      }
    //    }

    // explanation : this keyframe animation is used to animate the dashoffset of the path. It should wait for the previous animation to finish before starting.

}

function LinePath({ id, d, transform, fill, strokeColor, strokeWidth, delay, nbItem }: pathAttr & { delay: number, nbItem: number }) {
    const pathRef = useRef<SVGPathElement>(null);

    useEffect(() => {
        if (pathRef.current) {
            const length = pathRef.current.getTotalLength();
            pathRef.current.style.strokeDasharray = `${length} ${length}`;
            pathRef.current.style.strokeDashoffset = `${length}`;
            pathRef.current.style.animation = `dash-animation 1000ms ${delay}ms linear infinite`;
        }
    }, []);


    return <React.Fragment>
        {/*     <path
      id={id}
      d={d}
      transform={transform}
      fill={fill}
      stroke={strokeColor}
      strokeWidth={strokeWidth}
      filter='blur(1px)'
      style={{zIndex: -2}}
    /> */}
        <path
            id={id}
            d={d}
            transform={transform}
            fill={fill}
            stroke={strokeColor}
            strokeWidth={strokeWidth}
            filter="url(#glow)"
            ref={pathRef as React.Ref<SVGPathElement>}
            opacity={0.6}
        />
        <path
            id={id}
            d={d}
            transform={transform}
            fill={fill}
            stroke={strokeColor}
            strokeWidth={strokeWidth}
        />
        <style>
            {`
        @keyframes dash-animation {
          to {
            stroke-dashoffset: 0;
          }
        }
      `}
        </style>
    </React.Fragment>
}

function StopPoint({ id, cx, cy, r, fill }: { id: string, cx: number, cy: number, r: string, fill: string }) {
    const nR = Number(r);
    return <React.Fragment>
        <circle id={id} cx={cx} cy={cy} r={2 * nR} fill={fill} filter="url(#glow)" />
        <circle id={id} cx={cx} cy={cy} r={1.5 * nR} fill="#fff" />
    </React.Fragment>
}

function addIntensity(num: number) {
    const arr = [];
    while (num) {
        arr.push(<feMergeNode key={uuidv4()} in="blurred" />);
        num--;
    }

    return arr;
}

function SvgComponent() {
    const [points, setPoints] = React.useState<(point & { strokeWidth: string })[]>([]);
    const [pathData, setPathData] = React.useState<(pathAttr)[]>([]);

    React.useEffect(() => {
        getPathData().then((data) => {
            setPathData(data);
        });

        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);


    React.useEffect(() => {
        const newPoints: (point & { strokeWidth: string })[] = [];
        pathData.forEach((p) => {
            const res: {
                start: point | null;
                end: point | null;
            } | null = getPathEndpoints(p.d);
            console.log('p', p.id);
            console.log('res', res);

            for (let i = 0; i < points.length; i++) {
                if (
                    res &&
                    res.start &&
                    res.start.x === points[i].x &&
                    res.start.y === points[i].y
                ) {
                    res.start = null;
                }
                if (
                    res &&
                    res.end &&
                    res.end.x === points[i].x &&
                    res.end.y === points[i].y
                ) {
                    res.end = null;
                }
            }

            if (res) {
                if (res.start) {
                    newPoints.push({ ...res.start, strokeWidth: p.strokeWidth });
                }

                if (res.end) {
                    newPoints.push({ ...res.end, strokeWidth: p.strokeWidth });
                }
            }
        });

        setPoints((prevPoints) => [...prevPoints, ...newPoints]);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [pathData]);

    return (
        <svg
            id="test"
            xmlns="http://www.w3.org/2000/svg"
            xmlnsXlink="http://www.w3.org/1999/xlink"
            viewBox="0 0 300 300"
            shapeRendering="geometricPrecision"
            textRendering="geometricPrecision"
        >
            <defs>
                <filter id="glow" x="-50%" y="-50%" width="200%" height="200%">
                    <feGaussianBlur in="SourceGraphic" stdDeviation="0.4" result="blurred"></feGaussianBlur>
                    <feMerge>
                        {addIntensity(2)}
                    </feMerge>
                </filter>
            </defs>
            {pathData.map((s: pathAttr, index: number) => (<LinePath key={uuidv4()} {...s} delay={1000 * index} nbItem={pathData.length} />))}
            {points.map((p) => (<StopPoint key={uuidv4()} id={uuidv4()} cx={p.x} cy={p.y} r={p.strokeWidth} fill="#76d5e3" />))}
        </svg>
    );
}

export default SvgComponent;
