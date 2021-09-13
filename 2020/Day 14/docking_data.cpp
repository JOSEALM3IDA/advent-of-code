#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <bitset>

#define INPUT_FILE      "input.txt"
#define MASK_LEN            36

/* ============================ PROBLEM 1 ============================ */
unsigned long long applyMask(unsigned long long val, std::string mask) {
    std::bitset<MASK_LEN> valBits(val);
    for (int i = 0; i < MASK_LEN; i++) {
        if (mask[i] == 'X')
            continue;

        valBits[MASK_LEN - 1 - i] = mask[i] - '0';
    }
    return valBits.to_ullong();
}

int main1()
{
    std::ifstream inputFile(INPUT_FILE);
    if (!inputFile.is_open()) {
        std::cout << "Could not open file provided." << std::endl;
        return -1;
    }

    std::string line, mask;
    std::vector<unsigned long long> mem;
    while (getline(inputFile, line)) {
        if (line.substr(0, 4) == "mask") {
            mask = line.substr(line.find('=') + 2);
            continue;
        }
        
        unsigned int newPos = stoi(line.substr(line.find('[') + 1));
        unsigned int newVal = stoi(line.substr(line.find('=') + 2));
        while (newPos >= mem.size()) {
            mem.push_back(0);
        }

        mem[newPos] = applyMask(newVal, mask);
    }

    inputFile.close();

    unsigned long long sum = 0;
    for (auto n : mem)
        if (n != 0)
            sum += n;

    std::cout << "Sum of all values left in memory: " << sum << std::endl;
    return 0;
} 
/* =================================================================== */

/* ============================ PROBLEM 2 ============================ */
void getPossibilities(std::vector<unsigned long long> &rtn, std::string maskedAddr) {
    int nX = 0;

    for (int i = 0; i < MASK_LEN; i++)
        if (maskedAddr[i] == 'X') {
            nX++;
        }

    if (nX == 0) {
        rtn.push_back(stoull(maskedAddr, nullptr, 2));
        return;
    }

    size_t xPos = maskedAddr.find_first_of('X');

    maskedAddr[xPos] = '0';
    getPossibilities(rtn, maskedAddr);

    maskedAddr[xPos] = '1';
    getPossibilities(rtn, maskedAddr);
}

int main2()
{
    std::ifstream inputFile(INPUT_FILE);
    if (!inputFile.is_open()) {
        std::cout << "Could not open file provided." << std::endl;
        return -1;
    }

    std::string line, mask;
    std::vector<long int> val;
    std::vector<unsigned long long> pos;
    while (getline(inputFile, line)) {
        if (line.substr(0, 4) == "mask") {
            mask = line.substr(line.find('=') + 2);
            continue;
        }
        
        unsigned int newPos = stoi(line.substr(line.find('[') + 1));
        unsigned int newVal = stoi(line.substr(line.find('=') + 2));
        std::vector<unsigned long long> addr;
        std::bitset<MASK_LEN> addrBits(newPos);
        std::string maskedAddr = addrBits.to_string();

        for (int i = 0; i < MASK_LEN; i++) { // Apply mask to address
            if (mask[i] == '0')
                continue;

            maskedAddr[i] = mask[i];
        }
        
        getPossibilities(addr, maskedAddr);
            
        for (size_t i = 0; i < addr.size(); i++) {
            bool alreadyPresent = false;
            size_t j;
            for (j = 0; j < pos.size(); j++) {
                if (addr[i] == pos[j]) {
                    alreadyPresent = true;
                    break;
                }
                    
            }

            if (alreadyPresent) {
                val[j] = newVal;
            } else {
                pos.push_back(addr[i]);
                val.push_back(newVal);
            }
        }

    }

    inputFile.close();

    unsigned long long sum = 0;
    for (auto n : val)
        if (n != 0)
            sum += n;

    std::cout << "Sum of all values left in memory: " << sum << std::endl;
    return 0;
} 
/* =================================================================== */