package abcPlayerRand.player;

import abcPlayerRand.deciders.AlphaBetaDecider;
import abcPlayerRand.raters.StableDiscRater;
import abcPlayerRand.utils.IDecideMove;
import reversi.Coordinates;
import reversi.GameBoard;
import reversi.ReversiPlayer;
import abcPlayerRand.utils.IRateBoard;

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
