package abcPlayerRand.raters;

import abcPlayerRand.utils.Utils;
import reversi.Coordinates;
import reversi.GameBoard;
import reversi.OutOfBoundsException;
import abcPlayerRand.utils.IRateBoard;

import java.util.*;

public class StableDiscRater implements IRateBoard {
    private int player;

    public StableDiscRater() {
    }

    @Override
    public int rateBoard(GameBoard board) {
        try {
            boolean[][] stables = new boolean[8][8];
            int stableSum = 0;

            // look at the corners
            stables[0][0] = board.getOccupation(new Coordinates(1, 1)) == player;
            if (stables[0][0])
                stableSum++;
            stables[7][0] = board.getOccupation(new Coordinates(8, 1)) == player;
            if (stables[7][0])
                stableSum++;
            stables[0][7] = board.getOccupation(new Coordinates(1, 8)) == player;
            if (stables[0][7])
                stableSum++;
            stables[7][7] = board.getOccupation(new Coordinates(1, 1)) == player;
            if (stables[7][7])
                stableSum++;

            // look first at the edges:
            stableSum = checkEdgeVert(board, stables, stableSum, 0);
            stableSum = checkEdgeVert(board, stables, stableSum, 7);
            stableSum = checkEdgeHoriz(board, stables, stableSum, 0);
            stableSum = checkEdgeHoriz(board, stables, stableSum, 7);
            // bail out if we have very little to do
            if (stableSum < 12)
                return stableSum;

            // TODO:
            return stableSum;
        } catch (OutOfBoundsException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private int checkEdgeVert(GameBoard board, boolean[][] stables, int stableSum, int col) throws OutOfBoundsException {
        int idown;
        for (idown = 1; idown < 7 && stables[idown - 1][col]; ++idown) {
            stables[idown][col] = board.getOccupation(new Coordinates(idown + 1, col + 1)) == player;
            if (!stables[idown][col])
                break;
            stableSum++;
        }
        int iup;
        for (iup = 6; idown < iup && stables[iup + 1][col]; --iup) {
            stables[iup][col] = board.getOccupation(new Coordinates(iup + 1, col + 1)) == player;
            if (!stables[iup][col])
                break;
            stableSum++;
        }
        return stableSum;
    }

    private int checkEdgeHoriz(GameBoard board, boolean[][] stables, int stableSum, int row) throws OutOfBoundsException {
        int idown;
        for (idown = 1; idown < 7 && stables[row][idown - 1]; ++idown) {
            stables[row][idown] = board.getOccupation(new Coordinates(row + 1, idown + 1)) == player;
            if (!stables[row][idown])
                break;
            stableSum++;
        }
        int iup;
        for (iup = 6; idown < iup && stables[row][iup + 1]; --iup) {
            stables[row][iup] = board.getOccupation(new Coordinates(row + 1, iup + 1)) == player;
            if (!stables[row][iup])
                break;
            stableSum++;
        }
        return stableSum;
    }
    @Override
    public void setPlayer(int player) {
        this.player = player;
    }
}
