package Pieces;

import Main.Board;

public abstract class Pieces {
    // Get piece name
    public abstract String getPiece();

    // Get piece color
    public abstract boolean isWhite();

    // Check if requested movement is following piece's movement rules
    public abstract boolean canMove(Board board, int prevCol, int prevRow, int col, int row);
}
