
const SPD = require('svg-path-d');

function getPathEndpoints(d) {
    // get the start and end points of a path
    const res = SPD.fromString(d);
    const start = { x: 0, y: 0 };

    console.log(res);

    if (res.length === 0) {
        return null;
    }

    const first = res[0];

    if (first.name === 'M') {
        start.x = first.x;
        start.y = first.y;
    }

    const end = { x: 0, y: 0 };

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

// Path: PATHENDOINTS.js
console.log(getPathEndpoints("m 3597.0529,2835.0837 c 0,0 5.7216,-4.8195 6.2943,-8.2236 1.047,-6.2241 -3.2853,-10.0202 -3.2853,-10.0202 l -139.1337,-137.491 h 0.1643 v 0"))