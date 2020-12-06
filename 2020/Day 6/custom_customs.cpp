#include <iostream>
#include <fstream>
#include <sstream>
#include <string>

#define INPUT_FILE  "input.txt"

using namespace std;

int main1() {
    ifstream inputFile(INPUT_FILE);
    if (!inputFile.is_open()) {
        cout << "Could not open file provided." << endl;
        return -1;
    }

    ostringstream os;
    string line;
    int sum = 0, ansCount = 0;

    while (getline(inputFile, line)) {
        if (line.length() == 0) {
            sum += ansCount;
            ansCount = 0;
            os.str("");
            os.clear();
            continue;
        }
        
        for (char c1 : line) {
            bool flag = true;
            for (char c2 : os.str())
                if (c1 == c2)
                    flag = false;
                
            if (flag) {
                os << c1;
                ansCount++;
            }
        }
    }

    sum += ansCount; // In case input doesn't end with \n

    inputFile.close();

    cout << "Sum: " << sum << endl;
    
    return 0;
}

void check(string line, int& sum, int& personCount) {
    int newAnsCount = 0;
    for (auto c1 = line.begin(); c1 != line.end(); c1++) {
        for (auto c2 = c1; c2 != line.end(); c2++)
            if (*c1 == *c2)
                newAnsCount++;
                
        if (newAnsCount == personCount)
            sum++;

        newAnsCount = 0;
    }

    personCount = 0;
}

int main2() {
    ifstream inputFile(INPUT_FILE);
    if (!inputFile.is_open()) {
        cout << "Could not open file provided." << endl;
        return -1;
    }

    ostringstream os;
    string line;
    int sum = 0, personCount = 0;

    while (getline(inputFile, line)) {
        if (line.length() == 0) {
            check(os.str(), sum, personCount);
            os.str("");
            continue;
        }
        
        os << line;
        personCount++;
    }

    if (line.length() != 0) // In case input doesn't end with \n
        check(os.str(), sum, personCount);

    inputFile.close();

    cout << "Sum: " << sum << endl;
    
    return 0;
}