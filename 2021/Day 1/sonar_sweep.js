var fs = require('fs');
fs.readFile(__dirname + "/input.txt", (error, data) => {
    if (error) {
        throw error;
    }
    
    const lines = data.toString().split("\r\n").map(Number);
    console.log("Part 1: " + countDepthWindowIncreases(lines, 1));
    console.log("Part 2: " + countDepthWindowIncreases(lines, 3));
});

function countDepthWindowIncreases(lines, windowSize) {
    let count = 0;
    for (let i = windowSize; i < lines.length; i++) {
        if (lines[i] > lines [i - windowSize]) count++;
    }

    return count;
}