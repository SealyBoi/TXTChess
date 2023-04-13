package CheckmateValidation;

import Pieces.Pieces;

public class Check extends Thread {

    Pieces[][] board;
    Pieces whiteKing;
    Pieces blackKing;

    public Check (Pieces[][] board, Pieces whiteKing, Pieces blackKing) {
        this.board = board;
        this.whiteKing = whiteKing;
        this.blackKing = blackKing;
    }

    boolean inCheck = false;

    public boolean kingInCheck() {
        return inCheck;
    }

    public void run() {
        // Check all lanes and knight positions from king to see if there is an attacking piece
        LaneChecking lcThread = new LaneChecking(board, whiteKing, blackKing);
        KnightChecking kcThread = new KnightChecking(board, whiteKing, blackKing);

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
    Pieces[][] board;
    Pieces whiteKing;
    Pieces blackKing;

    public LaneChecking (Pieces[][] board, Pieces whiteKing, Pieces blackKing) {
        this.board = board;
        this.whiteKing = whiteKing;
        this.blackKing = blackKing;
    }

    boolean attacker = false;

    public boolean isAttacking() {
        return attacker;
    }

    public void run() {
        // TODO Check Lane1 for attacking pieces

        // TODO Check Lane2 for attacking pieces

        // TODO Check Lane3 for attacking pieces

        // TODO Check Lane4 for attacking pieces

        // TODO Check Lane5 for attacking piecces

        // TODO Check Lane6 for attacking pieces

        // TODO Check Lane7 for attacking pieces

        // TODO Check Lane8 for attacking pieces

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
    Pieces[][] board;
    Pieces whiteKing;
    Pieces blackKing;

    public KnightChecking (Pieces[][] board, Pieces whiteKing, Pieces blackKing) {
        this.board = board;
        this.whiteKing = whiteKing;
        this.blackKing = blackKing;
    }

    boolean attacker = false;

    public boolean isAttacking() {
        return attacker;
    }

    public void run() {
        // TODO Check Knight1 for attacking piece

        // TODO Check Knight2 for attacking piece

        // TODO Check Knight3 for attacking piece

        // TODO Check Knight4 for attacking piece

        // TODO Check Knight5 for attacking piece

        // TODO Check Knight6 for attacking piece

        // TODO Check Knight7 for attacking piece

        // TODO Check Knight8 for attacking piece

    }
  }