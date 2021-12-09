var fs = require('fs');
fs.readFile(__dirname + "/input.txt", (error, data) => {
    if (error) throw error;
    
    const lines = data.toString().split("\r\n");
    const caveMatrix = lines.map(x => x.split("").map(Number));

    console.log("Part 1: " + getTotalRisk(caveMatrix));
    console.log("Part 2: " + getBasinsMultiplied(caveMatrix));
});

function getTotalRisk(caveMatrix) {
    let totalRisk = 0;
    for (let i = 0; i < caveMatrix.length; i++) {
        const caveRow = caveMatrix[i];
        for (let j = 0; j < caveRow.length; j++) {
            const currHeight = caveRow[j];

            const neighbours = [];
            if (j > 0) neighbours.push(caveRow[j - 1]);
            if (j < caveRow.length - 1) neighbours.push(caveRow[j + 1]);
            if (i > 0) neighbours.push(caveMatrix[i - 1][j]);
            if (i < caveMatrix.length - 1) neighbours.push(caveMatrix[i + 1][j]);

            if (neighbours.filter(neighbourHeight => neighbourHeight <= currHeight).length == 0) totalRisk += currHeight + 1;
        }
    }   
    return totalRisk;
}

function getBasinsMultiplied(caveMatrix) {
    const getUniqueIndex = (row, col) => row * caveMatrix[0].length + col;

    let basinMap = new Map();
    let idBasinCounter = 0;
    for (let i = 0; i < caveMatrix.length; i++) {
        for (let j = 0; j < caveMatrix[i].length; j++) {
            if (caveMatrix[i][j] == 9) continue;

            let currBasinUp = -1;
            if (i > 0 && caveMatrix[i - 1][j] != 9) {
                currBasinUp = basinMap.get(getUniqueIndex(i - 1, j));
                basinMap.set(getUniqueIndex(i, j), currBasinUp);
            }

            if (j == 0 || caveMatrix[i][j - 1] == 9) {
                if (currBasinUp == -1) basinMap.set(getUniqueIndex(i, j), idBasinCounter++);
                continue;
            }

            let currBasinLeft = -1;
            if (j > 0 && caveMatrix[i][j - 1] != 9) {
                currBasinLeft = basinMap.get(getUniqueIndex(i, j - 1));
                if (currBasinUp == -1) {
                    basinMap.set(getUniqueIndex(i, j), currBasinLeft);
                } else if (currBasinLeft != currBasinUp) {
                    let tempJ = j - 1;
                    while (tempJ >= 0 && caveMatrix[i][tempJ] != 9) {
                        const indexElementAbove = getUniqueIndex(i - 1, tempJ);
                        if (basinMap.has(indexElementAbove)) {
                            basinMap.forEach((basin, index) => { if (basin == currBasinUp) basinMap.set(index, basinMap.get(indexElementAbove))})
                            break;
                        }

                        basinMap.set(getUniqueIndex(i, tempJ--), currBasinUp);
                    }
                }
            }

            if (currBasinUp == -1 && currBasinLeft == -1) basinMap.set(getUniqueIndex(i, j), idBasinCounter++);
        }
    }

    let basinCountMap = new Map();
    basinMap.forEach(function (basin, index) {
        basinCountMap.set(basin, basinCountMap.has(basin) ? basinCountMap.get(basin) + 1 : 1);
    })

    let basinsMultiplied = 1;
    const basinCountEntries = new Map([...basinCountMap.entries()].sort((a, b) => b[1] - a[1])).entries();
    for (let i = 0; i < 3; i++) basinsMultiplied *= basinCountEntries.next().value[1];
    return basinsMultiplied;
}