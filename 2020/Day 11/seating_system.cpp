#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <algorithm>

#define INPUT_FILE      "input.txt"
#define SEATS_TO_CHECK       8

using namespace std;

vector<string> readFile(string fileName) {
    ifstream inputFile(fileName);
    if (!inputFile.is_open()) {
        cout << "Could not open file provided." << endl;
        return {};
    }

    vector<string> input;
    string line;
    while (getline(inputFile, line))
        input.push_back(line);

    inputFile.close();
    return input;
}

/* ============================ PROBLEM 1 ============================ */
int adjacentSeats1(vector<string> const &input, int i, int j) {
    int adjFilled = 0;

    // UP-LEFT
    if (i > 0 && j > 0 && input[i - 1][j - 1] == '#')
        adjFilled++;

    // UP
    if (i > 0 && input[i - 1][j] == '#')
        adjFilled++;

    // UP-RIGHT
    if (i > 0 && j < (int)input[0].size() - 1 && input[i - 1][j + 1] == '#')
        adjFilled++;

    // LEFT
    if (j > 0 && input[i][j - 1] == '#')
        adjFilled++;

    // RIGHT
    if (j < (int)input[0].size() - 1 && input[i][j + 1] == '#')
        adjFilled++;

    // DOWN-LEFT
    if (i < (int)input.size() - 1 && j > 0 && input[i + 1][j - 1] == '#')
        adjFilled++;

    // DOWN
    if (i < (int)input.size() - 1 && input[i + 1][j] == '#')
        adjFilled++;

    // DOWN-RIGHT
    if (i < (int)input.size() - 1 && j < (int)input[0].size() - 1 && input[i + 1][j + 1] == '#')
        adjFilled++;

    return adjFilled;
}

int main1() { 
    vector<string> input = readFile(INPUT_FILE);

    // FILL ALL SEATS
    for (int i = 0; i < (int)input.size(); i++) // ROWS
        for (int j = 0; j < (int)input[0].size(); j++) // COLUMNS
            if (input[i][j] == 'L')
                input[i][j] = '#';

    int nChanges;
    vector<string> temp = input;
    do {
        nChanges = 0;
        for (int i = 0; i < (int)input.size(); i++)
            for (int j = 0; j < (int)input[0].size(); j++) {
                int adj = adjacentSeats1(input, i, j);
                if (input[i][j] == 'L' && adj == 0) {
                    temp[i][j] = '#';
                    nChanges++;
                } else if (input[i][j] == '#' && adj >= 4) {
                    temp[i][j] = 'L';
                    nChanges++;
                }
            }

        input = temp;
    } while (nChanges != 0);

    // COUNT OCCUPIED SEATS
    int nOccupied = 0;
    for (int i = 0; i < (int)input.size(); i++)
        for (int j = 0; j < (int)input[0].size(); j++)
            if (input[i][j] == '#')
                nOccupied++;

    cout << "Number of occupied seats: " << nOccupied << endl << endl;
    return 0;
} 
/* =================================================================== */

/* ============================ PROBLEM 2 ============================ */
int adjacentSeats2(vector<string> const &input, size_t i, size_t j) {
    int adjFilled = 0, iTemp, jTemp;

    // UP-LEFT UNTIL FIRST NON FLOOR
    jTemp = j; iTemp = i;
    while (1)
        if (iTemp > 0 && jTemp > 0) {
            char c = input[--iTemp][--jTemp];
            if (c == '#') {
                adjFilled++;
                break;
            } else if (c == 'L') break;
        } else break;
    
    // UP UNTIL FIRST NON FLOOR
    iTemp = i;
    while (1)
        if (iTemp > 0) {
            char c = input[--iTemp][j];
            if (c == '#') {
                adjFilled++;
                break;
            } else if (c == 'L') break;
        } else break;

    // UP-RIGHT UNTIL NON FLOOR
    jTemp = j; iTemp = i;
    while (1)
        if (iTemp > 0 && jTemp < (int)input[0].size() - 1) {
            char c = input[--iTemp][++jTemp];
            if (c == '#') {
                adjFilled++;
                break;
            } else if (c == 'L') break;
        } else break;

    // LEFT UNTIL NON FLOOR
    jTemp = j;
    while (1)
        if (jTemp > 0) {
            char c = input[i][--jTemp];
            if (c == '#') {
                adjFilled++;
                break;
            } else if (c == 'L') break;
        } else break;

    // RIGHT UNTIL NON FLOOR
    jTemp = j;
    while (1)
        if (jTemp < (int)input[0].size() - 1) {
            char c = input[i][++jTemp];
            if (c == '#') {
                adjFilled++;
                break;
            } else if (c == 'L') break;
        } else break;

    // DOWN-LEFT UNTIL FIRST NON FLOOR
    jTemp = j; iTemp = i;
    while (1)
        if (iTemp < (int)input.size() - 1 && jTemp > 0) {
            char c = input[++iTemp][--jTemp];
            if (c == '#') {
                adjFilled++;
                break;
            } else if (c == 'L') break;
        } else break;

    // DOWN UNTIL FIRST NON FLOOR
    iTemp = i;
    while (1) 
        if (iTemp < (int)input.size() - 1) {
            char c = input[++iTemp][j];
            if (c == '#') {
                adjFilled++;
                break;
            } else if (c == 'L') break;
        } else break;

    // DOWN-RIGHT UNTIL FIRST NON FLOOR
    jTemp = j; iTemp = i;
    while (1)
        if (iTemp < (int)input.size() - 1 && jTemp < (int)input[0].size() - 1) {
            char c = input[++iTemp][++jTemp];
            if (c == '#') {
                adjFilled++;
                break;
            } else if (c == 'L') break;
        } else break;

    return adjFilled;
}

int main2() { 
    vector<string> input = readFile(INPUT_FILE);

    // FILL ALL SEATS
    for (int i = 0; i < (int)input.size(); i++) // Rows
        for (int j = 0; j < (int)input[0].size(); j++) // Columns
            if (input[i][j] == 'L')
                input[i][j] = '#';

    int nChanges;
    vector<string> temp = input;
    do {
        nChanges = 0;
        for (int i = 0; i < (int)input.size(); i++) // Rows
            for (int j = 0; j < (int)input[0].size(); j++) { // Columns
                int adj = adjacentSeats2(input, i, j);
                if (input[i][j] == 'L' && adj == 0) {
                    temp[i][j] = '#';
                    nChanges++;
                } else if (input[i][j] == '#' && adj >= 5) {
                    temp[i][j] = 'L';
                    nChanges++;
                }
            }

        input = temp;
    } while (nChanges != 0);

    // COUNT OCCUPIED SEATS
    int nOccupied = 0;
    for (int i = 0; i < (int)input.size(); i++) // Rows
        for (int j = 0; j < (int)input[0].size(); j++) // Columns
            if (input[i][j] == '#')
                nOccupied++;

    cout << "Number of occupied seats: " << nOccupied << endl << endl;
    return 0;
} 
/* =================================================================== */