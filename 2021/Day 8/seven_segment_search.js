/*
  0:      1:      2:      3:      4:
 aaaa    ....    aaaa    aaaa    ....
b    c  .    c  .    c  .    c  b    c
b    c  .    c  .    c  .    c  b    c
 ....    ....    dddd    dddd    dddd
e    f  .    f  e    .  .    f  .    f
e    f  .    f  e    .  .    f  .    f
 gggg    ....    gggg    gggg    ....

  5:      6:      7:      8:      9:
 aaaa    aaaa    aaaa    aaaa    aaaa
b    .  b    .  .    c  b    c  b    c
b    .  b    .  .    c  b    c  b    c
 dddd    dddd    ....    dddd    dddd
.    f  e    f  .    f  e    f  .    f
.    f  e    f  .    f  e    f  .    f
 gggg    gggg    ....    gggg    gggg
*/


const SEGS_1 = "cf";
const SEGS_2 = "acdeg";
const SEGS_3 = "acdfg";
const SEGS_4 = "bcdf";
const SEGS_5 = "abdfg";
const SEGS_6 = "abdefg";
const SEGS_7 = "acf";
const SEGS_8 = "abcdefg";
const SEGS_9 = "abcdfg";

var fs = require('fs');
fs.readFile(__dirname + "/input.txt", (error, data) => {
    if (error) throw error;
    
    const lines = data.toString().split("\r\n");

    console.log("Part 1: " + getAmountUniqueDigits(lines));
    console.log("Part 2: " + decodeAll(lines));
});

function getAmountUniqueDigits(lines) {
    let amount = 0;
    for (let i = 0; i < lines.length; i++) {
        const currOutput = lines[i].split(" | ")[1];
        const individualOutputs = currOutput.split(" ");
        for (let j = 0; j < individualOutputs.length; j++) {
            const indLen = individualOutputs[j].length;
            if (indLen == SEGS_1.length || indLen == SEGS_4.length || indLen == SEGS_7.length || indLen == SEGS_8.length) amount++;
        }
    }
    
    return amount;
}

function strIntersection(str1, str2) {
    return str1.split("").filter(x => str2.includes(x)).join("");
}

function strDifference(str, strToSubtract) {
    const intersection = strIntersection(str, strToSubtract);
    return str.split("").filter(x => !intersection.includes(x)).join("");
}

function decodeLine(line) {
    const split = line.split(" | ");
    const inputDigits = split[0].split(' ');
    const outputDigits = split[1].split(' ');

    // directly obtainable by length
    const digit1 = inputDigits.filter(segments => segments.length == 2)[0];
    const digit4 = inputDigits.filter(segments => segments.length == 4)[0];
    const digit7 = inputDigits.filter(segments => segments.length == 3)[0];
    const digit8 = inputDigits.filter(segments => segments.length == 7)[0];

    const segsCF = digit1;
    const segA = strDifference(digit7, digit1)[0];

    const digit3 = inputDigits.filter(segments => segments.length == 5 && segments.includes(segA) && segments.includes(segsCF[0]) && segments.includes(segsCF[1]))[0];
    const segsDG = strDifference(digit3, segsCF + segA);

    const digit9 = inputDigits.filter(segments => segments.length == 6 && segments.includes(segA) && segments.includes(segsCF[0]) && segments.includes(segsCF[1]) && segments.includes(segsDG[0]) && segments.includes(segsDG[1]))[0];
    const segB = strDifference(digit9, segA + segsCF + segsDG);

    const segD = strDifference(digit4, segsCF + segB);
    const segG = strDifference(segsDG, segD);

    const digit5 = inputDigits.filter(segments => segments.length == 5 && segments.includes(segA) && segments.includes(segB) && segments.includes(segD) && segments.includes(segG) && (segments.includes(segsCF[0]) || segments.includes(segsCF[1])))[0];
    const segF = digit5.includes(segsCF[0]) ? strDifference(digit5, segA + segB + segD + segG + segsCF[1]) : strDifference(digit5, segA + segB + segD + segG + segsCF[0]);

    const segC = strDifference(segsCF, segF);
    const segE = strDifference(digit8, segA + segB + segC + segD + segF + segG);

    const digit0 = inputDigits.filter(segments => segments.length == 6 && segments.includes(segA) && segments.includes(segB) && segments.includes(segC) && segments.includes(segE) && segments.includes(segF) && segments.includes(segG))[0];
    const digit2 = inputDigits.filter(segments => segments.length == 5 && segments.includes(segA) && segments.includes(segC) && segments.includes(segD) && segments.includes(segE) && segments.includes(segG))[0];
    const digit6 = inputDigits.filter(segments => segments.length == 6 && segments.includes(segA) && segments.includes(segB) && segments.includes(segD) && segments.includes(segE) && segments.includes(segF) && segments.includes(segG))[0];


    const digitStrings = [digit0, digit1, digit2, digit3, digit4, digit5, digit6, digit7, digit8, digit9];

    let encodedNumber = 0;
    for (let i = 0; i < outputDigits.length; i++) {
        encodedNumber *= 10;
        for (let n = 0; n < digitStrings.length; n++) {
            if (outputDigits[i].split("").sort().join("") != digitStrings[n].split("").sort().join("")) continue;

            encodedNumber += n;
            break;
        }
    }

    return encodedNumber;
}

function decodeAll(lines) {
    return lines.reduce((decodeSum, line) => decodeSum + decodeLine(line), 0);
}