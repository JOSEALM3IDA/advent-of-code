var fs = require('fs');
fs.readFile(__dirname + "/input.txt", (error, data) => {
    if (error) throw error;
    
    const lines = data.toString().split("\r\n");
    const algorithm = lines[0].split("");
    const trenchMap = lines.slice(2);
    trenchMap.forEach((line, idx, arr) => arr[idx] = line.split(""));
    
    console.log("Part 1: " + enhanceImage(algorithm, trenchMap, 2));
    console.log("Part 2: " + enhanceImage(algorithm, trenchMap, 50));
});

function enhanceImage(algorithm, trenchMap, numTimes) {
    trenchMap = addTrenchBorders(trenchMap);
    let outsideChar = '.';
    for (let i = 0; i < numTimes; i++) {
        const enhancedMap = [];
        for (let l = 0; l < trenchMap.length; l++) {
            enhancedMap.push(trenchMap[l].slice());
            for (let c = 0; c < trenchMap[0].length; c++) enhancedMap[l][c] = enhancePixel(algorithm, trenchMap, l, c, outsideChar);
        }
        
        outsideChar = algorithm[outsideChar == '.' ? 0 : algorithm.length - 1];
        trenchMap = addTrenchBorders(enhancedMap, outsideChar);
    }

    let pixelCount = 0;
    trenchMap.forEach(line => line.forEach(ch => { if (ch == '#') pixelCount++; }));
    return pixelCount;
}

function addTrenchBorders(trenchMap, outsideChar) {
    let addedLeft = false;
    let addedRight = false;
    for (let l = 0; l < trenchMap.length; l++) {
        if (addedLeft && addedRight) break;

        if (!addedLeft && trenchMap[l][0] != outsideChar) {
            trenchMap.forEach((line, idx, arr) => arr[idx] = [outsideChar , ...line]);
            addedLeft = true;
        }

        if (!addedRight && trenchMap[l][trenchMap[0].length - 1] != outsideChar) {
            trenchMap.forEach((line, idx, arr) => arr[idx] = [...line, outsideChar]);
            addedRight = true;
        }
    }

    let addedAbove = false;
    let addedBelow = false;
    for (let c = addedLeft ? 1 : 0; c < addedRight ? trenchMap[0].length - 1 : trenchMap[0].length; c++) {
        if (addedAbove && addedBelow) break;

        if (!addedAbove && trenchMap[0][c] == outsideChar) {
            trenchMap = [new Array(trenchMap[0].length).fill(outsideChar), ...trenchMap];
            addedAbove = true;
        }

        if (!addedBelow && trenchMap[trenchMap.length - 1][c] == outsideChar) {
            trenchMap = [...trenchMap, new Array(trenchMap[0].length).fill(outsideChar)];
            addedBelow = true;
        }
    }

    return trenchMap;
}

function enhancePixel(algorithm, trenchMap, lin, col, charOutside) {
    let pos = 0;
    for (let currL = lin - 1; currL <= lin + 1; currL++) {
        for (let currC = col - 1; currC <= col + 1; currC++) {
            pos = pos << 1;

            let currChar = currL == -1 || currL == trenchMap.length || currC == -1 || currC == trenchMap[0].length ? charOutside : trenchMap[currL][currC];
            if (currChar == '#') pos++;
        }
    }

    return algorithm[pos];
}