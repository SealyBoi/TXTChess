package Pieces;

import Main.Board;

public class Queen extends Pieces{

    private String piece;
    private Boolean isWhite;

    public Queen (String piece, boolean isWhite) {
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
        return false;
    }
    
}
