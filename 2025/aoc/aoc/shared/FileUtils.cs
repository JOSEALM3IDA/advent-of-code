namespace aoc.shared;

public static class FileUtils
{

    public static string[] GetInput(string puzzleName)
    {
        return File.ReadAllText($"../../../input/{puzzleName}.txt").Split("\n");
    }
}