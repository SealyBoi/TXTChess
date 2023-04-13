package Main;
import java.util.Scanner;

import CheckmateValidation.Check;
import CheckmateValidation.Counter;
import CheckmateValidation.FreeSquare;
import CheckmateValidation.Interceptor;
import Pieces.*;

public class Board {
    Pieces whiteKing = new King("k", true, 4, 0);
    Pieces blackKing = new King("K", false, 4, 7);

    private Pieces[][] startingBoard = {
        {new Rook("R",false), new Knight("N",false), new Bishop("B",false), new Queen("Q",false), blackKing, new Bishop("B",false), new Knight("N",false), new Rook("R",false)},
        {new Pawn("P", false), new Pawn("P", false), new Pawn("P", false), new Pawn("P", false), new Pawn("P", false), new Pawn("P", false), new Pawn("P", false), new Pawn("P", false)},
        {null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null},
        {new Pawn("p", true), new Pawn("p", true), new Pawn("p", true), new Pawn("p", true), new Pawn("p", true), new Pawn("p", true), new Pawn("p", true), new Pawn("p", true)},
        {new Rook("r",true), new Knight("n",true), new Bishop("b",true), new Queen("q",true), whiteKing, new Bishop("b",true), new Knight("n",true), new Rook("r",true)},
    };

    private static Pieces[][] board;

    public void constructBoard() {
        board = startingBoard;
    }

    public Pieces getPiece(int col, int row) {
        if (board[7 - row][col] != null) {
            return board[7 - row][col];
        } else  {
            return null;
        }
    }

    public void movePiece(int prevCol, int prevRow, int col, int row) {
        board[7 - row][col] = board[7 - prevRow][prevCol];
        King king;
        if (getPiece(prevCol, prevRow).isWhite()) {
            king = (King) whiteKing;
        } else {
            king = (King) blackKing;
        }
        if (king.getPosition()[0] == prevCol && king.getPosition()[1] == prevRow) {
            king.updatePosition(col, row);
        }
        board[7 - prevRow][prevCol] = null;
    }

    public void promotePiece(int col, int row) {
        Scanner scan = new Scanner(System.in);

        System.out.println("Select a promotion: ");
        System.out.println("-Rook (r)");
        System.out.println("-Knight (n)");
        System.out.println("-Bishop (b)");
        System.out.println("-Queen (q)");

        String input = scan.nextLine();
        while (!input.toLowerCase().equals("r") && !input.toLowerCase().equals("b") && !input.toLowerCase().equals("n") && !input.toLowerCase().equals("q")) {
            System.out.println("Select a promotion: ");
            System.out.println("-Rook (r)");
            System.out.println("-Knight (n)");
            System.out.println("-Bishop (b)");
            System.out.println("-Queen (q)");
            input = scan.nextLine();
        }

        Pieces currPiece = getPiece(col, row);
        Pieces promotion = getPiece(col, row);
        switch (input.toLowerCase()) {
            case "r":
                promotion = new Rook(currPiece.isWhite() ? "r" : "R", currPiece.isWhite());
            break;
            case "n":
                promotion = new Knight(currPiece.isWhite() ? "k" : "K", currPiece.isWhite());
            break;
            case "b":
                promotion = new Bishop(currPiece.isWhite() ? "b" : "B", currPiece.isWhite());
            break;
            case "q":
                promotion = new Queen(currPiece.isWhite() ? "q" : "Q", currPiece.isWhite());
            break;
        }

        board[7 - row][col] = promotion;
    }

    public boolean squareIsEmpty(int col, int row) {
        Pieces square = getPiece(col, row);
        if (square == null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean squareContainsAlly(int prevCol, int prevRow, int col, int row) {
        Pieces oldSquare = getPiece(prevCol, prevRow);
        Pieces newSquare = getPiece(col, row);
        if (oldSquare.isWhite() && newSquare.isWhite() || !oldSquare.isWhite() && !newSquare.isWhite()) {
            return true;
        }
        return false;
    }

    public boolean squareContainsEnemy(int prevCol, int prevRow, int col, int row) {
        Pieces oldSquare = getPiece(prevCol, prevRow);
        Pieces newSquare = getPiece(col, row);
        if (newSquare != null && newSquare.isWhite() ^ oldSquare.isWhite()) {
            return true;
        }
        return false;
    }

    public boolean inCheck() {
        Check chThread = new Check(board, whiteKing, blackKing);
        chThread.start();
        try {
            chThread.join();
            return chThread.kingInCheck();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkForMate() {
        FreeSquare fsThread = new FreeSquare();
        Interceptor iThread = new Interceptor();
        Counter cThread = new Counter();
        fsThread.start();
        iThread.start();
        cThread.start();
        return false;
    }

    public void printBoard() {
       for (int i = 0; i < 8; i++) {
        System.out.print(8 - i + " |");
        for (int j = 0; j < 8; j++) {
            if (board[i][j] != null) {
                System.out.print(" " + board[i][j].getPiece() + " ");
            } else {
                System.out.print(" - ");
            }
        }
        System.out.print("|");
        System.out.println();
       }
       System.out.println("    a  b  c  d  e  f  g  h  ");
    }
}
