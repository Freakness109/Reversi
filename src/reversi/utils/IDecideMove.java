package reversi.utils;

import reversi.Coordinates;
import reversi.GameBoard;

/**
 * Provides an interface for diverse algorithms to make moves
 */
public interface IDecideMove {
    /**
     * @param board the board from which to find the optimal move
     * @return the coordinates of where to place the next piece
     */
    Coordinates nextMove(GameBoard board);

    /**
     * Sets the upper time limit the object may search for a move
     * @param timeout amount of time in milliseconds
     */
    void setTimeout(long timeout);

    /**
     * Sets the player the object should optimize for
     * @param player either {@link GameBoard.GREEN} or {@link GameBoard.RED}
     */
    void setPlayer(int player);

    /**
     * Set rating function to use in optimisation
     * @param rater function to use
     */
    void setRater(IRateBoard rater);
}
