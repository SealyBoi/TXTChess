package Pieces;

import Main.Board;

public class Pawn extends Pieces{

    private String piece;
    private Boolean isWhite;

    public Pawn (String piece, boolean isWhite) {
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
        int maxMove;
        // Check if piece is moving for the first time or not
        if (board.getPiece(prevCol, prevRow).isWhite() && prevRow == 1) {
            maxMove = 2;
        } else if (!board.getPiece(prevCol, prevRow).isWhite() && prevRow == 6) {
            maxMove = 2;
        } else {
            maxMove = 1;
        }
        // Check whether the piece is white or black
        int rowCalc;
        int rowCheckCalc;
        if (board.getPiece(prevCol, prevRow).isWhite()) {
            rowCalc = row - prevRow;
            rowCheckCalc = row + 1;
        } else {
            rowCalc = prevRow - row;
            rowCheckCalc = row - 1;
        }
        // Check if piece follows movement rules
        if (rowCalc <= maxMove && rowCalc > 0 && prevCol == col) {
            // Check if piece is not jumping over another piece when moving for first time
            if (maxMove == 2 && rowCalc == maxMove) {
                if (!board.squareIsEmpty(col, rowCheckCalc)) {
                    return false;
                }
            }
            if (board.squareIsEmpty(col, row)) {
                return true;
            } else if (board.squareContainsAlly(prevCol, prevRow, col, row)) {
                return false;
            }
        } else if (rowCalc == 1 && col - prevCol == 1 || col - prevCol == -1) {
            if (board.squareContainsEnemy(prevCol, prevRow, col, row)) {
                return true;
            }
        } 
        return false;
    }
    
}
