package pt.josealm3ida.aoc2022.day11;

enum Operation {
    SUM,
    PRODUCT,
    SQUARE;

    public static Operation getOperation(char c) {
        return switch (c) {
            case '+' -> SUM;
            case '*' -> PRODUCT;
            default -> SQUARE;
        };
    }

    public long applyOperation(long n1, int n2) {
        return switch (this) {
            case SUM -> n1 + n2;
            case PRODUCT -> n1 * n2;
            case SQUARE -> n1 * n1;
        };
    }
}
