var fs = require('fs');
fs.readFile(__dirname + "/input.txt", (error, data) => {
    if (error) throw error;
    
    const positions = data.toString().split(",").map(Number).sort((a, b) => a - b);
    console.log("Part 1: " + getIdealFuelCostConstant(positions));
    console.log("Part 2: " + getIdealFuelCostLinear(positions));
});

function getMedians(nums) {
    return nums.length % 2 == 0 ? [nums[nums.length / 2 - 1], nums[Math.floor(nums.length / 2)]] : nums[Math.floor(nums.length / 2)];
}

function getIdealFuelCostConstant(positions) {
    const possibleIdealPoints = getMedians(positions);

    let totalFuelCosts = [0, 0];
    for (let i = 0; i < positions.length; i++) {
        totalFuelCosts[0] += Math.abs(positions[i] - possibleIdealPoints[0]);
        if (possibleIdealPoints.length == 2) totalFuelCosts[1] += Math.abs(positions[i] - possibleIdealPoints[1]);
    }
    
    return totalFuelCosts.length == 1 ? totalFuelCosts[0] : totalFuelCosts[0] < totalFuelCosts[1] ? totalFuelCosts[0] : totalFuelCosts[1];
}

function getAverages(nums) {
    const sum = nums.reduce((a, b) => a + b, 0);
    return [(Math.floor(sum / nums.length)) || 0, (Math.ceil(sum / nums.length)) || 0];
}

function getIdealFuelCostLinear(positions) {
    const possibleIdealPoints = getAverages(positions);

    let totalFuelCostFloor = 0;
    let totalFuelCostCeil = 0;
    for (let i = 0; i < positions.length; i++) {
        let distance = Math.abs(positions[i] - possibleIdealPoints[0]);
        totalFuelCostFloor += distance * (distance + 1) / 2;

        distance = Math.abs(positions[i] - possibleIdealPoints[1]);
        totalFuelCostCeil += distance * (distance + 1) / 2;
    }

    return totalFuelCostFloor <= totalFuelCostCeil ? totalFuelCostFloor : totalFuelCostCeil;
}