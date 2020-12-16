package abcPlayerRand.raters;

import abcPlayerRand.utils.Utils;
import reversi.Coordinates;
import reversi.GameBoard;
import reversi.OutOfBoundsException;
import abcPlayerRand.utils.IRateBoard;

import java.util.*;

public class StableDiscRater implements IRateBoard {
    private int player;
    private static final Coordinates[] cornersAndNextTo;

    static {
        cornersAndNextTo = new Coordinates[12];
        cornersAndNextTo[0] = new Coordinates(1, 1);
        cornersAndNextTo[1] = new Coordinates(8, 1);
        cornersAndNextTo[2] = new Coordinates(1, 8);
        cornersAndNextTo[3] = new Coordinates(8, 8);
        cornersAndNextTo[4] = new Coordinates(2, 1);
        cornersAndNextTo[5] = new Coordinates(1, 2);
        cornersAndNextTo[6] = new Coordinates(8, 2);
        cornersAndNextTo[7] = new Coordinates(7, 1);
        cornersAndNextTo[8] = new Coordinates(2, 8);
        cornersAndNextTo[9] = new Coordinates(1, 7);
        cornersAndNextTo[10] = new Coordinates(7, 8);
        cornersAndNextTo[11] = new Coordinates(8, 7);
    }

    public StableDiscRater(){}
    @Override
    public int rateBoard(GameBoard board) {
        boolean[][] stables = new boolean[8][8];
        for (Coordinates coords : cornersAndNextTo) {
            int occ = 0;
            try {
                occ = board.getOccupation(coords);
            } catch (OutOfBoundsException e) {
                e.printStackTrace();
            }
            if (occ == 0)
                return 0;
            else if (occ == this.player)
                stables[coords.getRow() - 1][coords.getCol() - 1] = true;
        }
        return getStableDiscs(board);
    }

    private int getStableDiscs(GameBoard board) {
        ArrayList<Coordinates> seenCoords = new ArrayList<>();
        Deque<Coordinates> toDoList = new ArrayDeque<>();
        int sum = 0;
        toDoList.add(new Coordinates(1, 1));
        toDoList.add(new Coordinates(8, 1));
        toDoList.add(new Coordinates(1, 8));
        toDoList.add(new Coordinates(8, 8));
        while (!toDoList.isEmpty()) {
            Coordinates coords = toDoList.pop();
            seenCoords.add(coords);
            try {
                if (board.getOccupation(coords) == player) {
                    sum += 1;
                    List<Coordinates> surrounding = Utils.getSurrounding(coords);
                    for (Coordinates c : surrounding) {
                        if (!seenCoords.contains(c)) {
                            toDoList.add(c);
                        }
                    }
                }
            } catch (OutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        return 1000 * sum;
    }

    @Override
    public void setPlayer(int player) {
        this.player = player;
    }
}
