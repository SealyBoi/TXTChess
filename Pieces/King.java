package Pieces;

import Main.Board;

public class King extends Pieces{

    private String piece;
    private Boolean isWhite;
    private int[] position;
    private Boolean hasMoved = false;

    public King (String piece, boolean isWhite, int col, int row) {
        this.piece = piece;
        this.isWhite = isWhite;
        this.position = new int[]{col, row};
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

    public void updatePosition(int col, int row) {
        position = new int[]{col, row};
    }

    public void checkMovement() {
        if (!hasMoved()) {
            pieceMoved();
        }
    }

    public int[] getPosition() {
        return position;
    }

    @Override
    public boolean canMove(Board board, int prevCol, int prevRow, int col, int row) {
        if (Math.abs(col - prevCol) <= 1 && Math.abs(row - prevRow) <= 1) {
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
            } else if (Math.abs(col - prevCol) == Math.abs(row - prevRow)) {
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
                    checkMovement();
                    return true;
                } else if (board.squareContainsAlly(prevCol, prevRow, col, row)) {
                    return false;
                }
                checkMovement();
                return true;
            }
        }
        // Check if king is attempting to castle
        else if (Math.abs(col - prevCol) == 2 && row - prevRow == 0) {
            int dir = (col - prevCol) / Math.abs(col - prevCol);
            return canCastle(board, dir, prevCol, prevRow, col, row);
        }
        return false;
    }

    public boolean canCastle(Board board, int dir, int prevCol, int prevRow, int col, int row) {
        // Find which rook we need to check
        Pieces p;
        if (dir == -1) {
            p = board.getPiece(0, prevRow);
        } else {
            p = board.getPiece(7, prevRow);
        }

        if (p == null) {
            return false;
        }

        // Check if either piece has moved
        Rook r = (Rook) p;
        if (r.hasMoved() || hasMoved()) {
            return false;
        }

        // Check that every space in between the king and rook is empty
        for (int i = prevCol + dir; i >= 1 && i <= 6; i += dir) {
            if (!board.squareIsEmpty(i, prevRow)) {
                return false;
            }
        }

        // Check that there are no pieces attacking the spaces in between the king and where he wants to move
        for (int i = prevCol + dir; i != col; i += dir) {
            if (attackingPieces(board, i, prevRow)) {
                return false;
            }
        }

        if (dir == -1) {
            board.movePiece(0, prevRow, col - dir, row);
        } else {
            board.movePiece(7, prevRow, col - dir, row);
        }

        return true;
    }

    private boolean attackingPieces(Board board, int col, int row) {
        // Check Lane1 for attacking pieces
        if (checkLane(board, col, row, -1, 1)) {
            return true;
        }

        // Check Lane2 for attacking pieces
        if (checkLane(board, col, row, 0, 1)) {
            return true;
        }

        // Check Lane3 for attacking pieces
        if (checkLane(board, col, row, 1, 1)) {
            return true;
        }

        // Check Lane4 for attacking pieces
        if (checkLane(board, col, row, -1, 0)) {
            return true;
        }

        // Check Lane5 for attacking piecces
        if (checkLane(board, col, row, 1, 0)) {
            return true;
        }

        // Check Lane6 for attacking pieces
        if (checkLane(board, col, row, -1, -1)) {
            return true;
        }

        // Check Lane7 for attacking pieces
        if (checkLane(board, col, row, 0, -1)) {
            return true;
        }

        // Check Lane8 for attacking pieces
        if (checkLane(board, col, row, 1, -1)) {
           return true;
        }

        // Check Knight1 for attacking piece
        if (col - 1 >= 0 && row + 2 <= 7) {
            if (checkKnight(board, col - 1, row + 2)) {
                return true;
            }
        }

        // Check Knight2 for attacking piece
        if (col + 1 >= 0 && row + 2 <= 7) {
            if (checkKnight(board, col + 1, row + 2)) {
                return true;
            }
        }

        // Check Knight3 for attacking piece
        if (col - 2 >= 0 && row + 1 <= 7) {
            if (checkKnight(board, col - 2, row + 1)) {
                return true;
            }
        }

        // Check Knight4 for attacking piece
        if (col + 2 <= 7 && row + 1 <= 7) {
            if (checkKnight(board, col + 2, row + 1)) {
                return true;
            }
        }

        // Check Knight5 for attacking piece
        if (col - 2 >= 0 && row - 1 >= 0) {
            if (checkKnight(board, col - 2, row - 1)) {
                return true;
            }
        }

        // Check Knight6 for attacking piece
        if (col + 2 <= 7 && row - 1 >= 0) {
            if (checkKnight(board, col + 2, row - 1)) {
                return true;
            }
        }

        // Check Knight7 for attacking piece
        if (col - 1 >= 0 && row - 2 >= 0) {
            if (checkKnight(board, col - 1, row - 2)) {
                return true;
            }
        }

        // Check Knight8 for attacking piece
        if (col + 1 <= 7 && row - 2 >= 0) {
            if (checkKnight(board, col + 1, row - 2)) {
                return true;
            }
        }

        return false;
    }

    private boolean checkLane(Board board, int col, int row, int colInc, int rowInc) {
        int checkCol = col + colInc;
        int checkRow = row + rowInc;
        while (checkCol >= 0 && checkCol <= 7 && checkRow >= 0 && checkRow <= 7) {
            Pieces p = board.getPiece(checkCol, checkRow);
            if (p == null) {
                return false;
            }

            if (p.isWhite() != isWhite()) {
                String pType = p.getPiece().toLowerCase();
                    if (colInc == 0 || rowInc == 0) {
                        if (pType.equals("q") || pType.equals("r")) {
                            return true;
                        }
                    } else if (Math.abs(checkCol - col) == Math.abs(checkRow - row)) {
                        if (checkCol == col - 1 || checkCol == col + 1) {
                            if (!isWhite && checkRow == row - 1) {
                                if (pType.equals("p")) {
                                    return true;
                                }
                            } else if (isWhite && checkRow == row + 1) {
                                if (pType.equals("p")) {
                                    return true;
                                }
                            }
                        }
                        if (pType.equals("q") || pType.equals("b")) {
                            return true;
                        }
                    } else if (pType.equals("k") && checkCol == col - 1 || checkCol == col + 1 || checkCol == col && checkRow == row - 1 || checkRow == row + 1 || checkRow == row) {
                        return true;
                    }
            }
            checkCol += colInc;
            checkRow += rowInc;
        }
        return false;
    }

    private boolean checkKnight(Board board, int col, int row) {
        Pieces p;

        p = board.getPiece(col, row);
        if (p != null) {
            if (p.isWhite() != isWhite() && p.getPiece().toLowerCase().equals("n")) {
                return true;
            }
        }
        return false;
    }
    
}
