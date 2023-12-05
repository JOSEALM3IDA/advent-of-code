use std::fs;

fn main() {
    let input_full: String = fs::read_to_string("input.txt").expect("Couldn't read file");
    let input_lines: Vec<_> = input_full.split("\n").collect();
    let (winning_numbers, scratched_numbers) = get_scratchcards(input_lines);

    println!("Total number of points in the scratchcards (part 1): {}", part1(winning_numbers.clone(), scratched_numbers.clone()));
    println!("Total scratchcards (part 2): {}", part2(winning_numbers.clone(), scratched_numbers.clone()));
}

fn part1(winning_numbers: Vec<Vec<u16>>, scratched_numbers: Vec<Vec<u16>>) -> u64 {
    let mut sum: u64 = 0;

    for i in 0..winning_numbers.len() {
        let mut count: u32 = 0;
        for wn in &winning_numbers[i] {
            count += scratched_numbers[i].iter().fold(0, |count, n| if n == wn { count + 1 } else { count });
        }

        if count == 0 {
            continue;
        }

        let base: i32 = 2;
        sum += base.pow(count - 1) as u64;
    }

    return sum;
}

fn part2(winning_numbers: Vec<Vec<u16>>, scratched_numbers: Vec<Vec<u16>>) -> u64 {
    let mut scratchcard_count: Vec<u64> = vec![1; winning_numbers.len()];

    for i in 0..winning_numbers.len() {
        for c in 0..scratchcard_count[i] {
            let mut count: usize = 0;
            for wn in &winning_numbers[i] {
                count += scratched_numbers[i].iter().fold(0, |count, n| if n == wn { count + 1 } else { count });
            }

            if count == 0 {
                continue;
            }

            for j in 0..count {
                scratchcard_count[i + j + 1] += 1;
            }
        }        
    }

    return scratchcard_count.iter().sum();
}

fn get_scratchcards(input_lines: Vec<&str>) -> (Vec<Vec<u16>>, Vec<Vec<u16>>) {
    let mut winning_numbers = Vec::new();
    let mut scratched_numbers = Vec::new();

    for line in input_lines {
        let split: Vec<_> = line.split(": ").collect();
        let replace_spaces = split[1].replace("  ", " ");
        let numbers: Vec<_> = replace_spaces.split(" | ").collect();

        winning_numbers.push(numbers[0].split(" ").filter(|&x| !x.is_empty()).map(|x| x.parse::<u16>().unwrap()).collect());
        scratched_numbers.push(numbers[1].split(" ").filter(|&x| !x.is_empty()).map(|x| x.parse::<u16>().unwrap()).collect());
    }

    return (winning_numbers, scratched_numbers);
}