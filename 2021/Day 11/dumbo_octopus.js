const NUM_COLS = 10;
const octopuses = [];

var fs = require('fs');
fs.readFile(__dirname + "/input.txt", (error, data) => {
    if (error) throw error;
    
    const lines = data.toString().split("\r\n");
    lines.forEach((line) => line.split("").map(Number).forEach((octopus) => octopuses.push(octopus)));
    console.log("Part 1: " + getNumberOfFlashes());

    octopuses.length = 0;
    lines.forEach((line) => line.split("").map(Number).forEach((octopus) => octopuses.push(octopus)));
    console.log("Part 2: " + getFirstSynchronizedStepNumber());
});

function getUniqueIndex(lin, col) {
    return lin * NUM_COLS + col;
}

function getNonFlashedNeighbours(flashedSet, octopus) {
    const lin = Math.floor(octopus / NUM_COLS);
    const col = octopus % NUM_COLS;

    const hasColBefore = col > 0;
    const hasLinBefore = lin > 0;
    const hasColAfter = col < NUM_COLS - 1;
    const hasLinAfter = lin < NUM_COLS - 1;

    const neighbours = [];
    if (hasLinBefore && hasColBefore) neighbours.push(getUniqueIndex(lin - 1, col - 1));

    if (hasLinBefore) neighbours.push(getUniqueIndex(lin - 1, col));

    if (hasColAfter && hasLinBefore) neighbours.push(getUniqueIndex(lin - 1, col + 1));

    if (hasColBefore) neighbours.push(getUniqueIndex(lin, col - 1));

    if (hasColAfter) neighbours.push(getUniqueIndex(lin, col + 1));

    if (hasLinAfter) {
        if (hasColBefore) neighbours.push(getUniqueIndex(lin + 1, col - 1));

        neighbours.push(getUniqueIndex(lin + 1, col));

        if (hasColAfter) neighbours.push(getUniqueIndex(lin + 1, col + 1));
    }

    return neighbours.filter((octopus) => !flashedSet.has(octopus));
}

function addEnergyToOctopus(flashedSet, octopus) {
    if (flashedSet.has(octopus)) return;

    if (++octopuses[octopus] > 9) flashOctopus(flashedSet, octopus);
}

function flashOctopus(flashedSet, octopus) {
    flashedSet.add(octopus);
    octopuses[octopus] = 0;

    const nonFlashedNeighbours = getNonFlashedNeighbours(flashedSet, octopus);
    nonFlashedNeighbours.forEach(neighbour => addEnergyToOctopus(flashedSet, neighbour));
}

function advanceStep() {
    const flashedSet = new Set();
    octopuses.forEach((energy, octopus) => addEnergyToOctopus(flashedSet, octopus));
    return flashedSet.size;
}

function getNumberOfFlashes() {
    let totalFlashes = 0;
    for (let step = 0; step < 100; step++) totalFlashes += advanceStep();

    return totalFlashes;
}

function getFirstSynchronizedStepNumber() {
    let numSteps = 0;
    while (octopuses.filter((energy) => energy == 0).length != octopuses.length) {
        advanceStep();
        numSteps++;
    }

    return numSteps;
}