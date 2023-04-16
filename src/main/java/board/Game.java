package board;

import move.MoveGenerator;
import move.MoveLogic;
import shared.GameParameters;

import java.awt.*;
import java.util.List;

public class Game {

    private Board board;

    private boolean isP1Turn;

    private int skipIndex;

    public Game() {
        restart();
    }

    public Game copy() {
        Game g = new Game();
        g.board = board.copy();
        g.isP1Turn = isP1Turn;
        g.skipIndex = skipIndex;
        return g;
    }

    public void restart() {
        this.board = new Board();
        this.isP1Turn = true;
        this.skipIndex = -1;
    }

    public boolean move(int startIndex, int endIndex) {

        if (!MoveLogic.isValidMove(this, startIndex, endIndex)) {
            return false;
        }

        Point middle = Board.middle(startIndex, endIndex);
        int midIndex = Board.toIndex(middle);
        this.board.set(endIndex, board.get(startIndex));
        this.board.set(midIndex, Board.EMPTY);
        this.board.set(startIndex, Board.EMPTY);

        Point end = Board.toPoint(endIndex);
        int id = board.get(endIndex);
        boolean switchTurn = false;
        if (end.y == 0 && id == Board.WHITE_CHECKER) {
            this.board.set(endIndex, Board.WHITE_KING);
            switchTurn = true;
        } else if (end.y == GameParameters.getStaticSizeOfBoard() - 1 && id == Board.BLACK_CHECKER) {
            this.board.set(endIndex, Board.BLACK_KING);
            switchTurn = true;
        }

        boolean midValid = Board.isValidIndex(midIndex);
        if (midValid) {
            this.skipIndex = endIndex;
        }
        if (!midValid || MoveGenerator.getSkips(board.copy(), endIndex).isEmpty()) {
            switchTurn = true;
        }
        if (switchTurn) {
            this.isP1Turn = !isP1Turn;
            this.skipIndex = -1;
        }

        return true;
    }

    public Board getBoard() {
        return board.copy();
    }

    public boolean isGameOver() {

        List<Point> black = board.find(Board.BLACK_CHECKER);
        black.addAll(board.find(Board.BLACK_KING));
        if (black.isEmpty()) {
            return true;
        }
        List<Point> white = board.find(Board.WHITE_CHECKER);
        white.addAll(board.find(Board.WHITE_KING));
        if (white.isEmpty()) {
            return true;
        }

        List<Point> test = isP1Turn ? black : white;
        for (Point p : test) {
            int i = Board.toIndex(p);
            if (!MoveGenerator.getMoves(board, i).isEmpty() ||
                    !MoveGenerator.getSkips(board, i).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public boolean isP1Turn() {
        return isP1Turn;
    }

    public int getSkipIndex() {
        return skipIndex;
    }

    public void setGameState(String state) {

        restart();

        if (state == null || state.isEmpty()) {
            return;
        }

        int n = state.length();
        for (int i = 0; i < (GameParameters.getStaticSizeOfBoard() * 4) && i < n; i++) {
            try {
                int id = Integer.parseInt("" + state.charAt(i));
                this.board.set(i, id);
            } catch (NumberFormatException ignored) {

            }
        }

        if (n > GameParameters.getStaticSizeOfBoard() * 4) {
            this.isP1Turn = (state.charAt(GameParameters.getStaticSizeOfBoard() * 4) == '1');
        }
        if (n > (GameParameters.getStaticSizeOfBoard() * 4) + 1) {
            try {
                this.skipIndex = Integer.parseInt(state.substring((GameParameters.getStaticSizeOfBoard() * 4) + 1));
            } catch (NumberFormatException e) {
                this.skipIndex = -1;
            }
        }
    }
}
