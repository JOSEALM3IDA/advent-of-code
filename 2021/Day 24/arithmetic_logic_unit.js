/*
 * DISCLAIMER: Not written by me. Original author: /user/DrunkHacker 
 * Reason: I couldn't bother frying more brain cells over this.
 * 
 */

const fs = require('fs');
function run(input, biggest = true) {
    final = Array(14);
    stack = [];

    for (let i = 0; i < 14; i += 1) {
        x_add = parseInt(input[18 * i + 5].split(' ')[2]);
        y_add = parseInt(input[18 * i + 15].split(' ')[2]);
        if (x_add > 0) {
            stack.push([y_add, i]);
        } else {
            s = stack.pop();
            [y_add, y_index] = [s[0], s[1]];
            if (biggest) {
                to_add = 9;
                while (to_add + y_add + x_add > 9) to_add--;
            } else {
                to_add = 1;
                while (to_add + y_add + x_add < 1) to_add++;
            }
            final[y_index] = to_add;
            final[i] = to_add + y_add + x_add;
        }
    }
    return final.join('');
}

const raw_data = fs.readFileSync(__dirname + '/input.txt', 'utf8');
const input = raw_data.split('\r\n');

console.log('Part 1:', run(input));
console.log('Part 2:', run(input, false));
