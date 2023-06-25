

const input = `
<path
style="display:none;fill:none;stroke:#000000;stroke-opacity:1"
d="m 526.19672,4048.2932 -300.89526,-0.1127"
id="path477"
inkscape:label="ViroflayRG-VersaillesChateau" />
<path
style="display:none;fill:none;stroke:#000000;stroke-opacity:1"
d="m 856.8134,3981.8169 c -0.3566,0.2675 -48.62027,50.573 -56.50763,57.8218 -2.43185,2.235 -5.19571,6.8909 -16.45966,7.5795 -12.47208,0.7624 -257.64939,1.075 -257.64939,1.075"
id="path475"
inkscape:label="ChavilleVelizy-ViroflayRG" />
<path
style="display:none;fill:none;stroke:#000000;stroke-opacity:1"
d="m 925.84514,3891.1419 c 0,0 0.0638,8.5834 -1.43371,13.8547 -1.34001,4.717 -4.01726,9.5573 -7.3638,14.4555 -4.78114,6.998 -60.23423,62.3648 -60.23423,62.3648"
id="path473"
inkscape:label="MeudenValFleury-ChavilleVelizy" />
<path
style="display:none;fill:none;stroke:#000000;stroke-opacity:1"
d="m 926.07745,3764.5605 -0.23231,126.5814"
id="path471"
inkscape:label="Issy-MeudenValFleury" />
<path
style="display:none;fill:none;stroke:#000000;stroke-opacity:1"
d="m 925.80528,3643.4282 0.27217,121.1323"
id="path469"
inkscape:label="IssyValDeSeine-Issy" />
<path
style="display:none;fill:none;stroke:#000000;stroke-opacity:1"
d="m 925.69254,3346.0098 0.11274,297.4184"
id="path467"
inkscape:label="PontDuGarigliano-IssyValDeSeine" />
<path
style="display:none;fill:none;stroke:#000000;stroke-opacity:1"
d="m 925.80528,2974.8805 -0.11274,371.1293"
id="path465"
inkscape:label="Javel-PontDuGarigliano" />
<path
style="display:none;fill:none;stroke:#000000;stroke-opacity:1"
d="m 815.74681,2679.5131 c 0,0 69.83574,-0.3779 102.14792,-0.3847 4.89357,0 7.6591,-1.8199 8.16863,10.7874 0.5608,13.876 -0.25808,284.9647 -0.25808,284.9647"
id="path8643"
inkscape:label="AvenueDuPdtKennedy-Javel" />
`;

const lines = input.split('<path');
const troncons = [];

lines.forEach(line => {
    if (line.trim() !== '') {
        let id = line.match(/inkscape:label="([^"]+)"/)[1];
        const d = line.match(/d="([^"]+)"/)[1];

        // reverse id order 
        //id = id.split('-').reverse().join('-');



        const troncon = {
            id,
            d,
            beginStation: -1,
            endStation: -1
        };

        troncons.push(troncon);
    }
});


// reverse the order of the troncons
troncons.reverse();


// check that the for each troncon, the end station is the same as the begin station of the next troncon
for (let i = 0; i < troncons.length - 1; i++) {

    const troncon = troncons[i];
    const nextTroncon = troncons[i + 1];

    const tronconEndStation = troncon.id.split('-')[1];
    const nextTronconBeginStation = nextTroncon.id.split('-')[0];

    if (tronconEndStation !== nextTronconBeginStation) {
        console.log(troncon.id, nextTroncon.id);
        console.log('error');
        // exit
        process.exit(1);
    }
}


const output = {
    troncons
};

const fs = require('fs');

console.log(JSON.stringify(output, null, 4));

// write the output to a file
fs.writeFileSync('RERCAvenuePdtKennedy-VersailleChateau.json', JSON.stringify(output, null, 4));