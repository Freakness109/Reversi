package abcPlayerRand.player;

import abcPlayerRand.utils.IDecideMove;
import reversi.Coordinates;
import reversi.GameBoard;
import reversi.ReversiPlayer;
import abcPlayerRand.deciders.MiniMaxDecider;
import abcPlayerRand.raters.PieceDifferenceRater;
import abcPlayerRand.utils.IRateBoard;

public class MiniMaxPlayer implements ReversiPlayer {
    IDecideMove decider;

    public MiniMaxPlayer(){}
    @Override
    public void initialize(int i, long l) {
        decider = new MiniMaxDecider();
        decider.setPlayer(i);
        decider.setTimeout(l);
        IRateBoard rater = new PieceDifferenceRater();
        rater.setPlayer(i);
        decider.setRater(rater);
    }

    @Override
    public Coordinates nextMove(GameBoard gameBoard) {
        return decider.nextMove(gameBoard);
    }
}
