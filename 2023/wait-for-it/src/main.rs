use std::fs;

fn main() {
    let input_full: String = fs::read_to_string("input.txt").expect("Couldn't read file");
    let input_lines: Vec<_> = input_full.split("\n").collect();

    println!("Number of ways to beat the record, multiplied (part 1): {}", part1(input_lines.clone()));
    println!("Number of ways to beat the record (part 2): {}", part2(input_lines.clone()));
}

fn part1(input_lines: Vec<&str>) -> u64 {
    let times_split: Vec<_> = input_lines[0].split(":").collect();
    let times: Vec<u32> = times_split[1].split_ascii_whitespace().map(|x| x.parse().expect("NaN")).collect();

    let distances_split: Vec<_> = input_lines[1].split(":").collect();
    let distances: Vec<u32> = distances_split[1].split_ascii_whitespace().map(|x| x.parse().expect("NaN")).collect();

    let mut result: u64 = 1;

    for i in 0..times.len() {
        let time = times[i];
        let distance = distances[i];
        result *= get_num_ways(time as u64, distance as u64);
    }

    return result;
}

fn part2(input_lines: Vec<&str>) -> u64 {
    let times_split: Vec<_> = input_lines[0].split(":").collect();
    let time: u64 = times_split[1].replace(" ", "").parse().expect("NaN");

    let distances_split: Vec<_> = input_lines[1].split(":").collect();
    let distance: u64 = distances_split[1].replace(" ", "").parse().expect("NaN");
    
    return get_num_ways(time, distance);
}

fn get_num_ways(time: u64, distance: u64) -> u64 {
    let mut min_time_button = 0;
    for t in 1..time {
        let max_distance = t * (time - t);
        if max_distance > distance {
            min_time_button = t;
            break;
        }
    }

    let mut max_time_button = 0;
    for t in (1..time).rev() {
        let max_distance = t * (time - t);
        if max_distance > distance {
            max_time_button = t;
            break;
        }
    }

    return max_time_button - min_time_button + 1;
}