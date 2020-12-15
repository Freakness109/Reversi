package reversi.player;

import reversi.BitBoard;
import reversi.deciders.AlphaBetaDecider;
import reversi.raters.ComplexRater;
import reversi.raters.PositionalRater;
import reversi.utils.IDecideMove;
import reversi.utils.IRateBoard;
import reversi.Coordinates;
import reversi.GameBoard;
import reversi.ReversiPlayer;

/**
 * Player skeleton for various combinations of rating schemes
 * and deciding algorithms
 */
public class AlphaBetaPlayer implements ReversiPlayer {
    private IDecideMove decider;

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
}
