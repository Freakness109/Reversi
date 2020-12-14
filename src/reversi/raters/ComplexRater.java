package reversi.raters;

import reversi.utils.IRateBoard;
import reversi.utils.Utils;
import reversi.Coordinates;
import reversi.GameBoard;
import reversi.OutOfBoundsException;

import java.util.ArrayList;
import java.util.List;

/**
 * Rates the board using a variety of stats, such as
 * mobility, piece difference or corner domination.
 */
public class ComplexRater implements IRateBoard {
    private int player;
    private int otherPlayer;
    private final List<Coordinates> corners;
    private final List<Coordinates> nextToCorners;
    private final IRateBoard greedy;

    public ComplexRater() {
        this.corners = new ArrayList<>(4);
        this.nextToCorners = new ArrayList<>(8);

        corners.add(new Coordinates(1, 1));
        nextToCorners.add(new Coordinates(2, 1));
        nextToCorners.add(new Coordinates(1, 2));

        corners.add(new Coordinates(8, 1));
        nextToCorners.add(new Coordinates(7, 1));
        nextToCorners.add(new Coordinates(8, 2));

        corners.add(new Coordinates(1, 8));
        nextToCorners.add(new Coordinates(1, 7));
        nextToCorners.add(new Coordinates(2, 8));

        corners.add(new Coordinates(8, 8));
        nextToCorners.add(new Coordinates(8, 7));
        nextToCorners.add(new Coordinates(7, 8));

        greedy = new TotalPieceRater();
        greedy.setPlayer(player);
    }

    @Override
    public int rateBoard(GameBoard board) {
        int totalPieces = board.countStones(player) + board.countStones(otherPlayer);
        if (totalPieces > 55) {
            return greedy.rateBoard(board);
        } else {
            int pieceAdvantage = board.countStones(player) - board.countStones(otherPlayer);
            int mobilityAdvantage = board.mobility(player) - board.mobility(otherPlayer);
            int cornersCount = countCorners(board);
            int nextToCornersCount = countNextToCorners(board);

            return mobilityAdvantage * ((64 - totalPieces) * (64 - totalPieces) / 64 + (64 - totalPieces) / 4)
                    + pieceAdvantage * totalPieces + 1000 * cornersCount - 800 * nextToCornersCount;
        }
    }

    @Override
    public void setPlayer(int player) {
        this.player = player;
        this.otherPlayer = Utils.otherPlayer(player);
    }

    private int countNextToCorners(GameBoard board) {
        int sum = 0;
        try {
            for (Coordinates nextToCorner : nextToCorners) {
                int occ = board.getOccupation(nextToCorner);
                if (occ == 0) {
                    sum += 0;
                } else if (occ == player) {
                    sum += 1;
                } else {
                    sum += -1;
                }
            }
        } catch (OutOfBoundsException ignored) {
            // ignored
        }
        return sum;
    }

    private int countCorners(GameBoard board) {
        int sum = 0;
        try {
            for (Coordinates corner : corners) {
                int occ = board.getOccupation(corner);
                if (occ == 0) {
                    sum += 0;
                } else if (occ == player) {
                    sum += 1;
                } else {
                    sum += -1;
                }
            }
        } catch (OutOfBoundsException ignored) {
            // ignored
        }
        return sum;
    }
}
