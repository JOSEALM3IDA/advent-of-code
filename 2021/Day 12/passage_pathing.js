var fs = require('fs');
fs.readFile(__dirname + "/input.txt", (error, data) => {
    if (error) throw error;
    
    const lines = data.toString().split("\r\n");

    const adjRtn = getAdjacencyMapAndMatrix(lines);
    const adjColNames = adjRtn[0];
    const adjMatrix = adjRtn[1];

    printAdjMatrix(adjColNames, adjMatrix);

    console.log("Part 1: " + getAllPaths(adjColNames, adjMatrix, false));
    console.log("Part 2: " + getAllPaths(adjColNames, adjMatrix, true));
});

function printAdjMatrix(adjColNames, adjMatrix) {
    console.log("\t" + adjColNames.join("\t"));

    for (let i = 0; i < adjMatrix.length; i++) {
        console.log(adjColNames[i] + "\t" + adjMatrix[i].join("\t"));
    }
}

function getNeighbours(node, adjColNames, adjMatrix) {
    const nodeCol = adjColNames.findIndex(e => e == node);
    if (nodeCol == -1) {
        console.log("Node not found: " + node);
        return [];
    }
    const nodeAdjacencies = adjMatrix[nodeCol];

    const neighbours = [];
    for (let i = 0; i < nodeAdjacencies.length; i++) if (i != nodeCol && nodeAdjacencies[i] == 1) neighbours.push(adjColNames[i]);
    return neighbours;
}

function getAdjacencyMapAndMatrix(lines) {
    const adjMatrix = [];
    const adjColNames = [];

    const addEntry = (element) => {
        adjColNames.push(element);
        for (let k = 0; k < adjMatrix.length; k++) adjMatrix[k] = [...adjMatrix[k], 0];
        const newEntry = new Array(adjMatrix.length + 1).fill(0);
        newEntry[newEntry.length - 1] = 1;
        adjMatrix.push(newEntry);
    };

    for (let i = 0; i < lines.length; i++) {
        const elements = lines[i].split("-");
        const leftElement = elements[0];
        const rightElement = elements[1];

        if (!adjColNames.includes(leftElement)) addEntry(leftElement);
        if (!adjColNames.includes(rightElement)) addEntry(rightElement);

        const leftElementCol = adjColNames.findIndex(element => element == leftElement);
        const rightElementCol = adjColNames.findIndex(element => element == rightElement);
        adjMatrix[leftElementCol][rightElementCol] = adjMatrix[rightElementCol][leftElementCol] = 1;
    }

    return [adjColNames, adjMatrix];
}

function isSmallCave(cave) {
    return cave === cave.toLowerCase();
}

function includesSmallCaveTwice(path) {
    const uniqueNodes = new Set(path);

    let hasSmallCaveDuplicates = false;
    path.forEach(node => {
        if (node == "start" || node == "end" || !isSmallCave(node)) return;

        if (!uniqueNodes.has(node)) {
            hasSmallCaveDuplicates = true;
            return node;
        }
        uniqueNodes.delete(node);
    });

    return hasSmallCaveDuplicates;
}

function getAllPaths(adjColNames, adjMatrix, mayIncludeSmallCaveTwice) {
    const toSearch = [["start"]];
    const foundPaths = [];
    
    while (toSearch.length > 0) {
        const currPath = toSearch.pop();
        const currNode = currPath.at(-1);

        if (currNode == "end") {
            foundPaths.push(currPath);
            continue;
        }

        getNeighbours(currNode, adjColNames, adjMatrix).forEach(neighbour => {
            if (neighbour == 'start') return;
            if (isSmallCave(neighbour) && currPath.includes(neighbour) && (!mayIncludeSmallCaveTwice || includesSmallCaveTwice(currPath))) return;
            toSearch.push([...currPath, neighbour]);
        });
    }

    return foundPaths.length;
}