package CheckmateValidation;

import Pieces.Pieces;

public class FreeSquare extends Thread {
    
    Pieces[][] board;
    boolean isWhite;
    int col;
    int row;

    public FreeSquare(Pieces[][] board, boolean isWhite, int col, int row) {
        this.board = board;
        this.isWhite = isWhite;
        this.col = col;
        this.row = row;
    }

    boolean safe = true;

    public boolean isSafe() {
        return safe;
    }

    public void run() {
        // TODO Check all squares around the king to see if he can escape
    }

}
