package CheckmateValidation;

import Pieces.Pieces;
import Util.CopyBoard;

public class FreeSquare extends Thread {// Declaring ANSI_RESET so we can reset color
    // Declaring ANSI_RESET so we can reset color
    public static final String ANSI_RESET = "\u001B[0m";

    // Declaring green background for black pieces
    public static final String ANSI_BLUE = "\u001B[34m";

    // Declaring white background for white pieces
    public static final String ANSI_RED = "\u001B[31m";
    
    private Pieces[][] board;
    private boolean isWhite;
    private int col;
    private int row;

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
        // Check all squares around the king to see if he can escape
        safe = calcResult();
    }

    public boolean calcResult() {
        // Check if the king can move to a position
        return (
            canMoveTo(col - 1, row + 1) ||
            canMoveTo(col, row + 1) ||
            canMoveTo(col + 1, row + 1) ||
            canMoveTo(col - 1, row) ||
            canMoveTo(col + 1, row) ||
            canMoveTo(col - 1, row - 1) ||
            canMoveTo(col, row - 1) ||
            canMoveTo(col + 1, row - 1)
        );
    }

    // Check if king is able to move to given position
    private boolean canMoveTo(int col, int row) {
        if (col >= 0 && col <= 7 && row >= 0 && row <= 7) {
            Pieces p = board[7 - row][col];
            boolean canMove = false;
            if (p == null || p.isWhite() != isWhite) {
                canMove = checkBoard(col, row);
            }
            return canMove;
        }
        return false;
    }

    // Check if moving to given position would cause another check
    private boolean checkBoard(int col, int row) {
        Pieces[][] stateBoard = CopyBoard.copyBoard(board);
        movePiece(this.col, this.row, col, row);
        Check chThread = new Check(board, isWhite, col, row);
        boolean result = false;
        chThread.start();
        try {
            chThread.join();
            result = !chThread.kingInCheck();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        movePiece(col, row, this.col, this.row);
        board = CopyBoard.copyBoard(stateBoard);
        return result;
    }

    // Move piece to position
    private void movePiece(int prevCol, int prevRow, int col, int row) {
        board[7 - row][col] = board[7 - prevRow][prevCol];
        board[7 - prevRow][prevCol] = null;
    }

}
