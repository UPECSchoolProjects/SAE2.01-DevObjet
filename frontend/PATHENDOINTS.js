
const SPD = require('svg-path-d');
const readline = require('readline');

function askQuestion(query) {
    const rl = readline.createInterface({
        input: process.stdin,
        output: process.stdout,
    });

    return new Promise(resolve => rl.question(query, ans => {
        rl.close();
        resolve(ans);
    }))
}

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

//prompt end point
async function main() {
let d = await askQuestion('d: ');



console.log(getPathEndpoints(d))
}

main();