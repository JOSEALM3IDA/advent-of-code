let NUM_COLS = 0;
let NUM_ROWS = 0;
let caveMatrix = [];

var fs = require('fs');
const { start } = require('repl');
fs.readFile(__dirname + "/input.txt", (error, data) => {
    if (error) throw error;
    
    const lines = data.toString().split("\r\n");
    NUM_COLS = lines[0].length;
    NUM_ROWS = lines.length;

    fillCaveMatrix(getRiskMatrix(lines, false))
    caveMatrix.forEach(node => node.addNeighbours());
    console.log("Part 1: " + getLowestRisk());

    caveMatrix = [];
    fillCaveMatrix(getRiskMatrix(lines, true), true);
    caveMatrix.forEach(node => node.addNeighbours());

    console.log("Part 2: " + getLowestRisk());
});

function getRiskMatrix(lines, isFullMap) {
    let riskMatrix = lines.map(x => x.split("").map(Number));
    if (!isFullMap) return riskMatrix;

    const fullNumRows = NUM_ROWS * 5;
    const fullNumCols = NUM_COLS * 5;

    for (var r = 0; r < NUM_ROWS; r++) {
        for (let c = NUM_COLS; c < fullNumCols; c++) {
            let currRisk = riskMatrix[r][c - NUM_COLS] + 1;
            if (currRisk > 9) currRisk = 1;
            riskMatrix[r].push(currRisk);
        }
    }

    for (; r < fullNumRows; r++) {
        const rowToPush = [];
        for (var c = 0; c < fullNumCols; c++) {
            let currRisk = riskMatrix[r - NUM_ROWS][c] + 1;
            if (currRisk > 9) currRisk = 1;
            rowToPush.push(currRisk);
        }

        riskMatrix.push(rowToPush);
    }

    NUM_COLS = fullNumCols;
    NUM_ROWS = fullNumRows;
    return riskMatrix;
}

function fillCaveMatrix(riskMatrix) {
    for (var r = 0; r < NUM_ROWS; r++) {
        for (var c = 0; c < NUM_COLS; c++) {
            caveMatrix.push(new Node(r, c, riskMatrix[r][c]));
        }
    }
}

function getIndexOf(row, col) {
    return row * NUM_COLS + col;
}


function removeFromArray(array, node) {
    for (let i = array.length - 1; i >= 0; i--) {
        if (array[i] == node) array.splice(i, 1);
    }
}

class Node {
    constructor(row, col, cost) {
        this.row = row;
        this.col = col;
        this.cost = cost;
        this.g = 0;
        this.h = 0;
        this.f = 0;
    }

    addNeighbours() {
        this.neighbours = [];
        if (this.row > 0) this.neighbours.push(caveMatrix[getIndexOf(this.row - 1, this.col)]);
        if (this.row < NUM_ROWS - 1) this.neighbours.push(caveMatrix[getIndexOf(this.row + 1, this.col)]);
        if (this.col > 0) this.neighbours.push(caveMatrix[getIndexOf(this.row, this.col - 1)]);
        if (this.col < NUM_COLS - 1) this.neighbours.push(caveMatrix[getIndexOf(this.row, this.col + 1)]);
    }

    distanceTo(node) {
        return Math.abs(node.row - this.row) + Math.abs(node.col - this.col);
    }
}

function getLowestRisk() {
    const startNode = caveMatrix[0];
    const endNode = caveMatrix[getIndexOf(NUM_ROWS - 1, NUM_COLS - 1)];

    startNode.addNeighbours();
    let openSet = [startNode];
    let closedSet = [];

    while (openSet.length > 0) {
        let current = openSet[0];
        for (let i = 1; i < openSet.length; i++) {
            if (openSet[i].g < current.g) current = openSet[i];
        }

        removeFromArray(openSet, current)
        closedSet.push(current);

        if (current == endNode) return current.g;

        let neighbours = current.neighbours;
        for (let i = 0; i < neighbours.length; i++) {
            let neighbour = neighbours[i];

            if (closedSet.includes(neighbour)) continue;

            let tentativeG = current.g + neighbour.cost;

            if (openSet.includes(neighbour)) {
                if (tentativeG < neighbour.g) neighbour.g = tentativeG;
                continue;
            }

            neighbour.g = tentativeG;
            openSet.push(neighbour)

            neighbour.h = neighbour.distanceTo(endNode);
            neighbour.f = neighbour.g + neighbour.h;
        }
    }
    
    return -1;
}