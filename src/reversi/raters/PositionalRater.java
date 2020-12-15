package reversi.raters;

import reversi.Coordinates;
import reversi.GameBoard;
import reversi.OutOfBoundsException;
import reversi.utils.IRateBoard;

import java.util.ArrayList;

public class PositionalRater implements IRateBoard {
    private int player;
    private static ArrayList<Integer> valueLut;

    static {
        // weights from samsoft.org.uk/reversi/strategy.htm
        valueLut = new ArrayList<>(10);
        valueLut.add(99);
        valueLut.add(-8);
        valueLut.add(8);
        valueLut.add(6);
        valueLut.add(-24);
        valueLut.add(-4);
        valueLut.add(-3);
        valueLut.add(7);
        valueLut.add(4);
        valueLut.add(0);
    }

    @Override
    public int rateBoard(GameBoard board) {
        int sum = 0;
        for (int i = 1; i <= 8; ++i) {
            for (int j = 1; j <= 8; ++j) {
                int occupant = 0;
                try {
                    occupant = board.getOccupation(new Coordinates(i, j));
                } catch (OutOfBoundsException e) {
                    // should never happen
                    e.printStackTrace();
                }
                if (occupant != 0) {
                    sum += valueLut.get(indexFrom(i, j)) * occupant == player ? 1 : -1;
                }
            }
        }
        return sum;
    }

    private int indexFrom(int i, int j) {
        if (i > 4) {
            i = 9 - i;
        }
        if (j > 4) {
            j = 9 - j;
        }
        if (i < j) {
            int temp = i;
            i = j;
            j = temp;
        }

        if (j == 1) {
            return i - 1;
        } else if (j == 2) {
            return i + 2;
        } else if (j == 3) {
            return i + 4;
        } else {
            assert j == 4;
            return i + 5;
        }
    }

    @Override
    public void setPlayer(int player) {
        this.player = player;
    }
}
