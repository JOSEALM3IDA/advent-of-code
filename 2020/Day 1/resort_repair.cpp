#include <iostream>
#include <vector>
#include <fstream>
#include <string>

#define NUMBER_TO_FIND  2020
#define INPUT_FILE      "input.txt"

using namespace std;

vector<int> readFile(string fileName) {
    ifstream inputFile;
    vector<int> input;

    inputFile.open(fileName);
    if (inputFile.is_open()) {
        string line;
        while (getline(inputFile, line)) {
            input.push_back(atoi(line.c_str()));
        }
        inputFile.close();
    }

    return input;
}

int main1() {
    vector<int> input = readFile(INPUT_FILE);

    for(int n1 = 0; n1 < input.size(); n1++)
        for (int n2 = n1 + 1; n2 < input.size(); n2++)
            if (input[n1] + input[n2] == NUMBER_TO_FIND) {
                cout << "Numbers: " << input[n1] << " and " << input[n2] << endl;
                cout << "Multiplied: " << input[n1] * input[n2] << endl;
                return 1;
            }
    
    cout << "Not found :/" << endl;
    return 0;
}

int main2() {
    vector<int> input = readFile("input.txt");

    for(int n1 = 0; n1 < input.size(); n1++)
        for (int n2 = n1 + 1; n2 < input.size(); n2++)
            for (int n3 = n2 + 1; n3 < input.size(); n3++)
                if (input[n1] + input[n2] + input[n3] == NUMBER_TO_FIND) {
                    cout << "Numbers: " << input[n1] << " and " << input[n2] << " and " << input[n3] << endl;
                    cout << "Multiplied: " << input[n1] * input[n2] * input[n3] << endl;
                    return 1;
                }
    
    cout << "Not found :/" << endl;
    return 0;
}