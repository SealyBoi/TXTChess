package CheckmateValidation;

import Pieces.Pieces;

public class Interceptor extends Thread {

    Pieces[][] board;
    boolean isWhite;
    int col;
    int row;

    public Interceptor(Pieces[][] board, boolean isWhite, int col, int row) {
        this.board = board;
        this.isWhite = isWhite;
        this.col = col;
        this.row = row;
    }

    boolean safe = true;
    int[] attackerPos = new int[2];
    int[] defenderPos = new int[2];

    public boolean isSafe() {
        return safe;
    }

    public void setAttackerPos(int col, int row) {
        attackerPos[0] = col;
        attackerPos[1] = row;
    }

    public void setDefenderPos(int col, int row) {
        defenderPos[0] = col;
        defenderPos[1] = row;
    }
    
    public void run() {
        // TODO Check squares in between the king and attacker to see if any piece can intercept
        safe = true;
    }

    public boolean calcResult() {
        int colInc = 0;
        int rowInc = 0;
        // Check lanes to find attacking piece
        // Check lane 1 for attacking piece
        if (checkLane(col, row, isWhite, -1, 1, false)) {
            colInc = -1;
            rowInc = 1;
        }
        // Check lane 2 for attacking piece
        else if (checkLane(col, row, isWhite, 0, 1, false)) {
            colInc = 0;
            rowInc = 1;
        }
        // Check lane 3 for attacking piece
        else if (checkLane(col, row, isWhite, 1, 1, false)) {
            colInc = 1;
            rowInc = 1;
        }
        // Check lane 4 for attacking piece
        else if (checkLane(col, row, isWhite, -1, 0, false)) {
            colInc = -1;
            rowInc = 0;
        }
        // Check lane 5 for attacking piece
        else if (checkLane(col, row, isWhite, 1, 0, false)) {
            colInc = 1;
            rowInc = 0;
        }
        // Check lane 6 for attacking piece
        else if (checkLane(col, row, isWhite, -1, -1, false)) {
            colInc = -1;
            rowInc = -1;
        }
        // Check lane 7 for attacking piece
        else if (checkLane(col, row, isWhite, 0, -1, false)) {
            colInc = 0;
            rowInc = -1;
        }
        // Check lane 8 for attacking piece
        else if (checkLane(col, row, isWhite, 1, -1, false)) {
            colInc = 1;
            rowInc = -1;
        }
        // Check Knight1 for attacking piece
        else if (col - 1 >= 0 && row + 2 <= 7) {
            checkPos(col - 1, row + 2, isWhite, false);
        }
        // Check Knight2 for attacking piece
        else if (col + 1 <= 7 && row + 2 <= 7) {
            checkPos(col + 1, row + 2, isWhite, false);
        }
        // Check Knight3 for attacking piece
        else if (col - 2 >= 0 && row + 1 <= 7) {
            checkPos(col - 2, row + 1, isWhite, false);
        }
        // Check Knight4 for attacking piece
        else if (col + 2 <= 7 && row + 1 <= 7) {
            checkPos(col + 2, row + 1, isWhite, false);
        }
        // Check Knight5 for attacking piece
        else if (col - 2 >= 0 && row - 1 >= 0) {
            checkPos(col - 2, row - 1, isWhite, false);
        }
        // Check Knight6 for attacking piece
        else if (col + 2 <= 7 && row - 1 >= 0) {
            checkPos(col + 2, row - 1, isWhite, false);
        }
        // Check Knight7 for attacking piece
        else if (col - 1 >= 0 && row - 2 >= 0) {
            checkPos(col - 1, row - 2, isWhite, false);
        }
        // Check Knight8 for attacking piece
        else if (col + 1 <= 7 && row - 2 >= 0) {
            checkPos(col + 1, row - 2, isWhite, false);
        }

        int aCol = attackerPos[0];
        int aRow = attackerPos[1];
        int checkCol = col + colInc;
        int checkRow = row + rowInc;
        if (aCol == 0 || aRow == 0 || Math.abs(aCol - col) == Math.abs(aRow - row)) {
            while (checkCol != aCol && checkRow != aRow) {
                if (checkForDefender(checkCol, checkRow)) {
                    return true;
                }
                checkCol += colInc;
                checkRow += rowInc;
            }
        }
        return checkForDefender(aCol, aRow);
    }

    private boolean checkLane(int col, int row, boolean isWhite, int incCol, int incRow, boolean lfd) {
        int checkCol = col;
        int checkRow = row;
        while (checkCol + incCol >= 0 && checkCol + incCol <= 7 && checkRow + incRow >= 0 && checkRow + incRow <= 7) {
            checkCol += incCol;
            checkRow += incRow;
            Pieces p = board[7 - checkRow][checkCol];
            if (p != null) {
                if (p.isWhite() == isWhite) {
                    String pType = p.getPiece().toLowerCase();
                    if (incCol == 0 || incRow == 0) {
                        if (pType.equals("q") || pType.equals("r")) {
                            if (!lfd) {
                                setAttackerPos(checkCol, checkRow);
                            } else {
                                setDefenderPos(checkCol, checkRow);
                            }
                            return true;
                        } else if (pType.equals("k") && checkCol == col - 1 || checkCol == col + 1 || checkCol == col && checkRow == row - 1 || checkRow == row + 1 || checkRow == row) {
                            if (!lfd) {
                                setAttackerPos(checkCol, checkRow);
                            } else {
                                setDefenderPos(checkCol, checkRow);
                            }
                            return true;
                        }
                    } else {
                        if (checkCol == col - 1 || checkCol == col + 1) {
                            if (!isWhite && checkRow == row - 1) {
                                if (pType.equals("p")) {
                                    if (!lfd) {
                                        setAttackerPos(checkCol, checkRow);
                                    } else {
                                        setDefenderPos(checkCol, checkRow);
                                    }
                                    return true;
                                }
                            } else if (isWhite && checkRow == row + 1) {
                                if (pType.equals("p")) {
                                    if (!lfd) {
                                        setAttackerPos(checkCol, checkRow);
                                    } else {
                                        setDefenderPos(checkCol, checkRow);
                                    }
                                    return true;
                                }
                            }
                        }
                        if (pType.equals("q") || pType.equals("b")) {
                            if (!lfd) {
                                setAttackerPos(checkCol, checkRow);
                            } else {
                                setDefenderPos(checkCol, checkRow);
                            }
                            return true;
                        } else if (pType.equals("k") && checkCol == col - 1 || checkCol == col + 1 || checkCol == col && checkRow == row - 1 || checkRow == row + 1 || checkRow == row) {
                            if (!lfd) {
                                setAttackerPos(checkCol, checkRow);
                            } else {
                                setDefenderPos(checkCol, checkRow);
                            }
                            return true;
                        }
                    }
                }
                return false;
            }
        }
        return false;
    }

    private boolean checkPos(int col, int row, boolean isWhite, boolean lfd) {
        Pieces p;

        p = board[7 - row][col];
        if (p != null) {
            if (p.isWhite() == isWhite && p.getPiece().toLowerCase().equals("n")) {
                if (!lfd) {
                    setAttackerPos(7 - row, col);
                } else {
                    setDefenderPos(7 - row, col);
                }
                return true;
            }
        }
        return false;
    }

    public boolean checkForDefender(int col, int row) {
        boolean result = false;
        boolean lock = false;
        if (checkLane(col, row, isWhite, -1, 1, true) && !lock) {
            if (checkBoard(col, row)) {
                result = true;
                lock = true;
            }
        }
        if (checkLane(col, row, isWhite, 0, 1, true) && !lock) {
            if (checkBoard(col, row)) {
                result = true;
                lock = true;
            }
        }
        if (checkLane(col, row, isWhite, 1, 1, true) && !lock) {
            if (checkBoard(col, row)) {
                result = true;
                lock = true;
            }
        }
        if (checkLane(col, row, isWhite, -1, 0, true) && !lock) {
            if (checkBoard(col, row)) {
                result = true;
                lock = true;
            }
        }
        if (checkLane(col, row, isWhite, -1, 0, true) && !lock) {
            if (checkBoard(col, row)) {
                result = true;
                lock = true;
            }
        }
        if (checkLane(col, row, isWhite, -1, -1, true) && !lock) {
            if (checkBoard(col, row)) {
                result = true;
                lock = true;
            }
        }
        if (checkLane(col, row, isWhite, 0, -1, true) && !lock) {
            if (checkBoard(col, row)) {
                result = true;
                lock = true;
            }
        }
        if (checkLane(col, row, isWhite, -1, -1, true) && !lock) {
            if (checkBoard(col, row)) {
                result = true;
                lock = true;
            }
        }
        if (checkPos(col, row, isWhite, true) && !lock) {
            if (checkBoard(col, row)) {
                result = true;
                lock = true;
            }
        }
        return result;
    }

    // Check if moving to given position would cause another check
    private boolean checkBoard(int col, int row) {
        movePiece(this.col, this.row, col, row);
        Check chThread = new Check(board, isWhite, this.col, this.row);
        boolean result = false;
        chThread.start();
        try {
            chThread.join();
            result = !chThread.kingInCheck();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        movePiece(col, row, this.col, this.row);
        return result;
    }

    private void movePiece(int prevCol, int prevRow, int col, int row) {
        board[7 - row][col] = board[7 - prevRow][prevCol];
        board[7 - prevRow][prevCol] = null;
    }
}
