
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
console.log(getPathEndpoints("m 4207.0964,2792.1498 c 0,0 -102.0516,-102.7485 -106.8567,-107.4972 -3.0122,-2.9769 -13.0807,-9.5532 -17.3151,-9.6291 -2.7987,-0.05 -70.0408,-0.058 -70.0408,-0.058"))