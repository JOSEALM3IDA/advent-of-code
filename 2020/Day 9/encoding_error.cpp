#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>

#define INPUT_FILE      "input.txt"
#define PREAMBLE_LENGTH     25

using namespace std;

/* ============================ PROBLEM 1 ============================ */
int main1() {
    ifstream inputFile(INPUT_FILE);
    if (!inputFile.is_open()) {
        cout << "Could not open file provided." << endl;
        return -1;
    }

    string line;
    long long int lastNumbers[PREAMBLE_LENGTH];
    for (int i = 0; i < PREAMBLE_LENGTH; i++) {
        if (!getline(inputFile, line)) {
            cout << "Failure reading input." << endl;
            return -1;
        }
            
        lastNumbers[i] = stoi(line);
    }

    int insertIn = 0;
    while (getline(inputFile, line)) {
        long long int newNumber = stoi(line);

        bool valid = false;
        for (int i = 0; i < PREAMBLE_LENGTH; i++) {
            for (int j = i; j < PREAMBLE_LENGTH; j++)
                if (lastNumbers[i] + lastNumbers[j] == newNumber) {
                    valid = true;
                    break;
                }

            if (valid)
                break;
        }
        
        if (!valid) {
            cout << "First invalid number: " << newNumber << endl;
            inputFile.close();
            exit(0);
        }            

        lastNumbers[insertIn++] = newNumber;

        if (insertIn == PREAMBLE_LENGTH)
            insertIn = 0;
    }

    cout << "No invalid numbers." << endl;

    return 0;
}
/* =================================================================== */

/* ============================ PROBLEM 2 ============================ */
int main2() {
    ifstream inputFile(INPUT_FILE);
    if (!inputFile.is_open()) {
        cout << "Could not open file provided." << endl;
        return -1;
    }

    string line;
    long long int lastNumbers[PREAMBLE_LENGTH];
    vector<long long int> allNumbers;
    for (int i = 0; i < PREAMBLE_LENGTH; i++) {
        if (!getline(inputFile, line)) {
            cout << "Failure reading input." << endl;
            return -1;
        }
        
        long long int newNumber = stoi(line);
        lastNumbers[i] = newNumber;
        allNumbers.push_back(newNumber);
    }

    int insertIn = 0;
    while (getline(inputFile, line)) {
        long long int newNumber = stoi(line);

        bool valid = false;
        for (int i = 0; i < PREAMBLE_LENGTH; i++) {
            for (int j = i; j < PREAMBLE_LENGTH; j++)
                if (lastNumbers[i] + lastNumbers[j] == newNumber) {
                    valid = true;
                    break;
                }

            if (valid)
                break;
        }
        
        if (!valid) {
            cout << "First invalid number: " << newNumber << endl;
            int smallest, largest;

            for (int i = 0; i < allNumbers.size(); i++) {
                int sum = 0;
                smallest = largest = allNumbers[i];
                for (int j = i; j < allNumbers.size(); j++) {
                    if (allNumbers[j] < smallest)
                        smallest = allNumbers[j];
                    else if (allNumbers[j] > largest)
                        largest = allNumbers[j];
                    
                    sum += allNumbers[j];
                    
                    if (sum > newNumber)
                        break;

                    if (sum == newNumber) {
                        cout << "Smallest: " << smallest << "\tLargest: " << largest << "\nSum: " << smallest + largest << endl;
                        inputFile.close();
                        exit(0);
                    }
                }
            }

            cout << "Couldn't break XMAS encryption :(" << endl;
            inputFile.close();
            exit(0);
        }            

        lastNumbers[insertIn++] = newNumber;
        allNumbers.push_back(newNumber);

        if (insertIn == PREAMBLE_LENGTH)
            insertIn = 0;
    }

    cout << "No invalid numbers." << endl;

    return 0;
}
/* =================================================================== */