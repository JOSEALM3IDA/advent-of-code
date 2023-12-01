use std::fs;

fn main() {
    let input_full: String = fs::read_to_string("input.txt").expect("Couldn't read file");
    let input_lines: Vec<_> = input_full.split("\n").collect();

    println!("Sum of all the calibration values (part 1): {}", part1(input_lines.clone()));
    println!("Sum of all the calibration values (part 2): {}", part2(input_lines.clone()));
}

fn part1(input_lines: Vec<&str>) -> u32 {
    let mut total: u32 = 0;
    for line in input_lines {
        let mut first_digit: char = '?';
        let mut last_digit: char = '?';
        for c in line.chars() {
            if c.is_digit(10) {
                if first_digit == '?' {
                    first_digit = c;
                }
                
                last_digit = c;
            }
        }

        let mut nr_str = first_digit.to_string();
        nr_str.push(last_digit);
        total += nr_str.parse::<u32>().expect(format!("NaN {}", nr_str).as_str());
    }

    return total;
}

fn part2(input_lines: Vec<&str>) -> u32 {
    let mut total: u32 = 0;
    for line in input_lines {
        let mut first_digit: char = '?';
        let mut last_digit: char = '?';

        let chars: std::str::Chars<'_> = line.chars();
        let chars_count = chars.clone().count();
        for (i, c) in chars.enumerate() {
            let mut curr_digit = '?';

            if c.is_digit(10) {
                curr_digit = c;
            }
            
            if curr_digit == '?' && i + 4 < chars_count {
                let std = string_to_digit(&line[i..i+5]);
                if std.is_digit(10) {
                    curr_digit = std;
                }
            }
            
            if curr_digit == '?' && i + 3 < chars_count {
                let std = string_to_digit(&line[i..i+4]);
                if std.is_digit(10) {
                    curr_digit = std;
                }
            }
            
            if curr_digit == '?' && i + 2 < chars_count {
                let std = string_to_digit(&line[i..i+3]);
                if std.is_digit(10) {
                    curr_digit = std;
                }
            }

            if curr_digit != '?' {
                if first_digit == '?' {
                    first_digit = curr_digit;
                }
                
                last_digit = curr_digit;
            }
        }

        let mut nr_str = first_digit.to_string();
        nr_str.push(last_digit);
        total += nr_str.parse::<u32>().expect(format!("NaN {}", nr_str).as_str());
    }

    return total;
}

fn string_to_digit(str: &str) -> char {
    match str {
        "zero" => '0', // length 4
        "one" => '1', // 3
        "two" => '2', // 3
        "three" => '3', // 5
        "four" => '4', // 4
        "five" => '5', // 4
        "six" => '6', // 3
        "seven" => '7', // 5
        "eight" => '8', // 5
        "nine" => '9', // 4
        _ => '?'
    }
}