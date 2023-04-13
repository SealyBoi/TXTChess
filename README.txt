 ===================================================================
  ####  #    # ######  ####   ####          #   ##   #    #   ##   
 #    # #    # #      #      #              #  #  #  #    #  #  #  
 #      ###### #####   ####   ####          # #    # #    # #    # 
 #      #    # #           #      #         # ###### #    # ###### 
 #    # #    # #      #    # #    # ## #    # #    #  #  #  #    # 
  ####  #    # ######  ####   ####  ##  ####  #    #   ##   #    # 
===================================================================

/*
/ NOTE Castling is not implemented.
/ NOTE En passant is not implemented.
/ NOTE Checkmate is not implemented, the game ends when the king is captured.
*/

/ * THIS IS STILL A WORK IN PROGRESS * /

This is my rendition of chess, but in a txt format.

There is very minor error handling when it comes to making incorrect
moves as I was more concerned about creating a complete version. I will probably add error handling later on after my
friends tell me they keep losing matches because it crashes anytime they accidentally type the wrong thing.

To compile, simply type 'make' in the txtchess directory.
To launch application, simply type 'java Main/Main'.

To play simply type the format of "{piece}{currCol}{currRow}{col}{row}" where:
    -'piece' can be p, r, n, b, q, k (pawn, rook, knight, bishop, queen, king respectively)
    -'currCol'/'currRow' are that pieces current location
    -'col'/'row' are where you are trying to move the piece.

Ex: pe2e4 (opening pawn move from position e2 to e4)
    ra1a8 (rook move from the bottom of the a file to the top)
    nb1c3 (knight move from starting position to center defense)