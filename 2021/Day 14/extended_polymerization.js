var fs = require('fs');
fs.readFile(__dirname + "/input.txt", (error, data) => {
    if (error) throw error;
    
    const lines = data.toString().split("\r\n");

    const pairRulesMap = new Map();
    lines.slice(2).forEach(line => {
        const split = line.split(" -> ");
        pairRulesMap.set(split[0], split[1]);
    });

    console.log("Part 1: " + getMostMinusLeast(lines[0], pairRulesMap, 10));
    console.log("Part 2: " + getMostMinusLeast(lines[0], pairRulesMap, 40));
});


function getMostMinusLeast(initialPolymerLine, pairRulesMap, numSteps) {
    const firstAtom = initialPolymerLine[0];
    const lastAtom = initialPolymerLine[initialPolymerLine.length - 1];

    // Read initial polymer into map (key is pair, value is count)
    const initialPolymerMap = new Map();
    initialPolymerLine.split("").reduce((result, value, index, array) => {
        if (index == array.length - 1) return result;
        result.push(array.slice(index, index + 2).join(""));
        return result;
    }, []).forEach(pair => {
        initialPolymerMap.set(pair, initialPolymerMap.has(pair) ? initialPolymerMap.get(pair) + 1 : 1);
    });

    let polymerMap = initialPolymerMap;
    for (let i = 0; i < numSteps; i++) {
        let currStepPolymer = new Map();
        polymerMap.forEach((count, pair) => {
            let isCurrentPairInRules = false;
            pairRulesMap.forEach((atom, rule) => {
                if (isCurrentPairInRules || rule != pair) return;
                isCurrentPairInRules = true; // Should break after this is done, but I don't want to refactor
                const firstPair = [pair[0], atom].join("");
                const secondPair = [atom, pair[1]].join("");
                currStepPolymer.set(firstPair, currStepPolymer.has(firstPair) ? currStepPolymer.get(firstPair) + count : count);
                currStepPolymer.set(secondPair, currStepPolymer.has(secondPair) ? currStepPolymer.get(secondPair) + count : count);
            });

            if (!isCurrentPairInRules) currStepPolymer.set(pair, count);
        });

        polymerMap = currStepPolymer;
    }

    let atomCountMap = new Map();
    polymerMap.forEach((count, pair) => pair.split("").forEach(atom => atomCountMap.set(atom, atomCountMap.has(atom) ? atomCountMap.get(atom) + count/2 : count/2)))
    atomCountMap.set(firstAtom, atomCountMap.get(firstAtom) + 0.5);
    atomCountMap.set(lastAtom, atomCountMap.get(lastAtom) + 0.5);

    let biggest = -1;
    let smallest = -1;
    atomCountMap.forEach(count => {
        if (biggest == -1 || count > biggest) biggest = count;
        if (smallest == -1 || count < smallest) smallest = count;
    });

    return biggest - smallest;
}