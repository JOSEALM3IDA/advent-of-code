const OPENING_CHARS = ['(', '[', '{', '<'];
const CLOSING_CHARS = [')', ']', '}', '>'];
const ERROR_SCORE_MAP = new Map().set(')', 3).set(']', 57).set('}', 1197).set('>', 25137);
const AUTOCOMPLETE_SCORE_MAP = new Map().set(')', 1).set(']', 2).set('}', 3).set('>', 4);

var fs = require('fs');
fs.readFile(__dirname + "/input.txt", (error, data) => {
    if (error) throw error;
    
    const lines = data.toString().split("\r\n");

    console.log("Part 1: " + getCorruptedSum(lines));
    console.log("Part 2: " + getAutocompleteSum(lines));
});

function getCorruptedSum(lines) {
    let errorSum = 0;
    for (let i = 0; i < lines.length; i++) {
        const line = lines[i].split("");
        const charStack = [];
        for (let j = 0; j < line.length; j++) {
            const currChar = line[j];
            if (OPENING_CHARS.includes(currChar)) {
                charStack.push(currChar);
                continue;
            }

            const idxOpen = OPENING_CHARS.indexOf(charStack.pop());
            const idxClose = CLOSING_CHARS.indexOf(currChar);

            if (idxOpen != idxClose) {
                errorSum += ERROR_SCORE_MAP.get(currChar);
                break;
            }
        }
    }

    return errorSum;
}

function getAutocompleteSum(lines) {
    const scoreArray = [];
    for (let i = 0; i < lines.length; i++) {
        let line = lines[i].split("");
        const charStack = [];
        let isCorrupted = false;
        for (let j = 0; j < line.length; j++) {
            const currChar = line[j];
            if (OPENING_CHARS.includes(currChar)) {
                charStack.push(currChar);
                continue;
            }

            const idxOpen = OPENING_CHARS.indexOf(charStack.pop());
            const idxClose = CLOSING_CHARS.indexOf(currChar);

            if (idxOpen != idxClose) {
                isCorrupted = true;
                break;
            }
        }

        if (isCorrupted) continue;

        let charToClose;
        let autocompleteSum = 0;
        while ((charToClose = charStack.pop()) != undefined) {
            const idxClose = OPENING_CHARS.indexOf(charToClose);
            autocompleteSum *= 5;
            autocompleteSum += AUTOCOMPLETE_SCORE_MAP.get(CLOSING_CHARS[idxClose]);
        }

        scoreArray.push(autocompleteSum);
    }

    scoreArray.sort((a, b) => a - b);
    return scoreArray[Math.floor(scoreArray.length/2)];
}