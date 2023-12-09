use std::{fs, collections::HashMap};
use itertools::Itertools;

fn main() {
    let input_full: String = fs::read_to_string("input.txt").expect("Couldn't read file");
    let input_lines: Vec<_> = input_full.split("\n").collect();

    let (hands, bids) = parse_input(input_lines);

    println!("Total winnings (part 1): {}", part1(hands.clone(), bids.clone()));
    println!("Total winnings (part 2): {}", part2(hands.clone(), bids.clone()));
}

fn part1(hands: Vec<&str>, bids: HashMap<&str, u64>) -> u64 {
    let mut sorted_hands = hands.clone();
    sorted_hands.sort_by(|h1, h2| compare_two_hands(h1, h2, false));

    let mut total_winnings: u64 = 0;
    for (i, hand) in sorted_hands.iter().enumerate() {
        total_winnings += (i as u64 + 1) * bids.get(hand).expect("");
    }

    return total_winnings;
}

fn part2(hands: Vec<&str>, bids: HashMap<&str, u64>) -> u64 {
    let mut sorted_hands = hands.clone();
    sorted_hands.sort_by(|h1, h2| compare_two_hands(h1, h2, true));

    let mut total_winnings: u64 = 0;
    for (i, hand) in sorted_hands.iter().enumerate() {
        total_winnings += (i as u64 + 1) * bids.get(hand).expect("");
    }

    return total_winnings;
}

fn compare_two_hands(h1: &str, h2: &str, joker_rule: bool) -> std::cmp::Ordering {
    let h1_all_card_count_map: HashMap<char, u32> = h1.chars().into_grouping_map_by(|&x| x).fold(0, |acc, _key, _value| acc + 1);
    let h2_all_card_count_map: HashMap<char, u32> = h2.chars().into_grouping_map_by(|&x| x).fold(0, |acc, _key, _value| acc + 1);

    let mut h1_card_count_map = h1_all_card_count_map.clone();
    let mut h2_card_count_map = h2_all_card_count_map.clone();

    let h1_joker_count = h1_all_card_count_map.get(&'J').unwrap_or(&0);
    let h2_joker_count = h2_all_card_count_map.get(&'J').unwrap_or(&0);

    if joker_rule {
        h1_card_count_map.remove(&'J');
        h2_card_count_map.remove(&'J');
    }

    let h1_most_common_card_count: &u32 = h1_card_count_map.iter().max_by(|a, b| a.1.cmp(&b.1)).map(|(_k, v)| v).unwrap_or(&0);
    let h2_most_common_card_count: &u32 = h2_card_count_map.iter().max_by(|a, b| a.1.cmp(&b.1)).map(|(_k, v)| v).unwrap_or(&0);

    // Five of a kind //
    let is_h1_five_of_a_kind: bool = *h1_most_common_card_count == 5 || (joker_rule && h1_most_common_card_count + h1_joker_count == 5);
    let is_h2_five_of_a_kind: bool = *h2_most_common_card_count == 5 || (joker_rule && h2_most_common_card_count + h2_joker_count == 5);

    if is_h1_five_of_a_kind && !is_h2_five_of_a_kind {
        return std::cmp::Ordering::Greater;
    }

    if !is_h1_five_of_a_kind && is_h2_five_of_a_kind {
        return std::cmp::Ordering::Less;
    }

    if is_h1_five_of_a_kind && is_h2_five_of_a_kind {
        return compare_two_equal_hands(h1, h2, joker_rule);
    }
    //

    // Four of a kind //
    let is_h1_four_of_a_kind: bool = *h1_most_common_card_count == 4 || (joker_rule && h1_most_common_card_count + h1_joker_count == 4);
    let is_h2_four_of_a_kind: bool = *h2_most_common_card_count == 4 || (joker_rule && h2_most_common_card_count + h2_joker_count == 4);

    if is_h1_four_of_a_kind && !is_h2_four_of_a_kind {
        return std::cmp::Ordering::Greater;
    }

    if !is_h1_four_of_a_kind && is_h2_four_of_a_kind {
        return std::cmp::Ordering::Less;
    }

    if is_h1_four_of_a_kind && is_h2_four_of_a_kind {
        return compare_two_equal_hands(h1, h2, joker_rule);
    }
    //

    // Full house //
    let is_h1_full_house: bool = (h1_card_count_map.len() == 2 && *h1_most_common_card_count == 3) || (joker_rule && h1_card_count_map.len() + 1 == 3 && h1_most_common_card_count + h1_joker_count == 3);
    let is_h2_full_house: bool = (h2_card_count_map.len() == 2 && *h2_most_common_card_count == 3) || (joker_rule && h2_card_count_map.len() + 1 == 3 && h2_most_common_card_count + h2_joker_count == 3);

    if is_h1_full_house && !is_h2_full_house {
        return std::cmp::Ordering::Greater;
    }

    if !is_h1_full_house && is_h2_full_house {
        return std::cmp::Ordering::Less;
    }

    if is_h1_full_house && is_h2_full_house {
        return compare_two_equal_hands(h1, h2, joker_rule);
    }
    //

    // Three of a kind //
    let is_h1_three_of_a_kind: bool = *h1_most_common_card_count == 3 || (joker_rule && h1_most_common_card_count + h1_joker_count == 3);
    let is_h2_three_of_a_kind: bool = *h2_most_common_card_count == 3 || (joker_rule && h2_most_common_card_count + h2_joker_count == 3);

    if is_h1_three_of_a_kind && !is_h2_three_of_a_kind {
        return std::cmp::Ordering::Greater;
    }

    if !is_h1_three_of_a_kind && is_h2_three_of_a_kind {
        return std::cmp::Ordering::Less;
    }

    if is_h1_three_of_a_kind && is_h2_three_of_a_kind {
        return compare_two_equal_hands(h1, h2, joker_rule);
    }
    //

    // Two pair //
    let is_h1_two_pair: bool = (h1_card_count_map.len() == 3 && *h1_most_common_card_count == 2) || (joker_rule && h1_card_count_map.len() + 1 == 4 && h1_most_common_card_count + h1_joker_count == 2);
    let is_h2_two_pair: bool = (h2_card_count_map.len() == 3 && *h2_most_common_card_count == 2) || (joker_rule && h2_card_count_map.len() + 1 == 4 && h2_most_common_card_count + h2_joker_count == 2);

    if is_h1_two_pair && !is_h2_two_pair {
        return std::cmp::Ordering::Greater;
    }

    if !is_h1_two_pair && is_h2_two_pair {
        return std::cmp::Ordering::Less;
    }

    if is_h1_two_pair && is_h2_two_pair {
        return compare_two_equal_hands(h1, h2, joker_rule);
    }
    //

    // One pair //
    let is_h1_one_pair: bool = *h1_most_common_card_count == 2 || (joker_rule && h1_most_common_card_count + h1_joker_count == 2);
    let is_h2_one_pair: bool = *h2_most_common_card_count == 2 || (joker_rule && h2_most_common_card_count + h2_joker_count == 2);

    if is_h1_one_pair && !is_h2_one_pair {
        return std::cmp::Ordering::Greater;
    }

    if !is_h1_one_pair && is_h2_one_pair {
        return std::cmp::Ordering::Less;
    }

    if is_h1_one_pair && is_h2_one_pair {
        return compare_two_equal_hands(h1, h2, joker_rule);
    }
    //

    return compare_two_equal_hands(h1, h2, joker_rule);
}

fn compare_two_equal_hands(h1: &str, h2: &str, joker_rule: bool) -> std::cmp::Ordering {
    let card_strength_order: Vec<char> =  vec!['A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2'];

    for i in 0..5 {
        let card_h1: char = h1.chars().nth(i).expect("");
        let card_h2: char = h2.chars().nth(i).expect("");

        if card_h1 == card_h2 {
            continue;
        }

        if joker_rule && card_h1 == 'J' {
            return std::cmp::Ordering::Less;
        }

        if joker_rule && card_h2 == 'J' {
            return std::cmp::Ordering::Greater;
        }

        let strength_index_h1 = card_strength_order.iter().position(|&c| c == card_h1).expect("");
        let strength_index_h2 = card_strength_order.iter().position(|&c| c == card_h2).expect("");

        if strength_index_h1 < strength_index_h2 {
            return std::cmp::Ordering::Greater;
        } else {
            return std::cmp::Ordering::Less;
        }
    }

    return std::cmp::Ordering::Equal; 
}

fn parse_input(input_lines: Vec<&str>) -> (Vec<&str>, HashMap<&str, u64>) {
    let mut hands: Vec<&str> = Vec::new();
    let mut bids: HashMap<&str, u64> = HashMap::new();

    for line in input_lines {
        let line_split: Vec<_> = line.split(" ").collect();
        hands.push(line_split[0]);
        bids.insert(line_split[0], line_split[1].parse().expect(""));
    }

    return (hands, bids);
}