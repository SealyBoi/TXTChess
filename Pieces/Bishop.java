package Pieces;

import Main.Board;

public class Bishop extends Pieces{

    private String piece;
    private Boolean isWhite;

    public Bishop (String piece, boolean isWhite) {
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
        if (Math.abs(col - prevCol) == Math.abs(row - prevRow)) {
            int colIncrement;
            int rowIncrement;
            if (col > prevCol) {
                colIncrement = 1;
            } else {
                colIncrement = -1;
            }
            if (row > prevRow) {
                rowIncrement = 1;
            } else {
                rowIncrement = -1;
            }
            int j = prevRow + rowIncrement;
            for (int i = prevCol + colIncrement; i != col; i += colIncrement) {
                if (!board.squareIsEmpty(i, j)) {
                    return false;
                }
                j += rowIncrement;
            }
            if (board.squareIsEmpty(col, row)) {
                return true;
            } else if (board.squareContainsAlly(prevCol, prevRow, col, row)) {
                return false;
            }
            return true;
        }
        return false;
    }
    
}
