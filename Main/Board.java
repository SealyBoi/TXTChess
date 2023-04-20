package Main;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

import CheckmateValidation.Check;
import CheckmateValidation.FreeSquare;
import CheckmateValidation.Interceptor;
import Pieces.*;
import Util.CopyBoard;

public class Board implements Serializable {
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
    private Pieces[][] board = new Pieces[8][8];

    // Create list of pawns that can trigger en passant
    private ArrayList<Pawn> epList;

    // Construct board for new game
    public void constructBoard() {
        board = startingBoard;
        epList = new ArrayList<Pawn>();
    }

    public Pieces[][] getBoard() {
        return board;
    }

    public void updateBoard(Pieces[][] board) {
        this.board = board;
    }

    // Get piece from board
    public Pieces getPiece(int col, int row) {
        return board[7 - row][col];
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

    // Check if piece is a pawn and is in en passant
    public boolean canEnPassant(int col, int row) {
        Pieces square = getPiece(col, row);
        if (square != null && square.getPiece().toLowerCase().equals("p")) {
            Pawn p = (Pawn) square;
            if (p.getEP()) {
                return true;
            }
        }
        return false;
    }

    public void triggerEnPassant(int col, int row) {
        board[7 - row][col] = null;
    }

    // Add Pawn to En Passant list
    public void addEP(int col, int row) {
        epList.add((Pawn) getPiece(col, row));
    }

    // Remove Pawns that have exited En Passant
    public void updateEP(boolean isWhite) {
        for (int i = 0; i < epList.size(); i++) {
            if (epList.get(i).isWhite() == isWhite) {
                epList.get(i).flipEP();
                epList.remove(i);
                i--;
            }
        }
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
        // Create state check board
        Pieces[][] stateBoard = CopyBoard.copyBoard(board);
        // Check if king would be put into check after piece moved
        movePiece(prevCol, prevRow, newCol, newRow);
        boolean causeCheck = false;
        if (king.isWhite()) {
            causeCheck = inCheck(true);
        } else {
            causeCheck = inCheck(false);
        }
        movePiece(newCol, newRow, prevCol, prevRow);
        board = CopyBoard.copyBoard(stateBoard);
        return causeCheck;
    }

    // Method called to check if player is in checkmate
    public boolean checkForMate(boolean whiteToMove) {
        King k;
        boolean inMate = false;
        if (whiteToMove) {
            k = (King) whiteKing;
        } else {
            k = (King) blackKing;
        }
        // Create state check board
        Pieces[][] fsBoard = CopyBoard.copyBoard(board);
        Pieces[][] iBoard = CopyBoard.copyBoard(board);
        FreeSquare fsThread = new FreeSquare(fsBoard, k.isWhite(), k.getPosition()[0], k.getPosition()[1]);
        Interceptor iThread = new Interceptor(iBoard, k.isWhite(), k.getPosition()[0], k.getPosition()[1]);
        fsThread.start();
        iThread.start();
        try {
            fsThread.join();
            iThread.join();
            inMate = !(fsThread.isSafe() || iThread.isSafe());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return inMate;
    }

    // Print current board
    public void printBoard(boolean whiteToMove) {
        int goal;
        int increment;
        int start;
        String color;
        String alphabet;
        boolean flip = true;
        // Print board depending on which player's turn it is
        if (whiteToMove) {
            start = 0;
            goal = 8;
            increment = 1;
            alphabet = "    a  b  c  d  e  f  g  h  ";
        } else {
            start = 7;
            goal = -1;
            increment = -1;
            alphabet = "    h  g  f  e  d  c  b  a  ";
        }
       for (int i = start; i != goal; i += increment) {
        System.out.print(8 - i + " |");
        for (int j = start; j != goal; j += increment) {
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
       System.out.println(alphabet);
    }

}
