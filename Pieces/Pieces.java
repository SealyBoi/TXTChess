package Pieces;

import Main.Board;

public abstract class Pieces {
    public abstract String getPiece();

    public abstract boolean isWhite();

    public abstract boolean canMove(Board board, int prevCol, int prevRow, int col, int row);
}
