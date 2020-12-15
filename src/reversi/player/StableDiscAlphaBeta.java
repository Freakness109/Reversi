package reversi.player;

import reversi.Coordinates;
import reversi.GameBoard;
import reversi.ReversiPlayer;
import reversi.deciders.AlphaBetaDecider;
import reversi.raters.StableDiscRater;
import reversi.utils.IDecideMove;
import reversi.utils.IRateBoard;

public class StableDiscAlphaBeta implements ReversiPlayer {
    private IDecideMove decideMove;

    public StableDiscAlphaBeta(){}
    @Override
    public void initialize(int i, long l) {
        decideMove = new AlphaBetaDecider();
        IRateBoard rater = new StableDiscRater();
        rater.setPlayer(i);
        decideMove.setRater(rater);
        decideMove.setTimeout(l);
        decideMove.setPlayer(i);
    }

    @Override
    public Coordinates nextMove(GameBoard gameBoard) {
        return decideMove.nextMove(gameBoard);
    }
}
