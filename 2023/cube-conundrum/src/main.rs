use std::fs;

fn main() {
    let input_full: String = fs::read_to_string("input.txt").expect("Couldn't read file");
    let input_lines: Vec<_> = input_full.split("\n").collect();
    let games: Vec<Vec<Vec<u32>>> = parse_input(input_lines);

    println!("Sum of the IDs of possible games (part 1): {}", part1(games.clone()));
    println!("Sum of the power of the sets (part 2): {}", part2(games.clone()));
}

fn part1(games: Vec<Vec<Vec<u32>>>) -> u32 {
    let mut sum: u32 = 0;
    let max_cubes = vec![12, 13, 14]; // r g b
    for (i, game) in games.iter().enumerate() {
        let mut possible = true;
        for set in game {
            if set[0] > max_cubes[0] || set[1] > max_cubes[1] || set[2] > max_cubes[2] {
                possible = false;
                break;
            }
        }

        if possible {
            sum += (i + 1) as u32;
        }
    }

    return sum;
}

fn part2(games: Vec<Vec<Vec<u32>>>) -> u32 {
    let mut sum: u32 = 0;
    for game in games {
        let mut min_possible_cubes = vec![0, 0, 0]; // r g b
        for set in game {
            if set[0] > min_possible_cubes[0] {
                min_possible_cubes[0] = set[0];
            }

            if set[1] > min_possible_cubes[1] {
                min_possible_cubes[1] = set[1];
            }

            if set[2] > min_possible_cubes[2] {
                min_possible_cubes[2] = set[2];
            }
        }

        sum += min_possible_cubes[0] * min_possible_cubes[1] * min_possible_cubes[2];
    }

    return sum;
}

fn parse_input(input_lines: Vec<&str>) -> Vec<Vec<Vec<u32>>> {
    let mut games = Vec::new();

    for line in input_lines {
        games.push(parse_game(line));
    }
    
    return games;
}

fn parse_game(game_str: &str) -> Vec<Vec<u32>> {
    let sets_together: Vec<_> = game_str.split(": ").collect();
    let sets: Vec<_> = sets_together[1].split("; ").collect();

    let mut parsed_game: Vec<Vec<u32>> = Vec::new();

    for set in sets {
        let cubes_info: Vec<_> = set.split(", ").collect();
        let mut set_info: Vec<_> = vec![0, 0, 0]; // r g b

        for cube_info in cubes_info {
            let nr_color: Vec<_> = cube_info.split(" ").collect();

            let nr: u32 = nr_color[0].parse().expect("NaN");

            let color_idx: usize = match nr_color[1].chars().nth(0).expect("?") {
                'r' => 0,
                'g' => 1,
                'b' => 2,
                _ => 4
            };
            
            set_info[color_idx] = nr;
        }

        parsed_game.push(set_info);
    }

    return parsed_game;
}