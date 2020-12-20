#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <chrono> // To measure execution time

#define INPUT_FILE      "input.txt"

/* ============================ PROBLEM 1 ============================ */
int main1() 
{ 
    std::ifstream inputFile(INPUT_FILE);
    if (!inputFile.is_open()) {
        std::cout << "Could not open file provided." << std::endl;
        return -1;
    }

    std::string line;
    getline(inputFile, line);
    int timestamp = stoi(line);

    getline(inputFile, line);

    size_t pos;
    std::string sub = line;
    std::vector<int> IDs;
    while ((pos = line.find(',')) < line.size() + 1) {
        if (line[0] != 'x') {
            IDs.push_back(stoi(line.substr(0, pos)));
        }
        line = line.substr(pos + 1);
    }

    if (line[0] != 'x') {
        IDs.push_back(stoi(line.substr(0, pos)));
    }

    size_t biggestID = 0;
    for (int ID : IDs)
        if (ID > biggestID)
            biggestID = ID;

    std::vector<int> lastDeparture;
    for (int id : IDs) {
        lastDeparture.push_back(timestamp - timestamp % id);
    }
        
    int smallest = timestamp + biggestID;
    size_t smallestPos = 0;
    for (size_t i = 0; i < IDs.size(); i++) {
        int nextDeparture = lastDeparture[i] + IDs[i];
        if (nextDeparture < smallest) {
            smallest = nextDeparture;
            smallestPos = i;
        }
    }

    int waitTime = lastDeparture[smallestPos] + IDs[smallestPos] - timestamp;
    int choiceID = IDs[smallestPos];
    std::cout << "Take the bus with ID " << choiceID << ". That will only amount to ";
    std::cout << waitTime << " minutes of waiting." << std::endl;
    std::cout << "Result: " << waitTime * choiceID << std::endl;
    return 0;
} 
/* =================================================================== */

/* ============================ PROBLEM 2 ============================ */
int main() // Solution potentially works but takes way too much time (5h +). Should've used Chinese Remainder Theorem
{ 
    auto start = std::chrono::high_resolution_clock::now();
    std::ifstream inputFile(INPUT_FILE);
    if (!inputFile.is_open()) {
        std::cout << "Could not open file provided." << std::endl;
        return -1;
    }

    std::string line;
    getline(inputFile, line); // trash line from part 1

    getline(inputFile, line);

    size_t pos;
    std::string sub = line;
    std::vector<std::string> IDs;
    while ((pos = line.find(',')) < line.size() + 1) {
        IDs.push_back(line.substr(0, pos));
        line = line.substr(pos + 1);
    }

    if (line[0] != 'x') {
        IDs.push_back(line.substr(0, pos));
    }

    size_t biggestID = 0;
    for (std::string ID : IDs)
        if (ID[0] != 'x')
            if (stoi(ID) > biggestID)
                biggestID = stoi(ID);

    unsigned long long int initTime = 0;
    unsigned long long int tempTime = 0;
    unsigned long long int debug = 1;
    while (1) {
        tempTime += biggestID;
        initTime = tempTime - tempTime % stoi(IDs[0]);
        //std::cout << "Checking: " << tempTime << "\tInit: " << initTime << std::endl;
        auto finish = std::chrono::high_resolution_clock::now();
        if (initTime > debug) {
            std::cout << "Execution time: " << std::chrono::duration_cast<std::chrono::seconds>(finish-start).count() << "s\tCurrently checking: " << initTime << std::endl;
            debug *= 10;
        }

        bool bad = false;
        for (size_t i = 1; i < IDs.size(); i++) {
            if (IDs[i][0] == 'x')
                continue;

    	    unsigned long long int lastDeparture = initTime - initTime % stoi(IDs[i]);
            unsigned long long int nextDeparture = lastDeparture + stoi(IDs[i]);
            int difference = nextDeparture - initTime;
            if (difference != i) {
                bad = true;
                //std::cout << initTime << " is oofed because " << nextDeparture << " is not " << i << " minutes after." << std::endl;
                //std::cout << initTime - initTime % stoi(IDs[i]) + stoi(IDs[i]) - initTime << std::endl;
                break;
            }
        }

        if (!bad)
            break;
    }

    auto finish = std::chrono::high_resolution_clock::now();
    std::cout << "Earliest timestamp: " << initTime << std::endl;
    std::cout << "\nExecution time: " << std::chrono::duration_cast<std::chrono::seconds>(finish-start).count() << '.' << std::chrono::duration_cast<std::chrono::milliseconds>(finish-start).count() / 10 <<  "s";
    return 0;
} 
/* =================================================================== */