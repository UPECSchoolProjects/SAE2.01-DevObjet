import * as SPD from "svg-path-d";
import type { point } from '../types/LinesAttributes';

export function getPathEndpoints(d: string) {
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