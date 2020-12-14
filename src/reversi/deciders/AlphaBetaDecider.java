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

    int depth;

    @Override
    public Coordinates nextMove(GameBoard board) {
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
                    int searchResult = max(candidate, this.otherPlayer, depth - 1, max, Integer.MAX_VALUE);
                    if (searchResult > max) {
                        max = searchResult;
                        bestMove = move;
                    }
                }
                // last move optimisation
                Coordinates move = moves.get(moves.size() - 1);
                GameBoard candidate = board.clone();
                candidate.makeMove(player, move);
                int searchResult = max(candidate, this.otherPlayer, depth - 1, max, max + 3);
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

    private int max(GameBoard board, int player, int depth, int lowerBound, int upperBound) throws TimeOutException {
        if (System.currentTimeMillis() >= deadline) {
            throw new TimeOutException();
        }

        if (depth == 0) {
            return rater.rateBoard(board);
        } else {
            // maximize --> move lower bound up (if possible)

            // optimisation: search first promising moves
            PriorityQueue<Coordinates> moves = new PriorityQueue<>((a, b) -> {
                GameBoard gb1 = board.clone();
                gb1.makeMove(player, a);
                GameBoard gb2 = board.clone();
                gb2.makeMove(player, b);
                return rater.rateBoard(gb1) - rater.rateBoard(gb2);
            });
            moves.addAll(Utils.getPossibleMoves(board, player));
            int newLower = lowerBound;
            for (Coordinates move : moves) {
                GameBoard gb = board.clone();
                gb.makeMove(player, move);
                newLower = Integer.max(newLower,
                        min(gb, this.otherPlayer, depth - 1, newLower, upperBound));
                if (newLower >= upperBound)
                    // cut
                    return newLower;
            }
            return newLower;
        }
    }


    private int min(GameBoard board, int player, int depth, int lowerBound, int upperBound) throws TimeOutException {
        if (System.currentTimeMillis() >= deadline) {
            throw new TimeOutException();
        }

        if (depth == 0) {
            return rater.rateBoard(board);
        } else {
            // optimisation: search first promising moves
            PriorityQueue<Coordinates> moves = new PriorityQueue<>((a, b) -> {
                GameBoard gb1 = board.clone();
                gb1.makeMove(player, a);
                GameBoard gb2 = board.clone();
                gb2.makeMove(player, b);
                return rater.rateBoard(gb1) - rater.rateBoard(gb2);
            });
            moves.addAll(Utils.getPossibleMoves(board, player));
            int newUpper = upperBound;
            for (Coordinates move : moves) {
                GameBoard gb = board.clone();
                gb.makeMove(player, move);
                newUpper = Integer.min(newUpper,
                        max(gb, this.player, depth - 1, lowerBound, newUpper));
                if (lowerBound >= newUpper)
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
}
