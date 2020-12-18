#include <iostream>
#include <fstream>
#include <sstream>
#include <cstring>
#include <vector>

#define INPUT_FILE  "input.txt"
#define MY_BAG      "shiny gold"

using namespace std;

bool stringHas(string s, vector<string> bagsToFind) {
    for (string bag : bagsToFind)
        if (s.find(bag) < s.length())
            return true;
    
    return false;
}

int main1() {
    ifstream inputFile(INPUT_FILE);
    if (!inputFile.is_open()) {
        cout << "Could not open file provided." << endl;
        return -1;
    }

    string line;
    vector<string> input;
    while (getline(inputFile, line))
        input.push_back(line);
    inputFile.close();

    vector<string> bagsToSearchNext;
    vector<string> bagsSearching;
    vector<string> bagsSearched;

    bagsToSearchNext.push_back(MY_BAG);

    do {
        bagsSearching = bagsToSearchNext;
        bagsToSearchNext.clear();
        for (string s : input) {
            string contain = s.substr(s.find("contain") + strlen("contain "));
            if (!stringHas(contain, bagsSearching))
                continue;

            string bag = s.substr(0, s.find(" bags"));

            bool alreadySearched = false;
            for (string sBag : bagsSearched)
                if (sBag == bag)
                    alreadySearched = true;

            if (!alreadySearched) {
                bagsToSearchNext.push_back(bag);
                bagsSearched.push_back(bag);
            }
        }
    } while (bagsToSearchNext.size() > 0);

    cout << "Number of bags that (in)directly contain the " << MY_BAG << " bag: " << bagsSearched.size() << endl;
    return 0;
}

int nBagsInside(string currentBag, const vector<string>& input ) {
    int nBags = 0;
    for (string s : input) {
        if (s.find(currentBag) > s.find(" bags"))
            continue;

        string contain = s.substr(s.find("contain") + strlen("contain "));
        if (contain.substr(0, 2) == "no")
            return 0;

        int spaceNumPos = 0, spaceBagPos = 0;
        for (size_t i = 0; i < contain.size(); i++) {
            char c = contain[i];
            if (isdigit(c)) {
                spaceNumPos = contain.find(' ', i);
                spaceBagPos = contain.find(" bag", spaceNumPos + 1);

                int nBagsRaw = stoi(contain.substr(i, spaceNumPos - i)); // Number of bags inside currentBag, without counting the ones inside those
                string bagToSearch = contain.substr(spaceNumPos + 1, spaceBagPos - spaceNumPos - 1);
                
                nBags += nBagsRaw + nBagsRaw * nBagsInside(bagToSearch, input);

                i = spaceNumPos;
            }
        }
    }

    return nBags;
}

int main2() {
    ifstream inputFile(INPUT_FILE);
    if (!inputFile.is_open()) {
        cout << "Could not open file provided." << endl;
        return -1;
    }

    string line;
    vector<string> input;
    while (getline(inputFile, line))
        input.push_back(line);
    inputFile.close();

    int nBags = nBagsInside(MY_BAG, input);
    cout << "Number of bags inside the " << MY_BAG << " bag: " << nBags  << endl;
    return 0;
}