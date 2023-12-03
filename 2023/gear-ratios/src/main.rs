use std::fs;
use std::collections::HashMap;

// If it doesn't work, check if input is LF formatted
fn main() {
    let input_full: String = fs::read_to_string("input.txt").expect("Couldn't read file");
    let input_lines: Vec<_> = input_full.split("\n").collect();

    println!("Sum of all part numbers in the engine schematic (part 1): {}", part1(input_lines.clone()));
    println!("Sum of the gear rations in the engine schematic (part 2): {}", part2(input_lines.clone()));
}

fn part1(input_lines: Vec<&str>) -> u32 {
    let line_length: usize = input_lines[0].len();
    let mut sum: u32 = 0;

    for (i, l) in input_lines.clone().iter().enumerate() {
        let mut is_number_open: bool = false;
        let mut is_open_number_valid: bool = false;
        let mut open_number_start: usize = 0;
        for (j, c) in l.chars().enumerate() {
            let is_digit = c.is_digit(10);

            if !is_digit {
                if is_number_open {
                    if is_open_number_valid {
                        let n: u32 = l[open_number_start..j].parse().expect("NaN");
                        sum += n;
                    }
                    open_number_start = 0;
                    is_open_number_valid = false;
                    is_number_open = false;
                }
                continue;
            }

            if is_number_open {
                if !is_open_number_valid && is_digit_valid(input_lines.clone(), i, j) {
                    is_open_number_valid = true;
                }
                continue;
            }

            is_number_open = true;
            open_number_start = j;

            if is_digit_valid(input_lines.clone(), i, j) {
                is_open_number_valid = true;
            }
        }

        if is_number_open {
            if is_open_number_valid {
                let n: u32 = l[open_number_start..line_length].parse().expect("NaN");
                sum += n;
            }
        }
    }

    return sum;
}

fn part2(input_lines: Vec<&str>) -> u32 {
    let line_length: usize = input_lines[0].len();
    let mut gear_map: HashMap<String, u32> = HashMap::new();
    let mut seen_gear_map: HashMap<String, u32> = HashMap::new();

    for (i, l) in input_lines.clone().iter().enumerate() {
        let mut is_number_open: bool = false;
        let mut open_number_start: usize = 0;
        let mut gear: String = "".to_string();
        for (j, c) in l.chars().enumerate() {
            let is_digit = c.is_digit(10);

            if !is_digit {
                if is_number_open {
                    if gear != "" {
                        let seen_gear_times = seen_gear_map.entry(gear.clone()).or_insert(0);
                        *seen_gear_times += 1;

                        let n: u32 = l[open_number_start..j].parse().expect("NaN");
                        let curr_ratio = gear_map.entry(gear.clone()).or_insert(1);
                        *curr_ratio *= n;
                    }
                    open_number_start = 0;
                    gear = "".to_string();
                    is_number_open = false;
                }
                continue;
            }

            if is_number_open {
                let possible_gear: String = is_part_of_gear(input_lines.clone(), i, j);
                if gear == "" {
                    gear = possible_gear;
                }
                continue;
            }

            is_number_open = true;
            open_number_start = j;

            let possible_gear: String = is_part_of_gear(input_lines.clone(), i, j);
            if gear == "" {
                gear = possible_gear;
            }
        }

        if is_number_open {
            if gear != "" {
                let seen_gear_times = seen_gear_map.entry(gear.clone()).or_insert(0);
                *seen_gear_times += 1;

                let n: u32 = l[open_number_start..line_length].parse().expect("NaN");
                let curr_ratio = gear_map.entry(gear.clone()).or_insert(1);
                *curr_ratio *= n;
            }
        }
    }

    let mut sum: u32 = 0;
    for (gear, seen_times) in seen_gear_map {
        if seen_times > 1 {
            sum += gear_map.get(&gear).expect("Gear seen");
        }
    }

    return sum;
}

fn is_digit_valid(input_lines: Vec<&str>, row: usize, col: usize) -> bool {
    let n_lines: usize = input_lines.len();
    let line_length: usize = input_lines[0].len();

    // previous row //
    if row > 0 {
        if col > 0 {
            let ch = input_lines[row - 1].chars().nth(col - 1).expect("Panic out of bounds");
            if ch != '.' && !ch.is_digit(10) {
                return true;
            }
        }

        let ch = input_lines[row - 1].chars().nth(col).expect("Panic out of bounds");
        if ch != '.' && !ch.is_digit(10) {
            return true;
        }

        if col < line_length - 1 {
            let ch = input_lines[row - 1].chars().nth(col + 1).expect("Panic out of bounds");
            if ch != '.' && !ch.is_digit(10) {
                return true;
            }
        }
    }

    // current row //
    if col > 0 {
        let ch = input_lines[row].chars().nth(col - 1).expect("Panic out of bounds");
        if ch != '.' && !ch.is_digit(10) {
            return true;
        }
    }
    
    if col < line_length - 1 {
        let ch = input_lines[row].chars().nth(col + 1).expect("Panic out of bounds");
        if ch != '.' && !ch.is_digit(10) {
            return true;
        }
    }

    // next row //
    if row < n_lines - 1 {
        if col > 0 {
            let ch = input_lines[row + 1].chars().nth(col - 1).expect("Panic out of bounds");
            if ch != '.' && !ch.is_digit(10) {
                return true;
            }
        }

        let ch = input_lines[row + 1].chars().nth(col).expect("Panic out of bounds");
        if ch != '.' && !ch.is_digit(10) {
            return true;
        }

        if col < line_length - 1 {
            let ch = input_lines[row + 1].chars().nth(col + 1).expect("Panic out of bounds");
            if ch != '.' && !ch.is_digit(10) {
                return true;
            }
        }
    }

    return false;
}

fn is_part_of_gear(input_lines: Vec<&str>, row: usize, col: usize) -> String {
    let n_lines: usize = input_lines.len();
    let line_length: usize = input_lines[0].len();

    // previous row //
    if row > 0 {
        if col > 0 {
            let ch = input_lines[row - 1].chars().nth(col - 1).expect("Panic out of bounds");
            if ch == '*' {
                return (row - 1).to_string() + "," + &(col - 1).to_string();
            }
        }

        let ch = input_lines[row - 1].chars().nth(col).expect("Panic out of bounds");
        if ch == '*' {
            return (row - 1).to_string() + "," + &(col).to_string();
        }

        if col < line_length - 1 {
            let ch = input_lines[row - 1].chars().nth(col + 1).expect("Panic out of bounds");
            if ch == '*' {
                return (row - 1).to_string() + "," + &(col + 1).to_string();
            }
        }
    }

    // current row //
    if col > 0 {
        let ch = input_lines[row].chars().nth(col - 1).expect("Panic out of bounds");
        if ch == '*' {
            return row.to_string() + "," + &(col - 1).to_string();
        }
    }
    
    if col < line_length - 1 {
        let ch = input_lines[row].chars().nth(col + 1).expect("Panic out of bounds");
        if ch == '*' {
            return row.to_string() + "," + &(col + 1).to_string();
        }
    }

    // next row //
    if row < n_lines - 1 {
        if col > 0 {
            let ch = input_lines[row + 1].chars().nth(col - 1).expect("Panic out of bounds");
            if ch == '*' {
                return (row + 1).to_string() + "," + &(col - 1).to_string();
            }
        }

        let ch = input_lines[row + 1].chars().nth(col).expect("Panic out of bounds");
        if ch == '*' {
            return (row + 1).to_string() + "," + &col.to_string();
        }

        if col < line_length - 1 {
            let ch = input_lines[row + 1].chars().nth(col + 1).expect("Panic out of bounds");
            if ch == '*' {
                return (row + 1).to_string() + "," + &(col + 1).to_string();
            }
        }
    }

    return "".to_string();
}