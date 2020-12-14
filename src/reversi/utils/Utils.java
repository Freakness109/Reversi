package reversi.utils;

import reversi.Coordinates;
import reversi.GameBoard;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    // don't make throwaway objects to tax the GC less
    private static final List<Coordinates> allCoords;

    static {
        // board size is 8x8
        allCoords = new ArrayList<>(8 * 8);
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; j++) {
                allCoords.add(new Coordinates(i, j));
            }
        }
    }

    public static List<Coordinates> getPossibleMoves(GameBoard board, int player) {
        int mobility = board.mobility(player);
        ArrayList<Coordinates> ret = new ArrayList<>(mobility);
        for (Coordinates coords : allCoords) {
            if (board.checkMove(player, coords)) {
                ret.add(coords);
            }
        }
        return ret;
    }

    public static List<Coordinates> getSurrounding(Coordinates center) {
        int row = center.getRow();
        int col = center.getCol();

        ArrayList<Coordinates> ret = new ArrayList<>(8);

        if (row > 0) {
            ret.add(new Coordinates(row - 1, col));
        }
        if (row < 7) {
            ret.add(new Coordinates(row + 1, col));
        }
        if (col > 0) {
            ret.add(new Coordinates(row, col - 1));
        }
        if (col < 7) {
            ret.add(new Coordinates(row, col + 1));
        }
        if (row > 0 && col > 0) {
            ret.add(new Coordinates(row - 1, col - 1));
        }
        if (row < 7 && col < 7) {
            ret.add(new Coordinates(row + 1, col + 1));
        }
        if (row > 0 && col < 7) {
            ret.add(new Coordinates(row - 1, col + 1));
        }
        if (row < 7 && col > 0) {
            ret.add(new Coordinates(row + 1, col - 1));
        }
        return ret;
    }

    public static int otherPlayer(int player) {
        if (player == GameBoard.GREEN) {
            return GameBoard.RED;
        } else {
            return GameBoard.GREEN;
        }
    }
}
