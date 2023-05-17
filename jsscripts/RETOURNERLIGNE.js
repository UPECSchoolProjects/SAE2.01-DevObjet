const ligne = require('./4.json');


// read all the stations and starting from the end
// beginStation is the endStation and endStation is the beginStation
// revertName

//{
//    "id": "Barbara-BagneuxLucieAubrac",
//    "d": "m 2136.2817,3879.1056 -0.1594,-87.3897",
//    "beginStation": 377,
//    "endStation": 378
//}
// Become
//{
//    "id": "BagneuxLucieAubrac-Barbare",
//    "d": "m 2136.2817,3879.1056 -0.1594,-87.3897",
//    "beginStation": 378,
//    "endStation": 377
//}

const newLigne = []

for (let troncons of ligne.troncons.reverse()) {
    const beginStation = troncons.beginStation;
    const endStation = troncons.endStation;

    troncons.beginStation = endStation;
    troncons.endStation = beginStation;

    troncons.id = troncons.id.split('-').reverse().join('-');

    newLigne.push(troncons);
}

console.log(JSON.stringify(ligne, null, 4));