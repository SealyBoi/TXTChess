package Util;

import Pieces.Pieces;

public class CopyBoard {
    public static Pieces[][] copyBoard(Pieces[][] board) {
        Pieces[][] tmp = new Pieces[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tmp[i][j] = board[i][j];
            }
        }

        return tmp;
    }
}
