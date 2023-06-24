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

export function LinePath({ id, d, strokeColor, strokeWidth, delay, nbItem, animationDuration, activated, stopAnimation }: pathAttr & { strokeColor: string, strokeWidth: string, delay: number, nbItem: number, animationDuration: number, activated: boolean, stopAnimation: boolean }) {
    const pathRef = React.useRef<SVGPathElement>(null);

    React.useEffect(() => {
        if (pathRef.current) {
            const length = pathRef.current.getTotalLength();
            pathRef.current.style.strokeDasharray = `${length} ${length}`;
            pathRef.current.style.strokeDashoffset = `${length}`;
        }
    }, []);

    if (!activated) {
        return <path
            id={id + "-plain"}
            d={d}
            fill='none'
            stroke='#666'
            strokeWidth={strokeWidth}
        />
    }

    return <React.Fragment>
        {!stopAnimation && 
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
                        transition: 'opacity 800ms'
                    }}
                />
            )}
        </Transition>
        }
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
          65% {
            stroke-dashoffset: 1;
            opacity: 0.6
          }
          85% {
            opacity: 0
          }
          to {
            stroke-dashoffset: 0;
            opacity: 0.6;
          }
        }
      `}
        </style>
    </React.Fragment>
}

function getDistance(point1: point, point2: point) {
    return Math.sqrt(Math.pow(point1.x - point2.x, 2) + Math.pow(point1.y - point2.y, 2));
}