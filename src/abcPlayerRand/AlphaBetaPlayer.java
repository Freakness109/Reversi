package abcPlayerRand;

import abcPlayerRand.deciders.AlphaBetaDecider;
import abcPlayerRand.raters.ComplexRater;
import abcPlayerRand.raters.PositionalRater;
import abcPlayerRand.utils.IDecideMove;
import reversi.BitBoard;
import reversi.Coordinates;
import reversi.GameBoard;
import reversi.ReversiPlayer;
import abcPlayerRand.utils.IRateBoard;

/**
 * Player skeleton for various combinations of rating schemes
 * and deciding algorithms
 */
public class AlphaBetaPlayer implements ReversiPlayer {
    private IDecideMove decider;

    public AlphaBetaPlayer() {}

    @Override
    public void initialize(int player, long timeout) {
        IRateBoard rater = new ComplexRater();
        rater.setPlayer(player);

        IRateBoard secondaryRater = new PositionalRater();
        secondaryRater.setPlayer(player);

        decider = new AlphaBetaDecider();
        decider.setPlayer(player);
        decider.setTimeout(timeout);
        decider.setRater(rater);
        decider.setSecondaryRater(secondaryRater);
    }

    @Override
    public Coordinates nextMove(GameBoard gameBoard) {
        return decider.nextMove(new BitBoard(gameBoard));
    }
    public static void main(String[] args) {}
}
