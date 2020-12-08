#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>

#define INPUT_FILE  "input.txt"

using namespace std;

vector<string> getInput(string fileName) {
    ifstream inputFile(fileName);
    if (!inputFile.is_open()) {
        cout << "Could not open file provided." << endl;
        return {};
    }

    string line;
    vector<string> input;
    while (getline(inputFile, line))
        input.push_back(line);
    inputFile.close();

    return input;
}

/* ============================ PROBLEM 1 ============================ */
int getAcc1(vector<string> input) {
    int acc = 0;
    vector<size_t> readInstructions;

    for (size_t i = 0; i < input.size(); i++) {
        for (size_t n : readInstructions)
            if (i == n)
                return acc;
        
        int spacePos = input[i].find(' ');
        string instruction = input[i].substr(0 , spacePos);
        int val = stoi(input[i].substr(spacePos + 1));

        readInstructions.push_back(i);

        if (instruction == "acc")
            acc += val;
        else if (instruction == "jmp")
            i += val - 1;
    }
    return acc;
}

int main1() {
    vector<string> input = getInput(INPUT_FILE);

    cout << "Value of the accumulator: " << getAcc1(input) << endl;

    return 0;
}
/* =================================================================== */


/* ============================ PROBLEM 2 ============================ */
int getAcc2(vector<string> input) {
    int acc = 0;

    for (size_t i = 0; i < input.size(); i++) {
        int spacePos = input[i].find(' ');
        string instruction = input[i].substr(0 , spacePos);
        int val = stoi(input[i].substr(spacePos + 1));

        if (instruction == "acc")
            acc += val;
        else if (instruction == "jmp")
            i += val - 1;
    }
    return acc;
}


vector<size_t> getProblematic(vector<string> input) {
    int acc = 0;
    vector<size_t> readInstructions;
    vector<size_t> problematicInstructions;

    for (size_t i = 0; i < input.size(); i++) {
        int count = 0;
        for (size_t n : readInstructions)
            if (i == n && input[n].substr(0, 3) != "acc") {
                count++;

                bool alreadyProblematic = false;
                for (size_t n2 : problematicInstructions)
                    if (n == n2)
                        alreadyProblematic = true;
                
                if(!alreadyProblematic)
                    problematicInstructions.push_back(n);


                if(count == 2) {
                    return problematicInstructions;
                }
                
            }
        count = 0;
        int spacePos = input[i].find(' ');
        string instruction = input[i].substr(0 , spacePos);
        int val = stoi(input[i].substr(spacePos + 1));

        readInstructions.push_back(i);

        if (instruction == "acc")
            acc += val;
        else if (instruction == "jmp")
            i += val - 1;
    }
    return {};
}

int main() {
    vector<string> input = getInput(INPUT_FILE);

    vector<size_t> problematicInstructionsPos = getProblematic(input);
    if (problematicInstructionsPos.size() == 0) {
        cout << "Value of the accumulator: " << getAcc2(input) << endl;
        return 0;
    }
    
    for (int i = problematicInstructionsPos.size() - 1; i >= 0; i--) {
        vector<string> alteredInput = input;

        string* instruction = &alteredInput[problematicInstructionsPos[i]];

        (*instruction).substr(0, 3) == "jmp" ? (*instruction).replace(0, 3, "nop") : (*instruction).replace(0, 3, "jmp");

        vector<size_t> newProblematicPos = getProblematic(alteredInput);

        if (newProblematicPos.size() == 0) {
            cout << "The problem was in instruction '" << input[problematicInstructionsPos[i]] << "' (line " << problematicInstructionsPos[i] + 1 
                 << "), which was promptly altered to '" << alteredInput[problematicInstructionsPos[i]] << "'" << endl;

            cout << "Value of the accumulator: " << getAcc2(alteredInput) << endl;
            return 0;
        }

        problematicInstructionsPos.pop_back();
    }
    return 0;
}
/* =================================================================== */