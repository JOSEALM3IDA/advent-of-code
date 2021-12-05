var fs = require('fs');
fs.readFile(__dirname + "/input.txt", (error, data) => {
    if (error) throw error;
    
    const lines = data.toString().split("\r\n");
    const drawOrder = lines[0].split(",").map(Number);
    let boardArray = [];
    let currBoard = [];
    for (let i = 2; i < lines.length; i++) {
        let currLine = lines[i].trim().split(/[ ]+/);
        if (currLine.length == 1 && currLine[0] == '') {
            boardArray.push(currBoard);
            currBoard = [];
            continue;
        }

        currBoard.push(currLine.map(Number));
    }

    if (currBoard.length != 0) boardArray.push(currBoard);

    console.log("Part 1: " + getFirstWinnerBingo(drawOrder, boardArray));
    console.log("Part 2: " + getLastWinnerBingo(drawOrder, boardArray));
});

function checkArray(array) {
    return array.every((e) => e == -1);
}

// Returns -1 if nobody wins. 
// If somebody wins and exitOnWinnerFound is set to true, returns the winner board index. Else, returns the score of said winner
function drawNumber(boardArray, number, exitOnWinnerFound, winnersSet) {
    let winner = -1;
    let lastWinnerScore = -1;
    for (let b = 0; b < boardArray.length; b++) {
        let board = boardArray[b];
        for (let r = 0; r < board.length; r++) {
            let row = board[r];
            let c = 0;
            for (c = 0; c < row.length; c++) {
                if (row[c] != number) continue;

                row[c] = -1;
                if (!checkArray(row) && !checkArray(board.map(x => x[c]))) break;

                if (exitOnWinnerFound) return b;

                if (winnersSet.has(b)) break;

                winner = b;
                winnersSet.add(b);
                lastWinnerScore = calcWinnerScore(boardArray[winner], number);
                break;
            }

            if (c != row.length) break;
        }
    }

    return lastWinnerScore;
}

function calcWinnerScore(winnerBoard, lastDraw) {
    let sum = 0;
    for (let r = 0; r < winnerBoard.length; r++) {
        let row = winnerBoard[r];
        for (let c = 0; c < row.length; c++) {
            let num = row[c];
            if (num != -1) sum += num;
        }
    }

    return sum * lastDraw;
}

function getFirstWinnerBingo(drawOrder, boardArray) {
    let winner = -1;
    let lastDraw = -1;
    for (let i = 0; i < drawOrder.length; i++){
        lastDraw = drawOrder[i];
        winner = drawNumber(boardArray, lastDraw, true);
        if (winner != -1) break;
    }

    return winner == -1 ? -1 : calcWinnerScore(boardArray[winner], lastDraw);
}

function getLastWinnerBingo(drawOrder, boardArray) {
    let lastDraw = -1;
    let lastWinnerScore = -1;
    const winnersSet = new Set();
    for (let i = 0; i < drawOrder.length; i++){
        lastDraw = drawOrder[i];
        const currWinnerScore = drawNumber(boardArray, lastDraw, false, winnersSet);
        if (currWinnerScore != -1) lastWinnerScore = currWinnerScore;
    }

    return lastWinnerScore;
}