package Main;

import java.util.Scanner;

import Elo.Elo;
import Network.*;
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

    // Declaring string to clear screen
    public static final String ANSI_CLEAR = "\033[H\033[2J";

    // Declaring Server and Client
    static Network network;

    // Declaring Elo
    static Elo elo = new Elo();

    // Display main screen and options
    public static void main (String[] args) throws Exception {
        mainMenu();
    }

    // Main Screen Logo and Options
    public static void mainMenu() throws Exception {

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
        System.out.println(">Host");
        System.out.println(">Connect");
        System.out.println(">Rating");
        System.out.println(">Quit");

        String input = scan.nextLine().toLowerCase();
        int port;

        // Input validation
        while (!input.equals("play") && !input.equals("host") && !input.equals("connect") && !input.equals("rating") && !input.equals("quit")) {
            System.out.println(ANSI_RED_BACKGROUND + "[!]Invalid command" + ANSI_RESET);
            input = scan.nextLine();
        }

        switch (input.toLowerCase()) {
            case "play":
                clearScreen();
                startGame(false, false);
            break;
            case "host":
                System.out.print("[*]Select a port: ");
                port = Integer.parseInt(scan.nextLine());
                network = new Server(port);
                try {
                    network.run();
                    startGame(true, true);
                } catch (Exception e) {
                    throw new Error(e.getMessage());
                }
            break;
            case "connect":
                System.out.print("[*]Select a port: ");
                port = Integer.parseInt(scan.nextLine());
                System.out.println("[*]Enter server address: ");
                input = scan.nextLine();
                network = new Client(input, port);
                try {
                    network.run();
                    startGame(true, false);
                } catch (Exception e) {
                    throw new Error(e.getMessage());
                }
            break;
            case "rating":
                System.out.println("[!]Your rating: " + elo.getElo());
                System.out.println("[!]Press enter to return to menu");
                input = scan.nextLine();
                clearScreen();
                mainMenu();
            break;
            case "quit":
                clearScreen();
                quitGame();
            break;
        }

    }

    public static void clearScreen() {
        System.out.print(ANSI_CLEAR);  
        System.out.flush();  
    }

    // Initialize board and send it to the play loop
    public static void startGame(boolean isMultiplayer, boolean isServer) throws Exception {

        Board board = new Board();
        board.constructBoard();
        if (isMultiplayer) {
            if (isServer) {
                board.printBoard(true, -1, -1, -1, -1);
            } else {
                board.printBoard(false, -1, -1, -1, -1);
            }
        } else {
            board.printBoard(true, -1, -1, -1, -1);
        }
        System.out.println(ANSI_YELLOW + "[*]White to move" + ANSI_RESET);
        play(board, isMultiplayer, isServer);

    }

    // Exit game
    public static void quitGame() {
        System.exit(0);
    }

    // Play loop
    public static void play(Board board, boolean isMultiplayer, boolean isServer) throws Exception {

        boolean gameOver = false;
        boolean whiteToMove = true;
        String input;
        String[] indexedInput = new String[7];
        boolean firstMove = false;
        double oppElo = 0;
        if (isMultiplayer) {
            if (isServer) {
                firstMove = true;
                network.readInput();
            } else {
                network.sendOutput(null);
            }
        }

        GAME:
        while (!gameOver) {
            if (!firstMove && isMultiplayer && (isServer != whiteToMove)) {
                indexedInput = network.readInput();
                oppElo = Double.parseDouble(indexedInput[6]);
            } else {
                input = scan.nextLine();

                // String split into [piece, previousColumn, previousRow, column, row]
                String[] tmp = input.split("");
                for (int i = 0; i < tmp.length; i++) {
                    indexedInput[i] = tmp[i];
                }
                indexedInput[5] = "tmp";
            }

            // prevCol & prevRow refer to the piece's last position, while col & row refer to the piece's requested new position
            String piece, promotion;
            int prevCol, prevRow, col, row;
            if (indexedInput.length < 5) {
                printError("[!]Invalid input", whiteToMove, board);
                printTurn(whiteToMove, gameOver);
                continue GAME;
            }
            try {
                piece = indexedInput[0];
                prevCol = convertToInt(indexedInput[1]) - 1;
                prevRow = Integer.parseInt(indexedInput[2]) - 1;
                col = convertToInt(indexedInput[3]) - 1;
                row = Integer.parseInt(indexedInput[4]) - 1;
                promotion = indexedInput[5];
            } catch (NumberFormatException e) {
                printError("[!]Invalid input", whiteToMove, board);
                printTurn(whiteToMove, gameOver);
                continue GAME;
            }

            // Check if params are out of bounds
            if (!piece.toLowerCase().equals("r") && !piece.toLowerCase().equals("n") && !piece.toLowerCase().equals("b") && !piece.toLowerCase().equals("q") && !piece.toLowerCase().equals("k") && !piece.toLowerCase().equals("p")) {
                printError("[!]Invalid piece", whiteToMove, board);
            } else if (prevCol > 7 || prevCol < 0) {
                printError("[!]Invalid previous column", whiteToMove, board);
            } else if (prevRow > 7 || prevRow < 0) {
                printError("[!]Invalid previous row", whiteToMove, board);
            } else if (col > 7 || col < 0) {
                printError("[!]Invalid column", whiteToMove, board);
            } else if (row > 7 || row < 0) {
                printError("[!]Invalid row", whiteToMove, board);
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
                                    if (currPiece.getPiece().toLowerCase().equals("p") && ((row == 7 && currPiece.isWhite()) || (row == 0 && !currPiece.isWhite()))) {
                                        promotion = board.promotePiece(prevCol, prevRow, promotion);
                                    }
                                    // Move piece, switch turns, and print the new board
                                    board.movePiece(prevCol, prevRow, col, row);
                                    if (isMultiplayer && isServer == whiteToMove) {
                                        indexedInput[5] = promotion;
                                        indexedInput[6] = "" + elo.getElo();
                                        network.sendOutput(indexedInput);
                                    }
                                    whiteToMove = !whiteToMove;
                                    clearScreen();
                                    if (isMultiplayer) {
                                        if (isServer) {
                                            board.printBoard(true, prevCol, prevRow, col, row);
                                        } else {
                                            board.printBoard(false, prevCol, prevRow, col, row);
                                        }
                                    } else {
                                        board.printBoard(whiteToMove, prevCol, prevRow, col, row);
                                    }
                                    // Check if player has been put in check
                                    if (board.inCheck(whiteToMove)) {
                                        // Check if player has been put in checkmate
                                        if (board.checkForMate(whiteToMove)) {
                                            // Check for mate
                                            printMessage("Checkmate!");
                                            gameOver = true;
                                        } else {
                                            printMessage("Check!");
                                        }
                                    }
                                    board.updateEP(whiteToMove);
                                } else {
                                    printError("[!]Cannot Self Check", whiteToMove, board);
                                }
                            } else {
                                printError("[!]Invalid move", whiteToMove, board);
                            }
                        } else {
                            printError("[!]Invalid piece at " + indexedInput[1] + indexedInput[2], whiteToMove, board);
                        }
                    }
                } else {
                    printError("[!]No piece exists at " + indexedInput[1] + indexedInput[2], whiteToMove, board);
                }
            }

            // Print current turn
            printTurn(whiteToMove, gameOver);
            firstMove = false;
        }

        // If the game is multiplayer, determine who should win/lose elo
        // If white wins, then it was black to move when the game ended
        // If black wins, then it was white to move when the game ended
        double d = 0.5;
        if (isMultiplayer) {
            if (!whiteToMove && isServer) {
                d = 1;
            } else {
                d = 0;
            }
        }

        // Display elo changes
        elo.EloRating(elo.getElo(), oppElo, 30, d);

        // Print end game message and return user to Main Screen
        printMessage("[!]Press enter to return to the main menu");
        input = scan.nextLine();
        if (isMultiplayer) {
            network.closeNetwork();
        }
        mainMenu();
    }

    // Print current turn
    public static void printTurn(boolean whiteToMove, boolean gameOver) {
        if (whiteToMove && !gameOver) {
            printMessage("[*]White to move");
        } else {
            printMessage("[*]Black to move");
        }
    }

    // Throw Error
    public static void printError(String message, boolean whiteToMove, Board board) {
        clearScreen();
        board.printBoard(whiteToMove, -1, -1, -1, -1);
        System.out.println(ANSI_RED_BACKGROUND + message + ANSI_RESET);
    }

    // Print important messages
    public static void printMessage(String message) {
        System.out.println(ANSI_YELLOW + message + ANSI_RESET);
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