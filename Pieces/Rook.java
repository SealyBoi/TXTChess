package Pieces;

import Main.Board;

public class Rook extends Pieces{

    private String piece;
    private Boolean isWhite;
    private Boolean hasMoved = false;

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

    public boolean hasMoved() {
        return hasMoved;
    }

    private void pieceMoved() {
        hasMoved = true;
    }

    public void checkMovement() {
        if (!hasMoved()) {
            pieceMoved();
        }
    }

    @Override
    public boolean canMove(Board board, int prevCol, int prevRow, int col, int row) {
        if (prevRow == row) {
            for (int i = prevCol + 1; i < col; i++) {
                if (!board.squareIsEmpty(i, row)) {
                    return false;
                }
            }
            for (int i = prevCol - 1; i > col; i--) {
                if (!board.squareIsEmpty(i, row)) {
                    return false;
                }
            }
            if (board.squareIsEmpty(col, row)) {
                checkMovement();
                return true;
            } else if (board.squareContainsAlly(prevCol, prevRow, col, row)) {
                return false;
            }
            checkMovement();
            return true;
        } else if (prevCol == col) {
            for (int i = prevRow + 1; i < row; i++) {
                if (!board.squareIsEmpty(col, i)) {
                    return false;
                }
            }
            for (int i = prevRow - 1; i > row; i--) {
                if (!board.squareIsEmpty(col, i)) {
                    return false;
                }
            }
            if (board.squareIsEmpty(col, row)) {
                checkMovement();
                return true;
            } else if (board.squareContainsAlly(prevCol, prevRow, col, row)) {
                return false;
            }
            checkMovement();
            return true;
        }
        return false;
    }
    
}
