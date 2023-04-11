package Main;

import java.util.Scanner;

import Pieces.Pieces;

public class Main {

    // Display main screen and options
    public static void main (String[] args) {

        Scanner scan = new Scanner(System.in);

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

        while (!input.toLowerCase().equals("play") && !input.toLowerCase().equals("quit")) {
            System.out.println("[!]Invalid command");
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

        scan.close();
    }

    // Initialize board and send it to the play loop
    public static void startGame() {

        Board board = new Board();
        board.constructBoard();
        board.printBoard();
        play(board);

    }

    // Exit game
    public static void quitGame() {
        System.exit(0);
    }

    // Play loop
    public static void play(Board board) {

        boolean gameOver = false;
        Scanner scan = new Scanner(System.in);
        String input;
        String[] indexedInput;

        while (!gameOver) {
            input = scan.nextLine();
            // String split into [piece, column, row]
            indexedInput = input.split("");

            String piece = indexedInput[0];
            int prevCol = convertToInt(indexedInput[1]) - 1;
            int prevRow = Integer.parseInt(indexedInput[2]) - 1;
            int col = convertToInt(indexedInput[3]) - 1;
            int row = Integer.parseInt(indexedInput[4]) - 1;

            // Check if params are out of bounds
            if (!piece.toLowerCase().equals("r") && !piece.toLowerCase().equals("n") && !piece.toLowerCase().equals("b") && !piece.toLowerCase().equals("q") && !piece.toLowerCase().equals("k") && !piece.toLowerCase().equals("p")) {
                System.out.println("[!]Invalid piece");
            } else if (prevCol > 7 || prevCol < 0) {
                System.out.println("[!]Invalid previous column");
            } else if (prevRow > 7 || prevRow < 0) {
                System.out.println("[!]Invalid previous row");
            } else if (col > 7 || col < 0) {
                System.out.println("[!]Invalid column");
            } else if (row > 7 || row < 0) {
                System.out.println("[!]Invalid row");
            } else {
                // Grab piece from board
                Pieces currPiece = board.getPiece(prevCol, prevRow);
                // Check that a piece exists on that square
                if (currPiece != null) {
                    // Check that the input piece matches the piece on the square
                    if (piece.toLowerCase().equals(currPiece.getPiece().toLowerCase())) {
                        // Check that the piece is able to move where they want to move it
                        if (currPiece.canMove(board, prevCol, prevRow, col, row)) {
                            board.movePiece(prevCol, prevRow, col, row);
                            board.printBoard();
                        } else {
                            System.out.println("[!]Invalid move");
                        }
                    } else {
                        System.out.println("[!]Invalid piece at " + indexedInput[1] + indexedInput[2]);
                    }
                } else {
                    System.out.println("[!]No piece exists at " + indexedInput[1] + indexedInput[2]);
                }
            }

        }

        scan.close();
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