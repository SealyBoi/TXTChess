package Pieces;

import Main.Board;

public class Knight extends Pieces{

    private String piece;
    private Boolean isWhite;

    public Knight (String piece, boolean isWhite) {
        this.piece = piece;
        this.isWhite = isWhite;
    }

    @Override
    public String getPiece() {
        return piece;
    }

    @Override
    public boolean isWhite() {
        return isWhite;
    }

    @Override
    public boolean canMove(Board board, int prevCol, int prevRow, int col, int row) {
        if (Math.abs(col - prevCol) == 2 && Math.abs(row - prevRow) == 1 || Math.abs(col - prevCol) == 1 && Math.abs(row - prevRow) == 2) {
            if (board.squareIsEmpty(col, row)) {
                return true;
            }
            if (!board.squareContainsAlly(prevCol, prevRow, col, row)) {
                return true;
            }
        }
        return false;
    }
    
}
