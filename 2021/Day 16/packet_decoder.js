const { assert } = require('console');
var fs = require('fs');
const { version, type } = require('os');
fs.readFile(__dirname + "/input.txt", (error, data) => {
    if (error) throw error;
    
    const bits = hexToBin(data.toString().split("\r\n")[0]);  
    
    const parsedPacketInfo = parsePacket(bits);
    console.log("Part 1: " + parsedPacketInfo[0][0].verSum);
    console.log("Part 2: " + parsedPacketInfo[0][0].value);
});

function hexToBin(hex) {
    for (var bits = [], c = 0; c < hex.length; c += 2) {
        let currBits = (parseInt(hex.substr(c, 2), 16) >>> 0).toString(2);
        while (currBits.length != 8) currBits = '0' + currBits;
        bits.push(currBits);
    }

    return bits.join("");
}

function binToInt(bits) {
    return parseInt(bits.join(""), 2);
}

class Packet {
    constructor(ver, typeId, value, verSum, len, children) {
        this.ver = ver;
        this.typeId = typeId;
        this.value = value;
        this.verSum = verSum;
        if (len != undefined) this.len = len;
        if (children != undefined) this.children = children;
    }
}

function parsePacket(bits, numPackets) {
    let curr = 0;
    let children = [];
    while (curr < bits.length) {
        if (bits.length - curr < 8) break;

        if (numPackets != undefined) {
            if (numPackets == 0) break;
            numPackets--;
        }

        const verBits = [];
        for (let i = 0; i < 3; i++) verBits.push(bits[curr++]);
        const ver = binToInt(verBits);

        const packetTypeIdBits = [];
        for (let i = 0; i < 3; i++) packetTypeIdBits.push(bits[curr++]);
        const packetTypeId = binToInt(packetTypeIdBits);

        if (packetTypeId == 4) {
            const parsedLiteral = parseLiteralValue(bits.slice(curr));
            const value = parsedLiteral[1];
            curr += parsedLiteral[0];
            children.push(new Packet(ver, packetTypeId, value, ver));
            continue;
        }

        const lenLen = bits[curr++] == 1 ? 11 : 15;
        const lenBits = [];
        for (let i = 0; i < lenLen; i++) lenBits.push(bits[curr++]);
        const len = binToInt(lenBits);

        const parsedPacket = lenLen == 11 ? parsePacket(bits.slice(curr), len) : parsePacket(bits.slice(curr, curr + len));
        let verSum = ver;
        for (let i = 0; i < parsedPacket[0].length; i++) verSum += parsedPacket[0][i].verSum;
        curr += parsedPacket[1];

        const value = calculateValue(packetTypeId, parsedPacket[0]);
        children.push(new Packet(ver, packetTypeId, value, verSum, len, parsedPacket[0]))
    }

    return [children, curr];
}

function parseLiteralValue(bits) {
    let valueBits = [];
    let exit = -1;
    for (var curr = 0; curr < bits.length; curr++) {
        if (curr % 5 == 0) {
            if (bits[curr] == 0) exit = 3;
            continue;
        }

        valueBits.push(bits[curr]);
        if (exit == 0) break;
        else if (exit != -1) exit--;
    }

    return [curr + 1, binToInt(valueBits)];
}

function calculateValue(packetTypeId, children) {
    let value = -1;
    switch (packetTypeId) {
        case 0:
            value = 0;
            for (let i = 0; i < children.length; i++) value += children[i].value;
            break;

        case 1:
            value = 1;
            for (let i = 0; i < children.length; i++) value *= children[i].value;
            break;

        case 2:
            value = children.reduce((prev, curr) => prev.value < curr.value ? prev : curr).value;
            break;

        case 3:
            value = children.reduce((prev, curr) => prev.value > curr.value ? prev : curr).value;
            break;

        case 5:
            value = children[0].value > children[1].value ? 1 : 0;
            break;

        case 6:
            value = children[0].value < children[1].value ? 1 : 0;
            break;

        case 7:
            value = children[0].value == children[1].value ? 1 : 0;
            break;
    }

    return value;
}