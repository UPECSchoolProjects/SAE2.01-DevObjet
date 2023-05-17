
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
console.log(getPathEndpoints("m 1116.6828,2790.3929 c 0,0 100.9363,101.8259 107.334,107.8151 6.4063,5.9973 10.4759,7.1537 16.0301,7.6642 8.8789,0.816 25.0963,0.2323 25.0963,0.2323"))