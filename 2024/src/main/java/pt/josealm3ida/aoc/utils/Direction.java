package pt.josealm3ida.aoc.utils;

public enum Direction {
    LEFT(0, -1),
    DOWN(1, 0),
    UP(-1, 0),
    RIGHT(0, 1),
    LEFT_DOWN(1, -1),
    LEFT_UP(-1, -1),
    RIGHT_DOWN(1, 1),
    RIGHT_UP(-1, 1);

    private int changeVertical;
    private int changeHorizontal;

    private Direction(int changeVertical, int changeHorizontal) {
        this.changeVertical = changeVertical;
        this.changeHorizontal = changeHorizontal;
    }

    public int calcNextRow(int currRow) {
        return calcNextRowNTimes(currRow, 1);
    }

    public int calcNextRowNTimes(int currRow, int nTimes) {
        return currRow + nTimes * changeVertical;
    }

    public int calcNextCol(int currCol) {
        return calcNextColNTimes(currCol, 1);
    }

    public int calcNextColNTimes(int currCol, int nTimes) {
        return currCol + nTimes * changeHorizontal;
    }

    public Direction getOpposite() {
        return switch (this) {
            case LEFT -> RIGHT;
            case DOWN -> UP;
            case UP -> DOWN;
            case RIGHT -> LEFT;
            case LEFT_DOWN -> RIGHT_UP;
            case LEFT_UP -> RIGHT_DOWN;
            case RIGHT_DOWN -> LEFT_UP;
            case RIGHT_UP -> LEFT_DOWN;
        };
    }

    public boolean isDiagonal() {
        return switch (this) {
            case LEFT_DOWN, LEFT_UP, RIGHT_DOWN, RIGHT_UP -> true;
            default -> false;
        };
    }

    public boolean isVertical() {
        return switch (this) {
            case UP, DOWN -> true;
            default -> false;
        };
    }

    public boolean isHorizontal() {
        return switch (this) {
            case LEFT, RIGHT -> true;
            default -> false;
        };
    }

}
