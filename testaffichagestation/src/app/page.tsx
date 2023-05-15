"use client";

import { v4 as uuidv4 } from 'uuid';
import path from 'path';
import React from 'react';

type SVGCommand =
  | { command: 'M' | 'm'; x: number; y: number } // MoveTo
  | { command: 'L' | 'l' | 'H' | 'h' | 'V' | 'v'; x: number; y: number } // LineTo
  | {
      command: 'C' | 'c' | 'S' | 's';
      x1: number;
      y1: number;
      x2: number;
      y2: number;
      x: number;
      y: number;
    } // Cubic Bézier Curve
  | {
      command: 'Q' | 'q' | 'T' | 't';
      x1: number;
      y1: number;
      x: number;
      y: number;
    } // Quadratic Bézier Curve
  | {
      command: 'A' | 'a';
      rx: number;
      ry: number;
      xAxisRotation: number;
      largeArcFlag: number;
      sweepFlag: number;
      x: number;
      y: number;
    }; // Elliptical Arc Curve

    

interface pathAttr { id: string, d: string, transform: string, fill: string, strokeColor: string,  strokeWidth: string};

const paths: pathAttr[] = [
  {
    id: 'test-u-troncon3',
    d: 'M166.480447,249.72067l30.167598-46.368715',
    transform: 'translate(.000001 0.000001)',
    fill: 'none',
    strokeColor: '#3f5787',
    strokeWidth: '0.6',
  },
  {
    id: 'test-u-troncon2',
    d: 'M196.648045,203.351955l-36.871509-55.027934',
    transform: 'translate(.000001 0.000001)',
    fill: 'none',
    strokeColor: '#3f5787',
    strokeWidth: '0.6',
  },
  {
    id: 'test-u-troncon1',
    d: 'M159.776536,148.324021q48.044692-48.324022,48.044692-48.324022',
    transform: 'translate(.000001 0.000001)',
    fill: 'none',
    strokeColor: '#3f5787',
    strokeWidth: '0.6',
  },
];

function getPathEndpoints(d: string) {
  // get x y after M and x y after the last command
  const res = d.match(/M([\d.]+),([\d.]+).*([\d.]+),([\d.]+)/);

  if (!res) {
    return null;
  }

  return {
    start: {
      x: parseFloat(res[1]),
      y: parseFloat(res[2]),
    },
    end: {
      x: parseFloat(res[3]),
      y: parseFloat(res[4]),
    },
}
}

function CircleJunction({ id, d, transform, fill, strokeColor, strokeWidth, radius }: pathAttr & {radius: string}) {

  return <React.Fragment>
  <path
    id={id}
    d={d}
    transform={transform}
    fill={fill}
    stroke={strokeColor}
    strokeWidth={strokeWidth}
  />
</React.Fragment>
}

function SvgComponent() {
  const [points, setPoints] = React.useState<{x: number, y:number}[]>([]);

  React.useEffect(() => {
    paths.forEach((p) => {
      const res: {start: {x: number, y:number}, end: {x: number, y:number} | null} | null = getPathEndpoints(p.d);

      // check if point already exists
      if (res && points.find((p) => (p.x === res.start.x && p.y === res.start.y) || (res.end && p.x === res.end.x && p.y === res.end.y))) {
        return;
      }
  
      if (res) {
        if (res.end) {
          setPoints([...points, res.start, res.end]);
        } else {
          setPoints([...points, res.start]);
        }
      }
    });
  }, [points]);

  return (
    <svg
      id="test"
      xmlns="http://www.w3.org/2000/svg"
      xmlnsXlink="http://www.w3.org/1999/xlink"
      viewBox="0 0 300 300"
      shapeRendering="geometricPrecision"
      textRendering="geometricPrecision"
    >
      {paths.map((s: pathAttr) => (<CircleJunction key={uuidv4()} {...s} radius="2" /> ))}
      {points.map((p) => (<circle key={uuidv4()} cx={p.x} cy={p.y} r="2" fill="red" />))}
    </svg>
  );
}

export default SvgComponent;
