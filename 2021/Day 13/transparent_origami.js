var fs = require('fs');
fs.readFile(__dirname + "/input.txt", (error, data) => {
    if (error) throw error;
    
    const lines = data.toString().split("\r\n");

    const points = lines.filter(line => Number.isInteger(parseInt(line[0]))).map(line => line.split(","));
    const folds = lines.filter(line => line.startsWith("fold")).map(line => line.slice(11).split("="));

    let pointMatrix = getPointMatrix(points);

    console.log("Part 1: " + getNumPointsAfterFold(pointMatrix, folds[0]));

    console.log("Part 2: ");
    pointMatrix = foldAll(pointMatrix, folds);
    pointMatrix.forEach(item => console.log(item.join(" ")));
});

function getPointMatrix(points) {
    const pointMatrix = [];
    for (let i = 0; i < points.length; i++) {
        const x = points[i][0];
        const y = points[i][1];

        while (pointMatrix.length <= y) pointMatrix.push(new Array(pointMatrix.length == 0 ? 0 : pointMatrix[0].length).fill('.'));
        
        for (let j = 0; j < pointMatrix.length; j++) {
            while (pointMatrix[j].length <= x) pointMatrix[j].push(".");
        }

        pointMatrix[y][x] = '#';
    }

    return pointMatrix;
}

function fold(pointMatrix, y) {
    let numLinesToFold = pointMatrix.length - y;
    for (let i = 0; i < numLinesToFold; i++) {
        for (let j = 0; j < pointMatrix[y - i].length; j++) {
            if (pointMatrix[y + i][j] == '#') pointMatrix[y - i][j] = '#';
        }
    }

    while (numLinesToFold-- > 0) pointMatrix.pop();
}

function transposeMatrix(matrix) {
    return matrix[0].map((_, colIndex) => matrix.map(row => row[colIndex])); // Cursed
}

function foldOnce(pointMatrix, foldInfo) {
    if (foldInfo[0] == 'x') {
        pointMatrix = transposeMatrix(pointMatrix);
        fold(pointMatrix, parseInt(foldInfo[1]));
        pointMatrix = transposeMatrix(pointMatrix);
        return pointMatrix;
    }

    if (foldInfo[0] == 'y') {
        fold(pointMatrix, parseInt(foldInfo[1]));
        return pointMatrix;
    }
}

function getNumPointsAfterFold(pointMatrix, foldInfo) {
    pointMatrix = foldOnce(pointMatrix, foldInfo);
    let count = 0;
    pointMatrix.forEach(line => count += (line.join("").match(/\#/g) || []).length);
    return count;
}

function foldAll(pointMatrix, folds) {
    for (let i = 0; i < folds.length; i++) {
        pointMatrix = foldOnce(pointMatrix, folds[i]);
    }

    return pointMatrix;
}