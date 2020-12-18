#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>

#define INPUT_FILE      "input.txt"
#define SEATS_TO_CHECK       8

std::vector<std::string> readFile(std::string fileName) {
    std::ifstream inputFile(fileName);
    if (!inputFile.is_open()) {
        std::cout << "Could not open file provided." << std::endl;
        return {};
    }

    std::vector<std::string> input;
    std::string line;
    while (getline(inputFile, line))
        input.push_back(line);

    inputFile.close();
    return input;
}

/* ============================ PROBLEM 1 ============================ */
int main1() 
{ 
    std::vector<std::string> input = readFile(INPUT_FILE);
    int y, x, angle;

    y = x = angle = 0;

    for (int i = 0; i < input.size(); i++) {
        int val = atoi(input[i].substr(1).c_str());
        switch (input[i][0]) {
            case 'N':
                y += val;
            break;

            case 'S':
                y -= val;
            break;

            case 'E':
                x += val;
            break;

            case 'W':
                x -= val;
            break;

            case 'F':
                if (angle < 0 && angle != -180)
                    val = -val;

                switch (abs(angle)) {
                    case 0:
                        x += val;
                    break;

                    case 90:
                        y += val;
                    break;

                    case 180:
                        x -= val;
                    break;

                    case 270:
                        y -= val;
                    break;

                    default:
                        std::cout << "Unrecognized angle: " << angle << std::endl;
                }
            break;

            case 'L':
                angle += val;
                angle = angle % 360;
            break;

            case 'R':
                angle -= val;
                angle = angle % 360;
            break;

            default:
                std::cout << "Unknown char: " << input[i][0] << std::endl;
        }
    }

    std::cout << "Manhattan distance: " << abs(y) + abs(x) << std::endl;
    return 0;
} 
/* =================================================================== */

/* ============================ PROBLEM 2 ============================ */
int main2() 
{ 
    std::vector<std::string> input = readFile(INPUT_FILE);
    int ySH, yWP, xSH, xWP, temp;
    ySH = xSH = 0;
    yWP = ySH + 1;
    xWP = xSH + 10;

    for (int i = 0; i < input.size(); i++) {
        int val = atoi(input[i].substr(1).c_str());
        switch (input[i][0]) {
            case 'N':
                yWP += val;
            break;

            case 'S':
                yWP -= val;
            break;

            case 'E':
                xWP += val;
            break;

            case 'W':
                xWP -= val;
            break;

            case 'F':
                for (int t = 0; t < val; t++) {
                    xSH += xWP;
                    ySH += yWP;
                }
            break;

            case 'L':
            case 'R':
                switch (val) {
                    case 0: break;

                    case 90:
                        if (input[i][0] == 'L') {
                            temp = yWP;
                            yWP = xWP;
                            xWP = -temp;
                        } else {
                            temp = yWP;
                            yWP = -xWP;
                            xWP = temp;
                        }
                        
                    break;

                    case 180:
                        yWP = -yWP;
                        xWP = -xWP;
                    break;
                    
                    case 270:
                        if (input[i][0] == 'L') {
                            temp = yWP;
                            yWP = -xWP;
                            xWP = temp;
                        } else {
                            temp = yWP;
                            yWP = xWP;
                            xWP = -temp;
                        }
                        
                    break;

                    default:
                        std::cout << "Unrecognized value for angle: " << val << std::endl;
                }
            break;

            default:
                std::cout << "Unknown char: " << input[i][0] << std::endl;
        }
    }

    std::cout << "Manhattan distance: " << abs(ySH) + abs(xSH) << std::endl;
    return 0;
} 
/* =================================================================== */