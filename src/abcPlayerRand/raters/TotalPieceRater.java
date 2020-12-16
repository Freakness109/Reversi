package abcPlayerRand.raters;

import abcPlayerRand.utils.IRateBoard;
import reversi.GameBoard;

public class TotalPieceRater implements IRateBoard {
    private int player;

    public TotalPieceRater(){}
    @Override
    public int rateBoard(GameBoard board) {
        return board.countStones(player) * 1000;
    }

    @Override
    public void setPlayer(int player) {
        this.player = player;
    }
}
