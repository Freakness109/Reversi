package abcPlayerRand.raters;

import reversi.GameBoard;
import abcPlayerRand.utils.IRateBoard;
import abcPlayerRand.utils.Utils;

public class MobilityRater implements IRateBoard {
    private int player;
    private int otherPlayer;
    public MobilityRater(){}
    @Override
    public int rateBoard(GameBoard board) {
        return 1000*(board.mobility(player) - board.mobility(otherPlayer));
    }

    @Override
    public void setPlayer(int player) {
        this.player = player;
        this.otherPlayer = Utils.otherPlayer(player);
    }
}
