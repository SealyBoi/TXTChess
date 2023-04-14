package Main;

import java.util.Scanner;

import Pieces.Pieces;

public class Main {

    // Open Scanner for User Input
    static Scanner scan = new Scanner(System.in);

    // Declaring ANSI_RESET so we can reset color
    public static final String ANSI_RESET = "\u001B[0m";

    // Declaring red background for error messages
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";

    // Declaring text color for important messages
    public static final String ANSI_YELLOW = "\u001B[33m";

    // Display main screen and options
    public static void main (String[] args) {
        mainMenu();
    }

    // Main Screen Logo and Options
    public static void mainMenu() {

        System.out.println("===================================================================");
        System.out.println("  ####  #    # ######  ####   ####          #   ##   #    #   ##   ");
        System.out.println(" #    # #    # #      #      #              #  #  #  #    #  #  #  ");
        System.out.println(" #      ###### #####   ####   ####          # #    # #    # #    # ");
        System.out.println(" #      #    # #           #      #         # ###### #    # ###### ");
        System.out.println(" #    # #    # #      #    # #    # ## #    # #    #  #  #  #    # ");
        System.out.println("  ####  #    # ######  ####   ####  ##  ####  #    #   ##   #    # ");
        System.out.println("===================================================================");
        System.out.println();

        System.out.println(">Play");
        System.out.println(">Quit");

        String input = scan.nextLine();

        // Input validation
        while (!input.toLowerCase().equals("play") && !input.toLowerCase().equals("quit")) {
            System.out.println(ANSI_RED_BACKGROUND + "[!]Invalid command" + ANSI_RESET);
            input = scan.nextLine();
        }

        switch (input.toLowerCase()) {
            case "play":
                startGame();
            break;
            case "quit":
                quitGame();
            break;
        }

    }

    // Initialize board and send it to the play loop
    public static void startGame() {

        Board board = new Board();
        board.constructBoard();
        board.printBoard(true);
        System.out.println(ANSI_YELLOW + "[*]White to move" + ANSI_RESET);
        play(board);

    }

    // Exit game
    public static void quitGame() {
        System.exit(0);
    }

    // Play loop
    public static void play(Board board) {

        boolean gameOver = false;
        boolean whiteToMove = true;
        String input;
        String[] indexedInput;

        while (!gameOver) {
            input = scan.nextLine();
            // String split into [piece, previousColumn, previousRow, column, row]
            indexedInput = input.split("");

            // TODO Add input validation
            // prevCol & prevRow refer to the piece's last position, while col & row refer to the piece's requested new position
            String piece = indexedInput[0];
            int prevCol = convertToInt(indexedInput[1]) - 1;
            int prevRow = Integer.parseInt(indexedInput[2]) - 1;
            int col = convertToInt(indexedInput[3]) - 1;
            int row = Integer.parseInt(indexedInput[4]) - 1;

            // Check if params are out of bounds
            if (!piece.toLowerCase().equals("r") && !piece.toLowerCase().equals("n") && !piece.toLowerCase().equals("b") && !piece.toLowerCase().equals("q") && !piece.toLowerCase().equals("k") && !piece.toLowerCase().equals("p")) {
                System.out.println(ANSI_RED_BACKGROUND + "[!]Invalid piece" + ANSI_RESET);
            } else if (prevCol > 7 || prevCol < 0) {
                System.out.println(ANSI_RED_BACKGROUND + "[!]Invalid previous column" + ANSI_RESET);
            } else if (prevRow > 7 || prevRow < 0) {
                System.out.println(ANSI_RED_BACKGROUND + "[!]Invalid previous row" + ANSI_RESET);
            } else if (col > 7 || col < 0) {
                System.out.println(ANSI_RED_BACKGROUND + "[!]Invalid column" + ANSI_RESET);
            } else if (row > 7 || row < 0) {
                System.out.println(ANSI_RED_BACKGROUND + "[!]Invalid row" + ANSI_RESET);
            } else {
                // Grab piece player is trying to move from board
                Pieces currPiece = board.getPiece(prevCol, prevRow);
                // Check that a piece exists on that square
                if (currPiece != null) {
                    // Check that the piece being moved is the same color of whoever's turn it is
                    if (currPiece.isWhite() == whiteToMove) {
                        // Check that the input piece matches the piece on the square
                        if (piece.toLowerCase().equals(currPiece.getPiece().toLowerCase())) {
                            // Check that the requested position to move to follows the piece's movement rules
                            if (currPiece.canMove(board, prevCol, prevRow, col, row)) {
                                // Check if the move would cause the player to be put in check
                                if (!board.moveWouldCauseCheck(prevCol, prevRow, col, row)) {
                                    // Check if piece is a pawn that is ready to promote
                                    if (currPiece.getPiece().toLowerCase().equals("p") && (row == 7 && currPiece.isWhite()) || row == 0 && !currPiece.isWhite()) {
                                        board.promotePiece(prevCol, prevRow);
                                    }
                                    // Move piece, switch turns, and print the new board
                                    board.movePiece(prevCol, prevRow, col, row);
                                    whiteToMove = !whiteToMove;
                                    board.printBoard(whiteToMove);
                                    // Check if player has been put in check
                                    if (board.inCheck(whiteToMove)) {
                                        // Check if player has been put in checkmate
                                        if (board.checkForMate()) {
                                            // TODO Check for mate
                                        } else {
                                            System.out.println(ANSI_YELLOW + "Check!" + ANSI_RESET);
                                        }
                                    }
                                } else {
                                    System.out.println(ANSI_RED_BACKGROUND + "[!]Cannot Self Check" + ANSI_RESET);
                                }
                            } else {
                                System.out.println(ANSI_RED_BACKGROUND + "[!]Invalid move" + ANSI_RESET);
                            }
                        } else {
                            System.out.println(ANSI_RED_BACKGROUND + "[!]Invalid piece at " + indexedInput[1] + indexedInput[2] + ANSI_RESET);
                        }
                    }
                } else {
                    System.out.println(ANSI_RED_BACKGROUND + "[!]No piece exists at " + indexedInput[1] + indexedInput[2] + ANSI_RESET);
                }
            }

            // Print current turn
            if (whiteToMove && !gameOver) {
                System.out.println(ANSI_YELLOW + "[*]White to move" + ANSI_RESET);
            } else {
                System.out.println(ANSI_YELLOW + "[*]Black to move" + ANSI_RESET);
            }

        }

        // Print end game message and return user to Main Screen
        System.out.println(ANSI_YELLOW + "[!]Press enter to return to the main menu" + ANSI_RESET);
        input = scan.nextLine();
        mainMenu();
    }

    // Convert alphabetic character to an integer to obtain column value
    public static int convertToInt(String str) {

        String[] alphabet = { "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z" };

        for (int i = 1; i < alphabet.length; i++) {
            if (str.toLowerCase().equals(alphabet[i - 1])) {
                return i;
            }
        }
        return 0;

    }

}