var fs = require('fs');
fs.readFile(__dirname + "/input.txt", (error, data) => {
    if (error) throw error;
    
    const numbers = data.toString().split("\r\n");
    
    console.log("Part 1: " + getTotalMagnitude(numbers));
    console.log("Part 2: " + getLargestMagnitude(numbers))
});

class Node {
    constructor(value, childLeft, childRight) {
        if (value != undefined) {
            this.value = value;
            return;
        }

        this.childLeft = childLeft;
        this.childRight = childRight;
    }

    log(prefix, childrenPrefix) {
        if (prefix == undefined) prefix = "";
        if (childrenPrefix == undefined) childrenPrefix = "";

        console.log(prefix + (this.value != undefined ? ` ${this.value}`:  "┐" ));
        if (this.value != undefined) return;
        this.childRight.log(childrenPrefix + "├──", childrenPrefix + "│  ");
        this.childLeft.log(childrenPrefix + "└──", childrenPrefix + "   ");
    }
}

function getTotalMagnitude(numbers) {
    rootR = convertToTree(numbers[0])[0];
    for (let i = 1; i < numbers.length; i++) rootR = sumTrees(rootR, convertToTree(numbers[i])[0]);
    return calculateMagnitude(rootR);
}

function getLargestMagnitude(numbers) {
    let largest = -1;
    for (let i = 0; i < numbers.length; i++) {
        for (let j = 0; j < numbers.length; j++) {
            if (j == i) continue;
            const magnitude = calculateMagnitude(sumTrees(convertToTree(numbers[i])[0], convertToTree(numbers[j])[0]));
            if (largest == -1 || magnitude > largest) largest = magnitude;
        }
    }

    return largest;
}

function calculateMagnitude(root) {
    if (root.value != undefined) return root.value;
    return 3 * calculateMagnitude(root.childLeft) + 2 * calculateMagnitude(root.childRight);
}

function convertToTree(number) {
    let currPos = 1;
    let leftNode;
    if (number[1] == '[') {
        const convToTreeRtn = convertToTree(number.slice(1));
        leftNode = convToTreeRtn[0];
        currPos += convToTreeRtn[1];
    } else {
        let posComma = number.indexOf(',', currPos);
        if (posComma == -1) posComma = number.length - 1;
        leftNode = new Node(parseInt(number.slice(1, posComma)));
        currPos = posComma;
    }

    currPos++;

    let rightNode;
    if (number[currPos] == '[') {
        const convToTreeRtn = convertToTree(number.slice(currPos));
        rightNode = convToTreeRtn[0];
        currPos += convToTreeRtn[1];
    } else {
        const posComma = number.indexOf(']', currPos);
        rightNode = new Node(parseInt(number.slice(currPos, posComma)));
        currPos = posComma;
    }

    const newNode = new Node(undefined, leftNode, rightNode);
    leftNode.parent = newNode;
    leftNode.isLeft = true;
    rightNode.parent = newNode;
    rightNode.isLeft = false;

    return [newNode, currPos + 1];
}

function sumTrees(tree1, tree2) {
    let newRoot = new Node(undefined, tree1, tree2);
    tree1.parent = newRoot; tree1.isLeft = true;
    tree2.parent = newRoot; tree2.isLeft = false;
    rootR = newRoot;

    while (true) {
        if (getHighestDepth(newRoot) > 4) {
            doAllExplodes(newRoot, 0);
        } else if (hasSplittableNumbers(newRoot)) {
            doSingleSplit(newRoot);
        } else break;
    }

    return newRoot;
}

function getHighestDepth(root) {
    if (root.value != undefined) return 0;

    const leftDepth = getHighestDepth(root.childLeft);
    const rightDepth = getHighestDepth(root.childRight);
    return leftDepth > rightDepth ? leftDepth + 1 : rightDepth + 1;
}

function doAllExplodes(root, depth) {
    if (depth > 4) {
        explodeNode(root.parent);
        root.parent.isExploded = true;
    }

    if (root.value != undefined) return;

    doAllExplodes(root.childLeft, depth + 1);
    if (root.childRight != undefined) doAllExplodes(root.childRight, depth + 1);
}

function hasSplittableNumbers(root) {
    if (root.value != undefined) return root.value > 9;
    return hasSplittableNumbers(root.childLeft) || hasSplittableNumbers(root.childRight);
}

function doSingleSplit(root) {
    if (root.value != undefined) {
        if (root.value > 9) {
            splitNode(root);
            return true;
        }
        return false;
    }

    let done = doSingleSplit(root.childLeft);
    if (!done) done = doSingleSplit(root.childRight);
    return done;
}

function explodeNode(node) {
    let valueToAddLeft = node.childLeft.value;
    if (valueToAddLeft == undefined) {
        explodeNode(node.childLeft);
        valueToAddLeft = node.childLeft.value;
    }

    let valueToAddRight = node.childRight.value;
    if (valueToAddRight == undefined) {
        explodeNode(node.childRight);
        valueToAddRight = node.childRight.value;
    }

    addToClosestLeft(valueToAddLeft, node);
    addToClosestRight(valueToAddRight, node);

    node.value = 0;
    node.childLeft = undefined;
    node.childRight = undefined;
}

function addToClosestLeft(value, root) {
    let node = root;
    while (node.parent != undefined && node.isLeft) { node = node.parent; }
    if (node.parent == undefined) return;

    let leftNode = node.parent.childLeft;
    while (leftNode.value == undefined) leftNode = leftNode.childRight;
    leftNode.value += value;
}

function addToClosestRight(value, root) {
    let node = root;
    while (node.parent != undefined && !node.isLeft) { node = node.parent; }
    if (node.parent == undefined) return;

    let rightNode = node.parent.childRight;
    while (rightNode.value == undefined) rightNode = rightNode.childLeft;
    rightNode.value += value;
}

function splitNode(node) {
    const valueSplit = node.value / 2;
    const leftSplit = new Node(Math.floor(valueSplit));
    const rightSplit = new Node(Math.ceil(valueSplit));
    leftSplit.isLeft = true;
    rightSplit.isLeft = false;
    leftSplit.parent = node;
    rightSplit.parent = node;

    node.value = undefined;
    node.childLeft = leftSplit;
    node.childRight = rightSplit;
}

function calculateDepth(node) {
    let depth = 1;
    while (node.parent != undefined) {
        node = node.parent;
        depth++;
    }
    return depth;
}