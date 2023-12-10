use std::fs;
use std::collections::HashMap;
use num_integer::lcm;

fn main() {
    let input_full: String = fs::read_to_string("input.txt").expect("Couldn't read file");
    let input_lines: Vec<_> = input_full.split("\n").collect();

    let (instructions, node_map) = parse_input(input_lines);

    println!("Number of steps required to go from AAA to ZZZ (part 1): {}", part1(instructions.clone(), node_map.clone()));
    println!("Number of steps required for all nodes to end on Z (part 2): {}", part2(instructions.clone(), node_map.clone()));
}

fn part1(instructions: Vec<u8>, node_map: HashMap<&str, Vec<&str>>) -> u32 {
    let mut n_steps: u32 = 0;

    let mut curr_instruction_idx: usize = 0;
    let mut curr_node = "AAA";

    while curr_node != "ZZZ" {
        curr_node = node_map.get(curr_node).expect("")[instructions[curr_instruction_idx] as usize];

        if curr_instruction_idx == instructions.len() - 1 {
            curr_instruction_idx = 0;
        } else {
            curr_instruction_idx += 1;
        }

        n_steps += 1;
    }

    return n_steps;
}

fn part2(instructions: Vec<u8>, node_map: HashMap<&str, Vec<&str>>) -> u64 {
    let mut n_steps: u32 = 0;

    let mut curr_instruction_idx: usize = 0;

    let mut curr_nodes: Vec<&str> = node_map.keys().copied().filter(|k| k.ends_with('A')).collect();
    let mut moves_to_z_node: Vec<u64> = Vec::new();
    loop {
        for (idx, node) in curr_nodes.clone().iter().rev().enumerate() {
            if node.ends_with('Z') {
                moves_to_z_node.push(n_steps as u64);
                curr_nodes.remove(curr_nodes.len() - 1 - idx);
            }
        }
        
        if curr_nodes.is_empty() {
            break;
        }

        curr_nodes = curr_nodes.iter().map(|n| node_map.get(n).expect("")[instructions[curr_instruction_idx] as usize]).collect();

        if curr_instruction_idx == instructions.len() - 1 {
            curr_instruction_idx = 0;
        } else {
            curr_instruction_idx += 1;
        }

        n_steps += 1;
    }

    return lcm_multiple(moves_to_z_node);
}

fn lcm_multiple(numbers: Vec<u64>) -> u64 {
    if numbers.len() == 2 {
        return lcm(numbers[0], numbers[1]);
    }

    let lcm_other: u64 = lcm_multiple(numbers[1..].to_vec());

    return lcm(numbers[0], lcm_other);
}

fn parse_input(input_lines: Vec<&str>) -> (Vec<u8>, HashMap<&str, Vec<&str>>) {
    let instructions: Vec<u8> = input_lines[0].chars().map(|c| if c == 'L' { return 0; } else { return 1; }).collect();

    let mut node_map: HashMap<&str, Vec<&str>> = HashMap::new();

    for node_line in &input_lines[2..] {
        let node: &str = &node_line[..3];
        let left_node: &str = &node_line[7..10];
        let right_node: &str = &node_line[12..15];

        node_map.insert(node, vec![left_node, right_node]);
    }

    return (instructions, node_map);
}