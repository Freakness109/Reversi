package reversi.deciders;

import reversi.utils.IDecideMove;
import reversi.utils.IRateBoard;
import reversi.utils.TimeOutException;
import reversi.utils.Utils;
import reversi.Coordinates;
import reversi.GameBoard;

import java.util.List;
import java.util.PriorityQueue;

/**
 * Decides based on the Alpha-Beta algorithm.
 * Descends the game tree with increasing
 * depth and stops once time runs out.
 * Has some strange bug that is sometimes
 * triggered if there is only one valid move.
 */
public class AlphaBetaDecider implements IDecideMove {

    private long timeout;
    private long deadline;
    private int player;
    private int otherPlayer;
    private IRateBoard rater;
    private IRateBoard secondaryRater;

    int depth;

    @Override
    public Coordinates nextMove(GameBoard board) {
        if (secondaryRater == null)
            secondaryRater = rater;
        // -10 for safety margin
        deadline = timeout + System.currentTimeMillis() - 10;
        Coordinates ret = null;
        // keep a previous max to speed up search
        int max = Integer.MIN_VALUE;
        try {
            List<Coordinates> moves = Utils.getPossibleMoves(board, player);
            if (moves.isEmpty())
                return null;

            // search for as long as possible
            for (depth = 1; depth < 50; ++depth) {
                Coordinates bestMove = null;
                for (int i = 0; i < moves.size() - 1; i++) {
                    Coordinates move = moves.get(i);
                    GameBoard candidate = board.clone();
                    candidate.makeMove(player, move);
                    int searchResult = min(candidate, depth - 1, max, Integer.MAX_VALUE);
                    if (searchResult > max) {
                        max = searchResult;
                        bestMove = move;
                    }
                }
                // last move optimisation
                Coordinates move = moves.get(moves.size() - 1);
                GameBoard candidate = board.clone();
                candidate.makeMove(player, move);
                int searchResult = min(candidate, depth - 1, max, max + 3);
                if (searchResult > max) {
                    max = searchResult;
                    bestMove = move;
                }

                if (bestMove == null) {
                    // try again with more pessimistic assumption
                    max -= 50;
                    depth--;
                    continue;
                }


                ret = bestMove;
                int totalStones = board.countStones(1) + board.countStones(2);
                if (totalStones + depth == 64) {
                    break;
                }
            }
        } catch (TimeOutException ignored) {
            GameBoard nextMove = board.clone();
            nextMove.makeMove(this.player, ret);
            System.out.printf("Depth of search : %d, Rating: %d%n", depth, max);
        }
        return ret;
    }

    /**
     * We are making a move and trying to maximize the current board
     *
     * @param board      board to maximize
     * @param depth      depth to search
     * @param lowerBound worst value we can expect
     * @param upperBound best value we can expect
     * @return a new worst-case lower bound
     * @throws TimeOutException if we hit the deadline
     */
    private int max(GameBoard board, int depth, int lowerBound, int upperBound) throws TimeOutException {
        if (System.currentTimeMillis() >= deadline) {
            throw new TimeOutException();
        }
        if (depth == 0) {
            return rater.rateBoard(board);
        } else {

            // PriorityQueue is a MinHeap --> promising moves first
            // --> must return the opposite of what is the actual
            // comparison for better moves to be on top
            PriorityQueue<Coordinates> moves = new PriorityQueue<>((a, b) -> {
                GameBoard gb1 = board.clone();
                gb1.makeMove(this.player, a);
                GameBoard gb2 = board.clone();
                gb2.makeMove(this.player, b);
                // inverse because we have a MinHeap
                return secondaryRater.rateBoard(gb2) - secondaryRater.rateBoard(gb1);
            });
            moves.addAll(Utils.getPossibleMoves(board, this.player));

            int newLower = lowerBound;
            for (Coordinates move : moves) {
                GameBoard gb = board.clone();
                gb.makeMove(this.player, move);
                // see if we can move the lower bound up
                newLower = Integer.max(newLower,
                        min(gb, depth - 1, newLower, upperBound));

                // if we have a better lower bound than the upper bound, the above min
                // step will not take what we can provide --> cut branch here
                if (newLower >= upperBound)
                    // cut
                    return newLower;
            }
            return newLower;
        }
    }


    /**
     * The other player is making a move and trying to minimize the current board
     *
     * @param board      board to minimize
     * @param depth      depth to search
     * @param lowerBound worst value we can expect
     * @param upperBound best value we can expect
     * @return a new worst-case upper bound
     * @throws TimeOutException if we hit the deadline
     */
    private int min(GameBoard board, int depth, int lowerBound, int upperBound) throws TimeOutException {
        if (System.currentTimeMillis() >= deadline) {
            throw new TimeOutException();
        }

        if (depth == 0) {
            return rater.rateBoard(board);
        } else {
            // PriorityQueue is a MinHeap --> promising moves first
            // promising moves for other player are actually moves
            // with a lower value --> normal comparison
            PriorityQueue<Coordinates> moves = new PriorityQueue<>((a, b) -> {
                GameBoard gb1 = board.clone();
                gb1.makeMove(this.otherPlayer, a);
                GameBoard gb2 = board.clone();
                gb2.makeMove(this.otherPlayer, b);
                // normal because we want small on top
                return secondaryRater.rateBoard(gb1) - secondaryRater.rateBoard(gb2);
            });
            moves.addAll(Utils.getPossibleMoves(board, this.otherPlayer));

            int newUpper = upperBound;
            for (Coordinates move : moves) {
                GameBoard gb = board.clone();
                gb.makeMove(this.otherPlayer, move);
                // see if we can move the upper bound down
                newUpper = Integer.min(newUpper,
                        max(gb, depth - 1, lowerBound, newUpper));

                // if we have a better upper bound than the lower bound, the above max
                // step will not take what we can provide --> cut branch here
                if (newUpper < lowerBound)
                    // cut
                    return newUpper;
            }
            return newUpper;
        }
    }

    @Override
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public void setPlayer(int player) {
        this.player = player;
        this.otherPlayer = player == GameBoard.GREEN ? GameBoard.RED : GameBoard.GREEN;
    }

    @Override
    public void setRater(IRateBoard rater) {
        this.rater = rater;
    }

    @Override
    public void setSecondaryRater(IRateBoard secondaryRater) {
        this.secondaryRater = secondaryRater;
    }

}
