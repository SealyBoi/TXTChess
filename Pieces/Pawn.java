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
        if (board.getPiece(prevCol, prevRow).isWhite()) {
            // Check if piece follows movement rules
            if (row - prevRow <= maxMove && row - prevRow > 0 && prevCol == col) {
                // Check if piece is not jumping over another piece when moving for first time
                if (maxMove == 2 && row - prevRow == maxMove) {
                    if (!board.squareIsEmpty(col, row - 1)) {
                        return false;
                    }
                }
                if (board.squareIsEmpty(col, row)) {
                    return true;
                } else if (board.squareContainsAlly(prevCol, prevRow, col, row)) {
                    return false;
                }
            } else if (row - prevRow == 1 && col - prevCol == 1 || col - prevCol == -1) {
                if (board.squareContainsEnemy(prevCol, prevRow, col, row)) {
                    return true;
                }
            } 
        } else if (!board.getPiece(prevCol, prevRow).isWhite()) {
            if (prevRow - row <= maxMove && prevRow - row > 0 && prevCol == col) {
                if (maxMove == 2 && prevRow - row == maxMove) {
                    if (!board.squareIsEmpty(col, row - 1)) {
                        return false;
                    }
                }
                if (board.squareIsEmpty(col, row)) {
                    return true;
                } else if (board.squareContainsAlly(prevCol, prevRow, col, row)) {
                    return false;
                }
            } else if (prevRow - row == 1 && col - prevCol == 1 || col - prevCol == -1) {
                if (board.squareContainsEnemy(prevCol, prevRow, col, row)) {
                    return true;
                }
            } 
        }
        return false;
    }
    
}
