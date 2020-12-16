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
            stables[7][7] = board.getOccupation(new Coordinates(8, 8)) == player;
            if (stables[7][7])
                stableSum++;

            // look first at the edges:
            stableSum = checkEdgeVert(board, stables, stableSum, 0);
            stableSum = checkEdgeVert(board, stables, stableSum, 7);
            stableSum = checkEdgeHoriz(board, stables, stableSum, 0);
            stableSum = checkEdgeHoriz(board, stables, stableSum, 7);
            // bail out if we have very little to do
            if (stableSum < 12)
                return 1000*stableSum;

            // TODO:

            //fill stables
            if (stables[0][0]) {
                checkDiagonalUpperLeft(stables, board);
            }
            if (stables[7][7]) {
                checkDiagonalLowerRight(stables, board);
            }
            if (stables[1][7]) {
                checkDiagonalUpperRight(stables, board);
            }
            if (stables[7][1]) {
                checkDiagonalLowerLeft(stables, board);
            }

            //count true in stables
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    if (stables[x][y]) {
                        stableSum++;
                    }
                }
            }

            return 1000*stableSum;
        } catch (OutOfBoundsException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /*
     * 
     * @param row     >= 1
     * @param col     >= 1
     *
     */
    private boolean checkUpperLeft(boolean[][] stables, int row, int col, GameBoard board) {
        try {
            return stables[row - 1][col] && stables[row][col - 1] //direct neighbours
                    && (stables[row + 1][col - 1] || stables[row - 1][col + 1]) //diagonal neighbours
                    && board.getOccupation(new Coordinates(row + 1, col + 1)) == player;
        } catch (OutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void checkDiagonalUpperLeft(boolean[][] stables, GameBoard board) {
        for (int n = 2; n < 7; n++) { //diagonale
            int i = n - 1;
            int j = 1;
            int numberOfDiagonals = i - j + 1;
            int noFundamentalStableDisc = 0;

            for (int a = 0; a < numberOfDiagonals; a++) { //von unten links nach oben rechts
                if (checkUpperLeft(stables, i, j, board)) {
                    stables[i][j] = true;
                } else {
                    if (a == 0) {
                        noFundamentalStableDisc++;
                    }
                    break;
                }
                i--;
                j++;
            }

            i = 1;
            j = n - 1;

            for (int a = 0; a < numberOfDiagonals; a++) { //von oben rechts nach unten links
                if (checkUpperLeft(stables, i, j, board)) {
                    stables[i][j] = true;
                } else {
                    if (a == 0) {
                        noFundamentalStableDisc++;
                    }
                    break;
                }
                i++;
                j--;

            }

            if (noFundamentalStableDisc == 2) {
                break;
            }
        }
    }

    private boolean checkLowerRight(boolean[][] stables, int row, int col, GameBoard board) {
        try {
            return stables[row + 1][col] && stables[row][col + 1] //direct neighbours
                    && (stables[row + 1][col - 1] || stables[row - 1][col + 1]) //diagonal neighbours
                    && board.getOccupation(new Coordinates(row + 1, col + 1)) == player;
        } catch (OutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void checkDiagonalLowerRight(boolean[][] stables, GameBoard board) {
        for (int n = 12; n > 7; n--) { //diagonale
            int i = 6;
            int j = n - 6;
            int numberOfDiagonals = j - i + 1;
            int noFundamentalStableDisc = 0;

            for (int a = 0; a < numberOfDiagonals; a++) { //von unten links nach oben rechts
                if (checkLowerRight(stables, i, j, board)) {
                    stables[i][j] = true;
                } else {
                    if (a == 0) {
                        noFundamentalStableDisc++;
                    }
                    break;
                }
                i--;
                j++;
            }

            i = n - 6;
            j = 6;

            for (int a = 0; a < numberOfDiagonals; a++) { //von oben rechts nach unten links
                if (checkLowerRight(stables, i, j, board)) {
                    stables[i][j] = true;
                } else {
                    if (a == 0) {
                        noFundamentalStableDisc++;
                    }
                    break;
                }
                i++;
                j--;

            }

            if (noFundamentalStableDisc == 2) {
                break;
            }
        }
    }

    private boolean checkUpperRight(boolean[][] stables, int row, int col, GameBoard board) {
        try {
            return stables[row - 1][col] && stables[row][col + 1] //direct neighbours
                    && (stables[row - 1][col - 1] || stables[row + 1][col + 1]) //diagonal neighbours
                    && board.getOccupation(new Coordinates(row + 1, col + 1)) == player;
        } catch (OutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void checkDiagonalUpperRight(boolean[][] stables, GameBoard board) {
        int numberOfDiagonals = 0;
        for (int n = 6; n > 1; n--) { //diagonale
            int i = 1;
            int j = n;
            numberOfDiagonals++;
            int noFundamentalStableDisc = 0;

            for (int a = 0; a < numberOfDiagonals; a++) { //von oben links nach unten rechts
                if (checkUpperRight(stables, i, j, board)) {
                    stables[i][j] = true;
                } else {
                    if (a == 0) {
                        noFundamentalStableDisc++;
                    }
                    break;
                }
                i++;
                j++;
            }

            i = numberOfDiagonals;
            j = 6;

            for (int a = 0; a < numberOfDiagonals; a++) { //von oben rechts nach unten links
                if (checkUpperRight(stables, i, j, board)) {
                    stables[i][j] = true;
                } else {
                    if (a == 0) {
                        noFundamentalStableDisc++;
                    }
                    break;
                }
                i--;
                j--;

            }
            if (noFundamentalStableDisc == 2) {
                break;
            }
        }

    }

    private boolean checkLowerLeft(boolean[][] stables, int row, int col, GameBoard board) {
        try {
            return stables[row + 1][col] && stables[row][col - 1] //direct neighbours
                    && (stables[row + 1][col + 1] || stables[row - 1][col - 1]) //diagonal neighbours
                    && board.getOccupation(new Coordinates(row + 1, col + 1)) == player;
        } catch (OutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void checkDiagonalLowerLeft(boolean[][] stables, GameBoard board) {
        int numberOfDiagonals = 0;
        for (int n = 6; n > 1; n--) { //diagonale
            int i = n;
            int j = 1;
            numberOfDiagonals++;
            int noFundamentalStableDisc = 0;

            for (int a = 0; a < numberOfDiagonals; a++) { //von oben links nach unten rechts
                if (checkLowerLeft(stables, i, j, board)) {
                    stables[i][j] = true;
                } else {
                    if (a == 0) {
                        noFundamentalStableDisc++;
                    }
                    break;
                }
                i++;
                j++;
            }

            i = 6;
            j = numberOfDiagonals;

            for (int a = 0; a < numberOfDiagonals; a++) { //von oben rechts nach unten links
                if (checkLowerLeft(stables, i, j, board)) {
                    stables[i][j] = true;
                } else {
                    if (a == 0) {
                        noFundamentalStableDisc++;
                    }
                    break;
                }
                i--;
                j--;

            }
            if (noFundamentalStableDisc == 2) {
                break;
            }
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
