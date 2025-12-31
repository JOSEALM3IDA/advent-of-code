using aoc.shared;

namespace aoc.secret_entrance;

public class SecretEntranceSolver : ISolver
{
    private readonly string[] _input;

    private readonly List<sbyte> _rotations = [];
    private readonly List<sbyte> _fullRotations = [];

    public SecretEntranceSolver(string[] input)
    {
        _input = input;
        ParseInput();
    }

    private void ParseInput()
    {
        foreach (var line in _input)
        {
            var parsedDistance = int.Parse(line[1..]);
            var actualDistance = (sbyte) (parsedDistance % 100);
            
            _rotations.Add((sbyte)(line[0] == 'R' ? actualDistance : -actualDistance));
            _fullRotations.Add((sbyte) (parsedDistance / 100));
        }
    }

    public string SolvePart1()
    {
        var nZeros = 0;
        var dial = 50;
        foreach (var rotation in _rotations)
        {
            dial += rotation;

            switch (dial)
            {
                case < 0:
                    dial += 100;
                    break;
                case > 99:
                    dial %= 100;
                    break;
            }

            if (dial == 0)
                nZeros++;
        }

        return nZeros.ToString();
    }

    public string SolvePart2()
    {
        
        var nZeros = 0;
        var dial = 50;
        foreach (var (rotation, fullRotations) in _rotations.Zip(_fullRotations))
        {
            var dialStartedAtZero = dial == 0;
            
            nZeros += fullRotations;
            dial += rotation;

            switch (dial)
            {
                case < 0:
                    dial += 100;
                    if (!dialStartedAtZero)
                        nZeros++;
                    break;
                case > 99:
                    dial %= 100;
                    if (!dialStartedAtZero)
                        nZeros++;
                    break;
                case 0:
                    nZeros++;
                    break;
            }
        }

        return nZeros.ToString();
    }
}