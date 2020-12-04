#ifndef PASSPORT_H
#define PASSPORT_H

#include <string>
#include <vector>
#include <iostream>
#include <sstream>

using namespace std;

struct Passport {
    string byr, iyr, eyr, hgt, hcl, ecl, pid;

    Passport() { byr.clear(); iyr.clear(); eyr.clear(); hgt.clear(); hcl.clear(); ecl.clear(); pid.clear(); }

    bool allFieldsPresent() const { return !(byr.empty() || iyr.empty() || eyr.empty() || hgt.empty() || hcl.empty() || ecl.empty() || pid.empty()); }

    bool isValid() const {
        if (!allFieldsPresent())
            return false;
        
        int year = stoi(byr);
        if (year < 1920 || year > 2002)
            return false;

        year = stoi(iyr);
        if (year < 2010 || year > 2020)
            return false;

        year = stoi(eyr);
        if (year < 2020 || year > 2030)
            return false;

        int len = hgt.length();
        if (hgt[len - 2] == 'c' && hgt[len - 1] == 'm') { // cm
            int height = stoi(hgt.substr(0, 3));
            if (height < 150 || height > 193)
                return false;
        } else if (hgt[len - 2] == 'i' && hgt[len - 1] == 'n') { // in
            int height = stoi(hgt.substr(0, 2));
            if (height < 59 || height > 76)
                return false;
        } else return false;
        
        if (hcl.length() != 7)
            return false;
        for (char c : hcl.substr(1))
            if(!isxdigit(c))
                return false;
        
        if ( !(ecl == "amb" || ecl == "blu" || ecl == "brn" || ecl == "gry" || ecl == "grn" || ecl == "hzl" || ecl == "oth") )
            return false;

        if (pid.length() != 9)
            return false;
        for (char c : pid)
            if(!isdigit(c))
                return false;
        
        return true;
    }

    string getAsString() const {
        ostringstream os;
        os << "byr:" << byr << " iyr:" << iyr << " eyr:" << eyr << " hgt:" << hgt << " hcl:" << hcl << " ecl:" << ecl << " pid:" << pid;
        return os.str();
    }
};

#endif /* PASSPORT_H */