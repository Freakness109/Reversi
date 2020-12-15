package reversi.deciders;

import reversi.Coordinates;
import reversi.GameBoard;
import reversi.utils.IDecideMove;
import reversi.utils.IRateBoard;
import reversi.utils.TimeOutException;
import reversi.utils.Utils;

import java.util.List;

/**
 * Decides based on the MiniMax algorithm.
 * Descends the game tree with increasing
 * depth and stops once time runs out.
 */
public class MiniMaxDecider implements IDecideMove {

    private long timeout;
    private long deadline;
    private int player;
    private int otherPlayer;
    private IRateBoard rater;
    private int maxDepth = Integer.MAX_VALUE;

    public void setMaxDepth(int depth) {
        maxDepth = depth;
    }

    int depth;
    @Override
    public Coordinates nextMove(GameBoard board) {
        // -20 for safety margin
        deadline = timeout + System.currentTimeMillis() - 20;
        Coordinates ret = null;
        int max = 0;
        int lastmax = 0;
        try {
            List<Coordinates> moves = Utils.getPossibleMoves(board, player);
            if (moves.isEmpty())
                return null;

            // search for as long as possible
            for (depth = 1; depth <= maxDepth; ++depth) {
                max = Integer.MIN_VALUE;
                Coordinates bestMove = null;
                for (Coordinates move : moves) {
                    GameBoard candidate = board.clone();
                    candidate.makeMove(player, move);
                    int searchResult = miniMaxSearch(candidate, this.otherPlayer, depth - 1);
                    if (searchResult > max) {
                        max = searchResult;
                        bestMove = move;
                    }
                }
                GameBoard b = board.clone();
                b.makeMove(player, moves.get(0));
                ret = bestMove;
                lastmax = max;
                int totalStones = board.countStones(1) + board.countStones(2);
                if (totalStones + depth == 64) {
                    break;
                }
            }
        } catch (TimeOutException ignored) {
            GameBoard nextMove = board.clone();
            nextMove.makeMove(this.player, ret);
            System.out.printf("Depth of search : %d, Rating: %d%n", depth, lastmax);
        }

        return ret;
    }

    private int miniMaxSearch(GameBoard board, int player, int depth) throws TimeOutException {
        if (deadline <= System.currentTimeMillis()) {
            throw new TimeOutException();
        } else if (depth == 0 || board.isFull()) {
            return rater.rateBoard(board);
        } else if (player == this.player) {
            // maximize
            List<Coordinates> moves = Utils.getPossibleMoves(board, player);
            if (moves.isEmpty()) {
                return miniMaxSearch(board, this.otherPlayer, depth -1);
            } else {
                int max = Integer.MIN_VALUE;
                for (Coordinates move : moves) {
                    GameBoard candidateBoard = board.clone();
                    candidateBoard.makeMove(player, move);
                    max = Integer.max(max, miniMaxSearch(candidateBoard, this.otherPlayer, depth - 1));
                }
                return max;
            }
        } else {
            // minimize
            List<Coordinates> moves = Utils.getPossibleMoves(board, player);
            if (moves.isEmpty()) {
                return miniMaxSearch(board, this.player, depth - 1);
            } else {
                int min = Integer.MAX_VALUE;
                for (Coordinates move : moves) {
                    GameBoard candidateBoard = board.clone();
                    candidateBoard.makeMove(player, move);
                    min = Integer.min(min, miniMaxSearch(candidateBoard, this.player, depth - 1));
                }
                return min;
            }
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
}
