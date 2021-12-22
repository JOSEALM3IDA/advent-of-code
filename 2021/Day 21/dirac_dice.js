const DURAC_DICE_OUTCOME_WEIGTHS = [1, 3, 6, 7, 6, 3, 1];

var fs = require('fs');
fs.readFile(__dirname + "/input.txt", (error, data) => {
    if (error) throw error;
    
    const lines = data.toString().split("\r\n");
    const initPositions = lines.map(line => parseInt(line.slice(28)));
    
    console.log("Part 1: " + playGameDeterministic(initPositions));
    console.log("Part 2: " + playGameDirac(initPositions));
});

class DeterministicDice {
    numRolls = 1;

    roll() {
        let roll = (this.numRolls++) % 100;
        return roll == 0 ? 100 : roll;
    }

    getNumRolls() {
        return this.numRolls - 1;
    }
}

function playGameDeterministic(positions) {
    positions = positions.slice();
    let dice = new DeterministicDice();
    let points = [0, 0];
    let curr = 0;
    while (true) {
        let rollSum = dice.roll() + dice.roll() + dice.roll();
        let newPos = (rollSum + positions[curr]) % 10;
        if (newPos == 0) newPos = 10;
        positions[curr] = newPos;
        points[curr] += newPos;
        if (points[curr] >= 1000) break;

        curr = (curr + 1) % 2; // Change player
    }

    return points[(curr + 1) % 2] * dice.getNumRolls();
}

function playGameDirac(positions) {
    let wins = [0, 0];
    for (let i = 3; i <= 9; i++) {
        const subWins = playRoundDirac(positions, [0, 0], 0, i);
        let weight = DURAC_DICE_OUTCOME_WEIGTHS[i - 3];
        wins[0] += weight * subWins[0];
        wins[1] += weight * subWins[1];
    }

    return wins[0] > wins[1] ? wins[0] : wins[1];
}

function playRoundDirac(positions, scores, currPlayer, rollSum) {
    const positionsCopy = positions.slice();
    const scoresCopy = scores.slice();

    let newPos = (rollSum + positionsCopy[currPlayer]) % 10;
    if (newPos == 0) newPos = 10;
    
    positionsCopy[currPlayer] = newPos;
    scoresCopy[currPlayer] += newPos;

    const wins = [0, 0];
    if (scoresCopy[currPlayer] >= 21) {
        wins[currPlayer] = 1;
        return wins;
    }

    for (let i = 3; i <=     9; i++) {
        const subWins = playRoundDirac(positionsCopy, scoresCopy, (currPlayer + 1) % 2, i);
        let weight = DURAC_DICE_OUTCOME_WEIGTHS[i - 3];
        wins[0] += weight * subWins[0];
        wins[1] += weight * subWins[1];
    }
    return wins;
}