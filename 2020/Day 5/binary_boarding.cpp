#include <iostream>
#include <vector>
#include <fstream>
#include <string>

#define INPUT_FILE      "input.txt"
#define NUMBER_ROWS         128
#define NUMBER_COLUMNS       8

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

int main1() {
    vector<string> map = readFile(INPUT_FILE);
    int maxID = -1;

    for (string s : map) {
        int rowMin = 0, rowMax = NUMBER_ROWS - 1;
        int colMin = 0, colMax = NUMBER_COLUMNS - 1;
        int ID;

        for (char c : s)
            switch (c) {
                case 'F':
                    rowMax = rowMin + (rowMax - rowMin) / 2;
                    break;
                case 'B':
                    rowMin = rowMax - (rowMax - rowMin) / 2;
                    break;
                case 'R':
                    colMin = colMax - (colMax - colMin) / 2;
                    break;
                case 'L':
                    colMax = colMin + (colMax - colMin) / 2;
                    break;
            }

        ID = rowMin * 8 + colMin;

        if (ID > maxID)
            maxID = ID;

    }

    cout << "\nBiggest ID: " << maxID << endl;
    return 0;
}

int main2() {
    vector<string> map = readFile(INPUT_FILE);
    bool seats[NUMBER_ROWS * NUMBER_COLUMNS] = {false};
    int myID = -1;

    for (string s : map) {
        int rowMin = 0, rowMax = NUMBER_ROWS - 1;
        int colMin = 0, colMax = NUMBER_COLUMNS - 1;
        int ID;

        for (char c : s)
            switch (c) {
                case 'F':
                    rowMax = rowMin + (rowMax - rowMin) / 2;
                    break;
                case 'B':
                    rowMin = rowMax - (rowMax - rowMin) / 2;
                    break;
                case 'R':
                    colMin = colMax - (colMax - colMin) / 2;
                    break;
                case 'L':
                    colMax = colMin + (colMax - colMin) / 2;
                    break;
            }

        ID = rowMin * 8 + colMin;

        seats[ID] = true;
    }

    for (int i = 0; i < NUMBER_ROWS * NUMBER_COLUMNS; i++)
        if (seats[i] == false && seats[i - 1] && seats[i + 1]) {
            cout << "My seat is: " << i << endl;
            return 0;
        }
}