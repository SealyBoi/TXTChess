package Main;
import java.util.Scanner;

import CheckmateValidation.Check;
import CheckmateValidation.Counter;
import CheckmateValidation.FreeSquare;
import CheckmateValidation.Interceptor;
import Pieces.*;

public class Board {
    // Declaring ANSI_RESET so we can reset color
    public static final String ANSI_RESET = "\u001B[0m";

    // Declaring green background for black pieces
    public static final String ANSI_BLUE = "\u001B[34m";

    // Declaring white background for white pieces
    public static final String ANSI_RED = "\u001B[31m";

    // Save both kings for later reference
    Pieces whiteKing = new King("k", true, 4, 0);
    Pieces blackKing = new King("K", false, 4, 7);

    // Open Scanner for User Input
    static Scanner scan = new Scanner(System.in);

    // Create initial board state
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

    // Create current board state
    private static Pieces[][] board;

    // Construct board for new game
    public void constructBoard() {
        board = startingBoard;
    }

    // Get piece from board
    public Pieces getPiece(int col, int row) {
        if (board[7 - row][col] != null) {
            return board[7 - row][col];
        } else  {
            return null;
        }
    }

    // Move piece from previous position to new position
    public void movePiece(int prevCol, int prevRow, int col, int row) {
        board[7 - row][col] = board[7 - prevRow][prevCol];
        King king;
        if (getPiece(prevCol, prevRow).isWhite()) {
            king = (King) whiteKing;
        } else {
            king = (King) blackKing;
        }
        // If piece being moved is King, update King's position as well
        if (king.getPosition()[0] == prevCol && king.getPosition()[1] == prevRow) {
            king.updatePosition(col, row);
        }
        board[7 - prevRow][prevCol] = null;
    }

    // Promote pawn to another piece
    public void promotePiece(int col, int row) {
        String input = "";
        // Input validation
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

    // Check if square does not contain a piece
    public boolean squareIsEmpty(int col, int row) {
        Pieces square = getPiece(col, row);
        if (square == null) {
            return true;
        } else {
            return false;
        }
    }

    // Check if square contains a friendly piece
    public boolean squareContainsAlly(int prevCol, int prevRow, int col, int row) {
        Pieces oldSquare = getPiece(prevCol, prevRow);
        Pieces newSquare = getPiece(col, row);
        if (oldSquare.isWhite() && newSquare.isWhite() || !oldSquare.isWhite() && !newSquare.isWhite()) {
            return true;
        }
        return false;
    }

    // Check if square contains an opponent's piece
    public boolean squareContainsEnemy(int prevCol, int prevRow, int col, int row) {
        Pieces oldSquare = getPiece(prevCol, prevRow);
        Pieces newSquare = getPiece(col, row);
        if (newSquare != null && newSquare.isWhite() ^ oldSquare.isWhite()) {
            return true;
        }
        return false;
    }

    // Method called to check position of King after King has moved
    public boolean inCheck(boolean whiteToMove, int newCol, int newRow) {
        Check chThread;
        King k;
        if (whiteToMove) {
            k = (King) whiteKing;
        } else {
            k = (King) blackKing;
        }
        chThread = new Check(board, k.isWhite(), newCol, newRow);
        chThread.start();
        try {
            chThread.join();
            return chThread.kingInCheck();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method called to check position of King at end of turn
    public boolean inCheck(boolean whiteToMove) {
        Check chThread;
        King k;
        if (whiteToMove) {
            k = (King) whiteKing;
        } else {
            k = (King) blackKing;
        }
        chThread = new Check(board, k.isWhite(), k.getPosition()[0], k.getPosition()[1]);
        chThread.start();
        try {
            chThread.join();
            return chThread.kingInCheck();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method called to check if a move would put player into check
    public boolean moveWouldCauseCheck(int prevCol, int prevRow, int newCol, int newRow) {
        King king;
        if (getPiece(prevCol, prevRow).isWhite()) {
            king = (King) whiteKing;
        } else {
            king = (King) blackKing;
        }
        // If the piece moving is the king, check if he is moving into check
        if (getPiece(prevCol, prevRow).getPiece().toLowerCase().equals("k")) {
            if (king.isWhite()) {
                return inCheck(true, newCol, newRow);
            } else {
                return inCheck(false, newCol, newRow);
            }
        }
        // Else check if moving the piece would put the player into check
        else {
            movePiece(prevCol, prevRow, newCol, newRow);
            boolean causeCheck = false;
            if (king.isWhite()) {
                causeCheck = inCheck(true);
            } else {
                causeCheck = inCheck(false);
            }
            movePiece(newCol, newRow, prevCol, prevRow);
            return causeCheck;
        }
    }

    // Method called to check if player is in checkmate
    public boolean checkForMate(boolean whiteToMove) {
        King k;
        if (whiteToMove) {
            k = (King) whiteKing;
        } else {
            k = (King) blackKing;
        }
        FreeSquare fsThread = new FreeSquare(board, k.isWhite(), k.getPosition()[0], k.getPosition()[1]);
        Interceptor iThread = new Interceptor(board, k.isWhite(), k.getPosition()[0], k.getPosition()[1]);
        Counter cThread = new Counter(board, k.isWhite(), k.getPosition()[0], k.getPosition()[1]);
        fsThread.start();
        iThread.start();
        cThread.start();
        try {
            fsThread.join();
            iThread.join();
            cThread.join();
            return !(fsThread.isSafe() && iThread.isSafe() && cThread.isSafe());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Print current board
    public void printBoard(boolean whiteToMove) {
        int goal;
        int increment;
        int start;
        String color;
        boolean flip = true;
        // Print board depending on which player's turn it is
        if (whiteToMove) {
            start = 0;
            goal = 8;
            increment = 1;
        } else {
            start = 7;
            goal = -1;
            increment = -1;
        }
       for (int i = start; i != goal; i += increment) {
        System.out.print(8 - i + " |");
        for (int j = 0; j < 8; j++) {
            if (board[i][j] != null) {
                if (board[i][j].isWhite()) {
                    color = ANSI_RED;
                } else {
                    color = ANSI_BLUE;
                }
                System.out.print(color + " " + board[i][j].getPiece() + " " + ANSI_RESET);
            } else {
                if (flip) {
                    System.out.print(" - ");
                } else {
                    System.out.print (" + ");
                }
            }
            flip = !flip;
        }
        System.out.print("|");
        System.out.println();
        flip = !flip;
       }
       System.out.println("    a  b  c  d  e  f  g  h  ");
    }
}
