package CheckmateValidation;

import Pieces.Pieces;

public class Interceptor extends Thread {

    Pieces[][] board;
    boolean isWhite;
    int col;
    int row;

    public Interceptor(Pieces[][] board, boolean isWhite, int col, int row) {
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
        // TODO Check squares in between the king and attacker to see if any piece can intercept
    }

}
