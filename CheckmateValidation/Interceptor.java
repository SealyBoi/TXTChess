package CheckmateValidation;

import Pieces.Pieces;

public class Interceptor extends Thread {

    private Pieces[][] board;
    private boolean isWhite;
    private int col;
    private int row;

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
        // Check squares in between the king and attacker to see if any piece can intercept
        safe = calcResult();
    }

    public boolean calcResult() {
        int colInc = 0;
        int rowInc = 0;
        boolean lock = false;
        // Check lanes to find attacking piece
        // Check lane 1 for attacking piece
        if (!lock && checkLane(col, row, isWhite, -1, 1, false)) {
            colInc = -1;
            rowInc = 1;
            lock = true;
        }
        // Check lane 2 for attacking piece
        if (!lock && checkLane(col, row, isWhite, 0, 1, false)) {
            colInc = 0;
            rowInc = 1;
            lock = true;
        }
        // Check lane 3 for attacking piece
        if (!lock && checkLane(col, row, isWhite, 1, 1, false)) {
            colInc = 1;
            rowInc = 1;
            lock = true;
        }
        // Check lane 4 for attacking piece
        if (!lock && checkLane(col, row, isWhite, -1, 0, false)) {
            colInc = -1;
            rowInc = 0;
            lock = true;
        }
        // Check lane 5 for attacking piece
        if (!lock && checkLane(col, row, isWhite, 1, 0, false)) {
            colInc = 1;
            rowInc = 0;
            lock = true;
        }
        // Check lane 6 for attacking piece
        if (!lock && checkLane(col, row, isWhite, -1, -1, false)) {
            colInc = -1;
            rowInc = -1;
            lock = true;
        }
        // Check lane 7 for attacking piece
        if (!lock && checkLane(col, row, isWhite, 0, -1, false)) {
            colInc = 0;
            rowInc = -1;
            lock = true;
        }
        // Check lane 8 for attacking piece
        if (!lock && checkLane(col, row, isWhite, 1, -1, false)) {
            colInc = 1;
            rowInc = -1;
            lock = true;
        }
        // Check Knight1 for attacking piece
        if (!lock && col - 1 >= 0 && row + 2 <= 7) {
            if (checkPos(col - 1, row + 2, isWhite, false)) {
                lock = true;
            }
        }
        // Check Knight2 for attacking piece
        if (!lock && col + 1 <= 7 && row + 2 <= 7) {
            if (checkPos(col + 1, row + 2, isWhite, false)) {
                lock = true;
            }
        }
        // Check Knight3 for attacking piece
        if (!lock && col - 2 >= 0 && row + 1 <= 7) {
            if (checkPos(col - 2, row + 1, isWhite, false)) {
                lock = true;
            }
        }
        // Check Knight4 for attacking piece
        if (!lock && col + 2 <= 7 && row + 1 <= 7) {
            if (checkPos(col + 2, row + 1, isWhite, false)) {
                lock = true;
            }
        }
        // Check Knight5 for attacking piece
        if (!lock && col - 2 >= 0 && row - 1 >= 0) {
            if (checkPos(col - 2, row - 1, isWhite, false)) {
                lock = true;
            }
        }
        // Check Knight6 for attacking piece
        if (!lock && col + 2 <= 7 && row - 1 >= 0) {
            if (checkPos(col + 2, row - 1, isWhite, false)) {
                lock = true;
            }
        }
        // Check Knight7 for attacking piece
        if (!lock && col - 1 >= 0 && row - 2 >= 0) {
            if (checkPos(col - 1, row - 2, isWhite, false)) {
                lock = true;
            }
        }
        // Check Knight8 for attacking piece
        if (!lock && col + 1 <= 7 && row - 2 >= 0) {
            if (checkPos(col + 1, row - 2, isWhite, false)) {
                lock = true;
            }
        }

        int aCol = attackerPos[0];
        int aRow = attackerPos[1];
        int checkCol = col + colInc;
        int checkRow = row + rowInc;
        if (colInc == 0 || rowInc == 0 || Math.abs(aCol - col) == Math.abs(aRow - row)) {
            while (checkCol != aCol || checkRow != aRow) {
                if (checkForDefender(checkCol, checkRow)) {
                    return true;
                }
                checkCol += colInc;
                checkRow += rowInc;
            }
        }
        return checkForDefender(aCol, aRow);
    }

    private boolean updatePos(int checkCol, int checkRow, boolean lfd, Pieces p) {
        if (!lfd && p.isWhite() != isWhite) {
            setAttackerPos(checkCol, checkRow);
            return true;
        } else if (lfd && p.isWhite() == isWhite){
            setDefenderPos(checkCol, checkRow);
            return true;
        } else {
            return false;
        }
    }

    private boolean checkLane(int col, int row, boolean isWhite, int incCol, int incRow, boolean lfd) {
        int checkCol = col;
        int checkRow = row;
        while (checkCol + incCol >= 0 && checkCol + incCol <= 7 && checkRow + incRow >= 0 && checkRow + incRow <= 7) {
            checkCol += incCol;
            checkRow += incRow;
            Pieces p = board[7 - checkRow][checkCol];
            if (p != null) {
                String pType = p.getPiece().toLowerCase();
                if (incCol == 0 || incRow == 0) {
                    if (pType.equals("q") || pType.equals("r")) {
                        return updatePos(checkCol, checkRow, lfd, p);
                    } else if (!lfd && pType.equals("k") && checkCol == col - 1 || checkCol == col + 1 || checkCol == col && checkRow == row - 1 || checkRow == row + 1 || checkRow == row) {
                        return updatePos(checkCol, checkRow, lfd, p);
                    }
                } else {
                    if (checkCol == attackerPos[0] - 1 || checkCol == attackerPos[0] + 1) {
                        if (pType.equals("p")) {
                            if (!isWhite && checkRow == attackerPos[1] - 1 || isWhite && checkRow == attackerPos[1] + 1) {
                                return updatePos(checkCol, checkRow, lfd, p);
                            }
                        }
                    }
                    if (pType.equals("q") || pType.equals("b")) {
                        return updatePos(checkCol, checkRow, lfd, p);
                    } else if (!lfd && pType.equals("k") && checkCol == col - 1 || checkCol == col + 1 || checkCol == col && checkRow == row - 1 || checkRow == row + 1 || checkRow == row) {
                        return updatePos(checkCol, checkRow, lfd, p);
                   }
                }
            }
        }
        return false;
    }

    private boolean checkPos(int col, int row, boolean isWhite, boolean lfd) {
        Pieces p;

        p = board[7 - row][col];
        if (p != null) {
            if (p.isWhite() == isWhite && p.getPiece().toLowerCase().equals("n")) {
                return updatePos(col, row, lfd, p);
            }
        }
        return false;
    }

    public boolean checkForDefender(int col, int row) {
        boolean result = false;
        boolean lock = false;
        if (!lock && checkLane(col, row, isWhite, -1, 1, true)) {
            if (checkBoard(col, row)) {
                result = true;
                lock = true;
            }
        }
        if (!lock && checkLane(col, row, isWhite, 0, 1, true)) {
            if (checkBoard(col, row)) {
                result = true;
                lock = true;
            }
        }
        if (!lock && checkLane(col, row, isWhite, 1, 1, true)) {
            if (checkBoard(col, row)) {
                result = true;
                lock = true;
            }
        }
        if (!lock && checkLane(col, row, isWhite, -1, 0, true)) {
            if (checkBoard(col, row)) {
                result = true;
                lock = true;
            }
        }
        if (!lock && checkLane(col, row, isWhite, -1, 0, true)) {
            if (checkBoard(col, row)) {
                result = true;
                lock = true;
            }
        }
        if (!lock && checkLane(col, row, isWhite, -1, -1, true)) {
            if (checkBoard(col, row)) {
                result = true;
                lock = true;
            }
        }
        if (!lock && checkLane(col, row, isWhite, 0, -1, true)) {
            if (checkBoard(col, row)) {
                result = true;
                lock = true;
            }
        }
        if (!lock && checkLane(col, row, isWhite, -1, -1, true)) {
            if (checkBoard(col, row)) {
                result = true;
                lock = true;
            }
        }
        if (!lock && checkPos(col, row, isWhite, true)) {
            if (checkBoard(col, row)) {
                result = true;
                lock = true;
            }
        }
        return result;
    }

    // Check if moving to given position would cause another check
    private boolean checkBoard(int col, int row) {
        movePiece(defenderPos[0], defenderPos[1], col, row);
        Check chThread = new Check(board, isWhite, this.col, this.row);
        boolean result = false;
        chThread.start();
        try {
            chThread.join();
            result = !chThread.kingInCheck();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        movePiece(col, row, defenderPos[0], defenderPos[1]);
        return result;
    }

    private void movePiece(int prevCol, int prevRow, int col, int row) {
        board[7 - row][col] = board[7 - prevRow][prevCol];
        board[7 - prevRow][prevCol] = null;
    }
}
