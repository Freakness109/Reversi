package reversi.raters;

import reversi.Coordinates;
import reversi.GameBoard;
import reversi.OutOfBoundsException;
import reversi.utils.IRateBoard;
import reversi.utils.Utils;

import java.util.*;

public class StableDiscRater implements IRateBoard {
    private int player;
    @Override
    public int rateBoard(GameBoard board) {
        ArrayList<Coordinates> seenCoords = new ArrayList<>();
        Deque<Coordinates> toDoList = new ArrayDeque<>();
        int sum = 0;
        toDoList.add(new Coordinates(1,1));
        toDoList.add(new Coordinates(8,1));
        toDoList.add(new Coordinates(1,8));
        toDoList.add(new Coordinates(8,8));
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
        return 1000*sum;
    }

    @Override
    public void setPlayer(int player) {
        this.player = player;
    }
}
