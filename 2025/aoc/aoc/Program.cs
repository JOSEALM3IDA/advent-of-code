using aoc.secret_entrance;
using aoc.shared;

var day1Input = FileUtils.GetInput("secret_entrance");

var day1Solver = new SecretEntranceSolver(day1Input);
var day1Part1Solution = day1Solver.SolvePart1();
var day1Part2Solution = day1Solver.SolvePart2();

Console.WriteLine("Day 1 part 1: " + day1Part1Solution);
Console.WriteLine("Day 1 part 2: " + day1Part2Solution);
