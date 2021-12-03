var fs = require('fs');
fs.readFile(__dirname + "/input.txt", (error, data) => {
    if (error) {
        throw error;
    }
    
    const lines = data.toString().split("\r\n");
    console.log("Part 1: " + calculatePowerConsumption(lines));
    console.log("Part 2: " + calculateLifeSupport(lines));
});

function updateBitCountMap(lines) {
    let bitCountMap = new Map();
    for (let i = 0; i < lines.length; i++) {
        let line = lines[i];
        for (let j = 0; j < line.length; j++) {
            let currCount = bitCountMap.get(j);
            if (lines[i].charAt(j) != '1') {
                if (currCount == undefined) {
                    bitCountMap.set(j, 0);
                }
                continue;
            }

            bitCountMap.set(j, currCount == undefined ? 1 : currCount + 1);
        } 
    }

    bitCountMap = new Map([...bitCountMap.entries()].sort((a,b) => a - b));
    return bitCountMap;
}

function calculatePowerConsumption(lines) {
    let numLines = lines.length;
    let bitCountMap = updateBitCountMap(lines);

    let gammaBinary = "";
    for (let value of bitCountMap.values()) {
        let mostPresentBit = value > numLines / 2 ? '1' : '0';
        gammaBinary += mostPresentBit;
    }

    let epsilonBinary = "";
    for (let i = 0; i < gammaBinary.length; i++) {
        let currChar = gammaBinary.charAt(i);
        epsilonBinary += currChar == '0' ? '1' : '0';
    }

    let gamma = parseInt(gammaBinary, 2);
    let epsilon = parseInt(epsilonBinary, 2);
    return gamma * epsilon;
}

function calculateLifeSupport(lines) {
    let filteredLines = lines;
    let i = 0;
    while (filteredLines.length > 1) {
        let bitCountMap = updateBitCountMap(filteredLines);

        let mostPresentBit = bitCountMap.get(i) < filteredLines.length / 2 ? '0' : '1';
        filteredLines = filteredLines.filter(item => item.charAt(i) == mostPresentBit);
        i++;
    }

    let oxygenBinary = filteredLines[0];

    filteredLines = lines;
    i = 0;
    while (filteredLines.length > 1) {
        let bitCountMap = updateBitCountMap(filteredLines);

        let mostPresentBit = bitCountMap.get(i) >= filteredLines.length / 2 ? '0' : '1';
        filteredLines = filteredLines.filter(item => item.charAt(i) == mostPresentBit);
        i++;
    }

    let co2Binary = filteredLines[0];;

    let oxygen = parseInt(oxygenBinary, 2);
    let co2 = parseInt(co2Binary, 2);
    return oxygen * co2;
}