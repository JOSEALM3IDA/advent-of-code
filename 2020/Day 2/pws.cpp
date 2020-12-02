#include <iostream>
#include <vector>
#include <fstream>
#include <string>
#include <cstring>

#define BUFFSIZE 256

using namespace std;

int main() {
    ifstream inputFile("input.txt");
    int validCount = 0;

    if (inputFile.is_open()) {
        int min, max, chCount;
        char ch, pw[BUFFSIZE];
        string line;
        while (getline(inputFile, line)) {
            sscanf(line.c_str(), "%d-%d %c: %s", &min, &max, &ch, pw);

            chCount = 0;
            for (int i = 0; i < strlen(pw); i++) {
                if (pw[i] == ch)
                    chCount++;
            }

            if ( !(chCount < min || chCount > max) )
                validCount++;
        }

        inputFile.close();
        cout << "Valid pws: " << validCount << endl;
    }

    return 0;
}

int main2() {
    ifstream inputFile("input.txt");
    int validCount = 0;

    if (inputFile.is_open()) {
        int pos1, pos2;
        char ch, pw[BUFFSIZE];
        string line;
        while (getline(inputFile, line)) {
            sscanf(line.c_str(), "%d-%d %c: %s", &pos1, &pos2, &ch, pw);

            if (pw[pos1 - 1] == ch ^ pw[pos2 - 1] == ch)
                validCount++;
        }

        inputFile.close();
        cout << "Valid pws: " << validCount << endl;
    }

    return 0;
}