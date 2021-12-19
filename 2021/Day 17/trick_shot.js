var fs = require('fs');
fs.readFile(__dirname + "/input.txt", (error, data) => {
    if (error) throw error;
    
    const xyRange = data.toString().slice(13).split(', ');
    xyRange.forEach((range, index) => xyRange[index] = xyRange[index].slice(2).split('..').map(Number));
    
    console.log("Part 1: " + calcMaxPos(-xyRange[1][0] - 1));
    console.log("Part 2: " + getNumPossibleVelocities(xyRange, getHorizontalVelocities(xyRange), getVerticalVelocities(xyRange)));
});

function calcMaxPos(vel) {
    if (vel <= 0) return 0;
    return (vel + 1) * vel / 2;
}

function getNumPossibleVelocities(target, horizontalVelocities, verticalVelocities) {
    let count = 0;
    for (let i = 0; i < horizontalVelocities.length; i++) {
        for (let j = 0; j < verticalVelocities.length; j++) {
            if (checkHit(target, horizontalVelocities[i], verticalVelocities[j])) count++;
        }
    }

    return count;
}

function getHorizontalVelocities(target) {
    let x1 = target[0][0];
    let x2 = target[0][1];

    let velocities = [];
    for (let x = x1; x <= x2; x++) {
        for (let n = 1; n <= x2; n++) {
            let v0 = n/2 - 1/2 - x/-n;
            if (v0 % 1 == 0 && !velocities.includes(v0)) velocities.push(v0);
        }
    }

    return velocities;
}

function getVerticalVelocities(target) {
    let y1 = target[1][0];
    let velocities = [];
    for (let v0 = y1; v0 < -y1; v0++) velocities.push(v0);
    return velocities;
}

function checkHit(target, vx, vy) {
    let x1 = target[0][0];
    let x2 = target[0][1];
    let y1 = target[1][0];
    let y2 = target[1][1];

    let currXPos = 0;
    let currYPos = 0;
    for (let iter = 1; currXPos <= x2 && currYPos >= y1; iter++, vx--, vy--) {
        if (vx > 0) currXPos += vx;
        currYPos += vy;
        
        if (currXPos < x1 || currXPos > x2) continue;
        if (currYPos < y1 || currYPos > y2) continue;

        return true;
    }

    return false;
}