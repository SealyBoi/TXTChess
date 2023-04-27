package CheckmateValidation;

import Pieces.Pieces;

public class Check extends Thread {

    private Pieces[][] board;
    private boolean isWhite;
    private int col;
    private int row;

    public Check (Pieces[][] board, boolean isWhite, int col, int row) {
        this.board = board;
        this.isWhite = isWhite;
        this.col = col;
        this.row = row;
    }

    boolean inCheck = false;

    public boolean kingInCheck() {
        return inCheck;
    }

    public void run() {
        // Check all lanes and knight positions from king to see if there is an attacking piece
        LaneChecking lcThread = new LaneChecking(board, isWhite, col, row);
        KnightChecking kcThread = new KnightChecking(board, isWhite, col, row);

        lcThread.start();
        kcThread.start();

        try {
            lcThread.join();
            kcThread.join();

            inCheck = lcThread.isAttacking() || kcThread.isAttacking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}

/*
 * Along with the knight positions, we need to check each 'lane' coming from the king.
 * We will label these lanes as lane1, lane2, and so on. Where the lane number is associated
 * with the squares in a key-pad like order. For a visual, you can look below.
 * [ 1 2 3 ]
 * [ 4 k 5 ]
 * [ 6 7 8 ]
 */

 class LaneChecking extends Thread {
    private Pieces[][] board;
    private boolean isWhite;
    private int col;
    private int row;

    public LaneChecking (Pieces[][] board, boolean isWhite, int col, int row) {
        this.board = board;
        this.isWhite = isWhite;
        this.col = col;
        this.row = row;
    }

    boolean attacker = false;

    public boolean isAttacking() {
        return attacker;
    }

    public void run() {
        attacker = (
            checkLane(col, row, isWhite, -1, 1) ||
            checkLane(col, row, isWhite, 0, 1) ||
            checkLane(col, row, isWhite, 1, 1) ||
            checkLane(col, row, isWhite, -1, 0) ||
            checkLane(col, row, isWhite, 1, 0) ||
            checkLane(col, row, isWhite, -1, -1) ||
            checkLane(col, row, isWhite, 0, -1) ||
            checkLane(col, row, isWhite, 1, -1)
        );
    }

    public boolean checkLane(int col, int row, boolean isWhite, int incCol, int incRow) {
        int checkCol = col;
        int checkRow = row;
        while (checkCol + incCol >= 0 && checkCol + incCol <= 7 && checkRow + incRow >= 0 && checkRow + incRow <= 7) {
            checkCol += incCol;
            checkRow += incRow;
            Pieces p = board[7 - checkRow][checkCol];
            if (p != null) {
                if (p.isWhite() != isWhite) {
                    String pType = p.getPiece().toLowerCase();
                    if (incCol == 0 || incRow == 0) {
                        if (pType.equals("q") || pType.equals("r")) {
                            return true;
                        }
                    } else if (Math.abs(checkCol - col) == Math.abs(checkRow - row)) {
                        if (checkCol == col - 1 || checkCol == col + 1) {
                            if (!isWhite && checkRow == row - 1 || isWhite && checkRow == row + 1) {
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
            }
        }
        return false;
    }
 }

 /*
  * For knights, we will have the next eight lanes be checked in KnightChecking (labeled as Knight1, Knight2, etc.)
  * [ - 1 - 2 - ]
  * [ 3 - - - 4 ]
  * [ - - k - - ]
  * [ 5 - - - 6 ]
  * [ - 7 - 8 - ]
  */

  class KnightChecking extends Thread {
    private Pieces[][] board;
    private boolean isWhite;
    private int col;
    private int row;

    public KnightChecking (Pieces[][] board, boolean isWhite, int col, int row) {
        this.board = board;
        this.isWhite = isWhite;
        this.col = col;
        this.row = row;
    }

    boolean attacker = false;

    public boolean isAttacking() {
        return attacker;
    }

    public void run() {
        attacker = (
            checkPos(col - 1, row + 2, isWhite) ||
            checkPos(col + 1, row + 2, isWhite) ||
            checkPos(col - 2, row + 1, isWhite) ||
            checkPos(col + 2, row + 1, isWhite) ||
            checkPos(col - 2, row - 1, isWhite) ||
            checkPos(col + 2, row - 1, isWhite) ||
            checkPos(col - 1, row - 2, isWhite) ||
            checkPos(col + 1, row - 2, isWhite)
        );
    }

    public boolean checkPos(int col, int row, boolean isWhite) {
        Pieces p;

        if (col > 7 || col < 0 || row > 7 || row < 0) {
            return false;
        }

        p = board[7 - row][col];
        if (p != null) {
            if (p.isWhite() != isWhite && p.getPiece().toLowerCase().equals("n")) {
                return true;
            }
        }
        return false;
    }
  }