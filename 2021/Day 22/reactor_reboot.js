const REGION_RANGE = 100;
const MAX_MAP_SIZE = Math.pow(2, 24);

var fs = require('fs');
fs.readFile(__dirname + "/input.txt", (error, data) => {
    if (error) throw error;
    
    const procedure = data.toString().split("\r\n");
    console.log("Part 1: " + getNumCubes(procedure, true));
    //console.log("Part 2: " + getNumCubes(procedure, false));
});

function getUniqueIndex(x, y, z) {
    return x + "," + y + "," + z;
}

function getNumCubes(procedure, restrictArea) {
    const cubesSet = new Set();
    for (let i = 0; i < procedure.length; i++) {
        const currStep = procedure[i];
        turnOn = currStep[1] == 'n';
        let comma1Pos = currStep.indexOf(',');
        let comma2Pos = currStep.indexOf(',', comma1Pos + 1);

        let xRange = currStep.slice(currStep.indexOf(' ') + 3, comma1Pos).split('..').map(Number);
        let yRange = currStep.slice(comma1Pos + 3, comma2Pos).split('..').map(Number);
        let zRange = currStep.slice(comma2Pos + 3).split('..').map(Number);

        if (restrictArea) {
            xRange = intersectWithRange(xRange);
            yRange = intersectWithRange(yRange);
            zRange = intersectWithRange(zRange);
            if (xRange.length == 0 || yRange.length == 0 || zRange.length == 0) continue;
        }

        //console.log("Current: " + i);
        
        for (let x = xRange[0]; x <= xRange[1]; x++) {
            for (let y = yRange[0]; y <= yRange[1]; y++) {
                for (let z = zRange[0]; z <= zRange[1]; z++) {
                    const uid = getUniqueIndex(x, y, z);
                    if (turnOn) cubesSet.add(uid);
                    else cubesSet.delete(uid);
                }
            }
        }
    }

    return cubesSet.size;
}

function intersectWithRange(range) {
    const r2 = REGION_RANGE / 2;
    const x = range[0];
    const y = range[1];
    if (y < -r2) return [];
    if (x > r2) return [];
    return range;
}