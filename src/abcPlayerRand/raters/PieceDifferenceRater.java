package abcPlayerRand.raters;

import abcPlayerRand.utils.IRateBoard;
import reversi.GameBoard;

public class PieceDifferenceRater implements IRateBoard {
    private int player;
    @Override
    public int rateBoard(GameBoard board) {
        int greenPieces = board.countStones(GameBoard.GREEN);
        int redPieces = board.countStones(GameBoard.RED);

        if (player == GameBoard.GREEN)
            return 1000*(greenPieces - redPieces);
        else
            return 1000*(redPieces - greenPieces);
    }

    @Override
    public void setPlayer(int player) {
        this.player = player;
    }
}
