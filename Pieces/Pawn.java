package Pieces;

import Main.Board;

public class Pawn extends Pieces{

    private String piece;
    private Boolean isWhite;
    private Boolean ep = false;

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

    public void flipEP() {
        ep = !ep;
    }

    public boolean getEP() {
        return ep;
    }

    @Override
    public boolean canMove(Board board, int prevCol, int prevRow, int col, int row) {
        int maxMove;
        // Check if piece is moving for the first time or not
        if (isWhite() && prevRow == 1) {
            maxMove = 2;
        } else if (!isWhite() && prevRow == 6) {
            maxMove = 2;
        } else {
            maxMove = 1;
        }
        // Check whether the piece is white or black
        int rowCalc;
        if (isWhite()) {
            rowCalc = row - prevRow;
        } else {
            rowCalc = prevRow - row;
        }
        // Check if piece follows movement rules
        if (rowCalc <= maxMove && rowCalc > 0 && prevCol == col) {
            // Check if piece is not jumping over another piece when moving for first time
            if (maxMove == 2 && rowCalc == maxMove) {
                if (isWhite() && !board.squareIsEmpty(col, row - 1) || !isWhite() && !board.squareIsEmpty(col, row + 1)) {
                    return false;
                }
                flipEP();
                board.addEP(prevCol, prevRow);
            }
            if (board.squareIsEmpty(col, row)) {
                return true;
            } else if (board.squareContainsAlly(prevCol, prevRow, col, row)) {
                return false;
            }
        }
        // Check if piece is attempting to capture (including en passant)
        else if (rowCalc == 1 && (col - prevCol == 1 || col - prevCol == -1)) {
            if (board.squareContainsEnemy(prevCol, prevRow, col, row)) {
                return true;
            } else if (board.canEnPassant(col, row - 1) && isWhite() && board.squareContainsEnemy(prevCol, prevRow, col, row - 1)) {
                board.triggerEnPassant(col, row - 1);
                return true;
            } else if (board.canEnPassant(col, row + 1) && !isWhite() && board.squareContainsEnemy(prevCol, prevRow, col, row + 1)) {
                board.triggerEnPassant(col, row + 1);
                return true;
            }
        }
        return false;
    }
    
}
