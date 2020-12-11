#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <algorithm>

#define INPUT_FILE      "input.txt"

using namespace std;

vector<int> readFile(string fileName) {
    ifstream inputFile(fileName);
    if (!inputFile.is_open()) {
        cout << "Could not open file provided." << endl;
        return {};
    }

    vector<int> input;
    string line;
    while (getline(inputFile, line))
        input.push_back(stoi(line));

    inputFile.close();
    return input;
}

/* ============================ PROBLEM 1 ============================ */
int main1() { 
    vector<int> input = readFile(INPUT_FILE);
    sort(input.begin(), input.end());
    input.push_back(input[input.size() - 1] + 3);

    for (int n : input)
        cout << n << ' ';

    int lastN = 0, count1 = 0, count3 = 0;
    for (int &n : input) {
        int diff = n - lastN;
        if (diff == 1)
            count1++;        
        else if (diff == 3)
            count3++;
        lastN = n;
    }
    
    cout << "\nNumber of differences of 1: " << count1 << endl;
    cout << "Number of differences of 3: " << count3 << endl;
    cout << "Multiplied: " << count1 * count3 << endl;
} 
/* =================================================================== */

/* ============================ PROBLEM 2 ============================ */ // UNFINISHED
int main() { 
    vector<int> input = readFile(INPUT_FILE);
    sort(input.begin(), input.end());

    input.push_back(input[input.size() - 1] + 3);
    input.insert(input.begin(), 0);

    for (int n : input)
        cout << n << ' ';
    cout << endl << endl;

    for (size_t i = 1; i < input.size() - 1; i++) {
        if (input[i + 1] - input[i] == 3 || input[i] - input[i-1] == 3) {
            cout << input[i] << " is unskippable" << endl;
        }
    }


    //vector<int>::iterator ite; 
} 
/* =================================================================== */