#include <iostream>
#include <vector>
#include <fstream>
#include <string>

#define INPUT_FILE  "input.txt"

using namespace std;

vector<string> readFile(string fileName) {
    ifstream inputFile(fileName);
    if (!inputFile.is_open()) {
        cout << "Could not open file provided." << endl;
        return {};
    }

    vector<string> input;
    string line;
    while (getline(inputFile, line)) {
        input.push_back(line);
    }

    inputFile.close();
    return input;
}

int treesEncountered(vector<string> map, int slope_h, int slope_v) {
    int pWidth = map[0].size(), pHeight = map.size();
    int currLine = 0, currCol = 0;
    int treeCount = 0;

    while (currLine < pHeight) {
        if (map[currLine][currCol] == '#')
            treeCount++;

        if (currCol + slope_h >= pWidth)
            currCol = (currCol + slope_h) - pWidth;
        else
            currCol += slope_h;

        currLine += slope_v;
    }

    return treeCount;
}

int main1() {
    vector<string> map = readFile(INPUT_FILE);

    cout << "Trees encountered: " << treesEncountered(map, 3, 1) << endl;

    return 0;
}

int main2() {
    vector<string> map = readFile(INPUT_FILE);
    unsigned long long int multiTrees = 1;

    multiTrees *= treesEncountered(map, 1, 1);
    multiTrees *= treesEncountered(map, 3, 1);
    multiTrees *= treesEncountered(map, 5, 1);
    multiTrees *= treesEncountered(map, 7, 1);
    multiTrees *= treesEncountered(map, 1, 2);

    cout << "Tree numbers multiplied together: " << multiTrees << endl;

    return 0;
}