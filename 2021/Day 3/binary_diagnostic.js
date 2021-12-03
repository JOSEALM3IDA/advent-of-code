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
            if (line.charAt(j) == '1') {
                bitCountMap.set(j, currCount == undefined ? 1 : currCount + 1);
                continue;
            }

            if (currCount == undefined) bitCountMap.set(j, 0);
        } 
    }

    return new Map([...bitCountMap.entries()].sort((a,b) => a - b));
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

function filterLinesToSingleLine(lines, charToPrioritize, charElse) {
    const compareBiggerOrEqual = (val1, val2, retIfTrue, retElse) => { return val1 < val2 ? retIfTrue : retElse; }

    let i = 0;
    let filteredLines = lines;
    while (filteredLines.length > 1) {
        let bitCountMap = updateBitCountMap(filteredLines);

        let mostPresentBit = compareBiggerOrEqual(bitCountMap.get(i), filteredLines.length / 2, charToPrioritize, charElse);
        filteredLines = filteredLines.filter(item => item.charAt(i) == mostPresentBit);
        i++;
    }

    return filteredLines[0];
}

function calculateLifeSupport(lines) {
    let oxygenBinary = filterLinesToSingleLine(lines, '1', '0');
    let co2Binary = filterLinesToSingleLine(lines, '0', '1');

    let oxygen = parseInt(oxygenBinary, 2);
    let co2 = parseInt(co2Binary, 2);
    return oxygen * co2;
}