var fs = require('fs');
fs.readFile(__dirname + "/input.txt", (error, data) => {
    if (error) throw error;
    
    const lines = data.toString().split("\r\n");
    const parsedLines = [];
    for (let i = 0; i < lines.length; i++) {
        const points = lines[i].split(" -> ");
        const startPoint = new Point(points[0]);
        const endPoint = new Point(points[1]);
        parsedLines.push(new Line(startPoint, endPoint));
    }

    console.log("Part 1: " + numOverlappingLines(parsedLines, false));
    console.log("Part 2: " + numOverlappingLines(parsedLines, true));
});

class Line {
    constructor(startPoint, endPoint) {
        this.startPoint= startPoint;
        this.endPoint = endPoint;
    }
}

class Point {
    constructor(x, y) {
        if (y != undefined) {
            this.x = x;
            this.y = y;
            return;
        }

        let coords = x.split(",").map(Number);
        this.x = coords[0];
        this.y = coords[1];
    }

    getCoordsStr() {
        return this.x + ',' + this.y;
    }
}

function updatePointOverlapCount(pointOverlapMap, point) {
    const coordsStr = point.getCoordsStr();
    let currPointOverlaps = pointOverlapMap.get(coordsStr);
    if (currPointOverlaps == undefined) currPointOverlaps = 0;
    pointOverlapMap.set(coordsStr, ++currPointOverlaps);
    return pointOverlapMap;
}

function numOverlappingLines(lines, allowDiagonals) {
    let pointOverlapMap = new Map();
    
    for (let i = 0; i < lines.length; i++) {
        const currLine = lines[i];
        const startPoint = currLine.startPoint;
        const endPoint = currLine.endPoint;

        let currX = startPoint.x;
        let currY = startPoint.y;

        let endX = endPoint.x;
        let endY = endPoint.y;

        if (!allowDiagonals && currX != endX && currY != endY) continue;

        while (currX != endX || currY != endY) {
            const currPoint = new Point(currX, currY);

            pointOverlapMap = updatePointOverlapCount(pointOverlapMap, currPoint);

            if (currX > endX) currX--;
            else if (currX < endX) currX++;

            if (currY > endY) currY--;
            else if (currY < endY) currY++;
        }

        pointOverlapMap = updatePointOverlapCount(pointOverlapMap, endPoint);
    }

    const filteredMap = new Map([...pointOverlapMap].filter(([k, v]) => v > 1 ));
    return filteredMap.size;
}