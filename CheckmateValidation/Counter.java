package CheckmateValidation;

import Pieces.Pieces;

public class Counter extends Thread {

    Pieces[][] board;
    boolean isWhite;
    int col;
    int row;

    public Counter(Pieces[][] board, boolean isWhite, int col, int row) {
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
        // TODO Check if the attacking piece has any other pieces that can counter attack it
    }
    
}
