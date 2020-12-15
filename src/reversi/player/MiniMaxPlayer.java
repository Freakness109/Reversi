package reversi.player;

import reversi.Coordinates;
import reversi.GameBoard;
import reversi.ReversiPlayer;
import reversi.deciders.MiniMaxDecider;
import reversi.raters.ComplexRater;
import reversi.utils.IDecideMove;
import reversi.utils.IRateBoard;

public class MiniMaxPlayer implements ReversiPlayer {
    IDecideMove decider;

    public MiniMaxPlayer(){}
    @Override
    public void initialize(int i, long l) {
        decider = new MiniMaxDecider();
        decider.setPlayer(i);
        decider.setTimeout(l);
        IRateBoard rater = new ComplexRater();
        rater.setPlayer(i);
        decider.setRater(rater);
    }

    @Override
    public Coordinates nextMove(GameBoard gameBoard) {
        return decider.nextMove(gameBoard);
    }
}
