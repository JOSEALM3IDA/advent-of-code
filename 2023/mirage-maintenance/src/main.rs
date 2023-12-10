use std::fs;

fn main() {
    let input_full: String = fs::read_to_string("input.txt").expect("Couldn't read file");
    let input_lines: Vec<_> = input_full.split("\n").collect();

    let histories: Vec<Vec<i32>> = input_lines.iter().map(|h| h.split_ascii_whitespace().map(|n| n.parse().unwrap()).collect()).collect();

    println!("Sum of the forward extrapolated values (part 1): {}", part1(histories.clone()));
    println!("Sum of the backward extrapolated values (part 2): {}", part2(histories.clone()));
}

fn part1(histories: Vec<Vec<i32>>) -> i64 {
    let mut sum: i64 = 0;

    for history in histories {
        let diff_list: Vec<Vec<i32>> = get_diff_list(history);

        let mut extrapolated_diff_list: Vec<Vec<i32>> = Vec::new();
        let mut last_diff: Vec<i32> = diff_list.get(0).unwrap().to_vec();
        last_diff.push(0);

        extrapolated_diff_list.push(last_diff);

        for i in 1..diff_list.len() {
            let mut diff: Vec<i32> = diff_list.get(i).unwrap().to_vec();
            let last_element_current: i32 = *diff.last().unwrap();
            let last_element_previous: i32 = *extrapolated_diff_list.last().unwrap().last().unwrap();

            diff.push(last_element_current + last_element_previous);
            extrapolated_diff_list.push(diff);
        }

        sum += *extrapolated_diff_list.last().unwrap().last().unwrap() as i64;
    }

    return sum;
}

fn part2(histories: Vec<Vec<i32>>) -> i64 {
    let mut sum: i64 = 0;

    for history in histories {
        let diff_list: Vec<Vec<i32>> = get_diff_list(history);

        let mut extrapolated_diff_list: Vec<Vec<i32>> = Vec::new();
        let mut last_diff: Vec<i32> = diff_list.get(0).unwrap().to_vec();
        last_diff.insert(0, 0);

        extrapolated_diff_list.push(last_diff);

        for i in 1..diff_list.len() {
            let mut diff: Vec<i32> = diff_list.get(i).unwrap().to_vec();
            let first_element_current: i32 = *diff.first().unwrap();
            let first_element_previous: i32 = *extrapolated_diff_list.last().unwrap().first().unwrap();

            diff.insert(0, first_element_current - first_element_previous);
            extrapolated_diff_list.push(diff);
        }

        sum += *extrapolated_diff_list.last().unwrap().first().unwrap() as i64;
    }

    return sum;
}

fn get_diff_list(history: Vec<i32>) -> Vec<Vec<i32>> {
    let mut diff_list: Vec<Vec<i32>> = vec![history];
    loop {
        let last_diff: &Vec<i32> = &diff_list.last().unwrap();

        let mut curr_diff: Vec<i32> = Vec::new();
        for i in 0..last_diff.len()-1 {
            curr_diff.push(last_diff[i + 1] - last_diff[i]);
        }

        diff_list.push(curr_diff.clone());

        if !curr_diff.iter().any(|n| *n != 0) {
            break;
        }
    }

    diff_list.reverse();

    return diff_list;
}