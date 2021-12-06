var fs = require('fs');
fs.readFile(__dirname + "/input.txt", (error, data) => {
    if (error) throw error;
    
    const ages = data.toString().split(",").map(Number);
    const ageMap = new Map();

    ages.forEach((e) => { ageMap.set(e, ageMap.has(e) ? ageMap.get(e) + 1 : 1); });

    const sortedAgeMap = new Map([...ageMap.entries()].sort());
    console.log("Part 1: " + simulateLanternfishPopulation(sortedAgeMap, 80));
    console.log("Part 2: " + simulateLanternfishPopulation(sortedAgeMap, 256));
});

function simulateLanternfishPopulation(ageMap, days) {
    for (let d = 0; d < days; d++) {
        let mapAfterDay = new Map();
        ageMap.forEach((value, key) => {
            if (value == 0) return;

            if (key == 0) {
                mapAfterDay.set(8, value);
                mapAfterDay.set(6, value);
                return;
            }

            const newKey = key - 1;
            mapAfterDay.set(newKey, mapAfterDay.has(newKey) ? mapAfterDay.get(newKey) + value : value);
        });

        ageMap = new Map([...mapAfterDay.entries()].sort());
    }
    
    let sum = 0;
    ageMap.forEach((value, key) => { sum += value; });

    return sum;
}