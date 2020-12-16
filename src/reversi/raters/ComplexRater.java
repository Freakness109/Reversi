package reversi.raters;

import reversi.utils.IRateBoard;
import reversi.utils.Utils;
import reversi.Coordinates;
import reversi.GameBoard;

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
    private final IRateBoard stableDiscs;
    private final IRateBoard pieceDiff;
    private final IRateBoard mobilityDiff;
    private final IRateBoard staticRater;

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

        stableDiscs = new StableDiscRater();

        pieceDiff = new PieceDifferenceRater();

        mobilityDiff = new MobilityRater();

        staticRater = new PositionalRater();
    }

    @Override
    public int rateBoard(GameBoard board) {
        int totalPieces = board.countStones(player) + board.countStones(otherPlayer);
        if (totalPieces > 55) {
            return greedy.rateBoard(board);
        } else if (totalPieces > 40) {
            int stableDisc = stableDiscs.rateBoard(board);
            //int stableDisc = 0;
            int pieceAdvantage = pieceDiff.rateBoard(board);
            int mobilityAdvantage = mobilityDiff.rateBoard(board);
            return (stableDisc + pieceAdvantage + 2 * mobilityAdvantage) / 3;
        } else if (totalPieces > 20) {
            int staticRating = staticRater.rateBoard(board);
            int mobilityAdvantage = mobilityDiff.rateBoard(board);
            return (staticRating + mobilityAdvantage) / 2;
        } else if (totalPieces > 7) {
            int mobilityAdvantage = mobilityDiff.rateBoard(board);
            int staticRating = staticRater.rateBoard(board);
            return (2 * mobilityAdvantage + staticRating) / 3;
        } else {
            int mobilityAdvantage = mobilityDiff.rateBoard(board);
            int staticRating = staticRater.rateBoard(board);
            return (3 * mobilityAdvantage + staticRating) / 4;
        }
    }

    @Override
    public void setPlayer(int player) {
        greedy.setPlayer(player);
        stableDiscs.setPlayer(player);
        pieceDiff.setPlayer(player);
        mobilityDiff.setPlayer(player);
        staticRater.setPlayer(player);
        this.player = player;
        this.otherPlayer = Utils.otherPlayer(player);
    }
}
