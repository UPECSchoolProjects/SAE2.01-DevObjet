"use client";

import { v4 as uuidv4 } from 'uuid';
import React from 'react';
import SvgLineComponent from './Lines';


function addIntensity(num: number) {
    const arr = [];
    while (num) {
        arr.push(<feMergeNode key={uuidv4()} in="blurred" />);
        num--;
    }

    return arr;
}

function SvgComponent() {

    const lines = ["3", "3b"]

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
                    <feGaussianBlur in="SourceGraphic" stdDeviation="5" result="blurred"></feGaussianBlur>
                    <feMerge>
                        {addIntensity(3)}
                    </feMerge>
                </filter>
            </defs>
           {lines.map((line) => 
            <SvgLineComponent key={line} line={line} />
           )}
        </svg>
    );
}

export default SvgComponent;
