package Pieces;

import Main.Board;

public class Rook extends Pieces{

    private String piece;
    private Boolean isWhite;

    public Rook (String piece, boolean isWhite) {
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
        if (prevRow == row) {
            for (int i = prevCol + 1; i < col; i++) {
                if (!board.squareIsEmpty(i, row)) {
                    return false;
                }
            }
            if (board.squareIsEmpty(col, row)) {
                return true;
            } else if (board.squareContainsAlly(prevCol, prevRow, col, row)) {
                return false;
            }
            return true;
        } else if (prevCol == col) {
            for (int i = prevRow + 1; i < row; i++) {
                if (!board.squareIsEmpty(col, i)) {
                    return false;
                }
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
