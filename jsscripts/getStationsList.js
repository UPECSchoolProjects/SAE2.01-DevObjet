/*

export type Station = {
    id: string,
    idfmId: string,
    idName: string,
    displayName: string,
    line: string,
    position: point,
    displayType: "normal" | "correspTerminus"
}

export type Troncons = {
    id: string,
    d: string,
    beginStation: string,
    endStation: string,
    line?: string,
}
*/
const readline = require('readline');
const SPD = require('svg-path-d');

const ligne = require('./12.json'); // { troncons: Troncons[] }

const stations = []; // Station[]

const troncons = ligne.troncons;

const newTroncons = [];

const line = 'M12';

// Start from the beginning create first station
// first line :
//         {
//    "id": "Nation-Avron",
//    "d": "m 3525.8134,2613.8065 -88.7038,-87.3897",
//    "beginStation": -1,
//    "endStation": -1
//},

// first station is Nation ask for the id and displayName in console the position in the example is x: 3525.8134, y: 2613.8065
// after that the next troncon is Avon-...
// and the position is "d": m ...

// when finished add the troncons to the newTroncons array but replace the beginStation and endStation with the id of the station

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

(async () => {
    const lastStation = null;
    for (let [index, troncon] of troncons.entries()) {
        try {
            const res = SPD.fromString(troncon.d);

            const first = res[0];
            const last = res[res.length - 1];

            const firstStationName = troncon.id.split('-')[0];
            const lastStationName = troncon.id.split('-')[1];

            if (index === 0) {
                // ask for the id and displayName in console
                console.log('\n')
                console.log('Station :', firstStationName);

                // entered id in the console
                let id = null;
                do {
                    id = await askQuestion('id: ');
                    // verify valid number
                } while (!(id === "" || !isNaN(id)));

                let displayName = await askQuestion('displayName: ');

                if (displayName == "") {
                    displayName = firstStationName;
                }

                if (!first.x) {
                    console.log('Erreur X : ');
                    first.x = await askQuestion('x: ');
                }
                if (!first.y) {
                    console.log('Erreur Y : ');
                    first.y = await askQuestion('y: ');
                }

                stations.push({
                    id: parseInt(id),
                    idfmId: "",
                    idName: "station-" + firstStationName,
                    displayName: displayName,
                    line: line,
                    position: { x: parseInt(first.x), y: parseInt(first.y) },
                    displayType: "normal"
                });
            }

            // ask for the id and displayName in console
            console.log('\n')
            console.log('Station :', lastStationName);

            // entered id in the console
            let id = null;
            do {
                id = await askQuestion('id: ');
                // verify valid number
            } while (!(id === "" || !isNaN(id)));

            const displayName = await askQuestion('displayName: ');

            if (!last.x) {
                console.log('Erreur X : ');
                last.x = await askQuestion('x: ');
            }
            if (!last.y) {
                console.log('Erreur Y : ');
                last.y = await askQuestion('y: ');
            }

            stations.push({
                id: parseInt(id),
                idfmId: "",
                idName: "station-" + lastStationName,
                displayName: displayName,
                line: line,
                position: { x: parseInt(last.x), y: parseInt(last.y) },
                displayType: "normal"
            });

            troncon.beginStation = stations[stations.length - 2].id;
            troncon.endStation = stations[stations.length - 1].id;

            newTroncons.push(troncon);
        } catch (e) {
            console.log('Erreur : ', e);
        }
    }
    // write the stations in a file
    const fs = require('fs');

    console.log(JSON.stringify(stations, null, 4));

    fs.writeFileSync('exportStation.json', JSON.stringify(stations, null, 4));

    console.log(JSON.stringify(newTroncons, null, 4));

    fs.writeFileSync('exportTroncons.json', JSON.stringify(newTroncons, null, 4));

})();





