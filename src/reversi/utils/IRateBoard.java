package reversi.utils;

import reversi.GameBoard;

public interface IRateBoard {
    /**
     * Rates the given board for a player
     * range: -64000 to 64000
     * @param board board to rate
     * @return rating
     */
    int rateBoard(GameBoard board);

    /**
     * Sets the player that the rating should be
     * maximized / positive
     * @param player either {@link GameBoard.GREEN} or {@link GameBoard.RED}
     */
    void setPlayer(int player);
}
