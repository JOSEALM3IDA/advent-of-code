#include <iostream>
#include <vector>
#include <fstream>
#include <sstream>
#include <string>
#include <chrono> // To measure execution time

#include "passport.h"

#define INPUT_FILE  "input.txt"

using namespace std;

vector<Passport> getPassportsFromFile(string inputFileName) {
    ifstream inputFile(inputFileName);
    if (!inputFile.is_open()) {
        cout << "Could not open file provided." << endl;
        return {};
    }

    stringstream os;
    string line;
    while (getline(inputFile, line))
        line != "" ? os << line << " " : os << endl;

    inputFile.close();

    vector<Passport> passports;
    while (getline(os, line, '\n')) {
        unsigned int posColon = 0, posSpace = 0;
        Passport p;
        while (posColon < line.find_last_of(':')) {
            posColon = line.find(':', posSpace);
            posSpace = line.find(' ', posColon + 1); 
            string substr = line.substr(posColon - 3, 3);
            string val = line.substr(posColon + 1, posSpace - 1 - posColon);

            if (substr == "byr") p.byr = val;
            else if (substr == "iyr") p.iyr = val;
            else if (substr == "eyr") p.eyr = val;
            else if (substr == "hgt") p.hgt = val;
            else if (substr == "hcl") p.hcl = val;
            else if (substr == "ecl") p.ecl = val;
            else if (substr == "pid") p.pid = val;
        }

        passports.push_back(p);
    }

    return passports;
}

int main1() {
    vector<Passport> passports = getPassportsFromFile(INPUT_FILE);

    int validCount = 0;
    for (Passport p : passports) {
        if (p.allFieldsPresent()) {
            validCount++;
        }
    }
    
    cout << "Valid passports: " << validCount << endl;
    
    return 0;
}

int main2() {
    auto start = chrono::high_resolution_clock::now();
    vector<Passport> passports = getPassportsFromFile(INPUT_FILE);

    int validCount = 0;
    for (Passport p : passports) {
        if (p.isValid()) {
            validCount++;
        }
    }

    auto finish = chrono::high_resolution_clock::now();
    cout << "Valid passports: " << validCount << endl << endl << "Execution time: " << chrono::duration_cast<chrono::microseconds>(finish-start).count() << " microsec\n" << endl;
    return 0;
}