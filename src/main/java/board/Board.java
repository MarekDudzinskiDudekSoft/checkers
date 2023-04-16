package board;

import shared.GameParameters;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Board {

    public static final int INVALID = -1;
    public static final int EMPTY = 0;
    public static final int BLACK_CHECKER = (GameParameters.getStaticSizeOfBoard() / 2) * 1 + (GameParameters.getStaticSizeOfBoard() / 4) * 1 + 1 * 0;
    public static final int WHITE_CHECKER = (GameParameters.getStaticSizeOfBoard() / 2) * 1 + (GameParameters.getStaticSizeOfBoard() / 4) * 0 + 1 * 0;
    public static final int BLACK_KING = (GameParameters.getStaticSizeOfBoard() / 2) * 1 + (GameParameters.getStaticSizeOfBoard() / 4) * 1 + 1 * 1;
    public static final int WHITE_KING = (GameParameters.getStaticSizeOfBoard() / 2) * 1 + (GameParameters.getStaticSizeOfBoard() / 4) * 0 + 1 * 1;

    private int[] state;

    public Board() {
        reset();
    }

    public Board copy() {
        Board copy = new Board();
        copy.state = state.clone();
        return copy;
    }

    public void reset() {

        this.state = new int[3];
        for (int i = 0; i < calculateCheckersNumber(); i++) {
            set(i, BLACK_CHECKER);
            set((GameParameters.getStaticSizeOfBoard() * 4) - 1 - i, WHITE_CHECKER);
        }
    }

    public List<Point> find(int id) {

        List<Point> points = new ArrayList<>();
        for (int i = 0; i < GameParameters.getStaticSizeOfBoard() * 4; i++) {
            if (get(i) == id) {
                points.add(toPoint(i));
            }
        }
        return points;
    }

    public void set(int index, int id) {
        if (!isValidIndex(index)) {
            return;
        }

        if (id < 0) {
            id = EMPTY;
        }

        for (int i = 0; i < state.length; i++) {
            boolean set = ((1 << (state.length - i - 1)) & id) != 0;
            this.state[i] = setBit(state[i], index, set);
        }
    }

    public int get(int x, int y) {
        return get(toIndex(x, y));
    }

    public int get(int index) {
        if (!isValidIndex(index)) {
            return INVALID;
        }
        return getBit(state[0], index) * (GameParameters.getStaticSizeOfBoard() / 2) + getBit(state[1], index) * 2
                + getBit(state[(GameParameters.getStaticSizeOfBoard()/4)], index);
    }

    public static Point toPoint(int index) {
        int y = index / (GameParameters.getStaticSizeOfBoard() / 2);
        int x = 2 * (index % (GameParameters.getStaticSizeOfBoard() / 2)) + (y + 1) % 2;
        return !isValidIndex(index) ? new Point(-1, -1) : new Point(x, y);
    }

    public static int toIndex(int x, int y) {

        if (!isValidPoint(new Point(x, y))) {
            return -1;
        }

        return y * (GameParameters.getStaticSizeOfBoard() / 2) + x / 2;
    }

    public static int toIndex(Point p) {
        return (p == null) ? -1 : toIndex(p.x, p.y);
    }

    public static int setBit(int target, int bit, boolean set) {

        if (bit < 0 || bit > (GameParameters.getStaticSizeOfBoard() * 4) - 1) {
            return target;
        }

        if (set) {
            target |= (1 << bit);
        } else {
            target &= (~(1 << bit));
        }

        return target;
    }

    public static int getBit(int target, int bit) {

        if (bit < 0 || bit > (GameParameters.getStaticSizeOfBoard() * 4) - 1) {
            return 0;
        }

        return (target & (1 << bit)) != 0 ? 1 : 0;
    }


    public static Point middle(Point p1, Point p2) {

        if (p1 == null || p2 == null) {
            return new Point(-1, -1);
        }

        return middle(p1.x, p1.y, p2.x, p2.y);
    }

    public static Point middle(int index1, int index2) {
        return middle(toPoint(index1), toPoint(index2));
    }

    public static Point middle(int x1, int y1, int x2, int y2) {


        int dx = x2 - x1;
        int dy = y2 - y1;
        if (x1 < 0
                || y1 < 0
                || x2 < 0
                || y2 < 0
                || x1 > GameParameters.getStaticSizeOfBoard() - 1
                || y1 > GameParameters.getStaticSizeOfBoard() - 1
                || x2 > GameParameters.getStaticSizeOfBoard() - 1
                || y2 > GameParameters.getStaticSizeOfBoard() - 1) {
            return new Point(-1, -1);
        }
        if (x1 % 2 == y1 % 2 || x2 % 2 == y2 % 2) {
            return new Point(-1, -1);
        }
        if (Math.abs(dx) != Math.abs(dy) || Math.abs(dx) != 2) {
            return new Point(-1, -1);
        }

        return new Point(x1 + dx / 2, y1 + dy / 2);
    }

    public static boolean isValidIndex(int index) {
        return index >= 0 && index < (GameParameters.getStaticSizeOfBoard() * 4);
    }

    public static boolean isValidPoint(Point point) {

        if (point == null) {
            return false;
        }

        final int x = point.x;
        final int y = point.y;
        if (x < 0
                || x > GameParameters.getStaticSizeOfBoard() - 1
                || y < 0
                || y > GameParameters.getStaticSizeOfBoard() - 1) {
            return false;
        }

        return x % 2 != y % 2;
    }

    private int calculateCheckersNumber() {
        return ((GameParameters.getStaticSizeOfBoard() * 3) / 2);
    }

    @Override
    public String toString() {
        String obj = getClass().getName() + "[";
        for (int i = 0; i < (GameParameters.getStaticSizeOfBoard() * 4) - 1; i++) {
            obj += get(i) + ", ";
        }
        obj += get((GameParameters.getStaticSizeOfBoard() * 4) - 1);

        return obj + "]";
    }
}
