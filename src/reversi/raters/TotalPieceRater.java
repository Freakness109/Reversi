package reversi.raters;

import reversi.utils.IRateBoard;
import reversi.GameBoard;

public class TotalPieceRater implements IRateBoard {
    private int player;

    @Override
    public int rateBoard(GameBoard board) {
        return board.countStones(player) * 1000;
    }

    @Override
    public void setPlayer(int player) {
        this.player = player;
    }
}
