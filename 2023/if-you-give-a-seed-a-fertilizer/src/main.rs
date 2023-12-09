use std::fs;

fn main() {
    let input_full: String = fs::read_to_string("input.txt").expect("Couldn't read file");
    let input_lines: Vec<_> = input_full.split("\n").collect();

    let (initial_seeds, transform_maps) = parse_input(input_lines);

    println!("Lowest location of initial seeds (part 1): {}", part1(initial_seeds.clone(), transform_maps.clone()));
    println!("Lowest location of initial seeds (part 2): {}", part2(initial_seeds.clone(), transform_maps.clone()));
}

fn part1(initial_seeds: Vec<u64>, transform_maps: Vec<Vec<(u64, u64, u64)>>) -> u64 {
    return get_lowest_applied_transform(initial_seeds, transform_maps);
}


// Could be implemented with range comparison instead but not worth it, code is fast enough (just takes like 2 hours)
// 0 564468486 226119074 -> 904081095
// 1 3264322485 135071952 -> 697158568
// 2 3144185610 89794463 -> 275428333
// 3 1560365167 555667043 -> 77864446
// 4 2419038624 7808461 -> 77864446
// 5 1264209624 9380035 -> 77864446
// 6 105823719 425973940 -> 52210644
// 7 4115473551 104486997 -> 52210644
// 8 3784956593 300187503 -> 52210644
// 9 975280918 257129208 -> 52210644
fn part2(initial_seeds: Vec<u64>, transform_maps: Vec<Vec<(u64, u64, u64)>>) -> u64 {
    let mut lowest_final_transform: Option<u64> = None;

    for i in (0..initial_seeds.len()).step_by(2) {
        let seed_range_start: u64 = initial_seeds[i];
        let seed_range_end: u64 = seed_range_start + initial_seeds[i + 1] - 1;

        let seed_range: Vec<u64> = (seed_range_start..seed_range_end).collect();

        let sub_lowest_final_transform = get_lowest_applied_transform(seed_range, transform_maps.clone());

        if lowest_final_transform.is_none() || sub_lowest_final_transform < lowest_final_transform.expect("none") {
            lowest_final_transform = Some(sub_lowest_final_transform);
        }

        println!("Checked range {}. Current lowest location: {}", i/2, lowest_final_transform.expect("none"));
    }

    return lowest_final_transform.expect("none");
}

fn parse_input(input_lines: Vec<&str>) -> (Vec<u64>, Vec<Vec<(u64, u64, u64)>>) {
    let first_line_split: Vec<_> = input_lines[0].split(": ").collect();
    let initial_seeds: Vec<u64> = first_line_split[1].split_ascii_whitespace().map(|x| x.parse().expect("NaN")).collect();

    let relevant_input_slice: Vec<&&str> = input_lines[2..input_lines.len()].iter().filter(|s| !s.is_empty()).collect();
    let mut current_map: Vec<(u64, u64, u64)> = Vec::new();
    let mut transform_maps: Vec<Vec<(u64, u64, u64)>> = Vec::new();
    for line in relevant_input_slice {
        if !line.as_bytes()[0].is_ascii_digit() {
            if !current_map.is_empty() {
                transform_maps.push(current_map.clone());
                current_map.clear();
            }

            continue;
        }

        let line_nrs: Vec<u64> = line.split_ascii_whitespace().map(|x| x.parse().expect("NaN")).collect();
        current_map.push((line_nrs[0], line_nrs[1], line_nrs[2]));
    }

    transform_maps.push(current_map.clone());

    return (initial_seeds, transform_maps);
}

fn get_lowest_applied_transform(initial_seeds: Vec<u64>, transform_maps: Vec<Vec<(u64, u64, u64)>>) -> u64 {
    let mut lowest_final_transform: Option<u64> = None;

    for seed in initial_seeds {
        let mut curr_transform = seed;
        for transform_map in transform_maps.clone() {
            curr_transform = transform(curr_transform, transform_map);
        }

        if lowest_final_transform.is_none() || curr_transform < lowest_final_transform.expect("none") {
            lowest_final_transform = Some(curr_transform);
        }
    }

    return lowest_final_transform.expect("none");
}

fn transform(n: u64, transform_map: Vec<(u64, u64, u64)>) -> u64 {
    for range in transform_map {
        let destination_range_start: u64 = range.0;

        let source_range_start: u64 = range.1;
        let source_range_end: u64 = range.1 + range.2;

        //println!("{} <= {} < {}", source_range_start, n, source_range_end);
        if source_range_start <= n && n < source_range_end {
            return destination_range_start + (n - source_range_start);
        }
    }

    return n;
}