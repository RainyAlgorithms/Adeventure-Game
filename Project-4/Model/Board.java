package Model;

import java.util.ArrayList;

public class Board {
    public ArrayList<Square> squares;

    public Board()
    {
        this.squares = new ArrayList<>();
    }

    public Square showMove(Square currSquare, int numMoves)
    {
        int currSquareNum = currSquare.get_room_No();
        int newSquareNum = currSquareNum + numMoves;
        for (Square square : this.squares)
        {
            if (square.get_room_No() == newSquareNum)
            {
                return square;
            }
        }
        return null;
    }
}