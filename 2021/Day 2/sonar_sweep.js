var fs = require('fs');
fs.readFile(__dirname + "/input.txt", (error, data) => {
    if (error) {
        throw error;
    }
    
    const lines = data.toString().split("\r\n");
    console.log("Part 1: " + calculateNewPosition(lines));
    console.log("Part 2: " + calculateNewPositionAim(lines));
});

function calculateNewPosition(lines) {
    let depth = 0;
    let horizontal = 0;
    for (let i = 0; i < lines.length; i++) {
        let lineSplit = lines[i].split(' ');

        let value = parseInt(lineSplit[1]);
        switch (lineSplit[0]) {
            case "forward":
                horizontal += value;
                break;
            case "up":
                depth -= value;
                break;
            case "down":
                depth += value;
                break;
        }
    }
    
    return depth * horizontal;
}

function calculateNewPositionAim(lines) {
    let depth = 0;
    let horizontal = 0;
    let aim = 0;
    for (let i = 0; i < lines.length; i++) {
        let lineSplit = lines[i].split(' ');

        let value = parseInt(lineSplit[1]);
        switch (lineSplit[0]) {
            case "forward":
                horizontal += value;
                depth += aim * value;
                break;
            case "up":
                aim -= value;
                break;
            case "down":
                aim += value;
                break;
        }
    }

    return depth * horizontal;
}